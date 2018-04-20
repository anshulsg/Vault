package provider.server;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;

class AESEncrypt {
    private static final String ALGO = "AES";
    private static final byte[] keyValue =new byte[]
            { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };
    private static final Base64.Encoder encoder= Base64.getEncoder();
    private static final Base64.Decoder decoder= Base64.getDecoder();
    private Cipher cipher;
    private SecretKey key;
    AESEncrypt() throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        cipher= Cipher.getInstance(ALGO);
        key= new SecretKeySpec(keyValue, ALGO);
    }
    String encrypt(String val) throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] valBytes= val.getBytes();
        byte[] encry= cipher.doFinal(valBytes);
        return encoder.encodeToString(encry);
    }
    String decrypt(String val) throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException
    {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte valBytes[]= decoder.decode(val);
        byte[] decry= cipher.doFinal(valBytes);
        return new String(decry);
    }
}