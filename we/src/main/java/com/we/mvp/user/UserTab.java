package com.we.mvp.user;

import android.support.v4.app.Fragment;

import com.we.R;
import com.we.mvp.user.login.LoginFragement;
import com.we.mvp.user.regist.RegistFragment;


public enum UserTab
{
    LOGIN(0, R.string.user_login, new LoginFragement()),
    REGIST(1,  R.string.user_regist, new RegistFragment());

    private Fragment defaultFragmnet;
    private int id;
    private int resName;

    private UserTab(int id, int resName, Fragment fragment)
    {
        this.id = id;
        this.resName = resName;
        this.defaultFragmnet = fragment;
    }

    public Fragment getFragmnet()
    {
        return this.defaultFragmnet;
    }

    public int getId()
    {
        return this.id;
    }

    public int getResName()
    {
        return this.resName;
    }
}
