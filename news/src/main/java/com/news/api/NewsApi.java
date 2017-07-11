package com.news.api;


import com.news.mvp.detail.modle.DoubanDetail;
import com.news.mvp.detail.modle.ZhiHuDetail;
import com.news.mvp.doubian.model.DouBian;
import com.news.mvp.guoke.model.GuoKe;
import com.news.mvp.zhihu.modle.Zhihu;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsApi {

    ////////////////////////////网易新闻/////////////////////////////////////////


    //////////////////////////////豆瓣///////////////////////////////////////////////
    String NEWS_DOUBIAN_ROOT_API = "https://moment.douban.com/api/";
    // 获取文章具体内容
    // eg:https://moment.douban.com/api/post/100484
    String DOUBAN_ARTICLE_DETAIL = "https://moment.douban.com/api/post/";

    // 豆瓣一刻
    // 根据日期查询消息列表
    // eg:https://moment.douban.com/api/stream/date/2016-08-11
    @GET("stream/date/{date}")
    Observable<DouBian> obtainNewsDouBian(@Path("date") String date);

    @GET("post/{id}")
    Observable<DoubanDetail> obtainDouBianDetail(@Path("id") String id);


    //////////////////////////////果壳///////////////////////////////////////////////
    String NEWS_GUOKE_ROOT_API = "http://apis.guokr.com/handpick/";
    String NEWS_GUOKE_ROOT_API_V1 = "http://jingxuan.guokr.com/pick/";

    @GET("article.json?retrieve_type=by_since&category=all&limit=25&ad=1")
    Observable<GuoKe> obtaiNewsGuoke();

    @GET("{id}")
    Call<String> obtaiGuokeDetail(@Path("id") String id);


    ////////////////////////////////知乎////////////////////////////////////////
    String NEWS_ZHIHU_ROOT_API = "http://news.at.zhihu.com/api/4/news/";

    @GET("before/{date}")
    Observable<Zhihu> obtainZhiHuBefore(@Path("date") String date);

    @GET("latest")
    Observable<Zhihu> obtainZhiHuLatest();

    @GET("{id}")
    Observable<ZhiHuDetail> obtainZhiHuDetail(@Path("id") String newsID);


}
