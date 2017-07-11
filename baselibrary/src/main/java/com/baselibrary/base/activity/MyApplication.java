package com.baselibrary.base.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Process;

import com.baselibrary.config.ConfigSdk;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

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

        AuthInfo authInfo = new AuthInfo(getContext(), ConfigSdk.WEIBO_APP_KEY, ConfigSdk.WEIBO_REDIRECT_URL, ConfigSdk.WEIBO_SCOPE);
        WbSdk.install(getContext(),authInfo);
    }
}
