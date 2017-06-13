package com.we.mvp.user.regist.presence;


import com.we.mvp.user.regist.biz.UserRegist;
import com.we.mvp.user.regist.biz.UserRegistImpl;
import com.we.mvp.user.regist.view.RegistView;

public class UserRegistPreence
{
    private UserRegist mUserRegist;

    public UserRegistPreence(RegistView paramIUserRegistView)
    {
        this.mUserRegist = new UserRegistImpl(paramIUserRegistView);
    }

    public boolean confirmation()
    {
        return this.mUserRegist.confirmation();
    }

    public void destroyMobSms()
    {
        this.mUserRegist.destroyMobSms();
    }

    public void initMobSms()
    {
        this.mUserRegist.initMobSms();
    }


    public void submitVerificationCode()
    {
        this.mUserRegist.submitVerificationCode();
    }

    public void sendSmsCode(String telephone)
    {
        this.mUserRegist.sendSmsCode(telephone);
    }
    public void sendVoiceCode(String telephone)
    {
        this.mUserRegist.sendVoiceCode(telephone);
    }
}
