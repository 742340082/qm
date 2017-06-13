package com.game.mvp.gamenews.model;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/13.
 */

public class GameNewsGallary extends DataSupport{
    private String dateline;
    private String desc;
    private String gallaryImg;
    private int gid;
    private String img;
    private String news_id;
    private String title;
    private int type;
    private int video_id;
    private GameNewsResult gameNewsResult;

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGallaryImg() {
        return gallaryImg;
    }

    public void setGallaryImg(String gallaryImg) {
        this.gallaryImg = gallaryImg;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public GameNewsResult getGameNewsResult() {
        return gameNewsResult;
    }

    public void setGameNewsResult(GameNewsResult gameNewsResult) {
        this.gameNewsResult = gameNewsResult;
    }
}
