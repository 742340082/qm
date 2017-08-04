package com.baselibrary.model.game.newgame;

import com.baselibrary.model.game.Game;

import java.util.List;

/**
 * Created by 74234 on 2017/8/2.
 */

public class NewGameData {
    private long day;
    private List<Game> games;

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
