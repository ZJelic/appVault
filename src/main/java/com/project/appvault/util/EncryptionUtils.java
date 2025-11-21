package com.project.appvault.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final String KEY = "16ByteSecretKey!";
    private static final String ALGO = "AES";

    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGO);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decoded));
    }
}
