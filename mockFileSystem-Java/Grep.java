package commands;

import java.util.Arrays;
import java.util.regex.*;
import filesystem.*;

public class Grep extends Command {

  /**
   * private constructor
   */
  private Grep() {}

  /**
   * Factory method to create grep Object
   * 
   * @return Grep object
   */
  public static Grep createGrepObject() {
    return new Grep();
  }

  @Override
  /**
   * Main function called by program to analyze and perform user command for
   * the grep command
   * 
   * @param pramLine the line inputed by the user to use grep
   * @return returnString the line to be returned to be printed
   */
  public String performUserCommand(String parmLine) throws Exception {
    String[] parmList = super.processInput(parmLine);
    String printString = "";
    boolean rFlag = false;
    rFlag = paramCheck(2, parmList, rFlag);

    if (rFlag) {
      parmList = Arrays.copyOfRange(parmList, 1, parmList.length);
    }

    // compiling pattern - regex string is parmList[0]
    Pattern p = Pattern.compile(parmList[0]);

    // change parmList to only have paths
    parmList = Arrays.copyOfRange(parmList, 1, parmList.length);

    // Call pucHelper to traverse parmList
    printString = this.pucHelper(parmList, p, rFlag);

    // If there are no paths, traverse the current directory
    if (rFlag && parmList.length == 0) {
      printString = this.recursiveMatcher(Directory.getCurrentDirectory(), p);
    }

    return printString.trim();
  }

  /**
   * User Command helper that loops through the paramList paths and forms a
   * printString.
   * 
   * @param parmList
   * @param p
   * @return printString
   * @throws Exception
   */
  private String pucHelper(String[] parmList, Pattern p, boolean rFlag)
      throws Exception {
    String printString = "";

    // Traverse parmList paths
    for (String path : parmList) {
      Directory dir = Cd.dirBeforeLast(path); // get subject directory
      String name = Cd.findLast(path); // get String of subject name

      if (dir == null || dir.accessItem(name) == null) {
        // Throw error if location or item within does not exist
        throw new Exception(path + ": Invalid Path");
      }
      if (dir.fileExists(name)) {
        // If file exists accumulate with file trace
        printString += "\n" + this.fileMatcher((File) dir.accessItem(name), p);

      } else if (dir.directoryExists(name) && !rFlag) {
        // Throw error if directory found without recursive flag
        throw new Exception(dir.path() + name + ":\nCannot have directories as"
            + " parameters \nwithout [-r] - refer to manual.");

      } else {
        // Otherwise(Directory w/ flag) accumulate with recursive trace
        printString +=
            "\n" + this.recursiveMatcher((Directory) dir.accessItem(name), p);
      }
    }
    return printString;
  }

  /**
   * Traverses the lines of File(f) and matches it with the provided pattern
   * and returns the lines that match.
   * 
   * @param f - the file to inspect
   * @return Lines within f that match pattern p
   */
  private String fileMatcher(File f, Pattern p) {
    // Split file's contents into array of lines
    String[] fileContents = f.content().split("\n");

    // String for the matching lines and dividers
    String rgxLines = "";
    String divider =
        new String(new char[f.path().length() + 1]).replace("\0", "-") + "\n";
    Matcher m;

    // Traverse lines
    for (String line : fileContents) {
      m = p.matcher(line);

      if (m.find()) {
        // Accumulate matching lines
        rgxLines += line + "\n";

      }
    }
    if (rgxLines.equals("")) {
      return ""; // return empty if no matches
    }
    // Otherwise return formatted text with matched lines
    return divider + f.path() + ":\n" + divider + rgxLines.trim() + "\n";
  }

  /**
   * Recursively traces the given directory(dir) and checks all resident files
   * with the pattern p. (Using fileMatcher).
   * 
   * @param dir - the directory to inspect
   * @param p - given pattern
   * @return printString
   */
  private String recursiveMatcher(Directory dir, Pattern p) {

    // String for printing and (special) spacing
    String printString = "";
    String newLine = "";

    for (Object fileOrDir : dir.content()) {

      // If item is a file
      if (fileOrDir instanceof File) {
        newLine = "\n"; // newline if file is last
        File f = (File) fileOrDir;

        // Accumulate fileMatcher results for the current file
        printString = fileMatcher(f, p) + printString + "\n";

        // If item is a directory
      } else if (fileOrDir instanceof Directory) {
        newLine = ""; // no newline if directory is last
        Directory current = (Directory) fileOrDir;

        // Accumulate with recursive call to inner directory
        printString = printString + "\n" + recursiveMatcher(current, p);
      }
    }
    return printString.trim() + newLine;
  }

  /**
   * Overridden Command method that checks the parameters' validity and
   * searches for the recursive element and raises the flag if it is found
   * 
   * @param parmList
   * @throws Exception
   */
  public boolean paramCheck(int numParams, String[] parmList, boolean rFlag)
      throws Exception {

    // If parmList has fewer than (numParams) parameters
    if (parmList.length < numParams) {
      throw super.wrongParamError("Grep", parmList);
    }

    // if the call is recursive
    if (parmList[0].equalsIgnoreCase("-r")) {

      // set recursive boolean to true
      rFlag = true;
    }
    return rFlag;
  }
}
