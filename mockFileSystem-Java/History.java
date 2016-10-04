package commands;

import java.util.ArrayList;

/**
 * 
 * @variable historyArray stores all the inputed arguments
 * 
 * @variable notIntError is the instance variable when the input is not an
 *           integer.
 * 
 */
public class History extends Command {
	private static ArrayList<String> historyArray = new ArrayList<String>();
	private Exception notIntError = new Exception("The input provided must "
	    + "be an integer");

	/**
	 * Constructor for history command
	 */
	private History() {

	}

	/**
	 * Factory method for history command
	 * 
	 * @return History history instance created
	 */
	public static History createHistoryObject() {
		historyArray.clear();
		return new History();
	}

	/**
	 * Adds a line to the history stack
	 * 
	 * @param line
	 *            the line inputed by user to be added to the history
	 */
	public void addToHistory(String line) {
		historyArray.add(line);

	}

	@Override
	/**
	 * Main function called by program to analyze and perform user command
	 * 
	 * @param pramLine
	 *            the line inputed by the user to view history
	 * @return returnString the line to be returned to be printed
	 */
	public String performUserCommand(String parmLine) throws Exception {
		String[] parmList = this.processInput(parmLine);
		if (!(this.paramCheck(0, parmList) || this.paramCheck(1, parmList))) {
			throw this.wrongParamError("history", parmList);
		}

		String returnString = "";
		int number = 0;

		// if an argument of how many commands to print is given
		if (parmList.length == 1) {
			// try parsing input into an integer
			try {
				number = historyArray.size() - Integer.parseInt(parmList[0]);
				if (number < 0) {
					number = 0;
				}
			} catch (Exception notInt) {
				return (notIntError.toString());
			}
		}

		// print the amount of commands (either all or arg given)
		for (int i = number; i < historyArray.size(); i++) {
			returnString = returnString + (i + 1) + "\t" + historyArray.get(i)
					+ "\n";
		}
		return (returnString.trim());

	}
	 
	/**
	 * Method getter for getting the history item at the given index
	 * 
	 * @param index
	 * @return String at index
	 */
	public String get(int index) {
	  return historyArray.get(index - 1);
	}

	/**
	 * Method to return size of history object, i.e. How many commands have been
	 * called thus far
	 * 
	 * @return size of historyArray
	 */
	public int size() {
		return historyArray.size();
	}

}
