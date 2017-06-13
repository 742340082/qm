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
    public static final String NETEAST_HOST = "http://c.m.163.com/";
    public static final String END_URL = "-20.html";
    public static final String ENDDETAIL_URL = "/full.html";

    // 新闻详情
    public static final String NEWS_DETAIL = NETEAST_HOST + "nc/article/";

    // 头条TYPE
    public static final String HEADLINE_TYPE = "headline";
    // 房产TYPE
    public static final String HOUSE_TYPE = "house";
    // 其他TYPE
    public static final String OTHER_TYPE = "list";

    //    // 北京
    //    public static final String LOCAL_TYPE = "local";
    //    // 北京的Id
    //    public static final String BEIJING_ID = "5YyX5Lqs";

    // 头条id
    public static final String HEADLINE_ID = "T1348647909107";
    // 房产id
    public static final String HOUSE_ID = "5YyX5Lqs";
    // 足球
    public static final String FOOTBALL_ID = "T1399700447917";
    // 娱乐
    public static final String ENTERTAINMENT_ID = "T1348648517839";
    // 体育
    public static final String SPORTS_ID = "T1348649079062";
    // 财经
    public static final String FINANCE_ID = "T1348648756099";
    // 科技
    public static final String TECH_ID = "T1348649580692";
    // 电影
    public static final String MOVIE_ID = "T1348648650048";
    // 汽车
    public static final String CAR_ID = "T1348654060988";
    // 笑话
    public static final String JOKE_ID = "T1350383429665";
    // 游戏
    public static final String GAME_ID = "T1348654151579";
    // 时尚
    public static final String FASHION_ID = "T1348650593803";
    // 情感
    public static final String EMOTION_ID = "T1348650839000";
    // 精选
    public static final String CHOICE_ID = "T1370583240249";
    // 电台
    public static final String RADIO_ID = "T1379038288239";
    // nba
    public static final String NBA_ID = "T1348649145984";
    // 数码
    public static final String DIGITAL_ID = "T1348649776727";
    // 移动
    public static final String MOBILE_ID = "T1351233117091";
    // 彩票
    public static final String LOTTERY_ID = "T1356600029035";
    // 教育
    public static final String EDUCATION_ID = "T1348654225495";
    // 论坛
    public static final String FORUM_ID = "T1349837670307";
    // 旅游
    public static final String TOUR_ID = "T1348654204705";
    // 手机
    public static final String PHONE_ID = "T1348649654285";
    // 博客
    public static final String BLOG_ID = "T1349837698345";
    // 社会
    public static final String SOCIETY_ID = "T1348648037603";
    // 家居
    public static final String FURNISHING_ID = "T1348654105308";
    // 暴雪游戏
    public static final String BLIZZARD_ID = "T1397016069906";
    // 亲子
    public static final String PATERNITY_ID = "T1397116135282";
    // CBA
    public static final String CBA_ID = "T1348649475931";
    // 消息
    public static final String MSG_ID = "T1371543208049";
    // 军事
    public static final String MILITARY_ID = "T1348648141035";

    /**
     * 视频 http://c.3g.163.com/nc/video/list/V9LG4B3A0/n/10-10.html
     */
    public static final String Video = "nc/video/list/";
    public static final String VIDEO_CENTER = "/n/";
    public static final String VIDEO_END_URL = "-10.html";
    // 热点视频
    public static final String VIDEO_HOT_ID = "V9LG4B3A0";
    // 娱乐视频
    public static final String VIDEO_ENTERTAINMENT_ID = "V9LG4CHOR";
    // 搞笑视频
    public static final String VIDEO_FUN_ID = "V9LG4E6VR";
    // 精品视频
    public static final String VIDEO_CHOICE_ID = "00850FRB";







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
//    String GUOKR_ARTICLE_LINK_V1 = "http://jingxuan.guokr.com/pick/";
    @GET("article.json?retrieve_type=by_since&category=all&limit=25&ad=1")
    Observable<GuoKe> obtaiNewsGuoke();
   @GET("{id}")
   Call<String> obtaiGuokeDetail(@Path("id") String id);
    // 获取果壳精选的文章列表,通过组合相应的参数成为完整的url
    // Guokr handpick articles. make complete url by combining params
//    public static final String GUOKR_ARTICLES = "http://apis.guokr.com/handpick/article.json?retrieve_type=by_since&category=all&limit=25&ad=1";

    // 获取果壳文章的具体信息 V1
    // specific information of guokr post V1
    public static final String GUOKR_ARTICLE_LINK_V1 = "http://jingxuan.guokr.com/pick/";

    // 获取果壳文章的具体信息 V2
    // V2
    // public static final String GUOKR_ARTICLE_LINK_V2 = "http://jingxuan.guokr.com/pick/v2/";

    // 获取果壳精选的轮播文章列表
    // carousel posts
    // public static final String GUOKR_HANDPICK_CAROUSEL = "http://apis.guokr.com/flowingboard/item/handpick_carousel.json";


    ////////////////////////////////知乎////////////////////////////////////////
    String NEWS_ZHIHU_ROOT_API = "http://news.at.zhihu.com/api/4/news/";

    @GET("before/{date}")
    Observable<Zhihu> obtainZhiHuBefore(@Path("date") String date);
    @GET("latest")
    Observable<Zhihu> obtainZhiHuLatest();
    @GET("{id}")
    Observable<ZhiHuDetail> obtainZhiHuDetail(@Path("id") String newsID);


}
