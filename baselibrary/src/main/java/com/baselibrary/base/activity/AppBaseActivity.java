package com.baselibrary.base.activity;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baselibrary.UiOperate;

public class AppBaseActivity
        extends AppCompatActivity
        implements UiOperate
{
    public void click(View paramView, int paramInt) {}

    public int getLayoutResId()
    {
        return 0;
    }

    public void initData() throws RemoteException {}

    public void initListener() {}


    public void initView() {}

    public void onClick(View paramView) {}
}
