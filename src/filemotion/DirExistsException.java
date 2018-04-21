package filemotion;

/**
 * Created by student on 8/1/17.
 */
public class DirExistsException extends Exception {
    private String name;
    DirExistsException(String directory)
    {
        name= directory;
    }
    public String toString()
    {
        return "Directory "+name+" already exists. Please use another name";
    }
}
