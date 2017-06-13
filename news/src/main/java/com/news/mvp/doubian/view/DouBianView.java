package com.news.mvp.doubian.view;


import com.baselibrary.BaseView;
import com.news.mvp.doubian.model.DouBianPosts;

import java.util.List;

public  interface DouBianView extends BaseView<List<DouBianPosts>>
{
      void showContent(List<DouBianPosts> postses, boolean isloadMore);
}
