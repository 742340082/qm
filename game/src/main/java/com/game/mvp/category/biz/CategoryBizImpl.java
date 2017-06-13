package com.game.mvp.category.biz;

import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
import com.game.mvp.category.modle.Category;
import com.game.mvp.category.modle.CategoryResult;
import com.game.mvp.category.modle.Data;
import com.game.mvp.category.modle.Link;
import com.game.mvp.category.modle.Tag;
import com.game.mvp.category.view.CategoryView;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by 74234 on 2017/6/11.
 */

public class CategoryBizImpl implements CategoryBiz {
    private CategoryView categoryView;
    private GameApi gameApi;

    public CategoryBizImpl(CategoryView categoryView) {
        this.categoryView = categoryView;
        Retrofit instance = QMApi.getInstance(GameApi.GAME_ROOT_PATH);
        gameApi = instance.create(GameApi.class);
    }

    @Override
    public void initGameCategory() {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            gameApi.gameCategory()
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            categoryView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Category>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Category value) {
                            Logger.e("TAG", value.toString());
                            CategoryResult result = value.getResult();

                            List<Data> datas = result.getData();
                            List<Link> links = result.getLinks();
                            List<CategoryResult> categoryResults = DataSupport.where("1=1").find(CategoryResult.class);
                            if (categoryResults.size() !=1) {
                                for (Data data : datas) {

                                    List<Tag> tags = data.getTags();
                                    DataSupport.saveAll(tags);

                                }
                                DataSupport.saveAll(datas);
                                DataSupport.saveAll(links);
                                result.saveThrows();
                            }
                            categoryView.success(result);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            categoryView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else

        {
            CategoryResult first = DataSupport.findFirst(CategoryResult.class);
            if (first == null) {
                categoryView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            } else {
                CategoryResult categoryResult = DataSupport.where("1=1").findFirst(CategoryResult.class);
                if (categoryResult == null) {
                    categoryView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                } else {
                    List<Data> datas = DataSupport.where("categoryresult_id=?", categoryResult.getId() + "").find(Data.class);
                    for (int i = 0; i < datas.size(); i++) {
                        Data data = datas.get(i);
                        List<Tag> tags = DataSupport.where("data_id=?", data.getId() + "").find(Tag.class);
                        data.setTags(tags);
                    }
                    List<Link> links = DataSupport.where("categoryresult_id=?", categoryResult.getId() + "").find(Link.class);
                    categoryResult.setData(datas);
                    categoryResult.setLinks(links);
                    categoryView.success(categoryResult);
                }
            }
        }
    }
}
