package com.game.mvp.top.modle;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/12.
 */

public class Total extends DataSupport{
    private  int code;
    private String message;
    private TotalResult result;

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

    public TotalResult getResult() {
        return result;
    }

    public void setResult(TotalResult result) {
        this.result = result;
    }
}
