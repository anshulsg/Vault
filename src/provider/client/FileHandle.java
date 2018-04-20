package provider.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class FileHandle {
    private DataInputStream inStream;
    private DataOutputStream outStream;
    FileHandle(DataInputStream in, DataOutputStream out)
    {
        inStream= in;
        outStream = out;
    }
    void sendFile(String file_name, boolean delete_after) throws IOException
    {
        Path file= Paths.get(file_name);
        if(Files.exists(file)){
            long size= Files.size(file);
            String name= file.getFileName().toString();
            outStream.writeUTF(name);
            outStream.flush();
            outStream.writeLong(size);
            outStream.flush();
            byte[] fileData= Files.readAllBytes(file);
            outStream.write(fileData);
            outStream.flush();
            if(delete_after) deleteFile(file_name);
        }
        else{
            System.out.println("File does not exist");
        }
    }
    void receiveFile(String file_name) throws IOException
    {
        Path file= Paths.get(file_name);
        outStream.writeUTF(file_name);
        outStream.flush();
        long size= inStream.readLong();
        DataOutputStream fileStream= new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(file, StandardOpenOption.CREATE)));
        byte data;
        for(long i=0; i<size; i++){
            data= inStream.readByte();
            fileStream.write(data);
        }
        fileStream.flush();
        fileStream.close();
    }
    private void deleteFile(String file_name) throws IOException
    {
        Files.deleteIfExists(Paths.get(file_name));
    }
}