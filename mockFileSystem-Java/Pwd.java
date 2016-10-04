package commands;

import filesystem.Directory;


public class Pwd extends Command {

	/**
	 * Private constructor for Pwd command class
	 * 
	 */
	private Pwd() {
	}

	/**
	 * Factory method to create a Pwd instance
	 * 
	 * @return Pwd the Pwd instance
	 */
	public static Pwd createAPwdObject() {
		return (new Pwd());
	}

	@Override
	/**
	 * This method performs the user command
	 * which is to print the current working directory path 
	 * 
	 * @return currentDir.path()	the path for current directory 
	 */
	public String performUserCommand(String line) {
		return Directory.getCurrentDirectory().path();
	}

}