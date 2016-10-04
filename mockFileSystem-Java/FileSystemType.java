package filesystem;

public interface FileSystemType {

	public String name();
	public void setName(String newName);
	public String path();
	public void setPath(String newPath);
	public String toString();
}
