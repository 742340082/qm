package com.game.mvp.index.view;

import com.baselibrary.BaseView;
import com.game.mvp.index.modle.IndexPosterBlock;
import com.game.mvp.index.modle.IndexSubjectGame;

import java.util.List;

/**
 * Created by 74234 on 2017/5/18.
 */

public interface IndexView extends BaseView<List<IndexSubjectGame>> {
    void initHeader(IndexPosterBlock posterBlock);
}
