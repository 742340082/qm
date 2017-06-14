package com.game.mvp.gamenews.view;

import com.baselibrary.BaseView;
import com.game.mvp.gamenews.model.GameNewsResult;

/**
 * Created by 74234 on 2017/6/13.
 */

public interface GameNewsView extends BaseView<GameNewsResult> {
    void initHeader(GameNewsResult gameNewsResult);
}
