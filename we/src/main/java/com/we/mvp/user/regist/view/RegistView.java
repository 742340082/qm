package com.we.mvp.user.regist.view;


import com.baselibrary.BaseView;
import com.we.mvp.user.modle.User;

import java.util.List;

public  interface RegistView extends BaseView<List<User>>
{

      String getCountDown();

      String getPassword();

      String getUserCode();

      String getUserTelephone();

      void isBrainConfirmation(boolean paramBoolean);
      void setCodeEnabled(boolean paramBoolean);



}
