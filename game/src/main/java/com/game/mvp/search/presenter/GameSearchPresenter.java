package com.game.mvp.search.presenter;

import com.game.mvp.search.biz.GameSearchBiz;
import com.game.mvp.search.biz.GameSearchBizImpl;
import com.game.mvp.search.view.GameSearchView;

/**
 * Created by 74234 on 2017/6/17.
 */

public class GameSearchPresenter {
    private GameSearchBiz gameSearchBiz;
    public GameSearchPresenter(GameSearchView gameSearchView)
    {
        gameSearchBiz=new GameSearchBizImpl(gameSearchView);
    }
    public void initGameSearch()
    {
        gameSearchBiz.initGameSearch();
    }
    public void smallSearchGame(String word)
    {
        gameSearchBiz.smallSearchGame(word);
    }
    public void search(String word,int startKey)
    {
        gameSearchBiz.search(word,startKey);
    }
}
