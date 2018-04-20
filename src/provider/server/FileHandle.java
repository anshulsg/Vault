package provider.server;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;

class FileHandle {
    private String dir;
    private DataOutputStream outStream;
    private DataInputStream inStream;
    FileHandle(String user,DataInputStream in, DataOutputStream out) throws IOException
    {
        dir= user;
        if(!Files.isDirectory(Paths.get(dir))){
            Files.createDirectory(Paths.get(dir));
        }
        inStream=in;
        outStream=out;
    }
    void receiveFile(String file_name) throws IOException
    {
        long file_size= inStream.readLong();
        Path file= Paths.get(dir+"/"+file_name);
        DataOutputStream fileStream= new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(file, StandardOpenOption.CREATE)));
        byte data;

        for(long var=0; var<file_size; var++)
        {
            data=inStream.readByte();
            fileStream.write(data);
        }
        fileStream.flush();
        fileStream.close();
    }
    void sendFile(String file_name, boolean delete_after) throws IOException
    {
        Path file= Paths.get(dir+"/"+file_name);
        if(Files.exists(file))
        {
            outStream.writeLong(Files.size(file));
            outStream.flush();

            byte[] fileData= Files.readAllBytes(file);
            outStream.write(fileData);
            outStream.flush();
            if(delete_after) deleteFile(file_name);
        }
        else System.out.println("Not found requested file");
    }
    void viewAllFiles() throws IOException
    {
        Stream<Path> files= Files.walk(Paths.get(dir));
        outStream.writeLong(files.count());
        outStream.flush();
        files=Files.walk(Paths.get(dir));
        List<Path> vals= files.collect(Collectors.toList());
        for(Path file:vals){
            outStream.writeUTF(file.getFileName().toString());
            outStream.flush();
        }
    }
    void deleteFile(String file_name) throws IOException
    {
        Files.deleteIfExists(Paths.get(dir+"/"+file_name));
    }
    void shareFile(String file_name, String user) throws IOException
    {
        if(Files.exists(Paths.get(dir+"/"+file_name))){
            if(Files.isDirectory(Paths.get(user))){
                if(Files.notExists(Paths.get(user+"/"+file_name))){
                    Files.copy(Paths.get(dir+"/"+file_name), Paths.get(user+"/"+file_name));
                    outStream.writeUTF("Successfully Shared");
                }
                else outStream.writeUTF("User already has a similar named file");
            }
            else outStream.writeUTF("Invalid user name");
        }
        else outStream.writeUTF("File does not exist");
        outStream.flush();
    }
}