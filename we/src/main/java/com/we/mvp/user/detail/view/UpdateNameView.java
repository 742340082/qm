package com.we.mvp.user.detail.view;


import com.baselibrary.BaseView;
import com.we.mvp.user.modle.User;

import java.util.List;

/**
 * Created by yangyupan on 2017/4/15.
 */

public interface UpdateNameView extends BaseView<List<User>> {
    String getUserName();
}
