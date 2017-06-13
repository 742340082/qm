package com.news.mvp.guoke.biz;

import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.news.api.NewsApi;
import com.news.mvp.guoke.bean.GuoKe;
import com.news.mvp.guoke.bean.GuoKeResult;
import com.news.mvp.guoke.view.GuokeView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
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

public class GuoKeDailyBizImpl
        implements GuoKeDailyBiz {


    private final NewsApi mMcbNewsApi;
    private final GuokeView guokeView;

    public GuoKeDailyBizImpl(GuokeView guokeView) {
        this.guokeView = guokeView;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ConfigValues.VALUE_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ConfigValues.VALUE_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ConfigValues.VALUE_WRITE_TIMEOUT, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewsApi.NEWS_GUOKE_ROOT_API)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        mMcbNewsApi = retrofit.create(NewsApi.class);
    }

    public void DataFromNetWork() {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            mMcbNewsApi.obtaiNewsGuoke()
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            guokeView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GuoKe>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(GuoKe guoKe) {
                            if (guoKe.getResult().size() <= 0) {
                                guokeView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                            } else {
                                ArrayList<GuoKeResult> result = guoKe.getResult();
                                if (DataSupport.where(new String[]{"id=?", guoKe.getId() + ""}).findFirst(GuoKeResult.class) == null) {
                                    guoKe.saveThrows();
                                    DataSupport.saveAll(result);
                                }
                                guokeView.success(result);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            guokeView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Logger.i(this, "从缓存加载数据");
            GuoKe guode = DataSupport.findFirst(GuoKe.class);
            if (guode==null)
            {
                guokeView.error(ConfigStateCode.STATE_NO_NETWORK,ConfigStateCode.STATE_NO_NETWORK_VALUE);
            }else
            {
                List<GuoKeResult> guoKeResults = DataSupport.where(new String[]{"guoke_id=?",guode.getId()+""}).find(GuoKeResult.class);
                guokeView.success(guoKeResults);
            }

        }
    }


    public void loadingData() {
        DataFromNetWork();
    }
}
