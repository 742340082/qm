package com.we.mvp.user.detail.presence;

import android.content.Intent;

import com.tencent.tauth.Tencent;
import com.we.mvp.user.detail.biz.UserDetailBiz;
import com.we.mvp.user.detail.biz.UserDetailBizImpl;
import com.we.mvp.user.detail.view.UserDetailView;
import com.we.mvp.user.modle.User;

public class UserDetailPresence
{
    private UserDetailBiz mUserDetailBiz;

    public UserDetailPresence(UserDetailView paramUserDetailView)
    {
        this.mUserDetailBiz = new UserDetailBizImpl(paramUserDetailView);
    }

    public void handleCropError(Intent paramIntent, String paramString)
    {
        this.mUserDetailBiz.handleCropError(paramIntent, paramString);
    }

    public void handleCropResult(Intent intent, String takeTempSaceIconPath,User mcbUser)
    {
        this.mUserDetailBiz.handleCropResult(intent, takeTempSaceIconPath,mcbUser);
    }

    public void initUserInfo()
    {
        this.mUserDetailBiz.initUserInfo();
    }
    public void updateBindQQ(Tencent tencent, String account)
    {
        mUserDetailBiz.updateBindQQ(tencent,account);
    }
    public void unBindQQ(String account)
    {
        mUserDetailBiz.unBindQQ(account);
    }
    public void updateBindWeibo(String accessToken,String uid,String account)
    {
        mUserDetailBiz.updateBindWeibo(accessToken,uid,account);
    }
    public void unBindWeibo(String account)
    {
        mUserDetailBiz.unBindWeibo(account);
    }
}
