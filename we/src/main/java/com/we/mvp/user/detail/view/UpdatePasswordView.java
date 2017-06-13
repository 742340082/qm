package com.we.mvp.user.detail.view;


import com.baselibrary.BaseView;
import com.we.mvp.user.modle.User;

import java.util.List;

/**
 * Created by yangyupan on 2017/4/19.
 */

public interface UpdatePasswordView extends BaseView<List<User>> {
    String getNewPassWord();
    String getPassWord();
     String getCountDown();
    String getTelephone();
    String getUserCode();
      void isBrainConfirmation(boolean paramBoolean);
      void setCodeEnabled(boolean paramBoolean);
}
