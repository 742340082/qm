package com.game.mvp.index.modle;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 74234 on 2017/5/19.
 */

public class IndexPosterBlock extends DataSupport {
    private int id;

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private List<IndexRecBlock> recBlock;
    private List<IndexRecPoster> recPoster;

    public List<IndexRecBlock> getRecBlock() {
        return recBlock;
    }

    public void setRecBlock(List<IndexRecBlock> recBlock) {
        this.recBlock = recBlock;
    }

    public void setRecPoster(List<IndexRecPoster> recPoster) {
        this.recPoster = recPoster;
    }

    public List<IndexRecPoster> getRecPoster() {
        return recPoster;
    }
}
