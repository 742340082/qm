package com.baselibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkState
{
    public static boolean mobileDataConnected(Context context)
    {
        if (context != null)
        {
            ConnectivityManager manager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if ((activeNetworkInfo != null) && (activeNetworkInfo.getType() == 0)) {
                return true;
            }
        }
        return false;
    }

    public static boolean networkConnected(Context context)
    {
        if (context != null)
        {
            ConnectivityManager manager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean wifiConnected(Context context)
    {
        if (context != null)
        {
            ConnectivityManager manager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if ((activeNetworkInfo != null) && (activeNetworkInfo.getType() == 1)) {
                return activeNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
