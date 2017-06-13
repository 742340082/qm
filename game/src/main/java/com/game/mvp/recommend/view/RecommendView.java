package com.game.mvp.recommend.view;

import com.baselibrary.BaseView;
import com.game.mvp.recommend.modle.PosterBlock;
import com.game.mvp.recommend.modle.SubjectGame;

import java.util.List;

/**
 * Created by 74234 on 2017/5/18.
 */

public interface RecommendView extends BaseView<List<SubjectGame>> {
    void initHeader(PosterBlock posterBlock);
}
