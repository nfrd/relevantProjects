package commands;

import filesystem.Directory;
import filesystem.File;

/**
 * @variable invalidOldPath instance of Exception object to be used when the
 *           oldPath parameter is invalid
 * @variable invalidNewPath instance of Exception object to be used when the
 *           newPath is invalid
 * @variable dirToFile instance of Exception object to be used for trying to
 *           copy a directory to a file
 * @variable subDirError instance of Exception object to be used for copying
 *           contents of a directory to its subdirectory
 */

public class Cp extends Command {

  private Exception invalidOldPath = new Exception("Oldpath is not valid");
  private Exception invalidNewPath = new Exception("Newpath is not valid");
  private Exception dirToFile =
      new Exception("Cannot move a Directory to a File");
  private Exception subDirError =
      new Exception("Cannot copy/move to subdirectory");

  /**
   * Creates Cp instance
   */
  private Cp() {}

  /**
   * Static function (factory method) that creates Cp instances
   * 
   * @return Cp instance
   */
  public static Cp createCpObject() {
    return new Cp();
  }

  @Override
  /**
   * Performs the user's command, copies the contents of a directory or a file
   * and puts it into another directory or file.
   * 
   * @param String userInput
   * @return String
   */
  public String performUserCommand(String userInput) throws Exception {
    // processing the input into an array and checking number of args
    String[] parmList = super.processInput(userInput);
    if (super.paramCheck(2, parmList)) {
      // setting variables of the elements in parmList
      Directory oldLocation = Cd.dirBeforeLast(parmList[0]);
      String oldItem = Cd.findLast(parmList[0]);
      Directory newLocation = Cd.dirBeforeLast(parmList[1]);
      String newItem = Cd.findLast(parmList[1]);
      // cleans up the string
      String result = this.relocateItem(oldLocation, oldItem, newLocation,
          newItem, parmList[0].replaceAll("/+$|^/+", ""),
          parmList[1].replaceAll("/+$|^/+", ""));

      return result;

    } else {
      super.wrongParamError("Cp", parmList);
    }
    return "";
  }

  /**
   * Helper function that checks validity of parameters passed by user and
   * copies the item from oldLocation directory/file to newLocation
   * directory/file
   * 
   * @param Directory oldLocation, String oldItem, Directory newLocation, String
   *        newItem, String oldpath, String newpath
   * @return String
   */

  public String relocateItem(Directory oldLocation, String oldItem,
      Directory newLocation, String newItem, String oldpath, String newpath)
          throws Exception {
    // checks if the parameters are valid for the functionality of
    // performUserCommand
    if (oldLocation == null || oldLocation.accessItem(oldItem) == null) {
      throw invalidOldPath;
    } else if (newLocation == null || newLocation.accessItem(newItem) == null) {
      throw invalidNewPath;
    } else if (oldpath.length() > newpath.length()) {
      if ((oldpath.substring(0, newpath.length())).equals(newpath)) {
        throw subDirError;
      }
    }
    if (oldLocation.accessItem(oldItem) instanceof Directory
        && newLocation.accessItem(newItem) instanceof File) {
      throw dirToFile;
    }
    // call helper function for copying file to file
    if (oldLocation.fileExists(oldItem) && newLocation.fileExists(newItem)) {
      this.copyFileToFile(oldLocation, oldItem, newLocation, newItem);
    }
    // call helper function for copying file to directory
    else if (oldLocation.fileExists(oldItem)
        && newLocation.directoryExists(newItem)) {
      this.copyFileToDirectory(oldLocation, oldItem, newLocation, newItem);
    }
    // call helper function for copying directory to directory
    else {
      this.copyDirectory(oldLocation, oldItem, newLocation, newItem);
    }

    return "";
  }

  /**
   * Helper function for relocateItem that copies files to another file
   * 
   * @param Directory oldLocation, String oldItem, Directory newLocation, String
   *        newItem
   */
  private void copyFileToFile(Directory oldLocation, String oldItem,
      Directory newLocation, String newItem) throws Exception {
    // reference old file
    File firstFile = (File) oldLocation.accessItem(oldItem);
    String contents = firstFile.content();
    // copy old file
    File secondFile = (File) newLocation.accessItem(newItem);
    secondFile.eraseFile();
    secondFile.addLineToFile(contents);
  }

  /**
   * Helper function for relocateItem that copies files to directory
   * 
   * @param Directory oldLocation, String oldItem, Directory newLocation, String
   *        newItem
   */
  private void copyFileToDirectory(Directory oldLocation, String oldItem,
      Directory newLocation, String newItem) {
    // reference old file and copy to directory
    File oldFile = (File) oldLocation.accessItem(oldItem);
    Directory newDir = (Directory) newLocation.accessItem(newItem);
    newDir.addFile(oldFile);
  }

  /**
   * Helper function for relocateItem that copies contents of a directory to
   * another directory
   * 
   * @param Directory oldLocation, String oldItem, Directory newLocation, String
   *        newItem
   */
  private void copyDirectory(Directory oldLocation, String oldItem,
      Directory newLocation, String newItem) {
    // copy old directory and all its contents and put in new directory
    Directory oldDir = (Directory) oldLocation.accessItem(oldItem);
    Directory newDir = (Directory) newLocation.accessItem(newItem);
    newDir.addDirectory(oldDir);
  }


}
