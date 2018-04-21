package datainteraction;

import java.sql.*;

public class Database {
    private Connection db;
    private Statement statement;
    public Database(String location, String database, String user_name, String password) throws SQLException {
        db = DriverManager.getConnection("jdbc:mysql://" + location + "/" + database,
                user_name, password);
        statement = db.createStatement();
    }
    public String getString(String table_name) throws SQLException
    {
        ResultSet res= statement.executeQuery("SELECT * FROM "+table_name);
        ResultSetMetaData res_data= res.getMetaData();
        String returnable="";
        for(int i=0;i<res_data.getColumnCount();i++)
        {
            returnable+=res_data.getColumnName(i)+" ";
        }
        returnable+="\n";
        while(res.next())
        {
            for(int i=0; i<res_data.getColumnCount(); i++)
            {
                returnable+=res.getString(i)+" ";
            }
            returnable+="\n";
        }
        return returnable;
    }
    public void finalize() throws SQLException
    {
        statement.close();
        db.close();
    }
    public static void main(String args[]) throws Exception
    {
        Database db= new Database("192.168.5.154","TE3110db", "TE3110", "bateman");
        System.out.println("SUCCESSFUL");
    }
}
