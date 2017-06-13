package com.baselibrary.utils;

import android.util.Log;

public class Logger {
    public static boolean isShowLog = true;
    public  static  final int VERBOSE=1;
    public  static  final int DEBUG=2;
    public  static  final int INFO=3;
    public  static  final int WARN=4;
    public  static  final int ERROR=5;
    public  static  final int NOTHING=6;
    public  static  int level=VERBOSE;
    public static void i(Object obj, String message) {
        if (!isShowLog) {
            return;
        }
        if(level<=INFO) {
            String tag = null;
            if ((obj instanceof String)) {
                tag = (String) obj;
            }else {
                if ((obj instanceof Class)) {
                    tag = ((Class) obj).getSimpleName();
                } else {
                    tag = obj.getClass().getSimpleName();
                }
            }

            Log.i(tag, message);
        }
    }
    public static void d(Object obj, String message) {
        if (!isShowLog) {
            return;
        }
        if(level<=DEBUG) {
            String tag = null;
            if ((obj instanceof String)) {
                tag = (String) obj;
            }else {
                if ((obj instanceof Class)) {
                    tag = ((Class) obj).getSimpleName();
                } else {
                    tag = obj.getClass().getSimpleName();
                }
            }
            Log.d(tag, message);
        }
    }

    public static void v(Object obj, String message) {
        if (!isShowLog) {
            return;
        }
        if(level<=VERBOSE) {
            String tag = null;
            if ((obj instanceof String)) {
                tag = (String) obj;
            }else {
                if ((obj instanceof Class)) {
                    tag = ((Class) obj).getSimpleName();
                } else {
                    tag = obj.getClass().getSimpleName();
                }
            }
            Log.v(tag, message);
        }
    }

    public static void w(Object obj, String message) {
        if (!isShowLog) {
            return;
        }
        if(level<=WARN) {
            String tag = null;
            if ((obj instanceof String)) {
                tag = (String) obj;
            }else {
                if ((obj instanceof Class)) {
                    tag = ((Class) obj).getSimpleName();
                } else {
                    tag = obj.getClass().getSimpleName();
                }
            }
            Log.w(tag, message);
        }
    }

    public static void e(Object obj, String message) {
        if (!isShowLog) {
            return;
        }
        if(level<=ERROR) {
            String tag = null;
            if ((obj instanceof String)) {
                tag = (String) obj;
            }else {
                if ((obj instanceof Class)) {
                    tag = ((Class) obj).getSimpleName();
                } else {
                    tag = obj.getClass().getSimpleName();
                }
            }
            Log.e(tag, message);
        }
    }
}
