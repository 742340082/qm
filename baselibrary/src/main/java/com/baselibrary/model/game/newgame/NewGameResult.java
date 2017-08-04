package com.baselibrary.model.game.newgame;

import java.util.List;

/**
 * Created by 74234 on 2017/8/2.
 */

public class NewGameResult {
    private List<NewGameData> data;
    private List<NewGameLink> links;

    public List<NewGameData> getData() {
        return data;
    }

    public void setData(List<NewGameData> data) {
        this.data = data;
    }

    public List<NewGameLink> getLinks() {
        return links;
    }

    public void setLinks(List<NewGameLink> links) {
        this.links = links;
    }
}
