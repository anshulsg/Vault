package user;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable{
    private String u_name, key;
    public User(String u, String p)
    {
        u_name= u;
        key=p;
    }
    public boolean compareUser(String user)
    {
        return u_name.equals(user);
    }
    public boolean compareKey(String key)
    {
        return this.key.equals(key);
    }
    public int compareTo(User obj)
    {
        if(obj.u_name.equals(u_name) && obj.key.equals(key)) return 0;
        else if(obj.u_name.equals(u_name)) return 1;
        else return -1;
    }
}
