package com.we.mvp.user.detail.biz;


import com.we.config.ConfigUser;
import com.we.mvp.user.baseLogic.LogicOperateUser;
import com.we.mvp.user.detail.view.UpdateNameView;

/**
 * Created by yangyupan on 2017/4/15.
 */

public class UpdateNameBizImpl implements UpdateNameBiz {
    private UpdateNameView mUserNameView;
    private LogicOperateUser logicOperateUser;
    public UpdateNameBizImpl(UpdateNameView view)
    {
        mUserNameView=view;
        logicOperateUser=new LogicOperateUser(view);
    }
    @Override
    public void updateUserName(String account) {
        logicOperateUser.updateUser(
                account,
                mUserNameView.getUserName(),
                null,
                null,
                null,
                null,
                null,
                null,
                ConfigUser.USER_UPDATE_NAME
        );
    }


}
