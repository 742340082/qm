package com.game.mvp.search.view;


import com.baselibrary.BaseView;
import com.baselibrary.model.game.Game;
import com.baselibrary.model.game.search.GameSearchFirstResult;
import com.baselibrary.model.game.search.GameSearchResult;
import com.baselibrary.model.game.search.GameSmallSearchResult;

/**
 * Created by 74234 on 2017/6/17.
 */

public interface GameSearchView extends BaseView<GameSearchFirstResult> {
    void successSmallSearch(GameSmallSearchResult gameSmallSearchResult);
    void initSamllSearchHeader(Game game);
    void searchSuccess(GameSearchResult gameSearchResult);
}
