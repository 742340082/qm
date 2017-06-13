package com.we.mvp.user.login.biz;

        import com.tencent.tauth.Tencent;

public  interface UserLogin
{
    void login();
   void weibologin(String accessToken, String uid);
    void qqLogin(Tencent tencent);
    void alipayLogin();
    void getRefresgToken(String refreshToken);
}
