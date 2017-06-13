package com.yyp.qm.splash.view;


import com.we.mvp.user.modle.User;

public abstract interface SplashView
{
    public abstract void compute(User user);

    public abstract void error(int paramInt);
}
