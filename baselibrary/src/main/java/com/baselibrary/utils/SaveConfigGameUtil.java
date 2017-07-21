package com.baselibrary.utils;

import android.content.Context;

import java.io.File;

public class SaveConfigGameUtil {
    private static String SHARD_PREF_ROOT_PATH = "data/data";
    private static String GAME_CONFIG = "GAME_CONFIG";

    public static boolean getBoolean(Context context, String value, boolean flag) {
        return context.getSharedPreferences(GAME_CONFIG, 0).getBoolean(value, flag);
    }

    public static int getInt(Context context, String value, int defaultValue) {
        return context.getSharedPreferences(GAME_CONFIG, 0).getInt(value, defaultValue);
    }

    public static long getLong(Context context, String value, long defaultValue) {
        return context.getSharedPreferences(GAME_CONFIG, 0).getLong(value, defaultValue);
    }

    public static File getShared_PrefsFile() {
        Context localContext = UIUtils.getContext();
        return new File(SHARD_PREF_ROOT_PATH + File.separator + localContext.getPackageName() + File.separator + "shared_prefs" + File.separator + GAME_CONFIG + ".xml");
    }

    public static String getString(Context context, String value, String defaultValue) {
        return context.getSharedPreferences(GAME_CONFIG, 0).getString(value, defaultValue);
    }

    public static void setBoolean(Context context, String value, boolean defaultValue) {
        context.getSharedPreferences(GAME_CONFIG, 0).edit().putBoolean(value, defaultValue).commit();
    }

    public static void setInt(Context context, String value, int defaultValue) {
        context.getSharedPreferences(GAME_CONFIG, 0).edit().putInt(value, defaultValue).commit();
    }

    public static void setLong(Context context, String value, long defaultValue) {
        context.getSharedPreferences(GAME_CONFIG, 0).edit().putLong(value, defaultValue).commit();
    }

    public static void setString(Context context, String value, String defaultValue) {
        context.getSharedPreferences(GAME_CONFIG, 0).edit().putString(value, defaultValue).commit();
    }



}
