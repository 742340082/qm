package com.game.mvp.search.biz;

import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
import com.game.mvp.model.Game;
import com.game.mvp.search.model.GameSearch;
import com.game.mvp.search.model.GameSearchFirst;
import com.game.mvp.search.model.GameSearchHotWord;
import com.game.mvp.search.model.GameSearchFirstResult;
import com.game.mvp.search.model.GameSearchResult;
import com.game.mvp.search.model.GameSearchSuggestWord;
import com.game.mvp.search.model.GameSmallSearch;
import com.game.mvp.search.model.GameSmallSearchResult;
import com.game.mvp.search.view.GameSearchView;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 74234 on 2017/6/17.
 */

public class GameSearchBizImpl implements GameSearchBiz {
    private GameSearchView gameSearchView;
    private final GameApi gameApi;

    public GameSearchBizImpl(GameSearchView gameSearchView) {
        gameApi = QMApi.getInstance(GameApi.GAME_ROOT_PATH).create(GameApi.class);
        this.gameSearchView = gameSearchView;
    }

    @Override
    public void initGameSearch() {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            gameApi.gameSearch()
                    .subscribeOn(Schedulers.io())
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GameSearchFirst>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(GameSearchFirst value) {
                            final GameSearchFirstResult gameSearchResult = value.getResult();
                            final List<GameSearchHotWord> hotWords = gameSearchResult.getHotWords();
                            final List<GameSearchSuggestWord> searchSuggestWords = gameSearchResult.getSearchSuggestWords();
                            List<GameSearchFirstResult> gameSearchResults = DataSupport.where("1=1").find(GameSearchFirstResult.class);
                            if (gameSearchResults.size() == 0) {
                                Executors.newCachedThreadPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        DataSupport.saveAll(hotWords);
                                        DataSupport.saveAll(searchSuggestWords);
                                        gameSearchResult.saveThrows();
                                    }
                                });
                            }
                            gameSearchView.success(gameSearchResult);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            GameSearchFirstResult gameSearchResult = DataSupport.findFirst(GameSearchFirstResult.class);
            if (gameSearchResult != null) {
                List<GameSearchHotWord> gameSearchHotWords = DataSupport.where("gamesearchfirstresult_id=?", gameSearchResult.getId() + "").find(GameSearchHotWord.class);
                List<GameSearchSuggestWord> gameSearchSuggestWords = DataSupport.where("gamesearchfirstresult_id=?", gameSearchResult.getId() + "").find(GameSearchSuggestWord.class);
                gameSearchResult.setHotWords(gameSearchHotWords);
                gameSearchResult.setSearchSuggestWords(gameSearchSuggestWords);
                gameSearchView.success(gameSearchResult);
            } else {
                gameSearchView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            }
        }
    }

    @Override
    public void smallSearchGame(final String word) {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            gameApi.gameSmallSearch(word)
                    .subscribeOn(Schedulers.io())
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GameSmallSearch>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(GameSmallSearch value) {
                            final GameSmallSearchResult result = value.getResult();
                            result.setWord(word);
                            final List<Game> data = result.getData();
                            if (data != null && data.size() > 0) {
                                if (DataSupport.where("word=?", result.getWord())
                                        .findFirst(GameSmallSearchResult.class) == null) {
                                    DataSupport.saveAll(data);
                                    result.saveThrows();
                                }
                                gameSearchView.initSamllSearchHeader(data.get(0));
                                data.remove(0);
                                gameSearchView.successSmallSearch(result);
                            }

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else

        {
            GameSmallSearchResult gameSmallSearchResult = DataSupport.where("word=?", word)
                    .findFirst(GameSmallSearchResult.class);
            if (gameSmallSearchResult == null) {
                gameSearchView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            } else {
                List<Game> games = DataSupport.where("gamesmallsearchresult_id=?", gameSmallSearchResult.getId() + "").find(Game.class);
                gameSmallSearchResult.setData(games);
                gameSearchView.initSamllSearchHeader(games.get(0));
                games.remove(0);
                gameSearchView.successSmallSearch(gameSmallSearchResult);

            }

        }
    }

    @Override
    public void search(final String word,final int startKey) {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            gameApi.gameSearch(word, startKey)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            gameSearchView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GameSearch>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(GameSearch value) {
                            GameSearchResult result = value.getResult();
                            result.setWord(word);
                            List<Game> data = result.getData();
                            if (DataSupport.where(new String[]{"word=? and page=?", result.getWord(),result.getPage() + ""}).findFirst(GameSearchResult.class) == null) {
                                DataSupport.saveAll(data);
                                result.saveThrows();
                            }
                            gameSearchView.searchSuccess(result);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            gameSearchView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            GameSearchResult first = DataSupport.findFirst(GameSearchResult.class);
            if (first==null)
            {
                gameSearchView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            }else
            {
                GameSearchResult gameSearchResult = DataSupport.where(new String[]{"word=? and page=?", word, startKey + ""}).findFirst(GameSearchResult.class);
                if (startKey>1) {
                    if (gameSearchResult == null) {
                        gameSearchView.error(ConfigStateCode.STATE_LOAD_MORE_FAILURES, ConfigStateCode.STATE_LOAD_MORE_FAILURES_VALUE);
                    } else {
                        List<Game> games = DataSupport.where(new String[]{"gamesearchresult_id=?", gameSearchResult.getId() + ""}).find(Game.class);
                        gameSearchResult.setData(games);
                        gameSearchView.searchSuccess(gameSearchResult);
                    }
                }else
                {
                    if (gameSearchResult == null) {
                        gameSearchView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                    } else {
                        List<Game> games = DataSupport.where(new String[]{"gamesearchresult_id=?", gameSearchResult.getId() + ""}).find(Game.class);
                        gameSearchResult.setData(games);
                        gameSearchView.searchSuccess(gameSearchResult);
                    }
                }
            }
        }
    }
}

