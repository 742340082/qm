package com.news.mvp.detail.biz;

import com.baselibrary.api.okhttp.ApiListenerManager;
import com.baselibrary.api.okhttp.OkHttp;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.UIUtils;
import com.news.api.NewsApi;
import com.news.config.ConfigNews;
import com.news.mvp.detail.modle.DoubanDetail;
import com.news.mvp.detail.modle.GuokeDetail;
import com.news.mvp.detail.modle.ZhiHuDetail;
import com.news.mvp.detail.view.NewsDetailView;
import com.news.mvp.doubian.model.DouBianPosts;
import com.news.mvp.doubian.model.DouBianThumbs;
import com.news.mvp.guoke.model.GuoKeResult;
import com.news.utils.DateUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 74234 on 2017/5/12.
 */

public class NewsDetailBizImpl implements NewsDetailBiz {
    private final NewsApi mNewsApi;
    private NewsDetailView NewsDetailView;

    public NewsDetailBizImpl(NewsDetailView view,int newsType) {
        this.NewsDetailView = view;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ConfigValues.VALUE_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ConfigValues.VALUE_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ConfigValues.VALUE_WRITE_TIMEOUT, TimeUnit.SECONDS).build();
        Retrofit.Builder builder=null;
        switch (newsType) {
            case ConfigNews.NEWS_DOUBIAN_TYPE:
              builder = new Retrofit.Builder()
                        .baseUrl(NewsApi.NEWS_DOUBIAN_ROOT_API);
                break;
            case ConfigNews.NEWS_GUOKE_TYPE:
                builder = new Retrofit.Builder()
                        .baseUrl(NewsApi.NEWS_GUOKE_ROOT_API_V1);
                break;
            case ConfigNews.NEWS_ZHIHU_TYPE:
                builder = new Retrofit.Builder()
                        .baseUrl(NewsApi.NEWS_ZHIHU_ROOT_API);
                break;
        }
        Retrofit retrofit = builder.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        mNewsApi = retrofit.create(NewsApi.class);
    }

    @Override
    public void initNewsInfo(String id, int newType) {
        if (!NetworkState.networkConnected(UIUtils.getContext())) {
            NewsDetailView.loading();
            switch (newType) {
                case ConfigNews.NEWS_DOUBIAN_TYPE:
                    DoubanDetail doubanDetail = DataSupport.where(new String[]{"doubiandetail_id=?", id}).findFirst(DoubanDetail.class);
                    if (doubanDetail != null) {
                        NewsDetailView.success(doubanDetail);
                    } else {
                        NewsDetailView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
                    }
                    break;
                case ConfigNews.NEWS_GUOKE_TYPE:
                    GuokeDetail guokeDetail = DataSupport.where(new String[]{"guokedetail_id=?", id}).findFirst(GuokeDetail.class);
                    if (guokeDetail != null) {
                        NewsDetailView.success(guokeDetail);
                    } else {
                        NewsDetailView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
                    }
                    break;
                case ConfigNews.NEWS_ZHIHU_TYPE:
                    ZhiHuDetail zhiHuDetail = DataSupport.where(new String[]{"zhihudetail_id=?", id}).findFirst(ZhiHuDetail.class);
                    if (zhiHuDetail != null) {
                        NewsDetailView.success(zhiHuDetail);
                    } else {
                        NewsDetailView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
                    }
                    break;
            }
        } else {
            switch (newType) {
                case ConfigNews.NEWS_DOUBIAN_TYPE:
                    doubianDetail(id);
                    break;
                case ConfigNews.NEWS_GUOKE_TYPE:
                    guokeDetail(id);
                    break;
                case ConfigNews.NEWS_ZHIHU_TYPE:
                    zhiHuDetail(id);
                    break;
            }
        }

    }

    private void guokeDetail(final String id) {
        OkHttp.getInstance().asyncStringByUrl(NewsApi.NEWS_GUOKE_ROOT_API_V1 + id, new ApiListenerManager.obtainStringListener() {
            @Override
            public void onError(Exception throwable) {
                NewsDetailView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                ConfigStateCodeUtil.error(throwable);
            }

            @Override
            public void onResponst(String content) {
                Observable.just(content)
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                NewsDetailView.loading();
                            }
                        })
                        .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String value) throws Exception {
                                if(StringUtil.isEmpty(value))
                                {
                                    NewsDetailView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                                }else
                                {
                                    GuoKeResult guoKeResult = DataSupport.where(new String[]{"guokeresult_id=?", id}).findFirst(GuoKeResult.class);
                                    GuokeDetail guokeDetail = new GuokeDetail();
                                    guokeDetail.setGuokedetail_id(Long.parseLong(id));
                                    guokeDetail.setBody(value);
                                    guokeDetail.setImage(guoKeResult.getHeadline_img());
                                    guokeDetail.setTitle(guoKeResult.getTitle());
                                    guokeDetail.saveThrows();
                                    NewsDetailView.success(guokeDetail);
                                }
                            }
                        });
            }
        });
    }

    private void zhiHuDetail(String id) {
        mNewsApi.obtainZhiHuDetail(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        NewsDetailView.loading();
                    }
                })
                .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhiHuDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ZhiHuDetail zhiHuDetail) {
                        if (zhiHuDetail == null) {
                            NewsDetailView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                        } else {
                            zhiHuDetail.setZhihudetail_id(zhiHuDetail.getId());
                            zhiHuDetail.setCacheTime(DateUtil.getCurrentTime() + ConfigNews.NEWS_SAVE_TIME);
                            if (DataSupport.where(new String[]{"zhihudetail_id=?", zhiHuDetail.getZhihudetail_id() + ""}).findFirst(ZhiHuDetail.class) == null) {
                                zhiHuDetail.saveThrows();
                            }
                            NewsDetailView.success(zhiHuDetail);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        NewsDetailView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                        ConfigStateCodeUtil.error(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void doubianDetail(String id) {
        mNewsApi.obtainDouBianDetail(id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        NewsDetailView.loading();
                    }
                })
                .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DoubanDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DoubanDetail doubanDetail) {
                        if (doubanDetail == null) {
                            NewsDetailView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                        } else {
                            doubanDetail.setDoubiandetail_id(doubanDetail.getId());
                            doubanDetail.setCacheTime(DateUtil.getCurrentTime() + ConfigNews.NEWS_SAVE_TIME);
                            DouBianPosts posts = DataSupport.where(new String[]{"posts_id=?", doubanDetail.getId() + ""}).findFirst(DouBianPosts.class);
                            if (posts!=null) {
                                doubanDetail.setLarge_url(posts.getLarge_url());
                                doubanDetail.setMedium_url(posts.getMedium_url());
                                doubanDetail.setSmall_url(posts.getSmall_url());
                            }
                            if (DataSupport.where(new String[]{"doubiandetail_id=?", doubanDetail.getDoubiandetail_id() + ""}).findFirst(DoubanDetail.class) == null) {
                                doubanDetail.saveThrows();
                                ArrayList<DouBianThumbs> thumbs = doubanDetail.getPhotos();
                                for (DouBianThumbs item
                                        :
                                        thumbs) {
                                    DouBianPosts.large large = item.getLarge();
                                    DouBianPosts.small small = item.getSmall();
                                    DouBianPosts.medium medium = item.getMedium();
                                    if (large!=null)
                                    {
                                        item.setLarge_url(large.getUrl());
                                    }

                                    if (medium!=null)
                                    {
                                        item.setMedium_url(medium.getUrl());
                                    }
                                    if (small!=null)
                                    {
                                        item.setSmall_url(small.getUrl());
                                    }
                                    item.setThumbs_id(doubanDetail.getDoubiandetail_id());
                                    item.setCacheDate(DateUtil.getCurrentTime() + ConfigNews.NEWS_SAVE_TIME);
                                    item.saveThrows();

                                }
                            }
                            NewsDetailView.success(doubanDetail);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        NewsDetailView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                        ConfigStateCodeUtil.error(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
