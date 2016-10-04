package commands;

import filesystem.Directory;

public class Mv extends Command {

  /**
   * Creates Mv instance
   */
  private Mv() {}

  /**
   * Static function (factory method) that creates Mv instances
   * 
   * @return Mv instance
   */
  public static Mv createMvObject() {
    return new Mv();
  }

  /**
   * This method performs the user's command of moving one file/directory to a
   * different location.
   * 
   * @param String of the user's raw input
   * @return Empty string if function executes, otherwise error
   */
  public String performUserCommand(String userInput) throws Exception {
    String[] parmList = super.processInput(userInput);
    // Throw error if incorrect number of parameters
    if (!super.paramCheck(2, parmList)) {
      throw super.wrongParamError("Mv", parmList);
    }
    // Create cp object for copying file
    Cp cpObj = Cp.createCpObject();
    
    // Get Necessary name and directories from path
    Directory oldLocation = Cd.dirBeforeLast(parmList[0]);
    String oldItem = Cd.findLast(parmList[0]);
    Directory newLocation = Cd.dirBeforeLast(parmList[1]);
    String newItem = Cd.findLast(parmList[1]);
    
    // relocate old to new
    cpObj.relocateItem(oldLocation, oldItem, newLocation, newItem,
        parmList[0].replaceAll("/+$|^/+", ""),
        parmList[1].replaceAll("/+$|^/+", ""));
    
    // Delete old items (variant on whether it was a dir or file)
    if (oldLocation.directoryExists(oldItem)) {
      oldLocation.deleteDirectory(oldItem);
    } else {
      oldLocation.deleteFile(oldItem);
    }
    return "";
  }


}
