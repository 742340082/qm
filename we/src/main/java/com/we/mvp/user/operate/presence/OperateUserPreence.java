package com.we.mvp.user.operate.presence;


import com.we.mvp.user.login.modle.QQUserInfo;
import com.we.mvp.user.login.modle.WeiboUserInfo;
import com.we.mvp.user.operate.biz.OperateUserBiz;
import com.we.mvp.user.operate.biz.OperateUserBizImpl;
import com.we.mvp.user.operate.view.OperateUserView;

public class OperateUserPreence
{
    private OperateUserBiz mUserRegist;

    public OperateUserPreence(OperateUserView bindRegistView)
    {
        this.mUserRegist = new OperateUserBizImpl(bindRegistView);
    }

    public boolean confirmation(QQUserInfo qqUserInfo, WeiboUserInfo weiboUserInfo)
    {
        return this.mUserRegist.confirmation(qqUserInfo,weiboUserInfo);
    }

    public void destroyMobSms()
    {
        this.mUserRegist.destroyMobSms();
    }

    public void initMobSms( int loginType,int registType,int updateType,String telePhone)
    {
        this.mUserRegist.initMobSms(loginType,registType,updateType,telePhone);
    }

    public void sendSmsCode(String telephone)
    {
        mUserRegist.sendSmsCode(telephone);
    }
    public void sendVoiceCode(String telephone)
    {
        this.mUserRegist.sendVoiceCode(telephone);
    }
    public void submitVerificationCode()
    {
        mUserRegist.submitVerificationCode();
    }
}
