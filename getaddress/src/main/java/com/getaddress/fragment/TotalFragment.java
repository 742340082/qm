package com.getaddress.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.getaddress.R;
import com.getaddress.R2;
import com.getaddress.adapter.GetAddressAdapter;
import com.getaddress.fragment.presenter.TotalPresenter;
import com.getaddress.fragment.view.TotalView;
import com.getaddress.modle.PositionAddress;
import com.getaddress.modle.ReturnAddress;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by 74234 on 2017/4/26.
 */

public class TotalFragment extends BaseFragmnet implements TotalView, OnRetryListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R2.id.rv_getaddress_tab)
    RecyclerView rv_getaddress_tab;
    @BindView(R2.id.ll_getaddress_tab)
    LinearLayout ll_getaddress_tab;
    private StatusLayoutManager mStatusLayoutManager;
    private TotalPresenter presenter;
    private GetAddressAdapter getAddressAdapter;
    private PoiResult poiResult;
    private int pageNum;

    private LatLng latLngIndex=new LatLng(0,0);
    @Override
    public void initData() {
        presenter = new TotalPresenter(this);
        refreshData();
    }



    private void refreshData() {
        RxBus.getDefault().toObservable(ConfigValues.VALUE_ADDRESS_RX_SEND_LOCATION, PositionAddress.class)
                .subscribe(new Consumer<PositionAddress>() {

                    @Override
                    public void accept(PositionAddress positionAddress) throws Exception {
                        pageNum=0;
                        if (positionAddress.getLatLng().latitude!=latLngIndex.latitude
                                ||positionAddress.getLatLng().longitude!=latLngIndex.longitude) {
                            if (isVisible) {
                                Bundle bundle = TotalFragment.this.getArguments();
                                if (bundle != null) {
                                    String queryContent = bundle.getString(ConfigValues.VALUE_ADDRESS_SEND_QUERYCONTENT);
                                    presenter.initdata(queryContent,
                                            positionAddress.getCity(),
                                            positionAddress.getLatLng().longitude,
                                            positionAddress.getLatLng().latitude,
                                            pageNum);
                                }
                                latLngIndex = new LatLng(positionAddress.getLatLng().latitude, positionAddress.getLatLng().longitude, false);
                            }
                        }
                    }
                });

    }

    @Override
    public void initView() {
        mStatusLayoutManager = StatusLayoutManager.newBuilder(getContext())
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(this)
                .build();
        ll_getaddress_tab.addView(mStatusLayoutManager.getRootLayout(), ll_getaddress_tab.getChildCount() - 1);
    }

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.getaddress_tab;
    }

    @Override
    public void error(int errorCode) {
        switch (errorCode) {
            case ConfigStateCode.STATE_ERROE:
                ToastUtils.makeShowToast(UIUtils.getContext(), "错误");
                mStatusLayoutManager.showError();
                break;
            case ConfigStateCode.STATE_DATA_EMPTY:
                ToastUtils.makeShowToast(UIUtils.getContext(), "数据为空");
                mStatusLayoutManager.showEmptyData();
                break;
            case ConfigStateCode.STATE_NO_NETWORK:
                ToastUtils.makeShowToast(UIUtils.getContext(), "没有网络");
                mStatusLayoutManager.showNetWorkError();
                break;
            case ConfigStateCode.STATE_LOAD_MORE_FAILURES:
                ToastUtils.makeShowToast(UIUtils.getContext(), "加载更多失败");
                break;
        }
    }

    @Override
    public void startLoading() {
        mStatusLayoutManager.showLoading();
    }

    @Override
    public void success(PoiResult poiResult, boolean isloadmore) {
        mStatusLayoutManager.showContent();
        this.poiResult = poiResult;
        if (!isloadmore) {
            getAddressAdapter = new GetAddressAdapter(R.layout.item_address, poiResult.getPois());
            getAddressAdapter.openLoadAnimation(GetAddressAdapter.ALPHAIN);
            getAddressAdapter.setEnableLoadMore(true);
            getAddressAdapter.setOnLoadMoreListener(this, rv_getaddress_tab);
            rv_getaddress_tab.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
            rv_getaddress_tab.setAdapter(getAddressAdapter);
            getAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    PoiItem item = (PoiItem) adapter.getItem(position);
                    ReturnAddress returnAddress=new ReturnAddress();
                    returnAddress.setTitle(item.getTitle());
                    returnAddress.setLatLng(item.getLatLonPoint());
                    TextView tv_address_detail = (TextView) view.findViewById(R.id.tv_address_detail);
                    returnAddress.setAddress(tv_address_detail.getText().toString());
                    returnAddress.setCity(item.getCityName());
                    returnAddress.setProvince(item.getProvinceName());
                    RxBus.getDefault().post(ConfigValues.VALUE_ADDRESS_RX_SEND_RETURNADDRESS,returnAddress);
                    getActivity().onBackPressed();
                }
            });
        } else {
            getAddressAdapter.addData(poiResult.getPois());
            getAddressAdapter.loadMoreComplete();
        }
    }

    @Override
    public void retry() {
        pageNum=0;
        if (isVisible) {
            Bundle bundle = TotalFragment.this.getArguments();
            if (bundle != null) {
                String queryContent = bundle.getString(ConfigValues.VALUE_ADDRESS_SEND_QUERYCONTENT);
                String city = bundle.getString(ConfigValues.VALUE_ADDRESS_SEND_CITY);
                presenter.initdata(queryContent,
                        city,
                        latLngIndex.longitude,
                        latLngIndex.latitude,
                        pageNum);
            }
        }
    }

    @Override
    public void onLoadMoreRequested() {
        pageNum++;
        if (pageNum >= poiResult.getPageCount()) {
            ToastUtils.makeShowToast(UIUtils.getContext(), "没有更多搜索地址");
            getAddressAdapter.loadMoreComplete();
            getAddressAdapter.loadMoreEnd();
        } else {
            presenter.loadmore(pageNum);
        }
    }
}
