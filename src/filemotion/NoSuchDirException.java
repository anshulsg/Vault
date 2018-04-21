package filemotion;

/**
 * Created by student on 8/1/17.
 */
public class NoSuchDirException extends Exception {
    private String name;
    NoSuchDirException(String directory){
        name=directory;
    }
    public String toString()
    {
        return "Directory: "+name+" does not exist. Please create it first.";
    }
}
