package com.baselibrary;

import android.view.View;

public abstract interface UiOperate
        extends View.OnClickListener
{
    public abstract void click(View paramView, int paramInt);

    public abstract int getLayoutResId();

    public abstract void initData();

    public abstract void initListener();


    public abstract void initView();
}
