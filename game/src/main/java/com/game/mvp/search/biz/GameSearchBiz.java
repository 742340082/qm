package com.game.mvp.search.biz;

/**
 * Created by 74234 on 2017/6/17.
 */

public interface GameSearchBiz {
    void initGameSearch();
    void smallSearchGame(String word);
    void search(String word,int startKey);
}
