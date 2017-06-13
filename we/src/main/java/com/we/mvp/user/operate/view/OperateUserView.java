package com.we.mvp.user.operate.view;


import com.baselibrary.BaseView;
import com.we.mvp.user.modle.User;

import java.util.List;

public abstract interface OperateUserView extends BaseView<List<User>>
{

      String getCountDown();


      String getUserCode();

    String getAccount();

      String getUserTelephone();

      void isBrainConfirmation(boolean paramBoolean);

    void confirmationJudge();

      void setCodeEnabled(boolean paramBoolean);

}
