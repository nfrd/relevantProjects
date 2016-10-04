package commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import commands.Echo;

public class Get extends Command {

	private Echo echoCommand;

	/**
	 * creates command instance
	 */
	public Get() {
		echoCommand = Echo.createEchoObject();
	}

	/**
	 * Factory method to make a Get command object
	 * 
	 * @return Get a new get command object
	 */
	public static Get createGetObject() {
		return new Get();
	}

	@Override
	/**
	 * This method performs the user command for get command
	 * 
	 * @param line
	 *            this is the line the user has entered
	 * @return "" empty string, this command returns no output
	 * @throws wrongParamError
	 *             exception for when wrong number of args is entered by the
	 *             user
	 */
	public String performUserCommand(String line) throws Exception {
		String textString = "";

		// Checking input by user
		String[] parmList = this.processInput(line);
		if (!this.paramCheck(1, parmList)) {
			throw this.wrongParamError("Get", parmList);
		}
		// Getting the url entered by the user
		String url = parmList[0];
		// Getting the filename at the end of the url
		String[] urlSplit = url.split("/");
		String fileName = urlSplit[urlSplit.length - 1];

		// making the url object and the reader object
		URL oracle = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				oracle.openStream()));

		// reading file and saving it as a string
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			textString = textString + "\n" + (inputLine);
		}
		in.close();

		return echoCommand.performUserCommand("echo \"" + textString + "\">"
				+ fileName);

	}

}
