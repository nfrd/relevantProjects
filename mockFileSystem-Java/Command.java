package commands;

import java.util.Arrays;

public class Command {

  /**
   * creates command instance
   */
  public Command() {}

  /**
   * This function is to be overridden by all subclasses that inherit command
   * 
   * @return line this is the line the user has entered
   */
  public String performUserCommand(String line) throws Exception {
    return line;
  }

  /**
   * Takes in the raw input from the user and converts it into a list of
   * parameters for use by the individual commands.
   * 
   * @param rawInput: Takes in the raw user input as a string
   * @return parmArray: raw String content in list form, with spaced removed
   */
  public String[] processInput(String rawInput) throws Exception {
    // Trim outside white-space and reduce inside white-space
    rawInput = rawInput.trim().replaceAll("\\s+", " ");

    // Split at space
    String[] lineArray = rawInput.split(" ");

    // Take the array without the first element (command Name)
    String[] parmArray = Arrays.copyOfRange(lineArray, 1, lineArray.length);

    // Return it (list of parameters)
    return parmArray;
  }

  /**
   * Returns a boolean of whether the required number of parameters is equal to
   * the length of the provided parameter list.
   * 
   * @param numParams: an integer of the number of parameters
   * @param parmArray: A list of parameters
   * @return a boolean
   */
  public boolean paramCheck(int numParams, String[] parmArray)
      throws Exception {
    // Return whether the numParams is equal to the length of the parmArray
    return numParams == parmArray.length;
  }

  /**
   * This method returns an Exception for use when there are an incorrect
   * number of parameters
   * 
   * @param className: String of the class
   * @param parmArray: List of parameters
   * @return a new Exception with the constructed message
   */
  public Exception wrongParamError(String className, String[] parmArray) {
    // initialize a local variables for the number parameters
    int numParams = parmArray.length;
    String msg = className + " cannot accept " + numParams + " parameter(s).";
    return new Exception(msg);
  }

  /**
   * Helper method that checks if the string given is alphanumeric.
   * 
   * @param String s
   * @return boolean value
   */
  public boolean isAlphanumeric(String s) {
    // User String matches to see if (s) is alphanumeric. (accept . and _)
    boolean hasNonAlpha = s.matches("^.*[^a-zA-Z0-9._].*$");
    return !hasNonAlpha;
  }
}
