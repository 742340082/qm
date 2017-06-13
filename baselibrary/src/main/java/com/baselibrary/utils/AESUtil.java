package com.baselibrary.utils;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil
{
    private static byte[] key;

    static
    {
        key=getKey();
    }
    public static String decode(String decode)
    {
        return new String(decrypt(parseHexStr2Byte(decode)));
    }

    private static byte[] decrypt(byte[] bytes)
    {
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(key, "AES");
        try
        {
            Cipher localCipher = Cipher.getInstance("AES");
            localCipher.init(2, localSecretKeySpec);
            bytes = localCipher.doFinal(bytes);
            return bytes;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String encode(String encode)
    {
        return parseByte2HexStr(encrypt(encode.getBytes()));
    }

    private static byte[] encrypt(byte[] bytes)
    {
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(key, "AES");
        try
        {
            Cipher localCipher = Cipher.getInstance("AES");
            localCipher.init(1, localSecretKeySpec);
            bytes = localCipher.doFinal(bytes);
            return bytes;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getKey()
    {
        try
        {
            Object localObject = KeyGenerator.getInstance("AES");
            ((KeyGenerator)localObject).init(128);
            localObject = ((KeyGenerator)localObject).generateKey().getEncoded();
            return (byte[])localObject;
        }
        catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
        {
            localNoSuchAlgorithmException.printStackTrace();
        }
        return null;
    }

    public static String parseByte2HexStr(byte[] bytes)
    {
        StringBuffer localStringBuffer = new StringBuffer();
        int i = 0;
        while (i < bytes.length)
        {
            String str2 = Integer.toHexString(bytes[i] & 0xFF);
            String str1 = str2;
            if (str2.length() == 1) {
                str1 = '0' + str2;
            }
            localStringBuffer.append(str1.toUpperCase());
            i += 1;
        }
        return localStringBuffer.toString();
    }

    public static byte[] parseHexStr2Byte(String hexString)
    {
        int len = hexString.length()/2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位
        for(int i=0;i<len;i++){
            //右移四位得到高位
            high = (byte)((hexString.indexOf(hexString.charAt(2*i)))<<4);
            low = (byte)hexString.indexOf(hexString.charAt(2*i+1));
            bytes[i] = (byte) (high|low);//高地位做或运算
        }
        return bytes;
    }
}
