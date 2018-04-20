package provider.client;

import java.awt.Desktop;
import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;

public class Creator {
    private FileHandle filesys;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    private Socket sock;
    private Creator(String user_type, String user, String password, String ip, int port) throws IOException, InvalidUserException
    {
        sock= new Socket(ip, port);
        inStream= new DataInputStream(new BufferedInputStream(sock.getInputStream()));
        outStream= new DataOutputStream((new BufferedOutputStream(sock.getOutputStream())));
        filesys= new FileHandle(inStream, outStream);
        outStream.writeUTF(user_type);
        outStream.flush();
        outStream.writeUTF(user);
        outStream.flush();
        outStream.writeUTF(password);
        outStream.flush();
        String acknowledgement= inStream.readUTF();

        if(!acknowledgement.equalsIgnoreCase("connected")){
            inStream.close();
            outStream.close();
            sock.close();
            throw new InvalidUserException(acknowledgement);
        }
        else System.out.println("successfully connected to backup server: "+ip+ " at port:"+ port);
    }
    private void sendFile(String file_name, boolean delete_flag) throws IOException
    {
        if(delete_flag) outStream.writeLong(4);
        else outStream.writeLong(1);
        outStream.flush();
        filesys.sendFile(file_name,delete_flag);
    }
    private void receiveFile(String file_name, boolean delete_flag) throws IOException
    {
        if(delete_flag) outStream.writeLong(5);
        else outStream.writeLong(2);
        outStream.flush();
        filesys.receiveFile(file_name);
    }
    private void exit() throws IOException
    {
        outStream.writeLong(0);
        outStream.flush();
    }
    private void viewFiles() throws IOException
    {
        outStream.writeLong(3);
        outStream.flush();
        long count=inStream.readLong();
        System.out.println("Files currently on server are:");
        System.out.println("User Directory: "+ inStream.readUTF());
        for(int j=1; j<count; j++){
            System.out.println(j+"."+inStream.readUTF());
        }
    }
    private void shareFile(String file_name, String user) throws IOException
    {
        outStream.writeLong(6);
        outStream.flush();
        outStream.writeUTF(file_name);
        outStream.flush();
        outStream.writeUTF(user);
        outStream.flush();
        String ack=inStream.readUTF();
        System.out.println(ack);
    }
    public static void main(String args[]) throws Exception
    {
        if(args.length!=5){
            System.out.println("Usage: java Creator <action: login/create> <user> <password> <server-ip> <server-port>");
            System.exit(1);
        }
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        String file_name;
        int choice;
        boolean flag= false;

        try {
            Creator proc = new Creator(args[0], args[1], args[2], args[3], Integer.parseInt(args[4]));
            do {
                System.out.println("Choose Operation:\n 1.Backup a file\n 2.Receive a file \n 3.View all files\n"+
                        " 4.Move file to server\n 5.Move file from server\n 6.Share a file with another user\n 7.Open A File \n 0.Exit");
                choice = Integer.parseInt(br.readLine());
                switch (choice) {
                    case 0:
                        proc.exit();
                        break;
                    case 4:flag= true;
                    case 1:
                        System.out.println("Enter File name:");
                        file_name=br.readLine();
                        System.out.println("Sending File: "+ file_name);
                        proc.sendFile(file_name, flag);
                        System.out.println("Sent Successfully");
                        flag=false;
                        break;
                    case 5:flag=true;
                    case 2:
                        System.out.println("Enter File name:");
                        file_name=br.readLine();
                        System.out.println("Receiving File: "+ file_name);
                        proc.receiveFile(file_name, flag);
                        System.out.println("Received Successfully");
                        flag=false;
                        break;
                    case 3:
                        proc.viewFiles();
                        break;
                    case 6:
                        System.out.println("Enter File Name:");
                        file_name= br.readLine();
                        System.out.println("Enter Recipient's Username");
                        String user= br.readLine();
                        proc.shareFile(file_name,user);
                        break;
                    case 7:
                        System.out.println("Enter File Name:");
                        file_name= br.readLine();
                        try {
                            if (Desktop.isDesktopSupported()) {
                                File openable = Paths.get(file_name).toFile();
                                Desktop.getDesktop().open(openable);
                            }
                        }
                        catch (IOException exc)
                        {
                            System.out.println("No Application to open this file type");
                        }
                        catch (IllegalArgumentException exc)
                        {
                            System.out.println("Requested file does not exist");
                        }
                        catch (Exception e)
                        {
                            System.out.println("Error in opening file");
                        }
                }
            }while(choice!= 0);
        }
        catch(InvalidUserException u){
            System.out.println(u);
        }
        catch(IOException exc){
            System.out.println("Error while connecting to server, client will now close.");
        }
    }
}