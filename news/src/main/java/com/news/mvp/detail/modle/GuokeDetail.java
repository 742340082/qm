package com.news.mvp.detail.modle;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/5/16.
 */

public class GuokeDetail extends DataSupport{
    private String body;
    private long guokedetail_id;
    private String image;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getGuokedetail_id() {
        return guokedetail_id;
    }

    public void setGuokedetail_id(long guokedetail_id) {
        this.guokedetail_id = guokedetail_id;
    }
}
