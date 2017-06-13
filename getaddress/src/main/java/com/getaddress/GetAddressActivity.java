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
import com.baselibrary.basepager.HeaderRecyclerAndFooterWrapperAdapter;
import com.baselibrary.basepager.ViewPagerFragmentAdapter;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.QuickIndexBar;
import com.baselibrary.view.SuspensionDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.getaddress.adapter.CityAdapter;
import com.getaddress.adapter.GetAddressSearchAdapter;
import com.getaddress.modle.GetAddressCity;
import com.getaddress.modle.GetAddressPositionAddress;
import com.getaddress.modle.GetAddressReturnAddress;
import com.getaddress.modle.GetAddressTipMapLocation;
import com.getaddress.presenter.GetAddressPresenter;
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

public class GetAddressActivity extends AppCompatActivity implements GetAddressView {
    @BindView(R2.id.map)
    MapView map;
    @BindView(R2.id.tl_getaddress)
    TabLayout tl_getaddress;
    @BindView(R2.id.vp_getaddress)
    ViewPager vp_getaddress;
    @BindView(R2.id.tv_city)
    TextView tv_city;
    @BindView(R2.id.iv_city)
    ImageView iv_city;
    @BindView(R2.id.et_search_address)
    EditText et_search_address;
    @BindView(R2.id.tv_cancle_search)
    TextView tv_cancle_search;
    @BindView(R2.id.qi_bar)
    QuickIndexBar qi_bar;
    @BindView(R2.id.tv_quickindex_title)
    TextView tv_quickindex_title;

    @BindView(R2.id.rv_getaddress_search_list)
    RecyclerView rv_getaddress_search_list;
    @BindView(R2.id.rv_getaddress_city_list)
    RecyclerView rv_getaddress_city_list;

    @BindView(R2.id.ll_getaddress_city_list)
    LinearLayout ll_getaddress_city_list;
    @BindView(R2.id.rl_getaddress_city_list)
    RelativeLayout rl_getaddress_city_list;
    @BindView(R2.id.ll_getaddress_search_list)
    LinearLayout ll_getaddress_search_list;

    @BindView(R2.id.rl_getaddress)
    RelativeLayout rl_getaddress;


    private AMap aMap;
    private GetAddressPresenter presenter;
    private StatusLayoutManager mLocationLayoutManager;
    private StatusLayoutManager mSearchLayoutManager;
    private StatusLayoutManager mCityLayoutManager;
    private AMapLocation mapLocation;
    private boolean isLoadCity;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private CityAdapter cityAdapter;
    private List<GetAddressCity> mBodyDatas;
    private SuspensionDecoration mDecoration;
    private List<GetAddressCity> mSourceDatas;

    @OnClick(R2.id.iv_back)
    void back() {
        onBackPressed();
    }

    @OnClick(R2.id.ll_city)
    void chooseCity() {
        if (ll_getaddress_city_list.getVisibility() == View.VISIBLE) {

            iv_city.setImageResource(R.drawable.arrow_up);
            ll_getaddress_city_list.setVisibility(View.GONE);
        } else {
            iv_city.setImageResource(R.drawable.arrow_down);
            ll_getaddress_city_list.setVisibility(View.VISIBLE);
            if (!isLoadCity) {
                presenter.initCityList();
                isLoadCity = true;
            }
        }
    }

    @OnClick(R2.id.ib_location)
    void resetLocation() {
        presenter.resetLocation();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_getaddress);
        ButterKnife.bind(this);
        map.onCreate(bundle);
        initView();
        initData();
        initlistener();
    }

    public void initData() {

        RxBus.getDefault().toObservable(ConfigValues.VALUE_ADDRESS_RX_SEND_LOCATION, GetAddressPositionAddress.class)
                .subscribe(new Consumer<GetAddressPositionAddress>() {

                    @Override
                    public void accept(GetAddressPositionAddress positionAddress) throws Exception {
                        if (mapLocation != null) {
                            mapLocation.setCity(positionAddress.getCity());
                            mapLocation.setProvince(positionAddress.getProvince());
                            mapLocation.setDistrict(positionAddress.getDistrict());
                            mapLocation.setLatitude(positionAddress.getLatLng().latitude);
                            mapLocation.setLongitude(positionAddress.getLatLng().longitude);
                            tv_city.setText(mapLocation.getCity());
                        }
                    }
                });

        presenter = new GetAddressPresenter(this);
        if (aMap == null) {
            aMap = map.getMap();
        }
        presenter.initLocation(aMap);


    }

    private void initAdapter() {

        if (cityAdapter == null) {
            mBodyDatas = new ArrayList<>();
            mSourceDatas = new ArrayList<>();

            GetAddressCity city = new GetAddressCity();
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
                    GetAddressCity city1 = (GetAddressCity) o;
                    holder.setText(R.id.tv_city, city1.getName());
                    final ImageView iv_getaddress_reset_location = holder.getView(R.id.iv_getaddress_reset_location);
                    final ProgressBar pb_getaddress_reset_location = holder.getView(R.id.pb_getaddress_reset_location);
                    LinearLayout ll_getaddress_reset_location = holder.getView(R.id.ll_getaddress_reset_location);
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
            rv_getaddress_city_list.setLayoutManager(linearLayoutManager);
            rv_getaddress_city_list.setAdapter(mHeaderAdapter);
            rv_getaddress_city_list.addItemDecoration(mDecoration = new SuspensionDecoration(this, mSourceDatas)
                    .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - 1)
                    .setColorTitleBg(UIUtils.getColor(R.color.gray)));
            qi_bar.setLinearLayoutManager(linearLayoutManager).showTextView(tv_quickindex_title);
            mHeaderAdapter.notifyDataSetChanged();
        } else {
            GetAddressCity city = new GetAddressCity();
            city.setFirstLetter("当前定位的城市");
            if (mapLocation != null) {
                city.setName(mapLocation.getCity());
            } else {
                city.setName(UIUtils.getString(R.string.loading));
            }
            city.setCode("-1");
            mHeaderAdapter.notifyItemChanged(0, city);
            ll_getaddress_city_list.setVisibility(View.GONE);
        }
    }

    private void initlistener() {
        et_search_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_getaddress_search_list.setVisibility(View.VISIBLE);
                tv_cancle_search.setVisibility(View.VISIBLE);
                String inputText = et_search_address.getText().toString().trim();
                final String searchText = inputText.replace(" ", "");
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
        });
        tv_cancle_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_getaddress_search_list.setVisibility(View.GONE);
                tv_cancle_search.setVisibility(View.GONE);
            }
        });
        et_search_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = et_search_address.getText().toString().trim();
                final String searchText = inputText.replace(" ", "");
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
        rl_getaddress.addView(mLocationLayoutManager.getRootLayout(), rl_getaddress.getChildCount() - 1);
        mSearchLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        String inputText = et_search_address.getText().toString().trim();
                        String searchText = inputText.replace(" ", "");
                        presenter.startInputSeatch(searchText, mapLocation.getCity());
                    }
                })
                .build();
        ;
        ll_getaddress_search_list.addView(mSearchLayoutManager.getRootLayout(), ll_getaddress_search_list.getChildCount() - 1);
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
        rl_getaddress_city_list.addView(mCityLayoutManager.getRootLayout(), rl_getaddress_city_list.getChildCount() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        presenter.destoryLocation();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            if (ll_getaddress_search_list.getVisibility()==View.VISIBLE)
            {
                ll_getaddress_search_list.setVisibility(View.GONE);
                tv_cancle_search.setVisibility(View.GONE);
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
        Bundle bundle = getBundle(GetAddressTab.TOTAL.getResName(), mapLocation.getCity());
        viewPagerFragmentAdapter.addTabPage(GetAddressTab.TOTAL.getResName(), GetAddressTab.TOTAL.getDefaultFragmnet(), bundle);
        bundle = getBundle(GetAddressTab.OFFICE.getResName(), mapLocation.getCity());
        viewPagerFragmentAdapter.addTabPage(GetAddressTab.OFFICE.getResName(), GetAddressTab.OFFICE.getDefaultFragmnet(), bundle);
        bundle = getBundle(GetAddressTab.VILLAGE.getResName(), mapLocation.getCity());
        viewPagerFragmentAdapter.addTabPage(GetAddressTab.VILLAGE.getResName(), GetAddressTab.VILLAGE.getDefaultFragmnet(), bundle);
        bundle = getBundle(GetAddressTab.SCHOOl.getResName(), mapLocation.getCity());
        viewPagerFragmentAdapter.addTabPage(GetAddressTab.SCHOOl.getResName(), GetAddressTab.SCHOOl.getDefaultFragmnet(), bundle);
        vp_getaddress.setAdapter(viewPagerFragmentAdapter);
        vp_getaddress.setCurrentItem(0);
        vp_getaddress.setOffscreenPageLimit(1);
        tl_getaddress.setupWithViewPager(vp_getaddress);

        vp_getaddress.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                GetAddressPositionAddress positionAddress = new GetAddressPositionAddress();
                positionAddress.setLatLng(new LatLng(GetAddressActivity.this.mapLocation.getLatitude(),
                        GetAddressActivity.this.mapLocation.getLongitude(), false));
                positionAddress.setCity(GetAddressActivity.this.mapLocation.getCity());
                positionAddress.setDistrict(GetAddressActivity.this.mapLocation.getDistrict());
                positionAddress.setProvince(GetAddressActivity.this.mapLocation.getProvince());

                RxBus.getDefault().post(ConfigValues.VALUE_ADDRESS_RX_SEND_LOCATION, positionAddress);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //初始化第一页数据
        GetAddressPositionAddress positionAddress = new GetAddressPositionAddress();
        positionAddress.setLatLng(new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude(), false));
        positionAddress.setCity(mapLocation.getCity());
        RxBus.getDefault().post(ConfigValues.VALUE_ADDRESS_RX_SEND_LOCATION, positionAddress);
    }

    @Override
    public void searchSuccess(List<Tip> list, String searchText) {
        ArrayList<GetAddressTipMapLocation> tipMapLocations = new ArrayList<>();
        GetAddressTipMapLocation tipMapLocation = null;
        for (Tip tip :
                list) {
            tipMapLocation = new GetAddressTipMapLocation();
            tipMapLocation.setaMapLocation(mapLocation);
            tipMapLocation.setTip(tip);
            tipMapLocation.setSearchText(searchText);
            tipMapLocations.add(tipMapLocation);
        }
        mSearchLayoutManager.showContent();
        GetAddressSearchAdapter getAddressSearchAdapter = new GetAddressSearchAdapter(R.layout.item_address, tipMapLocations);
        rv_getaddress_search_list.setLayoutManager(new LinearLayoutManager(this, 1, false));
        rv_getaddress_search_list.setAdapter(getAddressSearchAdapter);
        getAddressSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GetAddressTipMapLocation item = (GetAddressTipMapLocation) adapter.getItem(position);
                GetAddressReturnAddress returnAddress=new GetAddressReturnAddress();
                returnAddress.setTitle(item.getTip().getName());
                returnAddress.setLatLng(item.getTip().getPoint());
                TextView tv_address_detail = (TextView) view.findViewById(R.id.tv_address_detail);
                returnAddress.setAddress(tv_address_detail.getText().toString());
                returnAddress.setProvince(item.getaMapLocation().getProvince());
                returnAddress.setCity(item.getaMapLocation().getCity());

                RxBus.getDefault().post(ConfigValues.VALUE_ADDRESS_RX_SEND_RETURNADDRESS,returnAddress);
                onBackPressed();
            }
        });
    }

    @Override
    public void citySuccess(final List<GetAddressCity> list) {
        mCityLayoutManager.showContent();
        Collections.sort(list);
        mBodyDatas = list;

        cityAdapter.addData(mBodyDatas);
        mHeaderAdapter.notifyDataSetChanged();
        mSourceDatas.addAll(mBodyDatas);
        mDecoration.setmDatas(mSourceDatas);
        qi_bar.setData(mSourceDatas).invalidate();


        cityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GeocodeSearch geocoderSearch = new GeocodeSearch(GetAddressActivity.this);
                geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                        if (i==1000) {
                            GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                            LatLonPoint latLonPoint = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                            GetAddressPositionAddress positionAddress = new GetAddressPositionAddress();
                            positionAddress.setLatLng(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude(), false));
                            positionAddress.setCity(geocodeAddress.getCity());
                            positionAddress.setDistrict(geocodeAddress.getDistrict());
                            positionAddress.setProvince(geocodeAddress.getProvince());
                            RxBus.getDefault().post(ConfigValues.VALUE_ADDRESS_RX_SEND_LOCATION, positionAddress);
                            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude(), false), 18));
                            ll_getaddress_city_list.setVisibility(View.GONE);
                        }
                    }
                });
                // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
                GetAddressCity item = (GetAddressCity) adapter.getItem(position);
                GeocodeQuery query = new GeocodeQuery(item.getName() + "政府", item.getName());

                geocoderSearch.getFromLocationNameAsyn(query);
            }
        });
    }


}
