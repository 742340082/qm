package com.game.mvp.recommend.modle;

import java.util.List;

/**
 * Created by 74234 on 2017/5/19.
 */

public class PosterBlock {
    private List<RecBlock> recBlock;
    private List<RecBlock> recPoster;

    public List<RecBlock> getRecBlock() {
        return recBlock;
    }

    public void setRecBlock(List<RecBlock> recBlock) {
        this.recBlock = recBlock;
    }

    public void setRecPoster(List<RecBlock> recPoster) {
        this.recPoster = recPoster;
    }

    public List<RecBlock> getRecPoster() {
        return recPoster;
    }
}
