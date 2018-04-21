package user;

/**
 * Created by student on 8/1/17.
 */
public class NoSuchUserException extends Exception{
    private String val;
    public String toString()
    {
        return "User: "+val+" does not exist. Please create a new user"
                +" or login with the correct username";
    }
}
