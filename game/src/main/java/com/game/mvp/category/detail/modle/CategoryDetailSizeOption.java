package com.game.mvp.category.detail.modle;

import org.litepal.crud.DataSupport;

/**
 * Created by 74234 on 2017/6/15.
 */

public class CategoryDetailSizeOption extends DataSupport{
    private String title;
    private String value;
    private int start;
    private int startKey;
    private CategoryDetailResult categoryDetailResult;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStartKey() {
        return startKey;
    }

    public void setStartKey(int startKey) {
        this.startKey = startKey;
    }

    public CategoryDetailResult getCategoryDetailResult() {
        return categoryDetailResult;
    }

    public void setCategoryDetailResult(CategoryDetailResult categoryDetailResult) {
        this.categoryDetailResult = categoryDetailResult;
    }
}
