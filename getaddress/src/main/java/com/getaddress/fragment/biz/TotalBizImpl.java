package com.getaddress.fragment.biz;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.getaddress.fragment.view.TotalView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by 74234 on 2017/4/27.
 */

public class TotalBizImpl implements TotalBiz {

    private TotalView totalView;
    private String queryCotent;
    private String city;
    private double longitude;
    private double latitude;

    public TotalBizImpl(TotalView totalView) {
        this.totalView = totalView;
    }

    @Override
    public void initdata(String queryCotent, String city, double longitude, double latitude, int pageNum) {
        if (!NetworkState.networkConnected(UIUtils.getContext())) {
            totalView.error(ConfigStateCode.STATE_NO_NETWORK);
            return;
        }
        this.queryCotent = queryCotent;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        startLoadData(pageNum, false);
    }

    private void startLoadData(int pageNum, final boolean isloadmore) {
        PoiSearch.Query query = new PoiSearch.Query(queryCotent,
                "汽车服务|汽车销售|\n" +
                        "汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|\n" +
                        "住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|\n" +
                        "金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施", city);
        if (queryCotent.equals("全部")) {
            query = new PoiSearch.Query("学校|住宅|写字楼",
                    "汽车服务|汽车销售|\n" +
                            "汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|\n" +
                            "住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|\n" +
                            "金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施", city);
        }
        if (queryCotent.equals("小区")) {
            query = new PoiSearch.Query("住宅",
                    "住宿服务|商务住宅", city);
        }
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(pageNum);//设置查询页码
        query.setCityLimit(true);//限制在当前城市
        if (!isloadmore) {
            totalView.startLoading();
        }
        PoiSearch poiSearch = new PoiSearch(UIUtils.getContext(), query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude,
                longitude), 5000));//设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult,final int i) {
                Observable.just(poiResult)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (!isloadmore) {
                                    totalView.startLoading();
                                }
                            }
                        })
                        .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<PoiResult>() {
                            @Override
                            public void accept(PoiResult poiResult) throws Exception {
                                if (!isloadmore) {
                                    if (i == 1000) {
                                        if (poiResult.getPois().size() != 0) {
                                            totalView.success(poiResult, isloadmore);
                                        } else {
                                            totalView.error(ConfigStateCode.STATE_DATA_EMPTY);
                                        }
                                    } else {
                                        totalView.error(ConfigStateCode.STATE_ERROE);
                                    }
                                } else {
                                    if (i == 1000) {
                                        if (poiResult.getPois().size() != 0) {
                                            totalView.success(poiResult, isloadmore);
                                        } else {
                                            totalView.error(ConfigStateCode.STATE_LOAD_MORE_FAILURES);
                                        }
                                    } else {
                                        totalView.error(ConfigStateCode.STATE_LOAD_MORE_FAILURES);
                                    }
                                }
                            }
                        });

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void loadMore(int pageNum) {
        startLoadData(pageNum, true);
    }
}
