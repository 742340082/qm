package com.baselibrary.utils;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.base.activity.MyApplication;

public class UIUtils
{
    public static int dip2px(float paramFloat)
    {
        return (int)(paramFloat * getContext().getResources().getDisplayMetrics().density + 0.5F);
    }

    public static int getColor(int paramInt)
    {
        return getContext().getResources().getColor(paramInt);
    }

    public static ColorStateList getColorStateList(int paramInt)
    {
        return getContext().getResources().getColorStateList(paramInt);
    }

    public static Context getContext()
    {
        return MyApplication.getContext();
    }

    public static float getDensity()
    {
        return getContext().getResources().getDisplayMetrics().density;
    }

    public static Drawable getDrawable(int paramInt)
    {
        return getContext().getResources().getDrawable(paramInt);
    }

    public static Bitmap getBitmap(int resid)
    {
        return BitmapFactory.decodeResource(getContext().getResources(),resid);
    }

    public static Handler getHandler()
    {
        return MyApplication.getHandler();
    }

    public static String getString(int paramInt)
    {
        return getContext().getResources().getString(paramInt);
    }

    public static String[] getStringS(int paramInt)
    {
        return getContext().getResources().getStringArray(paramInt);
    }

    public static int getUiThreadId()
    {
        return MyApplication.getUiThreadId();
    }

    public static View inflate(int paramInt)
    {
        return View.inflate(getContext(), paramInt, null);
    }

    public static boolean isUiThreadId()
    {
        return Process.myTid() == getUiThreadId();
    }

    public static float px2dip(int paramInt)
    {
        float f = getContext().getResources().getDisplayMetrics().density;
        return paramInt / f;
    }

    public static void runOnUIThread(Runnable paramRunnable)
    {
        if (isUiThreadId())
        {
            paramRunnable.run();
            return;
        }
        getHandler().post(paramRunnable);
    }

    public static void startActivity(Context paramContext, Intent paramIntent)
    {
        paramContext.startActivity(paramIntent);
    }

    public static void startActivity(Context paramContext, Class<?> paramClass)
    {
        paramContext.startActivity(new Intent(paramContext, paramClass));
    }

    public static void startActivity(Class<?> paramClass)
    {
        Context localContext = getContext();
        localContext.startActivity(new Intent(localContext, paramClass));
    }

    @TargetApi(21)
    public static void startActivityAndMaterAnimator(Context context, String content, View view, Class<?> aClass)
    {
        context.startActivity(new Intent(context, aClass), ActivityOptions.makeSceneTransitionAnimation((BaseActivity)context, view, content).toBundle());
    }


    @TargetApi(21)
    public static void startActivityAndMaterAnimator(Context context, String content, View view,Intent intent)
    {
        context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((BaseActivity)context, view, content).toBundle());
    }
    /**
     * 判断当前是否运行在主线程
     *
     * @return
     */
    public static boolean isRunOnUiThread() {
        return getUiThreadId() == android.os.Process.myTid();
    }


}
