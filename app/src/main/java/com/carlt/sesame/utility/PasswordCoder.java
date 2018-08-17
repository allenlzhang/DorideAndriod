
package com.carlt.sesame.utility;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class PasswordCoder {

    private static final String CHARSET = "UTF-8";

    // 密钥
    private final static String secretKey = "LY";

    // 向量
    private final static String iv = "01234567";

    /**
     * 加密
     * 
     * @param plainText 普通文本
     * @throws Exception
     */
    public static String encode(String plainText) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
        Key deskey = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes(CHARSET));
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(CHARSET));

        return new String(android.util.Base64.encode(encryptData, android.util.Base64.DEFAULT),
                CHARSET);
    }

    /**
     * 解密
     * 
     * @param encryptText 加密文本
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes(CHARSET));
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        Key deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes(CHARSET));
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] decryptData = cipher.doFinal(android.util.Base64.decode(encryptText,
                android.util.Base64.DEFAULT));

        return new String(decryptData, CHARSET);
    }

    public static String encodeSHA1(String string) {
        String saltedPass = mergePasswordAndSalt(string, secretKey);
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] digest = null;
        try {
            digest = messageDigest.digest(saltedPass.getBytes(CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(CHARSET + "not supported!");
        }
        return new String(Hex.encodeHex(digest));
    }

    public static String encodeMD5(String string) {
        String saltedPass = mergePasswordAndSalt(string, secretKey);
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] digest = null;
        try {
            digest = messageDigest.digest(saltedPass.getBytes(CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(CHARSET + "not supported!");
        }
        return new String(Hex.encodeHex(digest));
    }

    /**
     * 添加额外规则
     * 
     * @param string
     * @param salt
     * @param
     * @return
     */
    private static String mergePasswordAndSalt(String string, Object salt) {
        if (string == null) {
            string = "";
        }
        return string + "{" + salt + "}";
    }
}
