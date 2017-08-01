package com.game.mvp.gamenews.view;


import com.baselibrary.BaseView;
import com.baselibrary.model.game.gamenews.GameNewsResult;

/**
 * Created by 74234 on 2017/6/13.
 */

public interface GameNewsView extends BaseView<GameNewsResult> {
    void initHeader(GameNewsResult gameNewsResult);
}
