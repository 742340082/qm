package com.news.mvp.zhihu.biz;

public  interface ZhiHuBiz
{
      void loadMore(long dateTime);
      void loadingData(long dateTime);
      void selectreTimefreshData(long dateTime);

}
