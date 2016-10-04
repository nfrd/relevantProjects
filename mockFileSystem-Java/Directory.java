package filesystem;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @class Directory class
 * @description A class to make a root directory, as well as directories in the
 *              root directory
 */
public class Directory implements FileSystemType {
	private String path;
	private List<FileSystemType> contentDIR = new ArrayList<FileSystemType>();
	private Directory parent;
	private String name;
	private Exception sameNameUsed = new Exception("Invalid name, name "
			+ "already in use.");
	private static List<String> sessionPaths = new ArrayList<String>();
	private static Directory rootDIR = null;
	private static Directory currentDIR = null;

	/**
	 * Private constructor that makes a directory object with path p and sets
	 * the parent to the directory object par
	 * 
	 * @param loc
	 *            path location of the directory
	 * @param par
	 *            parent directory of the directory
	 */
	private Directory(String loc, Directory par) {
		path = loc;
		parent = par;
		sessionPaths.add(path);

	}

	/**
	 * Factory method 1 to create a root directory object (this should only be
	 * ever called once)
	 * 
	 * @return rootDIR directory object that is made by factory method
	 */
	public static Directory createRootDirectory() {

		Directory rootDIR = (new Directory("/", null));
		Directory.currentDIR = rootDIR;
		Directory.rootDIR = rootDIR;
		rootDIR.name = "/";
		sessionPaths = new ArrayList<String>();
		return rootDIR;
	}

	/**
	 * Method to return path of the directory
	 * 
	 * @return path path of the directory
	 */
	public String path() {
		return path;
	}

	/**
	 * Method to return the parent of this directory. root will return if parent
	 * is called on the root
	 * 
	 * @return parent the parent of the directory
	 */
	public Directory parent() {
		if (parent != null) {
			return parent;
		}
		return this;

	}

	/**
	 * A method to add to a file to the current directory content
	 * 
	 * @param fileName
	 *            name of file to be added to directory
	 */
	public void addFile(String fileName) {
		if (!this.anySameName(fileName)) {
			File f = File.createAnEmptyFile(fileName, this.path() + fileName);
			contentDIR.add(f);
		}
	}

	/**
	 * A method to add to a file to the current directory content
	 * 
	 * @param file
	 *            file object to be added to directory
	 */
	public void addFile(File file) {
		if (!this.anySameName(file.name())) {
			file = File.copyOfFile(file);
			file.setPath(this.path + file.name());
			contentDIR.add(file);
		}
	}

	/**
	 * This method deletes a file from the directory
	 * 
	 * @param fileName
	 *            the name of the file to be deleted
	 */
	public void deleteFile(String fileName) {
		if (this.fileExists(fileName)) {
			File fileToDel = (File) this.accessItem(fileName);
			contentDIR.remove(fileToDel);
		}
	}

	/**
	 * This method creates a directory in the instance by creating a new
	 * directory object with the name that is passed in call and sets it's path
	 * appropriately
	 * 
	 * @param nameTemp
	 *            the name of the directory to be created
	 */
	public void addADirectoryByName(String nameTemp) throws Exception {
		if (this.anySameName(nameTemp)) {
			throw this.sameNameUsed;
		} else {
			Directory newDIR = (new Directory(this.path() + nameTemp + "/",
					this));
			newDIR.name = nameTemp;
			contentDIR.add(newDIR);
		}
	}

	/**
	 * This method adds a directory into the directory object
	 * 
	 * @param dir
	 *            the directory object to be added
	 */
	public void addDirectory(Directory dir) {
		if (this.anySameName(dir.name())) {
			System.out.println(sameNameUsed);
		} else {
			Directory dirCopy = copyOfDirectory(dir);
			// setting the parent and path of the directory added
			dirCopy.parent = this;
			dirCopy.path = this.path + dirCopy.name + "/";

			correctPaths(dirCopy);
			contentDIR.add(dirCopy);
			sessionPaths.add(dirCopy.path());
		}
	}

	/**
	 * Method used to return the "clone" a directory object
	 * 
	 * @param d
	 *            Directory to be cloned
	 * @return newDIR clone of d
	 */
	public static Directory copyOfDirectory(Directory d) {
		Directory newDIR = (new Directory(d.path, d.parent));
		newDIR.name = d.name;
		newDIR.contentDIR = d.contentDIR;
		return newDIR;
	}

	/**
	 * This is a helper function used to correct the paths of everything in a
	 * directory. This is mainly used when a directory is added that has already
	 * been created. This is probably an action of cp or mv, or any command that
	 * moves an existing directory somewhere else changing the paths.
	 * 
	 * @param dir
	 *            the directory for which the paths have to be corrected
	 */
	private void correctPaths(Directory dir) {
		// looping through every object in the directory
		for (FileSystemType fileOrDir : dir.contentDIR) {
			if (fileOrDir instanceof Directory) {
				// if an object is a directory, correct its path and the path
				// of all its content using recursion
				Directory tempDir = copyOfDirectory(((Directory) fileOrDir));
				tempDir.setPath(dir.path + tempDir.name + "/");
				correctPaths(tempDir);
			} else if (fileOrDir instanceof File) {
				// if its a file, correct its path
				File tempFile = File.copyOfFile(((File) fileOrDir));
				tempFile.setPath(dir.path + tempFile.name());
			}
		}
	}

	/**
	 * This method deletes a directory in the directory object
	 * 
	 * @param dirName
	 *            the name of the directory to be deleted
	 */
	public void deleteDirectory(String dirName) {
		if (this.anySameName(dirName)) {
			Directory dirToDel = (Directory) this.accessItem(dirName);
			sessionPaths.remove(dirToDel.path());
			contentDIR.remove(dirToDel);
		}
	}

	/**
	 * A getter to get the ArrayList of the directory
	 * 
	 * @return contentDIR the array list containing the content of the directory
	 */
	public List<FileSystemType> content() {
		return contentDIR;
	}

	/**
	 * Returns the name of the directory, this is not the name of the path it is
	 * basically the name of the folder
	 * 
	 * @return name the name of the directory
	 */
	public String name() {
		return name;
	}

	/**
	 * If an object exists with the parameter name that is passed, it will
	 * return the object, else it will return null
	 * 
	 * @param name
	 *            the name of the item to be accessed
	 * @return obj the object of FileSystemType
	 */
	public FileSystemType accessItem(String name) {
		for (Object obj : contentDIR) {
			if (obj instanceof Directory) {
				if (((Directory) obj).name().equals(name)) {
					return (Directory) obj;
				}
			} else if (obj instanceof File) {
				if (((File) obj).name().equals(name)) {
					return (File) obj;
				}

			}

		}
		return null;
	}

	/**
	 * Overrides the toString function to provide the name of the directory
	 * 
	 * @return this.name() the name of the directory
	 */
	@Override
	public String toString() {
		return this.name();
	}

	/**
	 * This is a helper function the class uses to check if a path of a
	 * directory exists or not
	 * 
	 * @param path
	 *            the path that is being checked to see if it exists
	 * @return boolean boolean indicating if the directory path exist
	 */
	public boolean doesDirectoryPathExist(String path) {
		return (sessionPaths.contains(path));

	}

	/**
	 * This is a method used to check if a directory with the name passed exists
	 * in the directory instance that this method is used with
	 * 
	 * @param name
	 *            name of the directory to be checked
	 * @return boolean boolean that indicates if the directory exists or not
	 */
	public boolean directoryExists(String name) {
		for (Object obj : contentDIR) {
			if (obj instanceof Directory) {
				if (((Directory) obj).name().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This is a method used to check if a file with the name passed exists in
	 * the directory instance that this method is used with
	 * 
	 * @param name
	 *            name of the file to be checked
	 * @return boolean boolean that indicates if the file exists or not
	 */
	public boolean fileExists(String name) {
		for (Object obj : contentDIR) {
			if (obj instanceof File) {
				if (((File) obj).name().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This is a helper function the class uses to check if the name exists as a
	 * file or a directory
	 * 
	 * @param name
	 *            the name to be checked
	 * @return boolean indicator if the name exists or not
	 */
	public boolean anySameName(String name) {
		return (this.fileExists(name) || this.directoryExists(name));
	}

	/**
	 * This function returns a list of all the paths created on the current file
	 * system
	 * 
	 * @return sessionPaths the array list containing the paths in the session
	 */
	public static List<String> getSessionPaths() {
		return sessionPaths;
	}

	/**
	 * This function returns the current directory
	 * 
	 * @return Directory.currentDIR the current directory
	 */
	public static Directory getCurrentDirectory() {
		return Directory.currentDIR;
	}

	/**
	 * This method sets the current directory to something else
	 * 
	 * @param newCurrent
	 *            the new current directory to be set
	 */
	public static void setCurrentDirectory(Directory newCurrent) {
		Directory.currentDIR = newCurrent;
	}

	/**
	 * This method returns the root directory for current session
	 * 
	 * @return Directory.rootDIR the root directory of the file system
	 */
	public static Directory getRootDirectory() {
		return Directory.rootDIR;
	}

	/**
	 * Setter method to change the name of a directory
	 * 
	 * @param newName
	 *            the new name of the directory
	 */
	public void setName(String newName) {
		this.name = newName;
	}

	/**
	 * Setter method to change the name of the directory
	 * 
	 * @param newPath
	 *            the new name of the directory
	 */
	public void setPath(String newPath) {
		this.path = newPath;
	}
	
}
