package com.game.mvp.recommend.biz;

import android.support.annotation.NonNull;

import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
import com.game.mvp.model.Game;
import com.game.mvp.recommend.modle.Ext;
import com.game.mvp.recommend.modle.PosterBlock;
import com.game.mvp.recommend.modle.RecBlock;
import com.game.mvp.recommend.modle.Recommend;
import com.game.mvp.recommend.modle.RecommendResult;
import com.game.mvp.recommend.modle.SubjectGame;
import com.game.mvp.recommend.modle.SubjectList;
import com.game.mvp.recommend.view.RecommendView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by 74234 on 2017/5/18.
 */

public class RecommendBizImpl implements RecommendBiz {
    private RecommendView recommendView;
    private final GameApi gameApi;
    private int subjectNum = 0;
    private int gameNum = 0;

    public RecommendBizImpl(RecommendView recommendView) {
        this.recommendView = recommendView;
        Retrofit instance = QMApi.getInstance(GameApi.GAME_ROOT_PATH);
        gameApi = instance.create(GameApi.class);
    }

    @Override
    public void initGameRecommend() {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            gameApi.gameRecommend()
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            recommendView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Recommend>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Recommend value) {
                            RecommendResult result = value.getResult();
                            List<RecBlock> recPoster = result.getRecPoster();
                            List<RecBlock> recBlock = result.getRecBlock();

                            PosterBlock posterBlock = new PosterBlock();
                            posterBlock.setRecBlock(recBlock);
                            posterBlock.setRecPoster(recPoster);
                            recommendView.initHeader(posterBlock);

                            ArrayList<SubjectGame> subjectGames = initNetRecommendContent(result);
                            recommendView.success(subjectGames);
                        }


                        @Override
                        public void onError(Throwable throwable) {
                            recommendView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {

            SubjectGame first = DataSupport.findFirst(SubjectGame.class);
            if (first == null) {
                recommendView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            } else {
                final List<SubjectGame> subjectGames = DataSupport.where("1=1").find(SubjectGame.class);
                if (subjectGames.size() == 0) {
                    recommendView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                } else {
                    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
                    cachedThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < subjectGames.size(); i++) {
                                SubjectGame subjectGame = subjectGames.get(i);

                                SubjectList subjectList = DataSupport.where(new String[]{"subjectgame_id=?", subjectGame.getId() + ""}).findFirst(SubjectList.class);
                                if (subjectList != null) {
                                    Ext ext = DataSupport.where(new String[]{"subjectlist_id=?", subjectList.getId() + ""}).findFirst(Ext.class);
                                    List<Game> games = DataSupport.where(new String[]{"ext_id=?", ext.getId() + ""}).find(Game.class);
                                    ext.setList(games);
                                    subjectList.setExt(ext);
                                }

                                Game game = DataSupport.where(new String[]{"subjectgame_id=?", subjectGame.getId() + ""}).findFirst(Game.class);


                                subjectGame.setGame(game);
                                subjectGame.setSubjectList(subjectList);
                                subjectGames.set(i, subjectGame);
                            }
                        }
                    });
                    recommendView.success(subjectGames);

                }
            }
        }
    }

    @NonNull
    private ArrayList<SubjectGame> initNetRecommendContent(RecommendResult result) {
        final List<SubjectList> adList = result.getAdList();
        final List<Game> game = result.getRecGame();
        final ArrayList<SubjectGame> subjectGames = new ArrayList<>();

        SubjectGame subjectGame = null;
        subjectNum = 0;
        gameNum = 0;
        if (game.size() != 0 && adList.size() != 0) {
            for (int i = 0; i < (game.size() + adList.size()); i++) {
                subjectGame = new SubjectGame();
                if (subjectGames.size() % 5 == 0) {
                    if (i > 1) {
                        if (subjectNum < adList.size()) {

                            if (subjectNum != 1) {
                                SubjectList subjectList = adList.get(subjectNum);
                                List<SubjectGame> subjectGames1 = DataSupport.where("1=1").find(SubjectGame.class);

                                subjectGame.setSubjectList(subjectList);
                                subjectGames.add(subjectGame);
                                if (subjectGames1.size() != game.size() + adList.size()) {
                                    Ext ext = subjectList.getExt();
                                    DataSupport.saveAll(ext.getList());
                                    ext.saveThrows();
                                    subjectList.saveThrows();
                                    subjectGame.saveThrows();
                                }

                            }
                            subjectNum++;
                            continue;
                        }

                    }
                }
                List<SubjectGame> subjectGames1 = DataSupport.where("1=1").find(SubjectGame.class);
                Game game1 = game.get(gameNum);
                subjectGame.setGame(game1);
                if (subjectGames1.size() != (game.size() + adList.size())) {
                    game1.saveThrows();
                    subjectGame.saveThrows();
                }
                gameNum++;
                subjectGames.add(subjectGame);

            }
        }


        return subjectGames;
    }
}
