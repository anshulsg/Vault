package user;

/**
 * Created by student on 8/1/17.
 */
public class UserExistsException extends Exception {
    private String val;
    UserExistsException(String user)
    {
        val=user;
    }
    public String toString()
    {
        return "User: "+val+" already exists. Please pick a different username";
    }
}
