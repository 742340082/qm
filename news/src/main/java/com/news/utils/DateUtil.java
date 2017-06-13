package com.news.utils;


import com.news.config.ConfigNews;

import java.util.Calendar;

public class DateUtil
{
    public static long SaveNewsTime()
    {
        return Calendar.getInstance().getTimeInMillis() + ConfigNews.NEWS_SAVE_TIME;
    }



    public static long getCurrentTime()
    {
        return Calendar.getInstance().getTimeInMillis();
    }
}
