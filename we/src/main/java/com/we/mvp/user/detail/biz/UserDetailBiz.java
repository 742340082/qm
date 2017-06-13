package com.we.mvp.user.detail.biz;

import android.content.Intent;

import com.tencent.tauth.Tencent;
import com.we.mvp.user.modle.User;

public  interface UserDetailBiz
{
     void handleCropError(Intent paramIntent, String paramString);

     void handleCropResult(Intent paramIntent, String paramString, User mcbUser);

     void initUserInfo();
    void updateBindQQ(Tencent tencent, String account);
    void unBindQQ(String account);
    void updateBindWeibo(String accessToken, String uid, String account);
    void unBindWeibo(String account);
}
