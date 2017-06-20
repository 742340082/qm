package com.game.mvp.index.view;

import com.baselibrary.BaseView;
import com.game.mvp.index.modle.PosterBlock;
import com.game.mvp.index.modle.AdListGame;

import java.util.List;

/**
 * Created by 74234 on 2017/5/18.
 */

public interface IndexView extends BaseView<List<AdListGame>> {
    void initHeader(PosterBlock posterBlock);
}
