package com.news.mvp.zhihu.view;


import com.baselibrary.BaseView;
import com.news.mvp.zhihu.modle.Storie;
import com.news.mvp.zhihu.modle.TopStorie;

import java.util.List;

public interface ZhiHuView extends BaseView<List<Storie>>{

    void showContent(List<Storie> stories, List<TopStorie> topStories, boolean isLoadMore);
}
