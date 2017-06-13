package com.we.mvp.user.baseLogic;

import com.we.mvp.user.login.modle.QQUserInfo;
import com.we.mvp.user.login.modle.WeiboUserInfo;

/**
 * Created by 74234 on 2017/5/6.
 */

public interface LogicOperateUserInterface {
    void obtainUser(String account);
    void loginUser(String account, String password, String telephone, int loginType);
    void registUser(String account, String password, QQUserInfo qqUserInfo, WeiboUserInfo weiboUserInfo, int registType);
    void updateUser(String account, String userName,
                    String password,
                    String newPassword,
                    String telephone,
                    String payPassword,
                    QQUserInfo qqUserInfo,
                    WeiboUserInfo weiboUserInfo,
                    int updateType);
}
