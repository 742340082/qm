package com.game.mvp.detail.presenter;

import com.game.mvp.detail.biz.GameDetailBiz;
import com.game.mvp.detail.biz.GameDetailBizImpl;
import com.game.mvp.detail.view.GameDetailSummaryView;

/**
 * Created by 74234 on 2017/7/25.
 */

public class GameDetailPresenter {
    private GameDetailBiz gameDetailBiz;
    public GameDetailPresenter(GameDetailSummaryView gameDetailView)
    {
        gameDetailBiz=new GameDetailBizImpl(gameDetailView);
    }

    public void initGameDetail(String gameId,String packageName)
    {
        gameDetailBiz.initGameDetail(gameId,packageName);
    }
}
