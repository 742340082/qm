package com.game.mvp.index.presenter;

import com.game.mvp.index.biz.IndexBiz;
import com.game.mvp.index.biz.IndexBizImpl;
import com.game.mvp.index.view.IndexView;

/**
 * Created by 74234 on 2017/5/18.
 */

public class IndexPresenter {

    private final IndexBiz recommendBiz;

    public IndexPresenter(IndexView recommendView)
    {
        recommendBiz = new IndexBizImpl(recommendView);
    }
    public  void initRecommendGame()
    {
        recommendBiz.initGameRecommend();
    }
}
