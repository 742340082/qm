package com.news.mvp.guoke.presenter;

import com.news.mvp.guoke.biz.GuoKeDailyBizImpl;
import com.news.mvp.guoke.view.GuokeView;


public class GuoKeDailyPresenter
{
    private GuoKeDailyBizImpl mPresenter;

    public GuoKeDailyPresenter(GuokeView guokeView)
    {
        this.mPresenter = new GuoKeDailyBizImpl(guokeView);
    }


    public void loadingData()
    {
        this.mPresenter.loadingData();
    }

}
