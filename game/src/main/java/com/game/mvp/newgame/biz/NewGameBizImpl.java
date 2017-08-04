package com.game.mvp.newgame.biz;

import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.model.game.newgame.NewGame;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
import com.game.mvp.newgame.view.NewGameView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 74234 on 2017/8/2.
 */

public class NewGameBizImpl implements NewGameBiz {
    private NewGameView mNewGameView;
    private GameApi mGameApi;
    public NewGameBizImpl(NewGameView newGameView)
    {
        mNewGameView=newGameView;
        mGameApi = QMApi.getInstance(GameApi.GAME_ROOT_PATH).create(GameApi.class);
    }
    @Override
    public void initNewGame() {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            mGameApi.gameNewGame()
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mNewGameView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<NewGame>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(NewGame value) {
                            mNewGameView.success(value.getResult());
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mNewGameView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }else
        {

        }
    }
}
