package com.we.mvp.user.detail.biz;

/**
 * Created by yangyupan on 2017/4/19.
 */

public interface UpdatePasswordBiz {

    void updatePassword(int updateType);

      void destroyMobSms();
    boolean confirmation(String account, int updateType);
      void initMobSms(String account, int updateType);
      void sendSmsCode(String telephone);
      void sendVoiceCode(String telephone);
}
