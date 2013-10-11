package com.spontecorp.ferreasesor.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author jgcastillo
 */
public class Cifrador {

    private SecretKey key;
    private static final String CIPHER_INSTANCE = "AES/ECB/NoPadding";

    public Cifrador() {
        this.key = generaKey();
    }

    public String encrypt(String str) {
        try {
            Cipher ecipher = Cipher.getInstance(CIPHER_INSTANCE);
            ecipher.init(Cipher.ENCRYPT_MODE, key);

            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        } catch (InvalidKeyException e) {
        }
        return null;
    }

    public String decrypt(String str) {
        String result = ""; 
        try {
            Cipher dcipher = Cipher.getInstance(CIPHER_INSTANCE);
            dcipher.init(Cipher.DECRYPT_MODE, key);
            
            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            
            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            result = new String(utf8, "UTF8");
        } catch (BadPaddingException | IllegalBlockSizeException 
                 | IOException
                | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException e) {
            System.err.println("El error es: " + e);
        } 
        return result;
    }

    private SecretKey generaKey() {
        try {
            String passphrase = "la pequeña casa de la pradera";
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(passphrase.getBytes());
            key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
            return key;
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

}
