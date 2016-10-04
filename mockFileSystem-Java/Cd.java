package commands;

import filesystem.Directory;

public class Cd extends Command {

  /**
   * @variable cdErr instance variable if the directory doesn't exist
   */
  private Exception cdErr = new Exception("Invalid directory");

  /**
   * Constructor for cd command
   */
  private Cd() {}

  /**
   * Factory method for history command
   * 
   * @return Cd
   */
  public static Cd createCdObject() {
    return new Cd();
  }

  /**
   * Changes directory to the directory given if it exists
   * 
   * @param userInput
   * @exception cdErr, wronParam
   * @return empty String
   */
  public String performUserCommand(String userInput) throws Exception {
    String[] parmList = super.processInput(userInput);

    if (super.paramCheck(1, parmList)) {
      String parmString = parmList[0];
      // runs argument into helper function to check it exists
      Directory newDIR = dirAtPath(parmString);

      if (newDIR != null) {
        // sets current directory to the inputed directory
        Directory.setCurrentDirectory(newDIR);
        return "";
      }
      // throw error since directory does not exist
      throw cdErr;
    } else {
      throw super.wrongParamError("Cd", parmList);
    }
  }

  /**
   * Goes through the user's input and returns the correct directory
   * 
   * @param parmString - a path string
   * @return current returns the current directory
   */
  public static Directory dirAtPath(String parmString) throws Exception {

    Directory current = Directory.getCurrentDirectory();
    Directory oldCurrent = Directory.getCurrentDirectory();

    // checks if the / is the first in the string and sets current directory to
    // root
    if (parmString.indexOf("/") == 0) {
      current = Directory.getRootDirectory();
    }
    // filters the string to not have slashes at beginning and end of string
    parmString = parmString.replaceAll("/+$|^/+", "").replaceAll("\\/+", "/");
    String[] path = parmString.split("/");

    return dirTraverser(path, parmString, current, oldCurrent);
  }

  /**
   * Traverses based on the given parameters, and return the appropriate
   * directory at the end of it.
   * 
   * @param path - list of directory names (correct order)
   * @param parmString - user's input
   * @param current - current directory
   * @param oldCurrent - original directory (for ".")
   * @return current - the ending directory
   */
  private static Directory dirTraverser(String[] path, String parmString,
      Directory current, Directory oldCurrent) {
    
    int counter = 0;
    while (counter <= path.length - 1 && !parmString.equals("")) {
      // returns parent directory
      if (path[counter].equals("..")) {
        current = current.parent();
        // returns original current directory
      } else if (path[counter].equals(".")) {
        current = oldCurrent;

      } else {
        Object nextDir = (Object) current.accessItem(path[counter]);
        // changes to next directory
        if (nextDir != null && Directory.class.isInstance(nextDir)) {
          current = (Directory) nextDir;
          // directory to null if DNE (exit loop)
        } else {
          counter = path.length;
          current = null;
        }
      }
      counter++;
    }
    return current;
  }

  /**
   * Finds and returns the directory before the last one
   * 
   * @param path
   * @return Directory before final
   */
  public static Directory dirBeforeLast(String path) throws Exception {

    // Get string from beginning to the last slash
    String fileDir = path.replaceAll("/+$", "");
    fileDir = fileDir.substring(0, fileDir.lastIndexOf("/") + 1);
    // Return the directory at that new string path
    return Cd.dirAtPath(fileDir);
  }

  /**
   * Find and return the string that is the subject fileName or directory
   * 
   * @param path
   * @return String of last item's name
   */
  public static String findLast(String path) {

    String last = path.replaceAll("/+$", "");
    // Get string from last slash to the end
    last = last.substring(last.lastIndexOf("/") + 1, last.length());

    // return that string
    return last;

  }

}


