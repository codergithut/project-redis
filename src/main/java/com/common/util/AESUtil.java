package com.common.util;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/6/16
 * @description 加密具体实现的方法，这个方法是采用网上提供的AES加密，可以继承Security实现自己的加密方法
 * Security是模板方法尽量不要修改，如需修改请注意每个方法的作用
 */

@Service
@Component
public class AESUtil {

    public static byte[] getRawKey(String key) {
        return getRawKey(key.getBytes());
    }

    public byte[] getDecrKey() {
        return getRawKey("gtmap");
    }

    public byte[] getEncrKey() {
        return getRawKey("gtmap");
    }


    public String decrypt(String encrypted) {
        try {
            byte[] bytes = hexStringToBytes(encrypted);
            SecretKeySpec secretKeySpec = new SecretKeySpec(getDecrKey(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypted = cipher.doFinal(bytes);
            return new String(decrypted);
        } catch (Exception e) {
            return "";
        }
    }


    public String encrypt(String clearPwd) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(getEncrKey(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            if(null!=clearPwd && !clearPwd.isEmpty()){
                byte[] encypted = cipher.doFinal(clearPwd.getBytes());
                return byte2hex(encypted);
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }


    public static byte[] getRawKey(byte[] seed) {
        byte[] rawKey = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed);
            // AES加密数据块分组长度必须为128比特，密钥长度可以是128比特、192比特、256比特中的任意一个
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            rawKey = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
        }
        return rawKey;
    }

    private static String byte2hex(byte[] byteArray)
    {
        if ((byteArray == null) || (byteArray.length < 1))
        {
            throw new IllegalArgumentException(
                    "this byteArray must not be null or empty");
        }
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; ++i)
        {
            if ((byteArray[i] & 0xFF) < 16)
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return hexString.toString().toLowerCase();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
