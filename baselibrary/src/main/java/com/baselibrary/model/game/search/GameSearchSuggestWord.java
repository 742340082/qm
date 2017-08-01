package com.baselibrary.model.game.search;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/17.
 */

public class GameSearchSuggestWord extends DataSupport{
    private String word;
    private GameSearchFirstResult gameSearchResult;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public GameSearchFirstResult getGameSearchResult() {
        return gameSearchResult;
    }

    public void setGameSearchResult(GameSearchFirstResult gameSearchResult) {
        this.gameSearchResult = gameSearchResult;
    }
}
