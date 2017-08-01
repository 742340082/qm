package com.baselibrary.model.game.detail.strategy;

import java.util.List;

/**
 * Created by 74234 on 2017/7/28.
 */

public class GameStrategyNews {
    private int count;
    private int more;
    private int page;
    private int pagecount;
    private int perpage;
    private int start;
    private int startKey;

    private List<GameStrategyInfo> list;

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
}
