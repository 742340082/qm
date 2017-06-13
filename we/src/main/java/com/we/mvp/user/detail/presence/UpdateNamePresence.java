package com.we.mvp.user.detail.presence;


import com.we.mvp.user.detail.biz.UpdateNameBiz;
import com.we.mvp.user.detail.biz.UpdateNameBizImpl;
import com.we.mvp.user.detail.view.UpdateNameView;

/**
 * Created by yangyupan on 2017/4/15.
 */

public class UpdateNamePresence {
    private UpdateNameBiz mUserDetailUpdateUserNameBiz;
    public UpdateNamePresence(UpdateNameView view)
    {
        mUserDetailUpdateUserNameBiz=new UpdateNameBizImpl(view);
    }
    public void updateUserName(String account) {
        mUserDetailUpdateUserNameBiz.updateUserName(account);
    }
}
