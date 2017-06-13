package com.we.mvp.user.setting.biz;

import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.SaveConfigUserUtil;
import com.baselibrary.utils.UIUtils;
import com.google.gson.Gson;
import com.we.config.ConfigUser;
import com.we.mvp.user.modle.User;
import com.we.mvp.user.setting.view.SettingView;

import java.io.File;

public class SettingBizImpl
        implements SettingBiz
{
    private Gson mGson = new Gson();
    private SettingView settingView;

    public SettingBizImpl(SettingView paramSettingView)
    {
        this.settingView = paramSettingView;
    }

    public void deleteSaveUser(String account)
    {
            File localFile1 = isSaveUser();
            File localFile2 = new File(ConfigUser.SAVE_USER_ICON_URI(account, ".jpg").getPath());
            if (localFile2.exists()) {
                localFile2.delete();
            }
            if (localFile1.exists()) {
                localFile1.delete();
            }
        RxBus.getDefault().post(ConfigUser.USER_RX_SEND,new User());
    }

    public void isLogin()
    {
        if (SaveConfigUserUtil.getShared_PrefsFile().exists())
        {
            String str = SaveConfigUserUtil.getString(UIUtils.getContext(), ConfigUser.USER_SAVE, null);
          User mcbUser = mGson.fromJson(str, User.class);
            if (mcbUser != null)
            {
                this.settingView.IsLogin(mcbUser);
            }else {
                this.settingView.IsLogin(null);
            }
        }else {
            this.settingView.IsLogin(null);
        }
    }

    public File isSaveUser()
    {
        return SaveConfigUserUtil.getShared_PrefsFile();
    }
}
