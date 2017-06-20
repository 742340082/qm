package com.game.mvp.index.biz;

import android.support.annotation.NonNull;

import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
import com.game.mvp.index.modle.Index;
import com.game.mvp.index.modle.IndexExt;
import com.game.mvp.index.modle.PosterBlock;
import com.game.mvp.index.modle.IndexRecBlock;
import com.game.mvp.index.modle.IndexRecPoster;
import com.game.mvp.index.modle.IndexResult;
import com.game.mvp.index.modle.AdListGame;
import com.game.mvp.index.modle.IndexAdList;
import com.game.mvp.index.view.IndexView;
import com.game.mvp.model.Game;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
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

public class IndexBizImpl implements IndexBiz {
    private IndexView mIndexView;
    private final GameApi gameApi;
    private int subjectNum = 0;
    private int gameNum = 0;

    public IndexBizImpl(IndexView recommendView) {
        this.mIndexView = recommendView;
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
                            mIndexView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Index>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Index value) {
                            final IndexResult result = value.getResult();


                            final List<IndexRecPoster> recPoster = result.getRecPoster();
                            final List<IndexRecBlock> recBlock = result.getRecBlock();
                            final List<IndexAdList> adList = result.getAdList();
                            final List<Game> games = result.getRecGame();


                            List<IndexResult> indexResults = DataSupport.where("1=1").find(IndexResult.class);
                            if (indexResults.size() == 0) {
                                Executors.newCachedThreadPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        DataSupport.saveAll(recPoster);
                                        DataSupport.saveAll(recBlock);
                                        for (IndexAdList indexSubjectList : adList) {
                                            IndexExt ext = indexSubjectList.getExt();
                                            List<Game> list = ext.getList();
                                            DataSupport.saveAll(list);
                                            ext.saveThrows();
                                            indexSubjectList.saveThrows();

                                        }
                                        DataSupport.saveAll(games);
                                        result.saveThrows();
                                    }
                                });
                            }

                            PosterBlock posterBlock = new PosterBlock();
                            posterBlock.setRecBlock(recBlock);
                            posterBlock.setRecPoster(recPoster);

                            ArrayList<AdListGame> subjectGames = getIndexSubjectGames(adList, games);

                            mIndexView.initHeader(posterBlock);
                            mIndexView.success(subjectGames);


                        }


                        @Override
                        public void onError(Throwable throwable) {
                            mIndexView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {

            final IndexResult indexResult = DataSupport.findFirst(IndexResult.class);
            if (indexResult == null) {
                mIndexView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            } else {
                List<IndexRecBlock> indexRecBlocks = DataSupport.where("indexresult_id=?", indexResult.getId() + "").find(IndexRecBlock.class);
                List<IndexRecPoster> indexRecPosters = DataSupport.where("indexresult_id=?", indexResult.getId() + "").find(IndexRecPoster.class);
                List<IndexAdList> adList = DataSupport.where("indexresult_id=?", indexResult.getId() + "").find(IndexAdList.class);
                for (IndexAdList indexAdList : adList) {
                    IndexExt indexExt = DataSupport.where("indexadlist_id=?", indexAdList.getId() + "").findFirst(IndexExt.class);
                    List<Game> games = DataSupport.where("indexext_id=?", indexExt.getId() + "").find(Game.class);
                    indexExt.setList(games);
                    indexAdList.setExt(indexExt);
                }
                List<Game> games = DataSupport.where("indexresult_id=?", indexResult.getId() + "").find(Game.class);
                indexResult.setRecPoster(indexRecPosters);
                indexResult.setRecBlock(indexRecBlocks);
                indexResult.setAdList(adList);
                indexResult.setRecGame(games);


                PosterBlock posterBlock = new PosterBlock();
                posterBlock.setRecBlock(indexRecBlocks);
                posterBlock.setRecPoster(indexRecPosters);

                ArrayList<AdListGame> subjectGames = getIndexSubjectGames(adList, games);
                mIndexView.initHeader(posterBlock);
                mIndexView.success(subjectGames);
            }
        }
    }

    @NonNull
    private ArrayList<AdListGame> getIndexSubjectGames(List<IndexAdList> adList, List<Game> games) {
        ArrayList<AdListGame> subjectGames = new ArrayList<>();
        subjectNum = 0;
        gameNum = 0;
        if (games.size() != 0 && adList.size() != 0) {
            for (int i = 0; i < (games.size() + adList.size()); i++) {
                final AdListGame subjectGame = new AdListGame();
                if (subjectGames.size() % 5 == 0) {
                    if (i > 1) {
                        if (subjectNum < adList.size()) {

                            if (subjectNum != 1) {
                                IndexAdList subjectList = adList.get(subjectNum);
                                subjectGame.setSubjectList(subjectList);
                                subjectGames.add(subjectGame);
                            }
                            subjectNum++;
                            continue;
                        }

                    }
                }
                Game game1 = games.get(gameNum);
                subjectGame.setGame(game1);
                gameNum++;
                subjectGames.add(subjectGame);

            }
        }
        return subjectGames;
    }


}
