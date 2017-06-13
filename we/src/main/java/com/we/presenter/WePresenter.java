package com.we.presenter;

import com.we.biz.WeBiz;
import com.we.biz.WeBizImpl;
import com.we.view.UserMenuView;

/**
 * Created by 74234 on 2017/5/4.
 */

public class WePresenter {
    private WeBiz userMenuBiz;
    public WePresenter(UserMenuView userMenuView)
    {
        userMenuBiz=new WeBizImpl(userMenuView);
    }
    public void initSaveLogin()
    {
        userMenuBiz.initSaveLogin();
    }
}
