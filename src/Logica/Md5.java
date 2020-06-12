package Logica;
import java.util.*;
public class Md5 {

    public String algoritmoMd5(String message){
      try {
        java.security.MessageDigest md = java.security.MessageDigest
            .getInstance("MD5");
        byte[] array = md.digest(message.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
              .substring(1, 3));
        }
        return sb.toString();
      } catch (java.security.NoSuchAlgorithmException e) {
        System.out.println(e.getMessage());
      }
      return null;
    }

}
