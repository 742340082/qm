package com.game.mvp.index.view;


import com.baselibrary.BaseView;
import com.baselibrary.model.game.index.AdListGame;
import com.baselibrary.model.game.index.PosterBlock;

import java.util.List;

/**
 * Created by 74234 on 2017/5/18.
 */

public interface IndexView extends BaseView<List<AdListGame>> {
    void initHeader(PosterBlock posterBlock);
}
