package com.baselibrary.utils;

import android.content.Context;

import java.io.File;

public class SaveConfigUserUtil {
    private static String shard_pref_root_path = "data/data";
    private static String user_config = "user_config";

    public static boolean getBoolean(Context paramContext, String paramString, boolean paramBoolean) {
        return paramContext.getSharedPreferences(user_config, 0).getBoolean(paramString, paramBoolean);
    }

    public static int getInt(Context paramContext, String paramString, int paramInt) {
        return paramContext.getSharedPreferences(user_config, 0).getInt(paramString, paramInt);
    }

    public static long getLong(Context paramContext, String paramString, long paramLong) {
        return paramContext.getSharedPreferences(user_config, 0).getLong(paramString, paramLong);
    }

    public static File getShared_PrefsFile() {
        Context localContext = UIUtils.getContext();
        return new File(shard_pref_root_path + File.separator + localContext.getPackageName() + File.separator + "shared_prefs" + File.separator + user_config + ".xml");
    }

    public static String getString(Context paramContext, String paramString1, String paramString2) {
        return paramContext.getSharedPreferences(user_config, 0).getString(paramString1, paramString2);
    }

    public static void setBoolean(Context paramContext, String paramString, boolean paramBoolean) {
        paramContext.getSharedPreferences(user_config, 0).edit().putBoolean(paramString, paramBoolean).commit();
    }

    public static void setInt(Context paramContext, String paramString, int paramInt) {
        paramContext.getSharedPreferences(user_config, 0).edit().putInt(paramString, paramInt).commit();
    }

    public static void setLong(Context paramContext, String paramString, long paramLong) {
        paramContext.getSharedPreferences(user_config, 0).edit().putLong(paramString, paramLong).commit();
    }

    public static void setString(Context paramContext, String paramString1, String paramString2) {
        paramContext.getSharedPreferences(user_config, 0).edit().putString(paramString1, paramString2).commit();
    }


}
