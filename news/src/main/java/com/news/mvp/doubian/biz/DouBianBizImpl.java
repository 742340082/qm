package com.news.mvp.doubian.biz;

import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.DateFormatter;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.news.api.NewsApi;
import com.news.config.ConfigNews;
import com.news.mvp.doubian.model.DouBian;
import com.news.mvp.doubian.model.DouBianPosts;
import com.news.mvp.doubian.view.DouBianView;
import com.news.utils.DateUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DouBianBizImpl
        implements DouBianBiz {
    private DouBianView douBianView;
    private final NewsApi mMcbNewsApi;

    public DouBianBizImpl(DouBianView douBianView) {
        this.douBianView = douBianView;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ConfigValues.VALUE_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ConfigValues.VALUE_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ConfigValues.VALUE_WRITE_TIMEOUT, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsApi.NEWS_DOUBIAN_ROOT_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        mMcbNewsApi = retrofit.create(NewsApi.class);
    }

    public void DataFromNetWork(long date, final boolean isLoadMore) {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            mMcbNewsApi.obtainNewsDouBian(DateFormatter.DoubanDateFormat(date)).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Observer<DouBian>() {
                @Override
                public void onSubscribe(Disposable d) {
                    if (!isLoadMore) {
                        douBianView.loading();
                    }
                }

                @Override
                public void onNext(DouBian douBian) {
                    if (douBian.getTotal() == 0) {
                        douBianView.error(ConfigStateCode.STATE_DATA_EMPTY,ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                    } else {
                        ArrayList<DouBianPosts> posts = douBian.getPosts();
                        for(int i=0;i<posts.size();i++)
                        {
                            DouBianPosts posts1 = posts.get(i);
                            if (posts1!=null) {
                                posts1.setCacheDate(DateUtil.getCurrentTime()+ ConfigNews.NEWS_SAVE_TIME);
                                posts1.setCount(posts1.getThumbs().size());
                                if (posts1.getThumbs().size()>0) {
                                    posts1.setDescription(posts1.getThumbs().get(0).getDescription());
                                    DouBianPosts.large large = posts1.getThumbs().get(0).getLarge();
                                    DouBianPosts.medium medium = posts1.getThumbs().get(0).getMedium();
                                    DouBianPosts.small small = posts1.getThumbs().get(0).getSmall();
                                    if (large!=null)
                                    {
                                        posts1.setLarge_url(large.getUrl());
                                    }

                                    if (medium!=null)
                                    {
                                        posts1.setMedium_url(medium.getUrl());
                                    }
                                    if (small!=null)
                                    {
                                        posts1.setSmall_url(small.getUrl());
                                    }
                                }

                                if (DataSupport.where(new String[]{"posts_id=?", posts1.getPosts_id() + ""}).findFirst(DouBianPosts.class) == null) {
                                    posts1.save();
                                }
                            }
                        }
                        douBianView.showContent(posts,isLoadMore);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    douBianView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                    ConfigStateCodeUtil.error(throwable);
                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            Logger.i(this, "从缓存加载数据");
            //没有网络的时候
            if (isLoadMore) {
                //加载更多
                List<DouBianPosts> postses = DataSupport.where(new String[]{"date=?", DateFormatter.DoubanDateFormat(date)}).find(DouBianPosts.class);

                if (postses.size() == 0) {
                    //加载更多的数据为0条的时候,加载失败
                    douBianView.error(ConfigStateCode.STATE_LOAD_MORE_FAILURES,ConfigStateCode.STATE_LOAD_MORE_FAILURES_VALUE);
                } else {
                    //加载更多的数据为0条的时候,加载成功
                    douBianView.showContent(postses, true);
                }
            } else {
                //第一次充缓存取数据
                DouBianPosts douBianBean = DataSupport.findFirst(DouBianPosts.class);
                if (douBianBean != null) {
                    List<DouBianPosts> postses = DataSupport.where(new String[]{"date=?", DateFormatter.DoubanDateFormat(date)}).find(DouBianPosts.class);
                    if (postses.size()==0)
                    {
                        douBianView.error(ConfigStateCode.STATE_DATA_EMPTY,ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                    }else {
                        douBianView.showContent(postses, false);
                    }
                } else {
                    //数据为空
                    douBianView.error(ConfigStateCode.STATE_NO_NETWORK,ConfigStateCode.STATE_NO_NETWORK_VALUE);
                }

            }
        }
    }


    @Override
    public void loadMore(long dateTime) {
        DataFromNetWork(dateTime,true);
    }

    @Override
    public void loadingData(long dateTime) {
        DataFromNetWork(dateTime,false);
    }


}
