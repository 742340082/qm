package com.we.view;

import com.baselibrary.BaseView;
import com.we.mvp.user.modle.User;

import java.util.List;

/**
 * Created by 74234 on 2017/5/4.
 */

public interface UserMenuView extends BaseView<List<User>> {

      void resetShowUi();
}
