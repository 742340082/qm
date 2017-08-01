package com.game.mvp.detail.biz;

import android.util.Log;

import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.model.game.detail.summary.GameSummary;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
import com.game.mvp.detail.view.GameDetailSummaryView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 74234 on 2017/7/25.
 */

public class GameDetailBizImpl implements GameDetailBiz {

    private GameDetailSummaryView mGameDetailView;
    private GameApi mGameApi;
    public GameDetailBizImpl(GameDetailSummaryView gameDetailView)
    {
        this.mGameDetailView=gameDetailView;
        this.mGameApi = QMApi.getInstance(GameApi.GAME_ROOT_PATH).create(GameApi.class);
    }
    @Override
    public void initGameDetail(String gameId, String packageName) {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            mGameApi.gameDetail(gameId,packageName)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mGameDetailView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GameSummary>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(GameSummary value) {
                            Log.e("TAG",value.getMessage());
                            mGameDetailView.success(value.getResult());
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            mGameDetailView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
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

    @Override
    public void initGameStrategy(String gameId) {

    }
}
