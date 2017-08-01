package com.baselibrary.model.game.gamenews;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/13.
 */

public class GameNewsExt extends DataSupport {
    @SerializedName("id")
    private int gamenews_id;
    private  String url;
    private GameNewsLink gameNewsLink;

    public int getGamenews_id() {
        return gamenews_id;
    }

    public void setGamenews_id(int gamenews_id) {
        this.gamenews_id = gamenews_id;
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
