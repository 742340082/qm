package com.we.mvp.user.regist.biz;

public  interface UserRegist
{
      boolean confirmation();

      void destroyMobSms();

      void initMobSms();

      void submitVerificationCode();
      void sendSmsCode(String telephone);
      void sendVoiceCode(String telephone);
}
