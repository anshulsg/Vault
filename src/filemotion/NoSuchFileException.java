package filemotion;

/**
 * Created by student on 8/1/17.
 */
public class NoSuchFileException extends Exception {
    private String name;
    NoSuchFileException(String file)
    {
        name= file;
    }
    public String toString()
    {
        return "File: "+name+" does not exist.";
    }
}
