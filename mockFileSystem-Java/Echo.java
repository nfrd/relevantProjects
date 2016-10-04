package commands;

import filesystem.Directory;
import filesystem.File;
import java.util.*;

public class Echo extends Command {

  /**
   * @variable global Error instances
   */
  private Exception noPathError = new Exception("Path not present");
  private Exception wrongArrowsError =
      new Exception("> / >> missing or incorrectly placed");
  private Exception fileNameError =
      new Exception("The filename must be alphanumeric.");
  private Exception formatError = new Exception("Incorrect formatting");


  /**
   * @variable global variables for echo append and replace
   */
  // Intialize private variables for use in this classes
  private Directory subjectDir;
  private File subjectFile;

  /**
   * Creates echo instance
   */
  private Echo() {}

  /**
   * Static function (factory method) that creates echo instances
   * 
   * @return Echo instance
   */
  public static Echo createEchoObject() {
    return new Echo();
  }

  /**
   * Performs the expected user command (echo). (Less than 30 lines)
   * 
   * @param input: User input in raw string form
   * @return a blank string or error
   */
  public String performUserCommand(String input) throws Exception {
    String[] parmArray = this.processInput(input);

    // if paramChecks are false
    if (!super.paramCheck(1, parmArray) && !super.paramCheck(3, parmArray)) {
      throw super.wrongParamError("Echo", parmArray);
    }

    // Remove quotations from beginning and end
    String userString = parmArray[0].substring(1, parmArray[0].length() - 1);

    // if there is only one parameter - return
    if (parmArray.length == 1) {
      return userString;

    } else if (parmArray[1].equals(">")) {
      // Call appropriate method for replace echo
      return this.echoReplace(userString, parmArray[2]);

    } else if (parmArray[1].equals(">>")) {
      // Call method for append echo
      return this.echoAppend(userString, parmArray[2]);

    } else {
      throw wrongArrowsError;
    }
  }


  /**
   * This method processes the string given by the user and returns a list of
   * arguments from it. (Less than 30 lines)
   * 
   * @param raw: the user's raw input
   * @return parmArray: A list of arguments for the command
   */
  public String[] processInput(String raw) throws Exception {
    raw = raw.substring(raw.indexOf(" "), raw.length()).trim();

    // Check for format errors - incorrect location/amount of quotes
    if (raw.indexOf("\"") != 0 || raw.split("\"", -1).length - 1 < 2) {
      throw this.formatError;
    }

    // form a substring of the string argument
    String str = raw.substring(raw.indexOf("\""), raw.lastIndexOf("\"") + 1);

    String subject = this.getSubjectString(raw);

    if (subject.equals("")) {
      // if there is no content in the subject string
      return new String[] {str};
    }

    // split subject string at consecutive spaces
    String params[] = subject.split("\\s+");
    // convert to List
    List<String> captureArr = new ArrayList<String>(Arrays.asList(params));
    // add str to beginning
    captureArr.add(0, str);

    // Convert back to array and return
    return captureArr.toArray(new String[captureArr.size()]);
  }

  /**
   * Gets and returns the subject string - the string from after the final
   * quotation to the end (processed w spacing)
   * 
   * @param raw
   * @return subject
   */
  private String getSubjectString(String raw) {

    // Substring from after the last quotation to the end
    String subject = raw.substring(raw.lastIndexOf("\"") + 1, raw.length());

    // If there are arrows, add spaces around them
    if (subject.contains(">")) {
      subject = subject.substring(0, subject.indexOf(">")) + " "
          + subject.substring(subject.indexOf(">"),
              subject.lastIndexOf(">") + 1)
          + " " + raw.substring(raw.lastIndexOf(">") + 1, raw.length());
    }

    return subject.trim();
  }

  /**
   * This method takes the user's content and desired location for it and adds
   * the content to that file. If the file does not exist create it. If a
   * directory uses that name return an error. (Less than 30 lines)
   * 
   * @param userEntry: String of what the user wants to add
   * @param pathFile: A string of the path where the content should go
   * @return a blank string or error
   */
  private String echoAppend(String userEntry, String pathFile)
      throws Exception {
    // Get the directory before the last one
    subjectDir = Cd.dirBeforeLast(pathFile);

    // Get the string of the filename
    String fileName = Cd.findLast(pathFile);

    // If the directory DNE
    if (subjectDir == null) {
      throw noPathError;
      // If the filename is not alphanumeric
    } else if (!super.isAlphanumeric(fileName) || fileName.length() < 1) {
      throw fileNameError;

    } else {

      // Add to the file directly if the file exists
      if (subjectDir.fileExists(fileName)) {
        subjectFile = (File) subjectDir.accessItem(fileName);
        subjectFile.addLineToFile(userEntry);

        // Error if a directory is using the filename
      } else if (subjectDir.directoryExists(fileName)) {
        throw new Exception(fileName + ": A folder is using that name.");

      } else {
        // Otherwise add the file and then add to it
        subjectDir.addFile(fileName);
        subjectFile = (File) subjectDir.accessItem(fileName);
        subjectFile.addLineToFile(userEntry);
      }
    }
    return "";
  }

  /**
   * This method takes the user's content and desired location for it and
   * replaces the content of that file with the user's input. If the file does
   * not exist create it. If a directory uses that name return an error. (Less
   * than 30 lines)
   * 
   * @param userEntry: String of what the user wants to add
   * @param pathFile: A string of the path where the content should go
   * @return return a blank string or error
   */
  private String echoReplace(String userEntry, String pathFile)
      throws Exception {
    // Call echo append to add to the file
    echoAppend(userEntry, pathFile);

    // If there is a subjectDir erase the file and add userEntry
    subjectFile.eraseFile();
    subjectFile.addLineToFile(userEntry);
    return "";

  }

}
