package commands;

import java.util.*;

import filesystem.Directory;

/**
 * @variable msg instance of Error for invalid directory handling
 * @variable directoryStack to save directories pushed
 */

public class Pushd extends Command {
  private Exception invalidDIR = new Exception("Invalid directory.");

  private static Stack<Directory> directoryStack = new Stack<Directory>();

  /**
   * Default constructor for a Pushd class
   */
  private Pushd() {}

  // factory method to create an instance of the directory

  /**
   * Factory method to create Pushd object
   * 
   * @return Pushd
   */

  public static Pushd makeDirectoryStack() {
    directoryStack = new Stack<Directory>();

    return new Pushd();
  }

  /**
   * Takes in a path from user and pushes the current directory into the Stack
   * and sets the current directory to the path passed by user
   * 
   * @param String DIR
   * @return error or empty string
   * @throws Exception 
   */
  public String performUserCommand(String DIR) throws Exception {
    // calling parent class method to split the parameter
    String[] arg = super.processInput(DIR);

    Directory newDIR = Cd.dirAtPath(arg[0]);
    // check if the amount of parameters passed is equal to 1

    if (super.paramCheck(1, arg)) {
      // get current directory
      Directory current = Directory.getCurrentDirectory();
      // check if the path passed by user exists
      if (newDIR != null) {
        // push the current directory to Stack
        directoryStack.push(current);

        current = Cd.dirAtPath(arg[0]);
        // set current directory to the path passed by user
        Directory.setCurrentDirectory(current);

        return "";

      } else {
        throw invalidDIR;
      }
    }
    // returns appropriate error to wrong amount of parameters passed
    else {
      throw super.wrongParamError(DIR, arg);
    }
  }

  /**
   * Pops the top element of the directory stack
   * 
   * @return Directory
   */
  public static Directory popStack() {
    return directoryStack.pop();
  }

  /**
   * Checks to see if the directory stack is empty
   * 
   * @return boolean
   */
  public static boolean isEmpty() {
    return directoryStack.isEmpty();
  }

  /**
   * Checks the size of directory stack
   * 
   * @return int
   */
  public static int Size() {
    return directoryStack.size();
  }

}
