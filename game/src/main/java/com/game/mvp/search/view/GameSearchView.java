package com.game.mvp.search.view;

import com.baselibrary.BaseView;
import com.game.mvp.model.Game;
import com.game.mvp.search.model.GameSearchFirstResult;
import com.game.mvp.search.model.GameSearchResult;
import com.game.mvp.search.model.GameSmallSearchResult;

/**
 * Created by 74234 on 2017/6/17.
 */

public interface GameSearchView extends BaseView<GameSearchFirstResult> {
    void successSmallSearch(GameSmallSearchResult gameSmallSearchResult);
    void initSamllSearchHeader(Game game);
    void searchSuccess(GameSearchResult gameSearchResult);
}
