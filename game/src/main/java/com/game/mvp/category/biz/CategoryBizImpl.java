package com.game.mvp.category.biz;

import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.model.game.category.Category;
import com.baselibrary.model.game.category.CategoryData;
import com.baselibrary.model.game.category.CategoryLink;
import com.baselibrary.model.game.category.CategoryResult;
import com.baselibrary.model.game.category.CategoryTag;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
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


                            List<CategoryLink> links = result.getLinks();
                            List<CategoryData> datas = result.getData();
                            List<CategoryResult> categoryResults = DataSupport.where("1=1").find(CategoryResult.class);
                            if (categoryResults.size() !=1) {
                                for (CategoryData data : datas) {
                                    List<CategoryTag> tags = data.getTags();
                                    DataSupport.saveAll(tags);

                                }
                                DataSupport.saveAll(datas);
                                DataSupport.saveAll(links);
                                result.saveThrows();
                            }
                            categoryView.initHeader(links);
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
            CategoryResult categoryResult = DataSupport.findFirst(CategoryResult.class);
            if (categoryResult == null) {
                categoryView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            } else {
                    List<CategoryData> datas = DataSupport.where("categoryresult_id=?", categoryResult.getId() + "").find(CategoryData.class);
                    for (int i = 0; i < datas.size(); i++) {
                        CategoryData data = datas.get(i);
                        List<CategoryTag> tags = DataSupport.where("categorydata_id=?", data.getId() + "").find(CategoryTag.class);
                        data.setTags(tags);
                    }
                    List<CategoryLink> links = DataSupport.where("categoryresult_id=?", categoryResult.getId() + "").find(CategoryLink.class);
                    categoryResult.setData(datas);
                    categoryResult.setLinks(links);

                    categoryView.initHeader(links);
                    categoryView.success(categoryResult);
                }
            }

    }
}
