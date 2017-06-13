package com.we.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.we.config.ConfigUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by yangyupan on 2017/4/11.
 */

public class PresenceUtil {


    /**
     * 初始化用户图片
     *
     * @param account 用户的装好
     * @return 图片
     */
    public static Bitmap obtainBitmap(String account) {
        File file = new File(ConfigUser.SAVE_USER_ICON_URI(account, ".jpg").getPath());
        if (file.exists()) {
            try {
                Bitmap userIcon = BitmapFactory.decodeStream(new FileInputStream(file));
                return userIcon;
            } catch (FileNotFoundException paramString) {
                paramString.printStackTrace();
                return null;
            }
        }
        return null;
    }


}
