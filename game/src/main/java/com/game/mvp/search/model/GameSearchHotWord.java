package com.game.mvp.search.model;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/17.
 */

public class GameSearchHotWord extends DataSupport{
    private int gameId;
    private String icopath;
    private long times;
    private String word;
    private GameSearchFirstResult gameSearchResult;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getIcopath() {
        return icopath;
    }

    public void setIcopath(String icopath) {
        this.icopath = icopath;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

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
