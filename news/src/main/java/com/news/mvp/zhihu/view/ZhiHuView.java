package com.news.mvp.zhihu.view;


import com.baselibrary.BaseView;
import com.news.mvp.zhihu.modle.ZhihuStorie;
import com.news.mvp.zhihu.modle.ZhihuTopStorie;

import java.util.List;

public interface ZhiHuView extends BaseView<List<ZhihuStorie>>{

    void showContent(List<ZhihuStorie> stories, List<ZhihuTopStorie> topStories, boolean isLoadMore);
}
