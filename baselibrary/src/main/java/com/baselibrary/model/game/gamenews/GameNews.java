package com.baselibrary.model.game.gamenews;

/**
 * Created by 74234 on 2017/6/13.
 */

public class GameNews {
    private int code;
    private  String message;
    private GameNewsResult result;

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

    public GameNewsResult getResult() {
        return result;
    }

    public void setResult(GameNewsResult result) {
        this.result = result;
    }
}
