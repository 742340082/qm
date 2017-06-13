package com.game.mvp.recommend.presenter;

import com.game.mvp.recommend.biz.RecommendBiz;
import com.game.mvp.recommend.biz.RecommendBizImpl;
import com.game.mvp.recommend.view.RecommendView;

/**
 * Created by 74234 on 2017/5/18.
 */

public class RecommendPresenter {

    private final RecommendBiz recommendBiz;

    public RecommendPresenter(RecommendView recommendView)
    {
        recommendBiz = new RecommendBizImpl(recommendView);
    }
    public  void initRecommendGame()
    {
        recommendBiz.initGameRecommend();
    }
}
