package com.game.api;


import com.game.mvp.category.modle.Category;
import com.game.mvp.index.modle.Index;
import com.game.mvp.top.modle.Total;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by 74234 on 2017/5/18.
 */

public interface GameApi {
    String GAME_ROOT_PATH="http://cdn.4399sj.com/app/android/v3.8/";

    //4399推荐API
    @GET("game-index-mareacode-999999.html")
    Observable<Index> gameRecommend();

    //4399分类API
    @GET("game-category-mareacode-999999-n-20-startKey-.html")
    Observable<Category> gameCategory();

    //4399排行API新游戏
    @GET("game-top-mareacode-999999-n-20-startKey-{pageNumber}-type-new.html")
    Observable<Total> gameTopNEW(@Path("pageNumber") int pageNumber);
    //4399排行API网络游戏
    @GET("game-top-mareacode-999999-n-20-startKey-{pageNumber}-type-net.html")
    Observable<Total> gameTopNET(@Path("pageNumber") int pageNumber);
    //4399排行API单机游戏
    @GET("game-top-mareacode-999999-n-20-startKey-{pageNumber}-type-single.html")
    Observable<Total> gameTopSINGLE(@Path("pageNumber") int pageNumber);
    //4399排行API火的游戏
    @GET("game-top-mareacode-999999-n-20-startKey-{pageNumber}-type-hot.html")
    Observable<Total> gameTopHOT(@Path("pageNumber") int pageNumber);


    //4399新闻
    @GET("news-index-mareacode-999999-n-20-startKey-{pageNumber}.html")
    Observable<Total> gameNEWS(@Path("pageNumber") int pageNumber);
}
