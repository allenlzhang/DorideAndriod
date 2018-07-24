
package com.carlt.sesame.utility;

import com.ta.utdid2.android.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesCoderUtil {
    private static byte[] keys = {
            1, -1, 1, -1, 1, -1, 1, -1
    };

    private static String key = "zotyesoft";

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        DesCoderUtil.key = key;
    }

    /**
     * <p>
     * 对password进行MD5加密
     * 
     * @param source
     * @return
     * @return byte[] author: Heweipo
     */
    public static byte[] getMD5(byte[] source) {
        byte tmp[] = null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            tmp = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

    /**
     * <p>
     * 采用JDK内置类进行真正的加密操作
     * 
     * @param byteS
     * @param password
     * @return
     * @return byte[] author: Heweipo
     */
    private static byte[] encryptByte(byte[] byteS, byte password[]) {
        byte[] byteFina = null;
        try {// 初始化加密/解密工具
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(password);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(keys);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return byteFina;
    }

    /**
     * <p>
     * 采用JDK对应的内置类进行解密操作
     * 
     * @param byteS
     * @param password
     * @return
     * @return byte[] author: Heweipo
     */
    private static byte[] decryptByte(byte[] byteS, byte password[]) {
        byte[] byteFina = null;
        try {// 初始化加密/解密工具
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(password);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(keys);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return byteFina;
    }

    /**
     * <p>
     * Des加密strMing，然后base64转换
     * 
     * @param strMing
     * @param md5key
     * @return
     * @return String author: Heweipo
     */
    public static String encryptStr(String strMing, byte md5key[]) {
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        try {
            byteMing = strMing.getBytes("utf-8");
            if (md5key == null) {
                md5key = DesCoderUtil.getMD5(key.getBytes("utf-8"));
            }
            byteMi = encryptByte(byteMing, md5key);
            // strMi = base64Encoder.encode(byteMi);
            strMi = Base64.encodeToString(byteMi, Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }

    /**
     * <p>
     * Base64转换strMi,然后进行des解密
     * 
     * @param strMi
     * @param md5key
     * @return
     * @return String author: Heweipo
     */
    public static String decryptStr(String strMi, byte md5key[]) {
        byte[] byteMing = null;
        String strMing = "";
        try {
            // BASE64Decoder decoder = new BASE64Decoder();
            // byteMing = decoder.decodeBuffer(strMi);
            if (md5key == null) {
                md5key = DesCoderUtil.getMD5(key.getBytes("utf-8"));
            }
            byteMing = Base64.decode(strMi, Base64.DEFAULT);
            byteMing = decryptByte(byteMing, md5key);
            strMing = new String(byteMing);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            byteMing = null;
        }
        return strMing;
    }

    public static void main(String[] args) {

        String[] keys = {
                "leagsoft", "leagsoftpo", "leagsoftiu", "leagsoftyy", "leagsoftew", "leagsoftmm"
        };
        for (String key : keys) {
            String data = "我是learn_more，who are you?";
            try {
                System.out.println(DesCoderUtil.encryptStr(data,
                        DesCoderUtil.getMD5(key.getBytes("utf-8"))));
                System.out.println("--------------");
                System.out.println(DesCoderUtil.decryptStr(
                        DesCoderUtil.encryptStr(data, DesCoderUtil.getMD5(key.getBytes("utf-8"))),
                        DesCoderUtil.getMD5(key.getBytes("utf-8"))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
