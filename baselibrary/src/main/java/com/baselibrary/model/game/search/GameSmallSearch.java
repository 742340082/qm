package com.baselibrary.model.game.search;

/**
 * Created by 74234 on 2017/6/19.
 */

public class GameSmallSearch {
    private int code;
    private String message;
    private GameSmallSearchResult result;

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

    public GameSmallSearchResult getResult() {
        return result;
    }

    public void setResult(GameSmallSearchResult result) {
        this.result = result;
    }
}
