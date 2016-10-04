package commands;

public class Exit extends Command {

	/**
	 * Private constructor for exit class
	 */
	private Exit() {
	}

	/**
	 * Factory method to create an exit instance
	 * 
	 * @return Exit instance
	 */
	public static Exit createExitObject() {
		return new Exit();
	}

	@Override
	/**
	 * This exit command method just exits the shell, if given the correct
	 * parameters
	 * 
	 * @param line	the line inputed by the user
	 * @throws wrongParamError 	error if wrong amount of args are received
	 * 							from user
	 */
	public String performUserCommand(String line) throws Exception {
		// Process Input
		String[] parmArray = super.processInput(line);

		// Check parameter
		if (super.paramCheck(0, parmArray)) {
			System.exit(0);
			return "";
		} else {
			// return Error
			throw super.wrongParamError("Exit", parmArray);
		}
	}

}
