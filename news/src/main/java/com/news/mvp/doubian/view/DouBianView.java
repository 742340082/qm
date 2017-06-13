package com.news.mvp.doubian.view;


import com.baselibrary.BaseView;
import com.news.mvp.doubian.bean.Posts;

import java.util.List;

public  interface DouBianView extends BaseView<List<Posts>>
{
      void showContent(List<Posts> postses, boolean isloadMore);
}
