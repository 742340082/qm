package com.we.mvp.user.login.presence;

import com.tencent.tauth.Tencent;
import com.we.mvp.user.login.biz.UserLogin;
import com.we.mvp.user.login.biz.UserLoginImpl;
import com.we.mvp.user.login.view.IUserLoginView;

public class UserLoginPresence
{
    private UserLogin mUserLogin;

    public UserLoginPresence(IUserLoginView paramIUserLoginView)
    {
        this.mUserLogin = new UserLoginImpl(paramIUserLoginView);
    }

    public void login()
    {
        this.mUserLogin.login();
    }

    public void qqLogin(Tencent paramTencent)
    {
        this.mUserLogin.qqLogin(paramTencent);
    }

     public void weibologin(String accessToken,String uid)
     {
         mUserLogin.weibologin(accessToken,uid);
     }
    public void getRefresgToken(String refreshToken)
    {
        mUserLogin.getRefresgToken(refreshToken);
    }
    public void alipayLogin()
    {
        mUserLogin.alipayLogin();
    }
}
