package com.news.mvp.zhihu.biz;

import android.content.Context;

import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.DateFormatter;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.news.api.NewsApi;
import com.news.config.ConfigNews;
import com.news.mvp.zhihu.modle.ZhihuStorie;
import com.news.mvp.zhihu.modle.ZhihuTopStorie;
import com.news.mvp.zhihu.modle.Zhihu;
import com.news.mvp.zhihu.view.ZhiHuView;
import com.news.utils.DateUtil;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZhiHuBizImpl
        implements ZhiHuBiz {
    private final NewsApi mNewsApi;
    private Context context;
    private ZhiHuView baseView;

    public ZhiHuBizImpl(Context paramContext, ZhiHuView paramZhiHuView) {
        this.context = paramContext;
        this.baseView = paramZhiHuView;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ConfigValues.VALUE_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ConfigValues.VALUE_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ConfigValues.VALUE_WRITE_TIMEOUT, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsApi.NEWS_ZHIHU_ROOT_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        mNewsApi = retrofit.create(NewsApi.class);
    }


    public void DataFromNetWork(long date, final boolean isLoadMore) {
        if (NetworkState.networkConnected(this.context)) {
            if (isLoadMore) {
                LoadMoreData(date,false);
            } else {
                LoadLatest();
            }
        } else {
            //从缓存中取数据

            if (isLoadMore) {
                //加载更多
                Zhihu zhiHuData = DataSupport.where(new String[]{"date=?", DateFormatter.DateFormat(date)}).findFirst(Zhihu.class);
                if (zhiHuData== null) {
                    baseView.error(ConfigStateCode.STATE_LOAD_MORE_FAILURES, ConfigStateCode.STATE_LOAD_MORE_FAILURES_VALUE);
                } else {
                    List<ZhihuStorie> stories = DataSupport.where(new String[]{"zhihu_id=?",zhiHuData.getId()+""}).find(ZhihuStorie.class);
                    baseView.showContent(stories, null, true);
                }

            } else {
                //加载第一页数据
                Zhihu zhiHu = DataSupport.findFirst(Zhihu.class);
                if (zhiHu == null ) {
                    baseView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
                } else {
                    Zhihu zhiHuData = DataSupport.where(new String[]{"date=?", DateFormatter.DateFormat(date)}).findFirst(Zhihu.class);

                    if (zhiHuData==null) {
                        baseView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                    } else {
                        List<ZhihuStorie> stories = DataSupport.where(new String[]{"zhihu_id=?",zhiHuData.getId()+""}).find(ZhihuStorie.class);
                        List<ZhihuTopStorie> topStories = DataSupport.where(new String[]{"zhihu_id=?", zhiHuData.getId()+""}).find(ZhihuTopStorie.class);
                        baseView.showContent(stories, topStories, false);
                    }
                }
            }

        }

    }

    private void LoadLatest() {
        mNewsApi.obtainZhiHuLatest()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        baseView.loading();
                    }
                })
                .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Zhihu>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Zhihu zhiHu) {

                        if (zhiHu.getStorieList().size() <= 0 && zhiHu.getTop_storieList().size() <= 0) {
                            baseView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                        } else {
                            List<ZhihuStorie> storiess = zhiHu.getStorieList();
                            List<ZhihuTopStorie> top_storiess = zhiHu.getTop_storieList();
                            zhiHu.setCacheTime(DateUtil.getCurrentTime() + ConfigNews.NEWS_SAVE_TIME);
                            if (DataSupport.where(new String[]{"id=?", zhiHu.getId() + ""}).findFirst(Zhihu.class) == null) {
                                zhiHu.saveThrows();

                                DataSupport.saveAll(storiess);
                                DataSupport.saveAll(top_storiess);
                            }
                            baseView.showContent(storiess, top_storiess, false);
                        }


                    }

                    @Override
                    public void onError(Throwable throwable) {
                        baseView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                        ConfigStateCodeUtil.error(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void LoadMoreData(long date,final boolean isRefresh) {
        mNewsApi.obtainZhiHuBefore(DateFormatter.ZhihuDailyDateFormat(date))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (isRefresh)
                        {
                            baseView.loading();
                        }
                    }
                })
                .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Zhihu>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Zhihu zhiHu) {

                        //新闻条目
                        if (zhiHu.getStorieList().size() <= 0) {
                            baseView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                        } else {
                            List<ZhihuStorie> storiess = zhiHu.getStorieList();
                            zhiHu.setCacheTime(DateUtil.getCurrentTime() + ConfigNews.NEWS_SAVE_TIME);
                            if (DataSupport.where(new String[]{"id=?", zhiHu.getId() + ""}).findFirst(Zhihu.class) == null) {
                                zhiHu.saveThrows();
                                DataSupport.saveAll(storiess);
                            }
                            if (isRefresh) {
                                baseView.success(storiess);
                            }else
                            {
                                baseView.showContent(storiess, null, true);
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        baseView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                        ConfigStateCodeUtil.error(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void loadMore(long dateTime) {
        DataFromNetWork(dateTime, true);
    }

    @Override
    public void loadingData(long dateTime) {
        DataFromNetWork(dateTime, false);
    }

    @Override
    public void selectreTimefreshData(long dateTime) {
        String time = DateFormatter.DateFormat(dateTime);
        String currenttTime = DateFormatter.DateFormat(Calendar.getInstance().getTimeInMillis());
        if (NetworkState.networkConnected(UIUtils.getContext())) {

            if (!time.equals(currenttTime)) {
                LoadMoreData(dateTime, true);
            }else
            {
                LoadLatest();
            }
        }else {
            if (!time.equals(currenttTime)) {
                Zhihu zhiHu = DataSupport.findFirst(Zhihu.class);
                //开始判读有没有数据
                if (zhiHu==null) {
                    //没有数据
                    baseView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
                } else {
                    //有数据
                    Zhihu zhiHuData = DataSupport.where(new String[]{"date=?", DateFormatter.DateFormat(dateTime)}).findFirst(Zhihu.class);
                    if (zhiHuData==null) {
                        baseView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                    } else {
                        List<ZhihuStorie> stories = DataSupport.where(new String[]{"zhihu_id=?",zhiHuData.getId()+""}).find(ZhihuStorie.class);
                        List<ZhihuTopStorie> topStories = DataSupport.where(new String[]{"zhihu_id=?", zhiHuData.getId()+""}).find(ZhihuTopStorie.class);
                        baseView.showContent(stories, topStories, false);
                    }
                }
            }else
            {

                //开始判读有没有数据
                Zhihu zhiHu = DataSupport.findFirst(Zhihu.class);
                if (zhiHu==null) {
                    //没有数据
                    baseView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
                } else {
                    //加载第一页数据
                    Zhihu zhiHuData = DataSupport.where(new String[]{"date=?", DateFormatter.DateFormat(dateTime)}).findFirst(Zhihu.class);

                    if (zhiHuData==null) {
                        baseView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                    } else {
                        List<ZhihuStorie> stories = DataSupport.where(new String[]{"zhihu_id=?",zhiHuData.getId()+""}).find(ZhihuStorie.class);
                        List<ZhihuTopStorie> topStories = DataSupport.where(new String[]{"zhihu_id=?", zhiHuData.getId()+""}).find(ZhihuTopStorie.class);
                        baseView.showContent(stories, topStories, false);
                    }
                }
            }
        }
    }

}
