package com.news.config;

/**
 * Created by yangyupan on 2017/4/5.
 */

public interface ConfigNews {
    //新闻缓存的时间
     int NEWS_SAVE_TIME = 30*60*60*1000;
    //传递新闻ID
    String NEWS_SEND_ID="NEWS_SEND_ZHIHU_ID";
    //传递新闻详情页内容类型
    String NEWS_SEND_NEWS_TYPE="NEWS_SEND_NEWS_TYPE";
    //传递新闻URl
    String NEWS_SEND_NEWS_URL="NEWS_SEND_NEWS_URL";

    int NEWS_GUOKE_TYPE=70001;
    int NEWS_DOUBIAN_TYPE=70002;
    int NEWS_ZHIHU_TYPE=70003;
}
