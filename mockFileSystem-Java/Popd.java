package commands;

import filesystem.Directory;

/**
 * 
 * @variables emptyStack instance variable emptyStack messages for specific
 *            error handling
 *
 */
public class Popd extends Command {

  private Exception emptyStack = new Exception("Stack is empty.");

  /**
   * Default constructor for Popd class
   */
  private Popd() {}

  /**
   * Factory method for creating a Popd object
   * 
   * @return Popd
   */

  public static Popd makePopd() {
    return new Popd();
  }

  /**
   * Pops the element at the top of the directory stack and sets it to the
   * current directory
   * 
   * @param String line
   * @return error or empty string
   * @throws Exception 
   */
  public String performUserCommand(String line) throws Exception {
    String[] arg = super.processInput(line);

    if (super.paramCheck(0, arg)) {
      // pops the top of the stack and turns it into a directory
      if (Pushd.isEmpty()) {
        throw emptyStack;
      }

      else {
        // sets a new current directory, which was the one popped
        Directory top = Pushd.popStack();
        Directory.setCurrentDirectory(top);
        return "";
      }

    } else {
      throw super.wrongParamError("Popd", arg);
    }
  }

}
