package com.baselibrary.model.game.detail.strategy;

/**
 * Created by 74234 on 2017/7/28.
 */

public class GameStrategy {
    private int code;
    private String message;
    private GameStrategyResult result;

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

    public GameStrategyResult getResult() {
        return result;
    }

    public void setResult(GameStrategyResult result) {
        this.result = result;
    }
}
