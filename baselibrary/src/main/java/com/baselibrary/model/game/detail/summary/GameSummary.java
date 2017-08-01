package com.baselibrary.model.game.detail.summary;

/**
 * Created by 74234 on 2017/7/25.
 */

public class GameSummary {
    private int code;
    private String message;
    private GameSummaryResult result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GameSummaryResult getResult() {
        return result;
    }

    public void setResult(GameSummaryResult result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
