package com.game.mvp.category.modle;

/**
 * Created by 74234 on 2017/6/12.
 */

public class Category {
    private int code;
    private String message;
    private CategoryResult result;

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

    public CategoryResult getResult() {
        return result;
    }

    public void setResult(CategoryResult result) {
        this.result = result;
    }
}
