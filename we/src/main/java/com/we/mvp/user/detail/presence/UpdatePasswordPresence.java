package com.we.mvp.user.detail.presence;


import com.we.mvp.user.detail.biz.UpdatePasswordBiz;
import com.we.mvp.user.detail.biz.UpdatePasswordBizImpl;
import com.we.mvp.user.detail.view.UpdatePasswordView;

/**
 * Created by yangyupan on 2017/4/19.
 */

public class UpdatePasswordPresence {
    private UpdatePasswordBiz mUserPasswordBiz;

    public UpdatePasswordPresence(UpdatePasswordView view) {
        mUserPasswordBiz = new UpdatePasswordBizImpl(view);
    }

    public boolean confirmation(String account,int updateType) {
        return mUserPasswordBiz.confirmation(account,updateType);
    }

    public void updatePassword(int updateType)
    {
        mUserPasswordBiz.updatePassword(updateType);
    }
    public void destroyMobSms()
    {
        mUserPasswordBiz.destroyMobSms();
    }


    public void initMobSms(String account,int updateType)
    {
        mUserPasswordBiz.initMobSms(account,updateType);
    }



    public void sendSmsCode(String telephone)
    {
        mUserPasswordBiz.sendSmsCode(telephone);
    }
    public void sendVoiceCode(String telephone)
    {
        mUserPasswordBiz.sendVoiceCode(telephone);
    }
}
