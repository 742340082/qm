package com.game.mvp.gamenews.model;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/13.
 */

public class GameNewsLink extends DataSupport {
    private String img;
    private String name;
    private int type;
    private GameNewsExt gameNewsExt;
    private GameNewsResult gameNewsResult;

    public GameNewsExt getGameNewsExt() {
        return gameNewsExt;
    }

    public void setGameNewsExt(GameNewsExt gameNewsExt) {
        this.gameNewsExt = gameNewsExt;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GameNewsResult getGameNewsResult() {
        return gameNewsResult;
    }

    public void setGameNewsResult(GameNewsResult gameNewsResult) {
        this.gameNewsResult = gameNewsResult;
    }
}
