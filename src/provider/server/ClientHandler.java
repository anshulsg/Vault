package provider.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private FileHandle filesys;
    private String user;
    ClientHandler(Socket client, String username) throws IOException, InterruptedException
    {
        outStream= new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        inStream= new DataInputStream(new BufferedInputStream(client.getInputStream()));
        user= username;
        filesys= new FileHandle(user, inStream, outStream);
    }
    public void run()
    {
        System.out.println(user+" has connected.");
        try{
            long choice;
            boolean flag=false;
            String file_name;
            do{
                choice = inStream.readLong();
                switch ((int) choice) {
                    case 0:
                        System.out.println(user+" has exited.");
                        break;
                    case 4:
                    case 1:
                        file_name= inStream.readUTF();
                        filesys.receiveFile(file_name);
                        System.out.println("Received file "+file_name+" from "+user);
                        break;
                    case 5:flag=true;
                    case 2:
                        file_name= inStream.readUTF();
                        filesys.sendFile(file_name, flag);
                        System.out.println("Sent file "+file_name+" to "+user);
                        flag=false;
                        break;
                    case 3:
                        filesys.viewAllFiles();
                        break;
                    case 6:
                        file_name= inStream.readUTF();
                        String user_name= inStream.readUTF();
                        filesys.shareFile(file_name,user_name);
                        break;
                }
            }while(choice != 0);
        }
        catch(IOException exc){
            System.out.println("There was an error when reading/writing data. "+user+" disconnected.");
        }
    }
}