package filemotion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StreamFileMotion {
    DataOutputStream outStream;
    DataInputStream inStream;
    public StreamFileMotion(DataInputStream in, DataOutputStream out)
    {
        inStream=in;
        outStream=out;
    }
    public void sendFile(String filename)
    {
        Files.copy(Paths.get(filename), outStream);
    }
    public void receiveFile(String filename)
    {}
    private void deleteFile(String filename)
    {}
    public boolean moveFile(String filename, String src, String dest)
    {}
}
