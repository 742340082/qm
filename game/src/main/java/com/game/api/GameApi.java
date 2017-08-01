package com.game.api;


import com.baselibrary.model.game.category.Category;
import com.baselibrary.model.game.category.detail.CategoryDetail;
import com.baselibrary.model.game.detail.summary.GameSummary;
import com.baselibrary.model.game.gamenews.GameNews;
import com.baselibrary.model.game.index.Index;
import com.baselibrary.model.game.search.GameSearch;
import com.baselibrary.model.game.search.GameSearchFirst;
import com.baselibrary.model.game.search.GameSmallSearch;
import com.baselibrary.model.game.top.Total;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 74234 on 2017/5/18.
 */

public interface GameApi {
    String GAME_ROOT_PATH="http://cdn.4399sj.com/app/android/v3.8/";

    ////////////////////////////4399推荐API////////////////////////////////////
    @GET("game-index-mareacode-999999.html")
    Observable<Index> gameRecommend();

    ////////////////////////////4399分类API////////////////////////////////////
    @GET("game-category-mareacode-999999-n-20-startKey-.html")
    Observable<Category> gameCategory();
    ////////////////////////////4399分类内容详情页API////////////////////////////////////
    @GET(" game-list-mareacode-999999-kid-{kid}-n-20-size-{startSize}%2C{endSize}-startKey-{pageNumber}-subtype-{subtype}-tagId-{tagId}.html")
    Observable<CategoryDetail> gameCategoryDetail(@Path("kid") int kid,
                                                  @Path("startSize") int startSize,
                                                  @Path("endSize") int endSize,
                                                  @Path("pageNumber") int pageNumber,
                                                  @Path("subtype") String subtype,
                                                  @Path("tagId") int tagId
                                            );

    ////////////////////////////4399排行API//////////////////////////////////////////
    @GET("game-top-mareacode-999999-n-20-startKey-{pageNumber}-type-{type}.html")
    Observable<Total> gameTOP(@Path("pageNumber") int pageNumber, @Path("type")String type);



    ////////////////////////////4399新闻//////////////////////////////////////////
    @GET("news-index-mareacode-999999-n-20-startKey-{pageNumber}.html")
    Observable<GameNews> gameNEWS(@Path("pageNumber") int pageNumber);

    ////////////////////////////4399搜索///////////////////////////////////////////
    @GET("game-hotwords-mareacode-999999.html")
    Observable<GameSearchFirst> gameSearch();
    @GET("game-soSmart.html")
    Observable<GameSmallSearch> gameSmallSearch(@Query("word") String word);
    @GET("game-search.html")
    Observable<GameSearch> gameSearch(@Query("word") String word, @Query("startKey") int startKey);
    /////////////////////////////游戏详情页///////////////////////////////////////////
    @GET("game-info-mareacode-999999-id-{gameId}-package-{packageName}.html")
    Observable<GameSummary> gameDetail(@Path("gameId") String gameId, @Path("packageName") String packageName);
    @GET("gameDetail-news-mareacode-999999-id-{gameId}-startKey-.html")
    Observable<GameSummary> gameStrategy(@Path("gameId") String gameId);


}
