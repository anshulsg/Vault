package provider.server;

class User implements Comparable<User>{
    private String u_name, key;
    User(String u, String p)
    {
        u_name= u;
        key=p;
    }
    boolean compareUser(String user)
    {
        return u_name.equals(user);
    }
    boolean compareKey(String key)
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