package commands;

import filesystem.Directory;

/**
 * 
 * @variable dirExists instance of Exception object to be used when a directory
 *           already exists
 * 
 * @variable dirDNE instance of Exception object to be used when a directory
 *           does not exist
 * @variable invalid instance of Exception object to be used for invalid input
 */

public class Mkdir extends Command {

  private Exception dirExists = new Exception("Directory already exists.");
  private Exception dirDNE = new Exception("Directory does not exist.");
  private Exception invalid = new Exception("Invalid input.");

  // default constructor
  /**
   * This is the private constructor for the class
   * 
   */
  private Mkdir() {}

  /**
   * Factory method to create a mkdir object with root directory being the
   * parameter passed in, "root"
   * 
   * @return Mkdir object
   */
  public static Mkdir createMkdirObject() {
    return new Mkdir();
  }

  @Override
  /**
   * Function that takes an array of parameters and makes the directory for each
   * parameter in the array list
   * 
   * @param param the line entered by the user
   * @return Error or empty string
   * @throws wrongParamError this is thrown when wrong number of arguments are
   *         inputed by user
   */
  public String performUserCommand(String param) throws Exception {
    // the processInput method splits String param
    String[] paramList = super.processInput(param);
    String catchStr = "";

    // checks to see if there are more the 1 arguments passed, at least one
    // needs to be passed
    if (this.paramCheck(1, paramList)) {
      for (String arg : paramList) {

        // call on the helper function to make the directory
        catchStr = this.makeDirByPath(arg);

      }
      return catchStr;
    }
    // return an Error if number of parameters is less than 1
    else {
      throw super.wrongParamError("Mkdir", paramList);
    }
  }

  /**
   * Private method used as helper function for preformUserCommand that creates
   * a directory when the user gives a path as a parameter instead of just a
   * name
   * 
   * @param path path of directory to be made
   * @return Error or empty string
   * @throws dirDNE
   * @throws dirExists
   * @throws invalid
   */
  private String makeDirByPath(String path) throws Exception {
    /*
     * Call Cd method to reference the path before the final directory in the
     * path passed
     */
    Directory targetDir = Cd.dirBeforeLast(path);
    // call Cd method to reference the last directory in the path
    String dirName = Cd.findLast(path);

    // check if path targetDir does not exist
    if (targetDir == null) {
      throw dirDNE;
    }
    // check if the last directory in path exists to avoid duplicating
    else if (targetDir.directoryExists(dirName)) {
      throw dirExists;
    }
    // check if the directory added consists of only alphanumerics
    else if (!super.isAlphanumeric(dirName)) {
      throw invalid;
    }
    // adds directory if the above conditions don't hold
    else {
      targetDir.addADirectoryByName(dirName);
      return "";
    }

  }

  @Override
  /**
   * Overrides the Command method paramCheck to check if the performUserCommand
   * has more than 1 parameter
   * 
   * @param numParams the number of parameters required for mkdir
   * @param parmArray the array of arguments
   * @return boolean the status of the parameter check
   */
  public boolean paramCheck(int numParams, String[] parmArray) {
    // Return whether the numParams is equal to the length of the parmArray
    return numParams <= parmArray.length;
  }
}
