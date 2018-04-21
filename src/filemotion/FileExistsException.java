package filemotion;

/**
 * Created by student on 8/1/17.
 */
public class FileExistsException extends Exception {
    private String name;
    FileExistsException(String file)
    {
        name=file;
    }
    public String toString()
    {
        return "File: "+name+" already exists. Please delete it first";
    }
}
