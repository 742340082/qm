package com.baselibrary.model.game.search;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 74234 on 2017/6/17.
 */

public class GameSearchFirstResult extends DataSupport {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<GameSearchHotWord> getHotWords() {
        return hotWords;
    }

    public void setHotWords(List<GameSearchHotWord> hotWords) {
        this.hotWords = hotWords;
    }

    public List<GameSearchSuggestWord> getSearchSuggestWords() {
        return searchSuggestWords;
    }

    public void setSearchSuggestWords(List<GameSearchSuggestWord> searchSuggestWords) {
        this.searchSuggestWords = searchSuggestWords;
    }

    private List<GameSearchHotWord> hotWords;
    private List<GameSearchSuggestWord> searchSuggestWords;
}
