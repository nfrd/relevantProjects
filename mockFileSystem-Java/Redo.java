package commands;

import commands.History;
import driver.JShell;

public class Redo extends Command {

  private Exception notIntError =
      new Exception("The input provided must be an integer");
  private Exception notEnoughHistory = new Exception("The input provided is "
      + "larger than the total number of items in History");
  private Exception outOfRangeIndex =
      new Exception("The input provided cannot be a value less than 1");
  private Exception redoOnItself =
      new Exception("cannot call redo on itself");
  private History historyObj = History.createHistoryObject();

  private Redo() {

  }

  public static Redo createRedoObject() {
    return new Redo();
  }
  
  /**
   * Perform User Command for Redo command
   */
  public String performUserCommand(String parmLine) throws Exception {
    String[] parmList = processInput(parmLine);
    if (!super.paramCheck(1, parmList)) {
      throw this.wrongParamError("Redo", parmList);
    }
    
    int cmdIndex = Integer.parseInt(parmList[0]);

    if (cmdIndex > historyObj.size() - 1) {
      throw notEnoughHistory;
    } else if (cmdIndex < 1) {
      throw outOfRangeIndex;
    }
    
    
    String command = historyObj.get(cmdIndex);
    if (parmLine !=command){ 
      return JShell.executeLine(command);
    }
    else{
      throw redoOnItself;
    }
  }

  /**
   * Overridden processInput method for Redo
   */
  public String[] processInput(String rawInput) throws Exception {
    // Trim outside white-space and reduce inside white-space
    rawInput = rawInput.trim().replaceAll("\\s+", " ");
    rawInput = rawInput.replaceFirst("!", "");

    if (!this.isNumber(rawInput)) {
      throw notIntError;
    }
    if (Integer.parseInt(rawInput)<1){
      throw outOfRangeIndex;
    }

    // Split at space
    String[] lineArray = rawInput.split(" ");
    return lineArray;
  }

  /**
   * Private method for checking if a string is numeric
   * 
   * @param s
   * @return true/false
   */
  private boolean isNumber(String s) {
    try {
      Integer.parseInt(s);

    } catch (NumberFormatException e) {
      return false;
    }

    return true;
  }


}
