package com.game.mvp.category.detail.modle;

/**
 * Created by 74234 on 2017/6/15.
 */

public class CategoryDetail {
    private int code;
    private String message;
    private CategoryDetailResult result;

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

    public CategoryDetailResult getResult() {
        return result;
    }

    public void setResult(CategoryDetailResult result) {
        this.result = result;
    }
}
