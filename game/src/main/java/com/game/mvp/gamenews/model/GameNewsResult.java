package com.game.mvp.gamenews.model;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 74234 on 2017/6/13.
 */

public class GameNewsResult extends DataSupport{
    private int count;
    private int more;
    private int page;
    private int pagecount;
    private int perpage;
    private int start;
    private int startkey;
    private List<GameNewsData> data;
    private List<GameNewsGallary> gallary;
    private List<GameNewsLink> links;

    public List<GameNewsData> getData() {
        return data;
    }

    public void setData(List<GameNewsData> data) {
        this.data = data;
    }

    public List<GameNewsGallary> getGallary() {
        return gallary;
    }

    public void setGallary(List<GameNewsGallary> gallary) {
        this.gallary = gallary;
    }

    public List<GameNewsLink> getLinks() {
        return links;
    }

    public void setLinks(List<GameNewsLink> links) {
        this.links = links;
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

    public int getStartkey() {
        return startkey;
    }

    public void setStartkey(int startkey) {
        this.startkey = startkey;
    }
}
