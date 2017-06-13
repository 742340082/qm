package com.we.mvp.user.operate.biz;


import com.we.mvp.user.login.modle.QQUserInfo;
import com.we.mvp.user.login.modle.WeiboUserInfo;

public interface OperateUserBiz {
    boolean confirmation(QQUserInfo qqUserInfo, WeiboUserInfo weiboUserInfo);

      void destroyMobSms();

      void initMobSms(int loginType, int registType, int updateType, String telePhone);

        void submitVerificationCode();

      void sendSmsCode(String telephone);

      void sendVoiceCode(String telephone);
}
