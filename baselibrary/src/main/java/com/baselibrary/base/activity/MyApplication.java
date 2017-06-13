package com.baselibrary.base.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Process;

import org.litepal.LitePalApplication;

public class MyApplication
        extends LitePalApplication
{
    private static int UiThreadId;
    private static Context mContext;
    private static Handler mHandler;

    public static Context getContext()
    {
        return mContext;
    }

    public static Handler getHandler()
    {
        return mHandler;
    }

    public static int getUiThreadId()
    {
        return UiThreadId;
    }



    public void onCreate()
    {
        super.onCreate();
        mHandler = new Handler();
        mContext = getApplicationContext();
        UiThreadId = Process.myUid();

    }
}
