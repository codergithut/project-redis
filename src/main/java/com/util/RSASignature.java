package com.util;

import org.apache.xml.security.utils.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/4/18
 * @description 数字签名验证工具类
 */
public class RSASignature
{
    private static final String KEY_ALGORTHM = "RSA";
    private static final String SIGNATURE = "SHA1WithRSA";

    static String signature1;

//    public static void main(String[] args) throws IOException,
//            NullPointerException, DocumentException, NoMatchXMLDocumentException
//    {
//        String oldMsg = "";
//        File file = new File("D:\\RSA.xml");
//        byte[] bytes = FileUtil.getBytes(file);
//        String xmlInfos = new String(bytes, "UTF-8");
//        String key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANP1Ow58aqG/7dPF5T+M3CyQ6Vc8XfkZ9+bigaNTILZqOmUSJFn7nvHnBhe2qHUHe6WdJBjU8s78/DyLSODW7BCg7SLMaTfUKsiWgWkoBy97kl8bHRcErVx9+wakW+PMv+C9Fkce+oY4fUE7JJPwRw6sOe5PdjcW1hsZ14OUfoAZAgMBAAECgYEAt0IFEI5Dx5vg7cPhZOPODX4xMWqROWnZa7eVLHgYBX+tA2v/IAmssCv1mZUk6yJQJq3J4upjENGs6E/o7/UC3f3juJCIAj0xyJ49x8FoKfHob+VZkkZEBIt7mPyvo1Pl9vW6dE50SQGrEkIIVOZdlKeWKzMKhsyFZqKGNM5cAgECQQD6EeHoHnakfQicM7LHwho2QEtJO+CjKl3EhmDbE3qKGn/W/xVpaDB/EqTfSR45r48009s1bmFjxIslIjNT10lBAkEA2Pv6uPZenhC/cvQNzUZIhHdUJti086N8AAedMkW9sr1Bp3O3hspKvvNYtzwEfGvQWa42qVPLrIjmAmlxNn9o2QJAWFJ2kpAn4ULUBq9vxoP01BJzRMNkPNzaz22Sye2gSyS+4EWp31fQQSFpn/9oMIGkN8lX1BBPT3h8mLnynPsdwQJBALkanNTVN/pYBzqlgHCxmIOI2L0a+aMuwEW2OR/95spoMW4MhW/zerhTGEeYZ6tMvj0DJZZl6caSMsWz9eSr5GECQDq4SKdjEu21caizzRHqyTIlo3675w+iPPrWxx9sKbqfN3OhEPgA7DKdH7s19Smw3kszdu0hVVAR0RpznGAp0Y4=\n";
//        String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDT9TsOfGqhv+3TxeU/jNwskOlXPF35Gffm4oGjUyC2ajplEiRZ+57x5wYXtqh1B3ulnSQY1PLO/Pw8i0jg1uwQoO0izGk31CrIloFpKAcve5JfGx0XBK1cffsGpFvjzL/gvRZHHvqGOH1BOyST8EcOrDnuT3Y3FtYbGdeDlH6AGQIDAQAB";
//        String newString = getNewMsgWithSignature(xmlInfos, key);
//        try {
//            System.out.println(CheckXmlByPulKey(newString, public_key));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

//    public static String getNewMsgWithSignature(String oldMsg, String key)
//            throws DocumentException, NoMatchXMLDocumentException,
//            NullPointerException
//    {
//        if ((oldMsg == null) || (key == null) || ("".equals(oldMsg))
//                || ("".equals(key)))
//        {
//            throw new NullPointerException("消息源、秘钥不能为空.");
//        }
//        String msgId = getTextByXpath("/Message/Head/BizMsgID", oldMsg);
//        String msg = oldMsg.replaceFirst("<BizMsgID>[\\s\\S]*</BizMsgID>",
//                "<BizMsgID></BizMsgID>");
//        signature1 = getSignature(msg, key);
//        String newMsg = msg.replaceFirst("<BizMsgID></BizMsgID>",
//                "<BizMsgID>" + msgId + "</BizMsgID>\r\n" + "<DigitalSign>"
//                        + signature1 + "</DigitalSign>");
//        return newMsg;
//    }

    private static String getTextByXpath(String xpath, String xml)
            throws Exception
    {
        Document document = DocumentHelper.parseText(xml);
        Node destNode = document.selectSingleNode(xpath);
        if (destNode == null)
        {
            throw new Exception(
                    "XML文档中找不到匹配的路径/Message/Head/BizMsgID,无法解析文档.");
        }
        return destNode.getText();
    }

//    public static String getSignature(String msg, String key)
//    {
//        return byte2hex(getSignature(getKeyBytes(msg), key));
//    }

    private static byte[] getKeyBytes(String msg)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes("UTF-8"));
            return md.digest();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

//    private static byte[] getSignature(byte[] summary, String privateKey)
//    {
//        try
//        {
//            byte[] keyBytes = str2BASE64(privateKey);
//            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
//                    keyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
//            Signature signature = Signature.getInstance("SHA1WithRSA");
//            signature.initSign(privateK);
//            signature.update(summary);
//            byte[] signed = signature.sign();
//            return signed;
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private static String byte2hex(byte[] byteArray)
//    {
//        if ((byteArray == null) || (byteArray.length < 1))
//        {
//            throw new IllegalArgumentException(
//                    "this byteArray must not be null or empty");
//        }
//        StringBuilder hexString = new StringBuilder();
//        for (int i = 0; i < byteArray.length; ++i)
//        {
//            if ((byteArray[i] & 0xFF) < 16)
//                hexString.append("0");
//            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
//        }
//
//        return hexString.toString().toLowerCase();
//    }

    private static byte[] str2BASE64(String key) throws Exception
    {
        return Base64.decode(key);
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
    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static boolean CheckXmlByPulKey(String xmlContent, String publicKey) throws Exception {
        String msgId = getTextByXpath("/Message/Head/BizMsgID", xmlContent);
        String signature = getTextByXpath("/Message/Head/DigitalSign", xmlContent);
        String msg = xmlContent.replaceFirst("<BizMsgID>" + msgId + "</BizMsgID>\r\n" + "<DigitalSign>"
                        + signature + "</DigitalSign>",
                "<BizMsgID></BizMsgID>");

        return verify(getKeyBytes(msg), publicKey, hexStringToBytes(signature.toString().toLowerCase()));

    }

    public static boolean verify(byte[] data, String publicKey, byte[] sign)
            throws Exception
    {
        byte[] keyBytes = str2BASE64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(sign);
    }
}
