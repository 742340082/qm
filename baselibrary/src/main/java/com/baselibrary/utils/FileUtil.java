package com.baselibrary.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 74234 on 2017/4/25.
 */

public class FileUtil {
    public static  void  saveFile(String path, InputStream inputStream)
    {
        FileOutputStream fileOutputStream=null;
        try {
             fileOutputStream=new FileOutputStream(path);
            int len=0;
            byte[] bys=new byte[1024];
            while ((len=inputStream.read(bys))!=-1)
            {
                fileOutputStream.write(bys,0,len);
                fileOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public static String byteSwitch(int number,String size) {
        Long sizes = Long.parseLong(size);
        Long b=sizes;
        Long k=b/1024;
        Long m=k/1024;
        Long g=m/1024;
        Long t=g/1024;
        if (k==0)
        {
            return b+"B";
        }else
        if (m==0)
        {
            if (number!=0) {
                String bValue = String.valueOf(b);
                String num = bValue.substring(1, number+1);
                return k + "." + num + "K";
            }else
            {
                return k+"K";
            }

        }else
        if (g==0)
        {
            if (number!=0) {
                String kValue = String.valueOf(k);
                String num = kValue.substring(1, number+1);
                return m + "." + num + "M";
            }else
            {
                return m + "M";
            }
        }
        if (t==0)
        {
            if (number!=0) {
                String mValue = String.valueOf(m);
                String num = mValue.substring(1, number+1);
                return g + "." + num + "G";
            }else
            {
                return g + "G";
            }
        }
        else
        {
            if (number!=0) {
                String gValue = String.valueOf(g);
                String num = gValue.substring(1, number+1);
                return t + "." + num + "T";
            }else
            {
                return t + "T";
            }
        }
    }

    public  static  String downloadCountSwitch(int number,Long downnum)
    {
        Long wan=downnum/10000;
        Long yi=wan/10000;
        if (wan==0)
        {
            return downnum+"次下载";
        }else
        if (yi==0)
        {
            if (number!=0) {
                String wanValue = String.valueOf(wan);
                String num = wanValue.substring(1, number+1);
                return wan + "." + num + "万+下载";
            }else
            {
                return wan + "万+下载";
            }
        }else
        {
            if (number!=0) {
                String yiValue = String.valueOf(yi);
                String num = yiValue.substring(1, number+1);
                return yi + "." + num + "亿+下载";
            }else
            {
                return yi +  "亿+下载";
            }
        }
    }
    public static String downloadSpeed(int number,long speed)
    {
        Long b=speed;
        Long k=b/1024;
        Long m=k/1024;
        Long g=m/1024;
        Long t=g/1024;
        if (k==0)
        {
            return b+"B/s";
        }else
        if (m==0)
        {
            if (number!=0) {
                String bValue = String.valueOf(b);
                String num = bValue.substring(1, number+1);
                return k + "." + num + "K/s";
            }else
            {
                return k+"K/s";
            }

        }else
        if (g==0)
        {
            if (number!=0) {
                String kValue = String.valueOf(k);
                String num = kValue.substring(1, number+1);
                return m + "." + num + "M/s";
            }else
            {
                return m + "M/s";
            }
        }
        if (t==0)
        {
            if (number!=0) {
                String mValue = String.valueOf(m);
                String num = mValue.substring(1, number+1);
                return g + "." + num + "G/s";
            }else
            {
                return g + "G/s";
            }
        }
        else
        {
            if (number!=0) {
                String gValue = String.valueOf(g);
                String num = gValue.substring(1, number+1);
                return t + "." + num + "T/s";
            }else
            {
                return t + "T/s";
            }
        }
    }
}
