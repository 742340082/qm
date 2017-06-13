package com.we.utils;

import com.we.config.ConfigUser;

import java.util.Calendar;

public class DateUtil
{
//    public static long SaveNewsTime()
//    {
//        return Calendar.getInstance().getTimeInMillis() + ConfigNews.NEWS_SAVE_TIME;
//    }

    public static long SaveUserTime()
    {
        return Calendar.getInstance().getTimeInMillis() + ConfigUser.USER_TIME_CONTENT;
    }


    public static long getCurrentTime()
    {
        return Calendar.getInstance().getTimeInMillis();
    }
}
