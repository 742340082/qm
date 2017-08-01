package com.baselibrary.model.game.category.detail;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 74234 on 2017/6/16.
 */

public class CategoryDetailSubType extends DataSupport implements Serializable {
    private int current;
    private String title;
    private  String type;
    private CategoryDetailResult categoryDetailResult;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CategoryDetailResult getCategoryDetailResult() {
        return categoryDetailResult;
    }

    public void setCategoryDetailResult(CategoryDetailResult categoryDetailResult) {
        this.categoryDetailResult = categoryDetailResult;
    }
}
