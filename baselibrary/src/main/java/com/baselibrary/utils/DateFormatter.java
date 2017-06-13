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
}
