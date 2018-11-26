package com.blackfat.encrypt.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/11/26-14:01
 */
public class AesEncryptUtils {

    private static final String KEY = "abcdef0123456789";   // 密钥必须是16位的
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";//  加密算法/操作模式/填充模式

    public static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64Decode(String base64Code) throws Exception {
        return Base64.decodeBase64(base64Code);
    }

    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);   // 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));//  初始化为加密模式的密码器
        return cipher.doFinal(content.getBytes("utf-8")); // 加密
    }

    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);     // 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));// 初始化为解密模式的密码器
        byte[] decryptBytes = cipher.doFinal(encryptBytes);   //解密
        return new String(decryptBytes);
    }

    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    public static void main(String[] args) throws Exception {
        String content = "{\n" +
                "\"id\":1,\n" +
                "\"name\":\"wangfeiyang\",\n" +
                "\"age\":\"27\"\n" +
                "}";
        System.out.println("加密前：" + content);

        String encrypt = aesEncrypt(content, KEY);
        System.out.println(encrypt.length() + ":加密后：" + encrypt);

        String decrypt = aesDecrypt(encrypt, KEY);
        System.out.println("解密后：" + decrypt);
    }
}
