package com.news.mvp.zhihu.modle;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/5/12.
 */

public class ZhihuTopStorie extends DataSupport {
    private String ga_prefix;
    @SerializedName("id")
    private long topstorie_id;
    private String image;
    private String title;
    private int type;
    private Zhihu zhihu;
    public Zhihu getZhihu() {
        return zhihu;
    }

    public void setZhihu(Zhihu zhihu) {
        this.zhihu = zhihu;
    }




    public long getTopstorie_id() {
        return topstorie_id;
    }

    public void setTopstorie_id(long topstorie_id) {
        this.topstorie_id = topstorie_id;
    }







    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
