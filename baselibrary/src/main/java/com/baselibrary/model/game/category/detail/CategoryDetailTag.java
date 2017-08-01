package com.baselibrary.model.game.category.detail;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/11.
 */

public class CategoryDetailTag extends DataSupport {
    private String icon_tag;
    @SerializedName("id")
    private String tag_id;
    private String name;
    @SerializedName("qmid")
    private int id;
    private CategoryDetailResult categoryDetailResult;

    public CategoryDetailResult getCategoryDetailResult() {
        return categoryDetailResult;
    }

    public void setCategoryDetailResult(CategoryDetailResult categoryDetailResult) {
        this.categoryDetailResult = categoryDetailResult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getIcon_tag() {
        return icon_tag;
    }

    public void setIcon_tag(String icon_tag) {
        this.icon_tag = icon_tag;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
