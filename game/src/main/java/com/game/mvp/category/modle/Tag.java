package com.game.mvp.category.modle;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/11.
 */

public class Tag extends DataSupport {
    private int icon_tag;
    @SerializedName("id")
    private int tag_id;
    private String name;
    private Data data;
    @SerializedName("qmid")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getIcon_tag() {
        return icon_tag;
    }

    public void setIcon_tag(int icon_tag) {
        this.icon_tag = icon_tag;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
