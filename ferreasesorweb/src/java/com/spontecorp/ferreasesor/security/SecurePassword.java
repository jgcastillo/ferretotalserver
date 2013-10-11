package com.spontecorp.ferreasesor.security;

import java.security.MessageDigest;

/**
 *
 * @author jgcastillo
 */
public class SecurePassword {
    
    public static String encript(char[] password){
        String algorithm = "MD5";
        MessageDigest md = null;
        
        // Se obtienen los bytes que componen el password leido
        byte[] bytes = new byte[password.length * 2];
        for(int i = 0; i < password.length; i++){
            bytes[i*2] = (byte) (password[i] >> 8);
            bytes[i * 2 + 1] = (byte) password[i];
        }
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
        
        md.reset();
        md.update(bytes);
        byte[] encodedPassword = md.digest();
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                sb.append("0");
            }

            sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        return sb.toString();
    }
}
