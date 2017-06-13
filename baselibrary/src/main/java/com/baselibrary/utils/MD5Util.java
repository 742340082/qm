package com.baselibrary.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util
{

    public static String encode(String text)
    {
        try {
            MessageDigest digset=MessageDigest.getInstance("MD5");
            byte[] result=digset.digest(text.getBytes());
            StringBuffer sb=new StringBuffer();
            for(byte by:result)
            {
                int number=by&0xff;
                String hexString = Integer.toHexString(number);
                if(hexString.length()==1)
                {
                    sb.append(0+hexString);
                }else
                {
                    sb.append(hexString);
                }

            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}
