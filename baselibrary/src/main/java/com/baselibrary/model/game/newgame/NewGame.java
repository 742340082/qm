package com.baselibrary.model.game.newgame;

/**
 * Created by 74234 on 2017/8/2.
 */

public class NewGame {
    private int code;
    private String message;
    private NewGameResult result;

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

    public NewGameResult getResult() {
        return result;
    }

    public void setResult(NewGameResult result) {
        this.result = result;
    }
}
