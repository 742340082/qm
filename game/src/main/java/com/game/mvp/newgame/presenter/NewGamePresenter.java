package com.game.mvp.newgame.presenter;

import com.game.mvp.newgame.biz.NewGameBiz;
import com.game.mvp.newgame.biz.NewGameBizImpl;
import com.game.mvp.newgame.view.NewGameView;

/**
 * Created by 74234 on 2017/8/2.
 */

public class NewGamePresenter {
    private NewGameBiz mNewGameBiz;
    public NewGamePresenter(NewGameView newGameView)
    {
        mNewGameBiz=new NewGameBizImpl(newGameView);
    }

    public void initNewGame()
    {
        mNewGameBiz.initNewGame();
    }
}
