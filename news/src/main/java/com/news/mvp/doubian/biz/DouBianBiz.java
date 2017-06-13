package com.news.mvp.doubian.biz;

public abstract interface DouBianBiz
{
    void loadMore(long dateTime);
    void loadingData(long dateTime);
}
