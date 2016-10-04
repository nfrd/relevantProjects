package commands;

import filesystem.*;

/**
 * 
 * @variable doesNotExist this is an instance variable for the Exception object
 *           that is called when a file does not exist
 * 
 * @variable invalidFile this is an instance variable for the Exception object
 *           that is used when an argument is not a file
 * 
 */
public class Cat extends Command {

	private Exception doesNotExist = new Exception("This file does not exist");
	private Exception invalidFile = new Exception("This is not a file!");

	/**
	 * Private constructor
	 */
	private Cat() {
	}

	/**
	 * Factory method to create a cat object
	 * 
	 * @return cat the instance created by this factory method
	 */
	public static Cat createACatObject() {
		return (new Cat());
	}

	@Override
	/**
	 * This function takes in the array size of 1 of the user's argument and
	 * attains the file at the path or the name and then prints the content out
	 * of that file.
	 * 
	 * @param parmLine the line entered by the user
	 * @return returnLine either an error or the content of the file
	 * @throw Exception
	 */
	public String performUserCommand(String parmLine) throws Exception {
		// checking the number of arguments passed
		String[] parmList = this.processInput(parmLine);
		if (!this.paramCheck(1, parmList)) {
			throw this.wrongParamError("Cat", parmList);
		}
		String returnString = "";
		for (String arg : parmList) {

			String fileName;
			Directory fileDIR = Directory.getCurrentDirectory();
			// if the arg passed is a path, find the directory the file is in
			// and then get the file
			if (arg.contains("/")) {
				fileDIR = Cd.dirBeforeLast(arg);
				fileName = Cd.findLast(arg);

				// if the arg passed is just a name check for file in current
				// directory
			} else {
				fileName = arg;
			}
			Object obj = fileDIR.accessItem(fileName.replace("/", ""));
			if (obj == null) {
				throw doesNotExist;
			} else if (!(obj instanceof File)) {
				throw invalidFile;
			} else {
				File f = (File) fileDIR.accessItem(fileName.replace("/", ""));
				returnString = returnString + "\n" + fileName + ": \n"
						+ (f.content().trim());
			}
		}
		return returnString.trim();
	}

	
	@Override
	public boolean paramCheck(int numParams, String[] parmArray) {
		// Return whether the numParams is equal or less
		// to the length of the parmArray
		return numParams <= parmArray.length;
	}

}
