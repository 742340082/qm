package com.we.mvp.user.login.view;


import com.baselibrary.BaseView;
import com.we.mvp.user.login.modle.QQUserInfo;
import com.we.mvp.user.login.modle.WeiboRrfreshToken;
import com.we.mvp.user.login.modle.WeiboUserInfo;
import com.we.mvp.user.modle.User;

import java.util.List;

public  interface IUserLoginView extends BaseView<List<User>>
{

     String getPassword();

     String getUserName();

    void  getWeiboToken(WeiboRrfreshToken weiboRrfreshToken);

    void bindAccountActivity(QQUserInfo qqUserInfo, WeiboUserInfo weiboUserInfo,int registType);

}
