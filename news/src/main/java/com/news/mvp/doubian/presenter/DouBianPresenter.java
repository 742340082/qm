package com.news.mvp.doubian.presenter;

import com.news.mvp.doubian.biz.DouBianBizImpl;
import com.news.mvp.doubian.view.DouBianView;


public class DouBianPresenter
{

    private  DouBianBizImpl mPresenter;
    public DouBianPresenter(DouBianView bianView)
    {
        this.mPresenter = new DouBianBizImpl(bianView);
    }
    public void loadMore(long dateTime)
    {
        mPresenter.loadMore(dateTime);
    }
    public void loadingData(long dateTime)
    {
        mPresenter.loadingData(dateTime);
    }



}
