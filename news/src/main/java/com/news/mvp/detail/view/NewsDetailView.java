package com.news.mvp.detail.view;


import com.baselibrary.BaseView;
import com.news.mvp.detail.modle.DoubanDetail;
import com.news.mvp.detail.modle.GuokeDetail;
import com.news.mvp.detail.modle.ZhiHuDetail;

public interface NewsDetailView extends BaseView<String>{
    void success(ZhiHuDetail zhiHuDetail);
    void success(DoubanDetail doubanDetail);
    void success(GuokeDetail guokeDetail);
}
