package commands;

import java.util.Arrays;

import filesystem.*;

/**
 * 
 * @variable invalidDIR an instance of the error object used to return errors
 *           when a directory does not exist
 * 
 */
public class Ls extends Command {

	private Exception invalidDIR = new Exception("Directory does"
			+ " not exist");

	/**
	 * Private constructor used by factory methods
	 * 
	 * @param root
	 *            the root directory
	 */
	private Ls() {

	}

	/**
	 * Factory method to create an ls object
	 * 
	 * @return ls a new instance of the ls object
	 */
	public static Ls createLsObject() {
		return (new Ls());
	}

	@Override
	/**
	 * Returns a boolean of whether the required number of parameters for ls 
	 * is the satisfied length of the provided parameter list.
	 * For ls the numParams is >= 0 
	 * 
	 * @param numParams: an integer of the number of parameters
	 * @param parmArray: A list of parameters
	 * @return a boolean
	 */
	public boolean paramCheck(int numParams, String[] parmArray) {
		// Return whether the numParams is equal to the length of the parmArray
		return parmArray.length >= 0;
	}

	/**
	 * This function takes in the the arguments passed by the user in the the
	 * terminal (if any) and lists the content for each one
	 * 
	 * @param parmLine
	 *            the line entered by the user
	 * @return returnLine the return string of the method output
	 * @throws wrongParamException
	 *             thrown when wrong number of args are passed
	 * @throws invalidDIR
	 *             thrown when directory does not exist
	 */
	public String performUserCommand(String parmLine) throws Exception {
		String returnLine = "";
		boolean flag = false;
		// converting line to an array of arguments
		String[] parmList = this.processInput(parmLine);
		if (!this.paramCheck(0, parmList)) {
			throw this.wrongParamError("Ls", parmList);
		}

		// if the recursive flag is given
		if (parmList.length >= 1 && parmList[0].equalsIgnoreCase("-r")) {
			flag = true;
			parmList = Arrays.copyOfRange(parmList, 1, parmList.length);
		}

		// getting the current directory
		Directory current = Directory.getCurrentDirectory();
		// if no argument is passed
		if (parmList.length == 0) {
			returnLine = (this.listDirectoryContent(current, flag, 0));
			// if there is an argument being passed
		} else {
			Object obj;
			// looping through arguments passed
			for (String arg : parmList) {
				// if the arg passed is a path
				if (arg.contains("/")) {
					obj = Cd.dirAtPath(arg);
				}
				// if the arg passed is not a path then
				else {
					obj = current.accessItem(arg);
				}

				// If the path is a directory, list out the content
				if (obj == null) {
					throw invalidDIR;
				} else if (obj instanceof Directory) {
					Directory dir = (Directory) obj;
					returnLine = returnLine + "\n" + (dir.name() + ": ")
							+ (this.listDirectoryContent(dir, flag, 0));
					// If the path is a file print out the name
				} else if (obj instanceof File) {
					returnLine = returnLine + "\n" + arg;
				}
			}
		}
		return returnLine.trim();
	}

	/**
	 * A helper function used to list the content of directory dir given
	 * 
	 * @param dir
	 *            the directory which for the content has to be printed
	 *          
	 * @param recursive	indicates if call has -r flag or not 
	 * @param depth	indicates depth of recursion 
	 * @return printString the directory content
	 */
	private String listDirectoryContent(Directory dir, boolean recursive,
			int depth) {
		String printString = "";
		String depthTab = "";
		// formatting spacing
		for (int i = 0; i <= depth; i++) {
			depthTab = depthTab + "\t";
		}
		// for every object in the directory
		for (Object fileOrDir : dir.content()) {
			// if its a file indicate so and add it to the print string
			if (fileOrDir instanceof File) {
				printString = printString + "(F)" + ((File) fileOrDir).name()
						+ " ";
				// if it is a directory add it to the print string
			} else if (fileOrDir instanceof Directory) {
				Directory tempCur = (Directory) fileOrDir;
				printString = printString + tempCur.name() + " ";
				// if the call was recursive, add the content of the directory
				// to the print string
				if (recursive && !tempCur.content().isEmpty()) {
					printString = printString
							+ "\n"
							+ depthTab
							+ listDirectoryContent((Directory) fileOrDir, true,
									depth + 1) + "\n";
				}
			}
		}
		return (printString.trim());
	}
}
