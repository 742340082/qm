package com.baselibrary.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils
{
    private static Toast toast;

    public static void makeShowToast(Context context, String content)
    {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }
        toast.setText(content);
        toast.show();
    }
}
