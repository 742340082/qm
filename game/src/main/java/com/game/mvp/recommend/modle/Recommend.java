package com.game.mvp.recommend.modle;

/**
 * Created by 74234 on 2017/5/18.
 */

public class Recommend   {
    private int code;
    private  String message;
    private RecommendResult result;

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

    public RecommendResult getResult() {
        return result;
    }

    public void setResult(RecommendResult result) {
        this.result = result;
    }


}
