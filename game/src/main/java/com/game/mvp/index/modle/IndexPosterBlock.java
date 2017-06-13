package com.game.mvp.index.modle;

import java.util.List;

/**
 * Created by 74234 on 2017/5/19.
 */

public class IndexPosterBlock {
    private List<IndexRecBlock> recBlock;
    private List<IndexRecBlock> recPoster;

    public List<IndexRecBlock> getRecBlock() {
        return recBlock;
    }

    public void setRecBlock(List<IndexRecBlock> recBlock) {
        this.recBlock = recBlock;
    }

    public void setRecPoster(List<IndexRecBlock> recPoster) {
        this.recPoster = recPoster;
    }

    public List<IndexRecBlock> getRecPoster() {
        return recPoster;
    }
}
