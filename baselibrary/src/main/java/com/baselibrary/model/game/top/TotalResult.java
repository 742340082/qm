package com.baselibrary.model.game.top;


import com.baselibrary.model.game.Game;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 74234 on 2017/6/12.
 */

public class TotalResult extends DataSupport {
    private int count;
    private int more;
    private int page;
    private int  pagecount;
    private int perpage;
    private int start;
    private int startKey;
    private List<Game> data;
    private String top_type;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTop_type() {
        return top_type;
    }

    public void setTop_type(String top_type) {
        this.top_type = top_type;
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
}
