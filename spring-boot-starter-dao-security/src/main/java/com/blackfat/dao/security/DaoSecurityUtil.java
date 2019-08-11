package com.blackfat.dao.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangfeiyang
 * @Description
 * @create 2019-07-29 06:40
 * @since 1.0-SNAPSHOT
 */
@Component
@Slf4j
public class DaoSecurityUtil {
    private static final String iv = "blackfat@163.com";
    private static final int version = 0;
    private static IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
    // 加解密的key
    private static String securityKey;
    // 摘要的key
    private static String digestKey;
    private static boolean isCache;
    private static Map<String, Cipher> encryptCipherPoolMap = new ConcurrentHashMap();
    private static Map<String, Cipher> decryptCipherPoolMap = new ConcurrentHashMap();

    public DaoSecurityUtil() {
    }

    @Value("${blackfat.security.securityKey:blackfat@163.com")
    public void setSecurityKey(String securityKey) {
        DaoSecurityUtil.securityKey = securityKey;
    }

    @Value("${blackfat.security.digestKey:blackfat@163.com")
    public void setDigestKey(String digestKey) {
        DaoSecurityUtil.digestKey = digestKey;
    }

    @Value("${blackfat.security.isCache:true}")
    public void setIsCache(boolean isCache) {
        DaoSecurityUtil.isCache = isCache;
    }


    private static Cipher getEncryptCipher(String key) {
        Cipher cipher = (Cipher)encryptCipherPoolMap.get(key);
        if (cipher != null) {
            return cipher;
        } else {
            String secretkey = key + securityKey;

            try {
                cipher = Cipher.getInstance("AES/CFB/NoPadding");
                SecretKeySpec secretKeySpec = new SecretKeySpec(subBytes(sha256(secretkey.getBytes()), 0, 16), "AES");
                cipher.init(1, secretKeySpec, ivParameterSpec);
                encryptCipherPoolMap.put(key, cipher);
                return cipher;
            } catch (Exception e) {
                log.error("getEncryptCipher", e);
                return null;
            }
        }
    }

    private static Cipher getDecryptCipher(String key) {
        Cipher cipher = (Cipher)decryptCipherPoolMap.get(key);
        if (cipher != null) {
            return cipher;
        } else {
            String secretkey = key + securityKey;

            try {
                cipher = Cipher.getInstance("AES/CFB/NoPadding");
                SecretKeySpec secretKeySpec = new SecretKeySpec(subBytes(sha256(secretkey.getBytes()), 0, 16), "AES");
                cipher.init(2, secretKeySpec, ivParameterSpec);
                decryptCipherPoolMap.put(key, cipher);
                return cipher;
            } catch (Exception e) {
                log.error("getDecryptCipher", e);
                return null;
            }
        }
    }

    /**
     * 加密后的字符长度 = length(content)*4/3+2+2
     * @param key
     * @param content
     * @return
     */
    public static String encrypt(String key, String content) {
        if (isSafe(content)) {
            return content;
        } else {
            try {
                // Base64将三个字节转化成四个字节，因此Base64编码后的文本，会比原文本大出三分之一左右
                String cipher = Base64.encodeBase64String(getEncryptCipher(key).doFinal(content.getBytes("utf-8")));
                return cipher + sign(cipher);
            } catch (Exception e) {
                log.error("encrypt", e);
                return null;
            }
        }
    }

    public static String decrypt(String key, String content) {
        if (!isSafe(content)) {
            return content;
        } else {
            content = content.substring(0, content.length() - 4);

            try {
                return new String(getDecryptCipher(key).doFinal(Base64.decodeBase64(content)));
            } catch (Exception e) {
                log.error("decrypt", e);
                return null;
            }
        }
    }

    public static Long encrypt(String key, Long content) {
        if (Math.abs(content) > 72057594037927936L) {
            return null;
        } else if (isSafe(content)) {
            return content;
        } else {
            byte[] tail = subBytes(long2Byte(content), 0, 7);
            byte[] head = new byte[]{0};
            if (content < 0L) {
                head[0] = -128;
            } else {
                head[0] = 112;
            }

            try {
                return byte2Long(jointBytes(getEncryptCipher(key).doFinal(revertBytes(tail)), head));
            } catch (Exception e) {
                log.error("encrypt", e);
                return null;
            }
        }
    }

    public static Long decrypt(String key, Long content) {
        if (!isSafe(content)) {
            return content;
        } else {
            byte[] tail = subBytes(long2Byte(content), 0, 7);
            byte[] head = new byte[]{0};
            if (content < 0L) {
                head[0] = -1;
            } else {
                head[0] = 0;
            }

            try {
                return byte2Long(jointBytes(revertBytes(getDecryptCipher(key).doFinal(tail)), head));
            } catch (Exception e) {
                log.error("decrypt", e);
                return null;
            }
        }
    }

    public static final Boolean isSafe(Long x) {
        x = x >> 56 & 255L;
        return x == 112L || x == 128L;
    }

    public static final Boolean isSafe(String x) {
        if (x.length() <= 4) {
            return false;
        } else {
            String content = x.substring(0, x.length() - 4);
            String sign = x.substring(x.length() - 4);
            return sign.equals(sign(content));
        }
    }

    private static String sign(String content) {
        byte[] lens = new byte[]{(byte)(content.length() % 16 << 2 | 0)};

        try {
            return Base64.encodeBase64String(jointBytes(makeCRC(content.getBytes("utf-8")), lens));
        } catch (Exception e) {
            log.error("sign", e);
            return null;
        }
    }


    public static String generateKey(String content) {
        return md5(md5(digestKey + content));
    }

    private static byte[] long2Byte(Long x) {
        byte[] bb = new byte[8];

        for(int i = 0; i < 8; ++i) {
            bb[i] = (byte)((int)(x >> 8 * i & 255L));
        }

        return bb;
    }

    private static Long byte2Long(byte[] bb) {
        long value = 0L;

        for(int i = 0; i < 8; ++i) {
            value |= (long)(bb[i] & 255) << 8 * i;
        }

        return value;
    }

    private static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    private static byte[] jointBytes(byte[] a, byte[] b) {
        byte[] bs = new byte[a.length + b.length];
        System.arraycopy(a, 0, bs, 0, a.length);
        System.arraycopy(b, 0, bs, a.length, b.length);
        return bs;
    }

    private static byte[] revertBytes(byte[] x) {
        byte[] bs = new byte[x.length];

        for(int i = 0; i < bs.length; ++i) {
            bs[i] = x[bs.length - i - 1];
        }

        return bs;
    }

    private static byte[] sha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(data);
        } catch (Throwable var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static byte[] md5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(data);
        } catch (Throwable var2) {
            var2.printStackTrace();
            return null;
        }
    }

    private static String md5(String data) {
        return new String(Hex.encodeHex(md5(data.getBytes())));
    }

    private static byte[] makeCRC(byte[] data) {
        byte[] buf = new byte[data.length];

        int len;
        for(len = 0; len < data.length; ++len) {
            buf[len] = data[len];
        }

        len = buf.length;
        int crc = 65535;

        for(int pos = 0; pos < len; ++pos) {
            if (buf[pos] < 0) {
                crc ^= buf[pos] + 256;
            } else {
                crc ^= buf[pos];
            }

            for(int i = 8; i != 0; --i) {
                if ((crc & 1) != 0) {
                    crc >>= 1;
                    crc ^= 40961;
                } else {
                    crc >>= 1;
                }
            }
        }

        byte[] crcBytes = new byte[]{(byte)(255 & crc), (byte)(255 & crc >> 8)};
        return crcBytes;
    }

    public static void main(String[] args) {
        String decrypty = decrypt("b5766a1a18da4ee6b5a0fda694b4de92","wVIjUEo8BQwepXw=qCQA");
        System.out.println(decrypty);
    }
}
