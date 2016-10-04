package filesystem;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @class File class
 * 
 * @Description File class to simulate a file with text as it's content
 * 
 */
public class File implements FileSystemType {
	private String path;
	private String name;
	private List<String> fileContent = new ArrayList<String>();

	/**
	 * Private constructor used to make a File object
	 * 
	 * @param nameTemp
	 *            the name for the file
	 * @param pathTemp
	 *            the path of the file
	 */
	private File(String nameTemp, String pathTemp) {
		name = nameTemp;
		path = pathTemp;
	}

	/**
	 * Factory method used to create an empty file instance
	 * 
	 * @param nameTemp
	 *            the name for the file
	 * @param pathTemp
	 *            the path of the file
	 * @return File instance of the File class
	 */
	public static File createAnEmptyFile(String nameTemp, String pathTemp) {
		return (new File(nameTemp, pathTemp));

	}

	/**
	 * Factory method for creating a file with the first line being given
	 * 
	 * @param firstLine
	 *            the first line of the new file
	 * @param nameTemp
	 *            the name for the file
	 * @param pathTemp
	 *            the path of the file
	 * @return File instance of the File class
	 */
	public static File createAFile(String firstLine, String nameTemp,
			String pathTemp) {
		File f = File.createAnEmptyFile(nameTemp, pathTemp);
		f.addLineToFile(firstLine);
		return f;
	}

	/**
	 * Method to return path of the file
	 * 
	 * @return path the path of the file
	 */
	public String path() {
		return path;
	}

	/**
	 * Method to return name of file
	 * 
	 * @return name the name of the file
	 */
	public String name() {
		return name;
	}

	/**
	 * This method returns the content of the file
	 * 
	 * @return contentStr the string containing the content for the file
	 */
	public String content() {
		String contentStr = "";
		for (String s : fileContent) {
			contentStr = contentStr + s + "\n";
		}
		return contentStr;
	}

	/**
	 * This method adds a line to the file
	 * 
	 * @param line
	 *            the line to be added to the file
	 */
	public void addLineToFile(String line) {
		fileContent.add(line);
	}

	/**
	 * This method erases all the content in the current file
	 */
	public void eraseFile() {
		fileContent = new ArrayList<String>();
	}

	/**
	 * Setter method to change the name of a file
	 * 
	 * @param newName
	 *            the new name of the file
	 */
	public void setName(String newName) {
		this.name = newName;
	}

	/**
	 * Setter method to change the name of the file
	 * 
	 * @param newPath
	 *            the new name of the file
	 */
	public void setPath(String newPath) {
		this.path = newPath;
	}

	/**
	 * Method used to return the "clone" a file object
	 * 
	 * @param f
	 *            File to be cloned
	 * @return newDIR clone of f
	 */
	public static File copyOfFile(File f) {
		File newFile = (new File(f.name, f.path));
		newFile.fileContent = f.fileContent;
		return newFile;
	}

}