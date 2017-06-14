package com.game.mvp.gamenews.presenter;

import com.game.mvp.gamenews.biz.GameNewBizImpl;
import com.game.mvp.gamenews.biz.GameNewsBiz;
import com.game.mvp.gamenews.view.GameNewsView;

/**
 * Created by 74234 on 2017/6/13.
 */

public class GameNewsPresneter {

    private final GameNewsBiz gameNewBiz;

    public GameNewsPresneter(GameNewsView gameNewsView) {
        gameNewBiz = new GameNewBizImpl(gameNewsView);
    }

    public void initGameNews(int page) {
        gameNewBiz.initGameNews(page);
    }
}
