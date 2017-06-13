package com.game.mvp.gamenews.model;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/13.
 */

public class GameNewsExt extends DataSupport {
    private int id;
    private  String url;
    private GameNewsLink gameNewsLink;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GameNewsLink getGameNewsLink() {
        return gameNewsLink;
    }

    public void setGameNewsLink(GameNewsLink gameNewsLink) {
        this.gameNewsLink = gameNewsLink;
    }
}
