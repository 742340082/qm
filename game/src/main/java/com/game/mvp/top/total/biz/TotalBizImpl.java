package com.game.mvp.top.total.biz;

import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.model.game.Game;
import com.baselibrary.model.game.top.Total;
import com.baselibrary.model.game.top.TotalResult;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
import com.game.mvp.top.total.view.TotalView;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by 74234 on 2017/6/12.
 */

public class TotalBizImpl implements TotalBiz {
    private TotalView totalView;
    private final GameApi gameApi;

    public TotalBizImpl(TotalView totalView) {
        this.totalView = totalView;
        Retrofit instance = QMApi.getInstance(GameApi.GAME_ROOT_PATH);
        gameApi = instance.create(GameApi.class);
    }

    @Override
    public void initGameTopTotal(int page, final String topType) {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            gameApi.gameTOP(page, topType)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            totalView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Total>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Total value) {
                            Logger.e("TAG", value.toString());
                            TotalResult result = value.getResult();
                            result.setTop_type(topType);
                            if (DataSupport.where("top_type=? and page=?", result.getTop_type(), result.getPage() + "").findFirst(TotalResult.class) == null) {
                                List<Game> data = result.getData();
                                DataSupport.saveAll(data);
                                result.saveThrows();
                            }
                            totalView.success(result);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            totalView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            TotalResult first = DataSupport.findFirst(TotalResult.class);
            if (first == null) {
                totalView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            } else {
                TotalResult totalResult = DataSupport.where("top_type=? and page=?", topType, page + "").findFirst(TotalResult.class);
                if (page > 1) {
                    if (totalResult==null) {
                        totalView.error(ConfigStateCode.STATE_LOAD_MORE_FAILURES, ConfigStateCode.STATE_LOAD_MORE_FAILURES_VALUE);
                    }else
                    {
                        List<Game> games = DataSupport.where("totalresult_id=?", totalResult.getId() + "").find(Game.class);
                        totalResult.setData(games);
                        totalView.success(totalResult);
                    }
                } else {
                    if (totalResult == null) {
                        totalView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                    } else {
                        List<Game> games = DataSupport.where("totalresult_id=?", totalResult.getId() + "").find(Game.class);
                        totalResult.setData(games);
                        totalView.success(totalResult);
                    }

                }
            }

        }
    }
}
