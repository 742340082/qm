package com.game.mvp.category.modle;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 74234 on 2017/6/11.
 */

public class CategoryResult extends DataSupport {
    private List<CategoryData> data=new ArrayList<>();
    private List<CategoryLink> links=new ArrayList<>();
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public List<CategoryData> getData() {
        return data;
    }

    public void setData(List<CategoryData> data) {
        this.data = data;
    }

    public List<CategoryLink> getLinks() {
        return links;
    }

    public void setLinks(List<CategoryLink> links) {
        this.links = links;
    }
}
