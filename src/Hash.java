import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
This class is responsible to do hash and return hashString
It can do:
- return SHA256 string for any given string.
 */
public class Hash {
    MessageDigest MD = null;
    public Hash(){
        try {
            MD = MessageDigest.getInstance("SHA-256");

        } catch (NoSuchAlgorithmException e) {
            System.out.println("error in initializing MD");
        }
    }

    public String doHash(String s){

        byte[] bytesHash;
        String hashedString = null;
        try {
            bytesHash = MD.digest(s.getBytes("UTF-8"));
            hashedString = DatatypeConverter.printHexBinary(bytesHash);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error in hashing string");
        }

        return hashedString;
    }
}
