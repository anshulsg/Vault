package provider.server;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Creator{
    public static void main(String args[]) throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
        if(args.length!=1)
        {
            System.out.println("Usage: java Creator <server-port>");
            System.exit(1);
        }
        UserFileManager userFile= new UserFileManager();
        String action, user, pass;
        DataInputStream inStream;
        DataOutputStream outStream;
        Executor client_sched= Executors.newFixedThreadPool(5);
        try(ServerSocket sock= new ServerSocket(Integer.parseInt(args[0]))){
            while(true){
                try{
                    Socket client= sock.accept();
                    System.out.println("Connection Request from: "+
                            client.getInetAddress().getHostAddress());
                    outStream= new DataOutputStream(new BufferedOutputStream(
                            client.getOutputStream()));
                    inStream= new DataInputStream(new BufferedInputStream(
                            client.getInputStream()));
                    action= inStream.readUTF();
                    user=inStream.readUTF();
                    pass=inStream.readUTF();
                    if(action.equalsIgnoreCase("create")){
                        boolean flag=userFile.create(user, pass);
                        if(flag){
                            outStream.writeUTF("connected");
                            outStream.flush();
                        }
                        else{
                            outStream.writeUTF("User Already Exists");
                            outStream.flush();
                            continue;
                        }
                    }
                    else{
                        int query= userFile.login(user,pass);
                        switch(query){
                            case 0:
                                outStream.writeUTF("connected");
                                outStream.flush();
                                break;
                            case 1:
                                outStream.writeUTF("Incorrect Password");
                                outStream.flush();
                                break;
                            case -1:
                                outStream.writeUTF("User Does not exist");
                                outStream.flush();
                                break;
                        }
                        if(query!=0) continue;
                    }
                    client_sched.execute(new ClientHandler(client, user));
                }
                catch(IOException exc)
                {
                    System.out.println("IO Error with a client");
                }
                catch (Exception E)
                {}
            }
        }
        catch (IOException exc)
        {
            System.out.println("Connection Error:" + exc.toString());
            exc.printStackTrace();
        }
    }
}