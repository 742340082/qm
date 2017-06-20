package com.game.mvp.search.model;

/**
 * Created by 74234 on 2017/6/17.
 */

public class GameSearchFirst {
    private int code;
    private String message;
    private GameSearchFirstResult result;

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

    public GameSearchFirstResult getResult() {
        return result;
    }

    public void setResult(GameSearchFirstResult result) {
        this.result = result;
    }
}
