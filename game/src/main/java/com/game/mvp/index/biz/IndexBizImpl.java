package com.game.mvp.index.biz;

import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
import com.game.mvp.index.modle.Index;
import com.game.mvp.index.modle.IndexExt;
import com.game.mvp.index.modle.IndexPosterBlock;
import com.game.mvp.index.modle.IndexRecBlock;
import com.game.mvp.index.modle.IndexRecPoster;
import com.game.mvp.index.modle.IndexResult;
import com.game.mvp.index.modle.IndexSubjectGame;
import com.game.mvp.index.modle.IndexSubjectList;
import com.game.mvp.index.view.IndexView;
import com.game.mvp.model.Game;

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


                            final ArrayList<IndexSubjectGame> subjectGames = new ArrayList<>();
                            final IndexPosterBlock posterBlock = new IndexPosterBlock();
                            List<IndexRecPoster> recPoster = result.getRecPoster();
                            List<IndexRecBlock> recBlock = result.getRecBlock();
                            final List<IndexPosterBlock> indexPosterBlocks = DataSupport.where("1=1").find(IndexPosterBlock.class);

                            posterBlock.setRecBlock(recBlock);
                            posterBlock.setRecPoster(recPoster);
                            if (indexPosterBlocks.size() == 0) {
                                DataSupport.saveAll(recPoster);
                                DataSupport.saveAll(recBlock);
                                posterBlock.saveThrows();
                            }


                            final List<IndexSubjectList> adList = result.getAdList();
                            final List<Game> game = result.getRecGame();
                            IndexSubjectGame subjectGame = null;
                            subjectNum = 0;
                            gameNum = 0;
                            if (game.size() != 0 && adList.size() != 0) {
                                for (int i = 0; i < (game.size() + adList.size()); i++) {
                                    subjectGame = new IndexSubjectGame();
                                    if (subjectGames.size() % 5 == 0) {
                                        if (i > 1) {
                                            if (subjectNum < adList.size()) {

                                                if (subjectNum != 1) {
                                                    IndexSubjectList subjectList = adList.get(subjectNum);
                                                    List<IndexSubjectGame> subjectGames1 = DataSupport.where("1=1").find(IndexSubjectGame.class);

                                                    subjectGame.setSubjectList(subjectList);
                                                    subjectGames.add(subjectGame);
                                                    if (subjectGames1.size() != game.size() + adList.size()) {
                                                        IndexExt ext = subjectList.getExt();
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
                                    List<IndexSubjectGame> subjectGames1 = DataSupport.where("1=1").find(IndexSubjectGame.class);
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

            IndexSubjectGame first = DataSupport.findFirst(IndexSubjectGame.class);
            if (first == null) {
                mIndexView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            } else {
                final List<IndexSubjectGame> subjectGames = DataSupport.where("1=1").find(IndexSubjectGame.class);
                List<IndexPosterBlock> indexPosterBlocks = DataSupport.where("1=1").find(IndexPosterBlock.class);
                if (subjectGames.size() == 0 && indexPosterBlocks.size() == 0) {
                    mIndexView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                } else {
                    IndexPosterBlock indexPosterBlock = indexPosterBlocks.get(0);
                    List<IndexRecPoster> indexRecPosters = DataSupport.where("indexposterblock_id=?", indexPosterBlock.getId() + "").find(IndexRecPoster.class);
                    List<IndexRecBlock> indexRecBlocks = DataSupport.where("indexposterblock_id=?", indexPosterBlock.getId() + "").find(IndexRecBlock.class);
                    indexPosterBlock.setRecBlock(indexRecBlocks);
                    indexPosterBlock.setRecPoster(indexRecPosters);
                    mIndexView.initHeader(indexPosterBlock);

                    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
                    cachedThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < subjectGames.size(); i++) {
                                IndexSubjectGame subjectGame = subjectGames.get(i);
                                IndexSubjectList subjectList = DataSupport.where(new String[]{"indexsubjectgame_id=?", subjectGame.getId() + ""}).findFirst(IndexSubjectList.class);
                                if (subjectList != null) {
                                    IndexExt ext = DataSupport.where(new String[]{"indexsubjectlist_id=?", subjectList.getId() + ""}).findFirst(IndexExt.class);
                                    List<Game> games = DataSupport.where(new String[]{"indexext_id=?", ext.getId() + ""}).find(Game.class);
                                    ext.setList(games);
                                    subjectList.setExt(ext);
                                }

                                Game game = DataSupport.where(new String[]{"indexsubjectgame_id=?", subjectGame.getId() + ""}).findFirst(Game.class);


                                subjectGame.setGame(game);
                                subjectGame.setSubjectList(subjectList);
                                subjectGames.set(i, subjectGame);
                            }
                        }
                    });
                    mIndexView.success(subjectGames);

                }
            }
        }
    }


}
