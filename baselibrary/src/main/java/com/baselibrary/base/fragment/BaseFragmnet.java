package com.baselibrary.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baselibrary.UiOperate;
import com.baselibrary.config.ConfigValues;

import butterknife.ButterKnife;

public abstract class BaseFragmnet
        extends AppBaseFragment
        implements UiOperate {
    public View rootView;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    protected boolean isVisible;
    private boolean isLoadData;
    public Bundle getBundle(String title) {
        Bundle localBundle = new Bundle();
        localBundle.putString(ConfigValues.VALUE_SEND_TITLE, title);
        return localBundle;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    public void onClick(View paramView) {
        paramView.getId();
        click(paramView, paramView.getId());
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {

        if (rootView == null) {
            setHasOptionsMenu(true);
            this.rootView = layoutInflater.inflate(getLayoutResId(), null);
            isPrepared = true;
        }
        return this.rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyLoad();
    }
    protected void lazyLoad() {
        if (!isPrepared || !isVisible||isLoadData) {
            return;
        }
        if (isAdded()) {
            ButterKnife.bind(this, rootView);
            initView();
            isLoadData = true;
            //填充各控件的数据
            initData();
            initListener();
        }
    }

}
