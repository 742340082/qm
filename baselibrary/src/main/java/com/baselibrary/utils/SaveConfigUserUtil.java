package com.baselibrary.utils;

import android.content.Context;

import java.io.File;

public class SaveConfigUserUtil {
    private static String shard_pref_root_path = "data/data";
    private static String user_config = "user_config";

    public static boolean getBoolean(Context context, String value, boolean flag) {
        return context.getSharedPreferences(user_config, 0).getBoolean(value, flag);
    }

    public static int getInt(Context context, String value, int defaultValue) {
        return context.getSharedPreferences(user_config, 0).getInt(value, defaultValue);
    }

    public static long getLong(Context context, String value, long defaultValue) {
        return context.getSharedPreferences(user_config, 0).getLong(value, defaultValue);
    }

    public static File getShared_PrefsFile() {
        Context localContext = UIUtils.getContext();
        return new File(shard_pref_root_path + File.separator + localContext.getPackageName() + File.separator + "shared_prefs" + File.separator + user_config + ".xml");
    }

    public static String getString(Context context, String value, String defaultValue) {
        return context.getSharedPreferences(user_config, 0).getString(value, defaultValue);
    }

    public static void setBoolean(Context context, String value, boolean defaultValue) {
        context.getSharedPreferences(user_config, 0).edit().putBoolean(value, defaultValue).commit();
    }

    public static void setInt(Context context, String value, int defaultValue) {
        context.getSharedPreferences(user_config, 0).edit().putInt(value, defaultValue).commit();
    }

    public static void setLong(Context context, String value, long defaultValue) {
        context.getSharedPreferences(user_config, 0).edit().putLong(value, defaultValue).commit();
    }

    public static void setString(Context context, String value, String defaultValue) {
        context.getSharedPreferences(user_config, 0).edit().putString(value, defaultValue).commit();
    }

}
