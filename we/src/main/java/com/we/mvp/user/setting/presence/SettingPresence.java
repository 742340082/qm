package com.we.mvp.user.setting.presence;


import com.we.mvp.user.setting.biz.SettingBizImpl;
import com.we.mvp.user.setting.view.SettingView;

public class SettingPresence
{
    private SettingBizImpl mSettingBiz;

    public SettingPresence(SettingView paramSettingView)
    {
        this.mSettingBiz = new SettingBizImpl(paramSettingView);
    }

    public void deleteSaveUser(String account)
    {
        this.mSettingBiz.deleteSaveUser(account);
    }

    public void isLogin()
    {
        this.mSettingBiz.isLogin();
    }
}
