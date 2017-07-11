package com.getaddress;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.baselibrary.base.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.baselibrary.base.adapter.ViewPagerFragmentAdapter;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.QuickIndexBar;
import com.baselibrary.view.SuspensionDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.getaddress.adapter.AddressSearchAdapter;
import com.getaddress.adapter.CityAdapter;
import com.getaddress.modle.AddressCity;
import com.getaddress.modle.AddressPositionAddress;
import com.getaddress.modle.AddressReturnAddress;
import com.getaddress.modle.AddressTipMapLocation;
import com.getaddress.presenter.AddressPresenter;
import com.getaddress.view.GetAddressView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by 74234 on 2017/4/24.
 */

public class AddressActivity extends AppCompatActivity implements GetAddressView {
    @BindView(R2.id.mv_address)
    MapView mv_address;
    @BindView(R2.id.tl_address)
    TabLayout tl_address;
    @BindView(R2.id.vp_address)
    ViewPager vp_address;
    @BindView(R2.id.tv_address_city)
    TextView tv_address_city;
    @BindView(R2.id.iv_address_city)
    ImageView iv_address_city;
    @BindView(R2.id.et_address_search)
    EditText et_address_search;
    @BindView(R2.id.tv_address_cancle_search)
    TextView tv_address_cancle_search;
    @BindView(R2.id.qi_address)
    QuickIndexBar qi_address;
    @BindView(R2.id.tv_address_quickindex_title)
    TextView tv_address_quickindex_title;

    @BindView(R2.id.rv_address_search_list)
    RecyclerView rv_address_search_list;
    @BindView(R2.id.rv_address_city_list)
    RecyclerView rv_address_city_list;

    @BindView(R2.id.ll_address_city_list)
    LinearLayout ll_address_city_list;
    @BindView(R2.id.rl_address_city_list)
    RelativeLayout rl_address_city_list;
    @BindView(R2.id.ll_address_search_list)
    LinearLayout ll_address_search_list;

    @BindView(R2.id.rl_address)
    RelativeLayout rl_address;


    private AMap aMap;
    private AddressPresenter presenter;
    private StatusLayoutManager mLocationLayoutManager;
    private StatusLayoutManager mSearchLayoutManager;
    private StatusLayoutManager mCityLayoutManager;
    private AMapLocation mapLocation;
    private boolean isLoadCity;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private CityAdapter cityAdapter;
    private List<AddressCity> mBodyDatas;
    private SuspensionDecoration mDecoration;
    private List<AddressCity> mSourceDatas;

    @OnClick(R2.id.iv_back)
    void back() {
        onBackPressed();
    }

    @OnClick(R2.id.ll_address_city)
    void chooseCity() {
        if (ll_address_city_list.getVisibility() == View.VISIBLE) {

            iv_address_city.setImageResource(R.drawable.arrow_up);
            ll_address_city_list.setVisibility(View.GONE);
        } else {
            iv_address_city.setImageResource(R.drawable.arrow_down);
            ll_address_city_list.setVisibility(View.VISIBLE);
            if (!isLoadCity && mapLocation != null) {
                presenter.initCityList();
                isLoadCity = true;
            }
        }
    }

    @OnClick(R2.id.ib_address_location)
    void resetLocation() {
        presenter.resetLocation();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        mv_address.onCreate(bundle);
        initView();
        initData();
        initlistener();
    }

    public void initData() {

        RxBus.getDefault().toObservable(ConfigValues.VALUE_ADDRESS_RX_SEND_LOCATION, AddressPositionAddress.class)
                .subscribe(new Consumer<AddressPositionAddress>() {

                    @Override
                    public void accept(AddressPositionAddress positionAddress) throws Exception {
                        if (mapLocation != null) {
                            mapLocation.setCity(positionAddress.getCity());
                            mapLocation.setProvince(positionAddress.getProvince());
                            mapLocation.setDistrict(positionAddress.getDistrict());
                            mapLocation.setLatitude(positionAddress.getLatLng().latitude);
                            mapLocation.setLongitude(positionAddress.getLatLng().longitude);
                            tv_address_city.setText(mapLocation.getCity());
                        }
                    }
                });

        presenter = new AddressPresenter(this);
        if (aMap == null) {
            aMap = mv_address.getMap();
        }
        presenter.initLocation(aMap);


    }

    private void initAdapter() {


        if (cityAdapter == null) {
            mBodyDatas = new ArrayList<>();
            mSourceDatas = new ArrayList<>();

            AddressCity city = new AddressCity();
            city.setFirstLetter(UIUtils.getString(R.string.location_city));
            if (mapLocation != null) {
                city.setName(mapLocation.getCity());
            } else {
                city.setName(UIUtils.getString(R.string.loading));
            }
            city.setCode("-1");
            mSourceDatas.add(0, city);
            cityAdapter = new CityAdapter(R.layout.item_city, mBodyDatas);
            mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(cityAdapter) {

                @Override
                protected void onBindHeaderHolder(BaseViewHolder holder, int headerPos, int layoutId, Object o) {
                    AddressCity city1 = (AddressCity) o;
                    holder.setText(R.id.tv_address_city, city1.getName());
                    final ImageView iv_getaddress_reset_location = holder.getView(R.id.iv_address_reset_location);
                    final ProgressBar pb_getaddress_reset_location = holder.getView(R.id.pb_address_reset_location);
                    LinearLayout ll_getaddress_reset_location = holder.getView(R.id.ll_address_reset_location);
                    ll_getaddress_reset_location.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Observable.just(v)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe(new Consumer<Disposable>() {
                                        @Override
                                        public void accept(Disposable disposable) throws Exception {
                                            iv_getaddress_reset_location.setVisibility(View.GONE);
                                            pb_getaddress_reset_location.setVisibility(View.VISIBLE);
                                        }
                                    })
                                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<View>() {

                                        @Override
                                        public void accept(View view) throws Exception {
                                            iv_getaddress_reset_location.setVisibility(View.VISIBLE);
                                            pb_getaddress_reset_location.setVisibility(View.GONE);
                                            presenter.startLocation(aMap);
                                        }
                                    });

                        }
                    });
                }
            };
            mHeaderAdapter.setHeaderView(0, R.layout.item_city_header, city);

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, 1, false);
            rv_address_city_list.setLayoutManager(linearLayoutManager);
            rv_address_city_list.setAdapter(mHeaderAdapter);
            rv_address_city_list.addItemDecoration(mDecoration = new SuspensionDecoration(this, mSourceDatas)
                    .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - 1)
                    .setColorTitleBg(UIUtils.getColor(R.color.graydeep)));
            qi_address.setLinearLayoutManager(linearLayoutManager).showTextView(tv_address_quickindex_title);
            mHeaderAdapter.notifyDataSetChanged();
        } else {
            AddressCity city = new AddressCity();
            city.setFirstLetter("当前定位的城市");
            if (mapLocation != null) {
                city.setName(mapLocation.getCity());
            } else {
                city.setName(UIUtils.getString(R.string.loading));
            }
            city.setCode("-1");
            mHeaderAdapter.notifyItemChanged(0, city);
            ll_address_city_list.setVisibility(View.GONE);
        }
    }

    private void initlistener() {
        et_address_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_address_search_list.setVisibility(View.VISIBLE);
                tv_address_cancle_search.setVisibility(View.VISIBLE);
                String inputText = et_address_search.getText().toString().trim();
                final String searchText = inputText.replace(" ", "");
                if (!StringUtil.isEmpty(searchText)) {
                    Observable.just(searchText)
                            .debounce(200L, TimeUnit.MILLISECONDS)
                            .switchMap(new Function<String, ObservableSource<String>>() {
                                @Override
                                public ObservableSource<String> apply(String str) throws Exception {
                                    return Observable.just(str);
                                }
                            }).subscribe(new Consumer<String>() {
                        public void accept(@NonNull String str) {

                            presenter.startInputSeatch(searchText, mapLocation.getCity());
                        }
                    });
                }
            }
        });
        tv_address_cancle_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_address_search_list.setVisibility(View.GONE);
                tv_address_cancle_search.setVisibility(View.GONE);
            }
        });
        et_address_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = et_address_search.getText().toString().trim();
                final String searchText = inputText.replace(" ", "");
                if (!StringUtil.isEmpty(searchText)) {
                    Observable.just(searchText)
                            .debounce(200L, TimeUnit.MILLISECONDS)
                            .switchMap(new Function<String, ObservableSource<String>>() {
                                @Override
                                public ObservableSource<String> apply(String str) throws Exception {
                                    return Observable.just(str);
                                }
                            }).subscribe(new Consumer<String>() {
                        public void accept(@NonNull String str) {
                            presenter.startInputSeatch(searchText, mapLocation.getCity());
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initView() {
        mLocationLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        presenter.initLocation(aMap);
                    }
                })
                .build();
        rl_address.addView(mLocationLayoutManager.getRootLayout(), rl_address.getChildCount() - 1);
        mSearchLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        String inputText = et_address_search.getText().toString().trim();
                        String searchText = inputText.replace(" ", "");
                        presenter.startInputSeatch(searchText, mapLocation.getCity());
                    }
                })
                .build();
        ;
        ll_address_search_list.addView(mSearchLayoutManager.getRootLayout(), ll_address_search_list.getChildCount() - 1);
        mCityLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        presenter.initCityList();
                    }
                })
                .build();
        ;
        rl_address_city_list.addView(mCityLayoutManager.getRootLayout(), rl_address_city_list.getChildCount() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mv_address.onDestroy();
        presenter.destoryLocation();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mv_address.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv_address.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv_address.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ll_address_search_list.getVisibility() == View.VISIBLE) {
                ll_address_search_list.setVisibility(View.GONE);
                tv_address_cancle_search.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public Bundle getBundle(String content, String city) {
        Bundle localBundle = new Bundle();
        localBundle.putString(ConfigValues.VALUE_ADDRESS_SEND_QUERYCONTENT, content);
        localBundle.putString(ConfigValues.VALUE_ADDRESS_SEND_CITY, city);
        return localBundle;
    }

    @Override
    public void locationError(int errorCode) {
        switch (errorCode) {
            case ConfigStateCode.STATE_ERROE:
                ToastUtils.makeShowToast(this, "定位失败");
                mLocationLayoutManager.showError();
                break;
            case ConfigStateCode.STATE_NO_NETWORK:
                ToastUtils.makeShowToast(this, "没有网络");
                mLocationLayoutManager.showNetWorkError();
                break;
        }
    }

    @Override
    public void searchError(int errorCode) {
        switch (errorCode) {
            case ConfigStateCode.STATE_ERROE:
                ToastUtils.makeShowToast(this, "搜索失败");
                mSearchLayoutManager.showError();
                break;
            case ConfigStateCode.STATE_NO_NETWORK:
                ToastUtils.makeShowToast(this, "没有网络");
                mSearchLayoutManager.showNetWorkError();
                break;
            case ConfigStateCode.STATE_DATA_EMPTY:
                ToastUtils.makeShowToast(this, "数据为空");
                mSearchLayoutManager.showEmptyData();
                break;
        }
    }

    @Override
    public void cityError(int errorCode) {
        switch (errorCode) {
            case ConfigStateCode.RESULT_CITY_FAILER:
                ToastUtils.makeShowToast(this, "获取城市失败");
                mCityLayoutManager.showError();
                break;
            case ConfigStateCode.STATE_NO_NETWORK:
                ToastUtils.makeShowToast(this, "没有网络");
                mCityLayoutManager.showNetWorkError();
                break;
        }
    }

    @Override
    public void startLocationLoading() {
        mLocationLayoutManager.showContent();
    }

    @Override
    public void startSearchLoading() {
        mSearchLayoutManager.showLoading();
    }

    @Override
    public void startCityLoading() {
        mCityLayoutManager.showContent();
    }

    @Override
    public void locationSuccess(AMapLocation mapLocation) {
        this.mapLocation = mapLocation;
        mLocationLayoutManager.showContent();
        initAdapter();
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        Bundle bundle = getBundle(AddressTab.TOTAL.getResName(), mapLocation.getCity());
        viewPagerFragmentAdapter.addTabPage(AddressTab.TOTAL.getResName(), AddressTab.TOTAL.getDefaultFragmnet(), bundle);
        bundle = getBundle(AddressTab.OFFICE.getResName(), mapLocation.getCity());
        viewPagerFragmentAdapter.addTabPage(AddressTab.OFFICE.getResName(), AddressTab.OFFICE.getDefaultFragmnet(), bundle);
        bundle = getBundle(AddressTab.VILLAGE.getResName(), mapLocation.getCity());
        viewPagerFragmentAdapter.addTabPage(AddressTab.VILLAGE.getResName(), AddressTab.VILLAGE.getDefaultFragmnet(), bundle);
        bundle = getBundle(AddressTab.SCHOOl.getResName(), mapLocation.getCity());
        viewPagerFragmentAdapter.addTabPage(AddressTab.SCHOOl.getResName(), AddressTab.SCHOOl.getDefaultFragmnet(), bundle);
        vp_address.setAdapter(viewPagerFragmentAdapter);
        vp_address.setCurrentItem(0);
        vp_address.setOffscreenPageLimit(1);
        tl_address.setupWithViewPager(vp_address);

        vp_address.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AddressPositionAddress positionAddress = new AddressPositionAddress();
                positionAddress.setLatLng(new LatLng(AddressActivity.this.mapLocation.getLatitude(),
                        AddressActivity.this.mapLocation.getLongitude(), false));
                positionAddress.setCity(AddressActivity.this.mapLocation.getCity());
                positionAddress.setDistrict(AddressActivity.this.mapLocation.getDistrict());
                positionAddress.setProvince(AddressActivity.this.mapLocation.getProvince());

                RxBus.getDefault().post(ConfigValues.VALUE_ADDRESS_RX_SEND_LOCATION, positionAddress);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //初始化第一页数据
        AddressPositionAddress positionAddress = new AddressPositionAddress();
        positionAddress.setLatLng(new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude(), false));
        positionAddress.setCity(mapLocation.getCity());
        RxBus.getDefault().post(ConfigValues.VALUE_ADDRESS_RX_SEND_LOCATION, positionAddress);
    }

    @Override
    public void searchSuccess(List<Tip> list, String searchText) {
        ArrayList<AddressTipMapLocation> tipMapLocations = new ArrayList<>();
        AddressTipMapLocation tipMapLocation = null;
        for (Tip tip :
                list) {
            tipMapLocation = new AddressTipMapLocation();
            tipMapLocation.setaMapLocation(mapLocation);
            tipMapLocation.setTip(tip);
            tipMapLocation.setSearchText(searchText);
            tipMapLocations.add(tipMapLocation);
        }
        mSearchLayoutManager.showContent();
        AddressSearchAdapter getAddressSearchAdapter = new AddressSearchAdapter(R.layout.item_address, tipMapLocations);
        rv_address_search_list.setLayoutManager(new LinearLayoutManager(this, 1, false));
        rv_address_search_list.setAdapter(getAddressSearchAdapter);
        getAddressSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AddressTipMapLocation item = (AddressTipMapLocation) adapter.getItem(position);
                AddressReturnAddress returnAddress = new AddressReturnAddress();
                returnAddress.setTitle(item.getTip().getName());
                returnAddress.setLatLng(item.getTip().getPoint());
                TextView tv_address_detail = (TextView) view.findViewById(R.id.tv_address_detail);
                returnAddress.setAddress(tv_address_detail.getText().toString());
                returnAddress.setProvince(item.getaMapLocation().getProvince());
                returnAddress.setCity(item.getaMapLocation().getCity());

                RxBus.getDefault().post(ConfigValues.VALUE_ADDRESS_RX_SEND_RETURNADDRESS, returnAddress);
                onBackPressed();
            }
        });
    }

    @Override
    public void citySuccess(final List<AddressCity> list) {
        mCityLayoutManager.showContent();
        Collections.sort(list);
        mBodyDatas = list;
        if (cityAdapter != null) {
            cityAdapter.addData(mBodyDatas);
            mHeaderAdapter.notifyDataSetChanged();
            mSourceDatas.addAll(mBodyDatas);
            mDecoration.setmDatas(mSourceDatas);
            qi_address.setData(mSourceDatas).invalidate();
            cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    GeocodeSearch geocoderSearch = new GeocodeSearch(AddressActivity.this);
                    geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                        @Override
                        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                        }

                        @Override
                        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                            if (i == 1000) {
                                GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                                LatLonPoint latLonPoint = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                                AddressPositionAddress positionAddress = new AddressPositionAddress();
                                positionAddress.setLatLng(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude(), false));
                                positionAddress.setCity(geocodeAddress.getCity());
                                positionAddress.setDistrict(geocodeAddress.getDistrict());
                                positionAddress.setProvince(geocodeAddress.getProvince());
                                RxBus.getDefault().post(ConfigValues.VALUE_ADDRESS_RX_SEND_LOCATION, positionAddress);
                                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude(), false), 18));
                                ll_address_city_list.setVisibility(View.GONE);
                            }
                        }
                    });
                    // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
                    AddressCity item = (AddressCity) adapter.getItem(position);
                    GeocodeQuery query = new GeocodeQuery(item.getName() + "政府", item.getName());

                    geocoderSearch.getFromLocationNameAsyn(query);
                }
            });
        }
    }


}
