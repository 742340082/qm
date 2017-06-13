package com.news.mvp.zhihu.presenter;

import android.content.Context;

import com.news.mvp.zhihu.biz.ZhiHuBizImpl;
import com.news.mvp.zhihu.view.ZhiHuView;

public class ZhiHuPresenter
{
    private Context context;
    private ZhiHuBizImpl mPresenter;

    public ZhiHuPresenter(Context paramContext, ZhiHuView paramZhiHuView)
    {
        this.context = paramContext;
        this.mPresenter = new ZhiHuBizImpl(this.context, paramZhiHuView);
    }


    public void loadMore(long paramLong)
    {
        this.mPresenter.loadMore(paramLong);
    }

    public void loadingData(long dateTime)
    {
        this.mPresenter.loadingData(dateTime);
    }
    public void selectreTimefreshData(long dateTime)
    {
        mPresenter.selectreTimefreshData(dateTime);
    }
}
