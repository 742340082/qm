package com.game.mvp.gamenews.biz;

import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
import com.game.mvp.gamenews.model.GameNews;
import com.game.mvp.gamenews.model.GameNewsData;
import com.game.mvp.gamenews.model.GameNewsExt;
import com.game.mvp.gamenews.model.GameNewsGallary;
import com.game.mvp.gamenews.model.GameNewsLink;
import com.game.mvp.gamenews.model.GameNewsResult;
import com.game.mvp.gamenews.view.GameNewsView;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static org.litepal.crud.DataSupport.findFirst;

/**
 * Created by 74234 on 2017/6/13.
 */

public class GameNewBizImpl implements GameNewsBiz {
    private GameNewsView gameNewsView;
    private final GameApi gameApi;

    public GameNewBizImpl(GameNewsView gameNewsView) {
        this.gameNewsView = gameNewsView;
        Retrofit instance = QMApi.getInstance(GameApi.GAME_ROOT_PATH);
        gameApi = instance.create(GameApi.class);
    }

    @Override
    public void initGameNews(int page) {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            gameApi.gameNEWS(page)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            gameNewsView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GameNews>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(GameNews value) {
                            Logger.e("TAG", value.toString());
                            GameNewsResult gameNewsResult = value.getResult();


                            List<GameNewsGallary> gallary = gameNewsResult.getGallary();
                            if (DataSupport.where(new String[]{"page=?", gameNewsResult.getPage() + ""}).findFirst(GameNewsResult.class) == null) {
                                List<GameNewsData> data = gameNewsResult.getData();
                                List<GameNewsLink> links = gameNewsResult.getLinks();
                                if (links!=null) {
                                    for (GameNewsLink link : links) {
                                        GameNewsExt gameNewsExt = link.getExt();
                                        gameNewsExt.saveThrows();
                                        link.saveThrows();
                                    }
                                }
                                if (gallary!=null) {
                                    DataSupport.saveAll(gallary);
                                }
                                DataSupport.saveAll(data);
                                gameNewsResult.saveThrows();
                            }
                            if (gameNewsResult.getPage() == 1) {
                                gameNewsView.initHeader(gameNewsResult);
                            }
                            gameNewsView.success(gameNewsResult);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            gameNewsView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            GameNewsResult first = findFirst(GameNewsResult.class);
            if (first == null) {
                gameNewsView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            } else {
                GameNewsResult gameNewsResult = DataSupport.where(new String[]{"page=?", page + ""}).findFirst(GameNewsResult.class);
                if (page>1) {
                    if (gameNewsResult == null) {
                        gameNewsView.error(ConfigStateCode.STATE_LOAD_MORE_FAILURES, ConfigStateCode.STATE_LOAD_MORE_FAILURES_VALUE);
                    } else {
                        List<GameNewsData> gameNewsDatas = DataSupport.where("gamenewsresult_id=?", gameNewsResult.getId() + "").find(GameNewsData.class);
                        List<GameNewsGallary> gameNewsGallaries = DataSupport.where("gamenewsresult_id=?", gameNewsResult.getId() + "").find(GameNewsGallary.class);
                        List<GameNewsLink> gameNewsLinks = DataSupport.where("gamenewsresult_id=?", gameNewsResult.getId() + "").find(GameNewsLink.class);
                        for (GameNewsLink gameNewsLink : gameNewsLinks) {
                            GameNewsExt gameNewsExt = DataSupport.where("gamenewslink_id=?", gameNewsLink.getId() + "").findFirst(GameNewsExt.class);
                            gameNewsLink.setExt(gameNewsExt);
                        }
                        gameNewsResult.setGallary(gameNewsGallaries);
                        gameNewsResult.setLinks(gameNewsLinks);
                        gameNewsResult.setData(gameNewsDatas);
                        if (gameNewsResult.getPage() == 1) {
                            gameNewsView.initHeader(gameNewsResult);
                        }
                        gameNewsView.success(gameNewsResult);
                    }
                } else {
                    if (gameNewsResult == null) {
                        gameNewsView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                    } else {
                        List<GameNewsData> gameNewsDatas = DataSupport.where("gamenewsresult_id=?", gameNewsResult.getId() + "").find(GameNewsData.class);
                        List<GameNewsGallary> gameNewsGallaries = DataSupport.where("gamenewsresult_id=?", gameNewsResult.getId() + "").find(GameNewsGallary.class);
                        List<GameNewsLink> gameNewsLinks = DataSupport.where("gamenewsresult_id=?", gameNewsResult.getId() + "").find(GameNewsLink.class);
                        for (GameNewsLink gameNewsLink : gameNewsLinks) {
                            GameNewsExt gameNewsExt = DataSupport.where("gamenewslink_id=?", gameNewsLink.getId() + "").findFirst(GameNewsExt.class);
                            gameNewsLink.setExt(gameNewsExt);
                        }
                        gameNewsResult.setGallary(gameNewsGallaries);
                        gameNewsResult.setLinks(gameNewsLinks);
                        gameNewsResult.setData(gameNewsDatas);
                        if (gameNewsResult.getPage() == 1) {
                            gameNewsView.initHeader(gameNewsResult);
                        }
                        gameNewsView.success(gameNewsResult);
                    }
                }
            }
        }
    }
}
