package com.baselibrary.model.game.index;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/5/18.
 */

public class IndexRecPoster extends DataSupport {
    private long dateline;
    private long id;
    private  String name;
    private  String poster;
    private  String type;
    private  String url;



    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
