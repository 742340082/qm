package com.we.biz;

import android.graphics.Bitmap;

import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.SaveConfigUserUtil;
import com.baselibrary.utils.UIUtils;
import com.google.gson.Gson;
import com.we.config.ConfigUser;
import com.we.mvp.user.modle.User;
import com.we.utils.PresenceUtil;
import com.we.view.UserMenuView;

import java.io.File;

/**
 * Created by 74234 on 2017/5/4.
 */

public class WeBizImpl implements WeBiz {
    private UserMenuView userMenuView;
    private Gson mGson;
    private User mUser;
    public WeBizImpl(UserMenuView userMenuView) {
       this.userMenuView= userMenuView;
        mGson=new Gson();
    }

    private void noNetWorkLogin() {
        Bitmap userIconBitmap = PresenceUtil.obtainBitmap(mUser.getAccount());
        RxBus.getDefault().post(ConfigUser.USER_RX_ICON,userIconBitmap);
        RxBus.getDefault().post(ConfigUser.USER_RX_SEND, mUser);
    }
    @Override
    public void initSaveLogin() {

        if (SaveConfigUserUtil.getShared_PrefsFile().exists()) {
            String json = SaveConfigUserUtil.getString(UIUtils.getContext(), ConfigUser.USER_SAVE, null);
            mUser =  mGson.fromJson(json, User.class);
            long save_user_time = SaveConfigUserUtil.getLong(UIUtils.getContext(), ConfigUser.USER_CACHE_TIME, -1L);
            if (save_user_time != -1L|| mUser !=null) {
                if (System.currentTimeMillis() < save_user_time) {
                    noNetWorkLogin();
                    return;
                }else {
                    this.userMenuView.resetShowUi();
                    userMenuView.error(ConfigStateCode.STATE_SHELF_LIFE_HAS_BEEN_RELOGIN,ConfigStateCode.STATE_SHELF_LIFE_HAS_BEEN_RELOGIN_VALUE);
                    deleteSaveUser();
                    return;
                }
            }
            userMenuView.resetShowUi();
        }

        if (SaveConfigUserUtil.getShared_PrefsFile().exists()) {
            String json = SaveConfigUserUtil.getString(UIUtils.getContext(), ConfigUser.USER_SAVE, null);
            mUser =  mGson.fromJson(json, User.class);
            if (mUser !=null) {
                    noNetWorkLogin();
            }else {
                this.userMenuView.resetShowUi();
            }
        }else
        {
            this.userMenuView.resetShowUi();
        }
    }
    /**
     * 删除用户数据还有图片
     */
    public void deleteSaveUser() {

        String json = SaveConfigUserUtil.getString(UIUtils.getContext(), ConfigUser.USER_SAVE, null);
        mUser = mGson.fromJson(json, User.class);
        if (this.mUser != null) {
            File userFile = isSaveUser();
            File userIconFile = new File(ConfigUser.SAVE_USER_ICON_URI(mUser.getAccount(), ".jpg").getPath());
            if (userIconFile.exists()) {
                userIconFile.delete();
            }
            if (userFile.exists()) {
                userFile.delete();
            }
            mUser = null;
        }
    }
    /**
     * 用户保存新的文件
     * @return
     */
    public File isSaveUser() {
        return SaveConfigUserUtil.getShared_PrefsFile();
    }
}
