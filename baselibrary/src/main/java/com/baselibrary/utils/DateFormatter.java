package com.baselibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter
{
    public static String DateFormat(long paramLong)
    {
        Date localDate = new Date(paramLong);
        return new SimpleDateFormat("yyyyMMdd").format(localDate);
    }

    public static String DoubanDateFormat(long paramLong)
    {
        Date localDate = new Date(paramLong);
        return new SimpleDateFormat("yyyy-MM-dd").format(localDate);
    }

    public static String ZhihuDailyDateFormat(long paramLong)
    {
        Date localDate = new Date(86400000L + paramLong);
        return new SimpleDateFormat("yyyyMMdd").format(localDate);
    }

    public  static String formatGapTime(long gaoTime)
    {
        long s = gaoTime / 1000;
        long m = s / 60;
        long h = m / 60;
        long d=h/24;
        long M=d/30;
        long y=m/12;
        if (m==0)
        {
            return s+"秒前";
        }else if (h==0)
        {
            return m+"分钟前";
        }else if (d==0)
        {
            return h+"小时前";
        }else if (M==0)
        {
            return d+"天前";
        }else if (y==0)
        {
            return M+"月前";
        }else
        {
            return y+"年前";
        }
    }
}
