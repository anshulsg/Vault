package provider.server;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

class UserFileManager{
    private static final Path userFile= Paths.get(".user_info");
    private Vector<User> list= new Vector<>();
    private DataInputStream fileReader;
    private DataOutputStream fileWriter;
    private AESEncrypt enc;
    UserFileManager() throws IOException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
            NoSuchPaddingException
    {
        enc = new AESEncrypt();
        if(Files.notExists(userFile)) Files.createFile(userFile);
        fileReader= new DataInputStream(new BufferedInputStream(
                Files.newInputStream(userFile)));
        fileWriter= new DataOutputStream(new BufferedOutputStream(
                Files.newOutputStream(userFile, StandardOpenOption.APPEND)));
        try{
            while(true)
            {
                String user= fileReader.readUTF();
                String key= fileReader.readUTF();
                key= enc.decrypt(key);
                list.add(new User(user, key));
            }
        }
        catch(EOFException e) {// to exit from while loop using EOFException, Do nothing here.
        }
    }
    int login(String user, String key)
    {
        for(User u: list)
        {
            if(u.compareUser(user))
            {
                if(u.compareKey(key)) return 0;
                else return 1;
            }
        }
        return -1;
    }
    boolean create(String user, String pass) throws IOException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException
    {
        if(login(user,pass)==0 || login(user,pass)==1) return false;
        else{
            String passkey= enc.encrypt(pass);
            fileWriter.writeUTF(user);
            fileWriter.flush();
            fileWriter.writeUTF(passkey);
            fileWriter.flush();
            list.add(new User(user,pass));
            return true;
        }
    }
    public void finalize() throws IOException
    {
        fileReader.close();
        fileWriter.close();
    }
}