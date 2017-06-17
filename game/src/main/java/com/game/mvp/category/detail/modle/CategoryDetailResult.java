package com.game.mvp.category.detail.modle;

import com.game.mvp.model.Game;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 74234 on 2017/6/15.
 */

public class CategoryDetailResult extends DataSupport implements Serializable{
    private int count;
    private int more;
    private int page;
    private int  pagecount;
    private int perpage;
    private int start;
    private int startKey;
    private List<Game> data;
    private List<CategoryDetailTag> tags;
    private List<CategoryDetailSubType> subtypes;
    private List<CategoryDetailSizeOption> sizeOptions;
    private String top_type;
    private int id;

    private int kId;
    private int startSize;
    private  int endSize;
    private String subtype;
    private int tagId;

    public List<CategoryDetailSubType> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(List<CategoryDetailSubType> subtypes) {
        this.subtypes = subtypes;
    }

    public int getkId() {
        return kId;
    }

    public void setkId(int kId) {
        this.kId = kId;
    }

    public int getStartSize() {
        return startSize;
    }

    public void setStartSize(int startSize) {
        this.startSize = startSize;
    }

    public int getEndSize() {
        return endSize;
    }

    public void setEndSize(int endSize) {
        this.endSize = endSize;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagecount() {
        return pagecount;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
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

    public List<Game> getData() {
        return data;
    }

    public void setData(List<Game> data) {
        this.data = data;
    }

    public List<CategoryDetailTag> getTags() {
        return tags;
    }

    public void setTags(List<CategoryDetailTag> tags) {
        this.tags = tags;
    }

    public List<CategoryDetailSizeOption> getSizeOptions() {
        return sizeOptions;
    }

    public void setSizeOptions(List<CategoryDetailSizeOption> sizeOptions) {
        this.sizeOptions = sizeOptions;
    }

    public String getTop_type() {
        return top_type;
    }

    public void setTop_type(String top_type) {
        this.top_type = top_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
