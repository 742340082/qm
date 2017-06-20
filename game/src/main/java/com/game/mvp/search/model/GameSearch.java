package com.game.mvp.search.model;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/19.
 */

public class GameSearch extends DataSupport {
    private int code;
    private String message;
    private GameSearchResult result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GameSearchResult getResult() {
        return result;
    }

    public void setResult(GameSearchResult result) {
        this.result = result;
    }
}
