package com.game.mvp.category.detail.top.biz;


import com.baselibrary.api.QMApi;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.UIUtils;
import com.game.api.GameApi;
import com.game.config.ConfigGame;
import com.game.mvp.category.detail.modle.CategoryDetail;
import com.game.mvp.category.detail.modle.CategoryDetailResult;
import com.game.mvp.category.detail.modle.CategoryDetailSizeOption;
import com.game.mvp.category.detail.modle.CategoryDetailSubType;
import com.game.mvp.category.detail.modle.CategoryDetailTag;
import com.game.mvp.category.detail.top.view.CategoryDetailTopView;
import com.game.mvp.model.Game;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static org.litepal.crud.DataSupport.findFirst;

/**
 * Created by 74234 on 2017/6/15.
 */

public class CategoryDetailTopBizImpl implements CategoryDetailTopBiz {
    private CategoryDetailTopView categoryDetailView;
    private final GameApi gameApi;

    public CategoryDetailTopBizImpl(CategoryDetailTopView categoryDetailView)
    {
        this.categoryDetailView=categoryDetailView;
        gameApi = QMApi.getInstance(GameApi.GAME_ROOT_PATH).create(GameApi.class);
    }
    @Override
    public void initCategoryDetail(final int kid, final int startSize, final int endSize, int page, final String subtype, final int tagId) {
        if (NetworkState.networkConnected(UIUtils.getContext())) {
            gameApi.gameCategoryDetail(kid,
                    startSize,
                    endSize,
                    page,
                    subtype,
                    tagId)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            categoryDetailView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CategoryDetail>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(CategoryDetail value) {
                            CategoryDetailResult result = value.getResult();
                            result.setkId(kid);
                            result.setStartSize(startSize);
                            result.setEndSize(endSize);
                            result.setSubtype(subtype);
                            result.setTagId(tagId);
                            if (result.getPage()==1&&result.getData().size()==0)
                            {
                                categoryDetailView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                            }else
                            {
                                if ( DataSupport.where("kid=? and startsize=? and endsize=? and page=? and subtype=? and tagid=?"
                                        ,result.getkId()+"",result.getStartSize()+"",result.getEndSize()+"",
                                        result.getPage()+"",result.getSubtype(),result.getTagId()+"").findFirst(CategoryDetailResult.class)==null)
                                {
                                    List<Game> data = result.getData();
                                    List<CategoryDetailSizeOption> sizeOptions = result.getSizeOptions();
                                    List<CategoryDetailTag> tags = result.getTags();
                                    List<CategoryDetailSubType> subtypes = result.getSubtypes();
                                    DataSupport.saveAll(subtypes);
                                    DataSupport.saveAll(data);
                                    DataSupport.saveAll(sizeOptions);
                                    DataSupport.saveAll(tags);
                                    result.saveThrows();
                                }

                                categoryDetailView.success(result);
                                RxBus.getDefault().post(ConfigGame.GAME_SEND_RX_CATEGORY,result);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            categoryDetailView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else
        {
            CategoryDetailResult first = findFirst(CategoryDetailResult.class);
            if (first==null)
            {
                categoryDetailView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            }else
            {
                CategoryDetailResult categoryDetailResult = DataSupport.where("kid=? and startsize=? and endsize=? and page=? and subtype=? and tagid=?"
                        , kid + "", startSize + "", endSize + "",
                        page + "", subtype, tagId + "").findFirst(CategoryDetailResult.class);
                if (page>1) {
                    if (categoryDetailResult == null) {
                        categoryDetailView.error(ConfigStateCode.STATE_LOAD_MORE_FAILURES, ConfigStateCode.STATE_LOAD_MORE_FAILURES_VALUE);
                    } else
                    {
                        List<Game> games = DataSupport.where("categorydetailresult_id=?", categoryDetailResult.getId() + "").find(Game.class);
                        List<CategoryDetailTag> categoryDetailTags = DataSupport.where("categorydetailresult_id=?", categoryDetailResult.getId() + "").find(CategoryDetailTag.class);
                        List<CategoryDetailSizeOption> categoryDetailSizeOptions = DataSupport.where("categorydetailresult_id=?", categoryDetailResult.getId() + "").find(CategoryDetailSizeOption.class);
                        List<CategoryDetailSubType> categoryDetailSubTypes = DataSupport.where("categorydetailresult_id=?", categoryDetailResult.getId() + "").find(CategoryDetailSubType.class);
                        categoryDetailResult.setData(games);
                        categoryDetailResult.setTags(categoryDetailTags);
                        categoryDetailResult.setSubtypes(categoryDetailSubTypes);
                        categoryDetailResult.setSizeOptions(categoryDetailSizeOptions);

                        categoryDetailView.success(categoryDetailResult);
                        RxBus.getDefault().post(ConfigGame.GAME_SEND_RX_CATEGORY, categoryDetailResult);
                    }

                }else {
                    if (categoryDetailResult == null) {
                        categoryDetailView.error(ConfigStateCode.STATE_DATA_EMPTY, ConfigStateCode.STATE_DATA_EMPTY_VALUE);
                    } else {
                        List<Game> games = DataSupport.where("categorydetailresult_id=?", categoryDetailResult.getId() + "").find(Game.class);
                        List<CategoryDetailTag> categoryDetailTags = DataSupport.where("categorydetailresult_id=?", categoryDetailResult.getId() + "").find(CategoryDetailTag.class);
                        List<CategoryDetailSizeOption> categoryDetailSizeOptions = DataSupport.where("categorydetailresult_id=?", categoryDetailResult.getId() + "").find(CategoryDetailSizeOption.class);
                        List<CategoryDetailSubType> categoryDetailSubTypes = DataSupport.where("categorydetailresult_id=?", categoryDetailResult.getId() + "").find(CategoryDetailSubType.class);
                        categoryDetailResult.setData(games);
                        categoryDetailResult.setTags(categoryDetailTags);
                        categoryDetailResult.setSubtypes(categoryDetailSubTypes);
                        categoryDetailResult.setSizeOptions(categoryDetailSizeOptions);

                        categoryDetailView.success(categoryDetailResult);
                        RxBus.getDefault().post(ConfigGame.GAME_SEND_RX_CATEGORY, categoryDetailResult);
                    }
                }
            }
        }
    }
}
