package com.getaddress.biz;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.baselibrary.api.Result;
import com.baselibrary.config.ConfigSdk;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.UIUtils;
import com.getaddress.R;
import com.getaddress.api.AddressApi;
import com.getaddress.modle.AddressCity;
import com.getaddress.modle.AddressPositionAddress;
import com.getaddress.view.GetAddressView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 74234 on 2017/4/28.
 */

public class AddressBizImpl implements AddressBiz {


    private final AddressApi addressApi;
    //地图控制中心
    private AMapLocationClient mlocationClient;
    //地图拖动的标记
    private Marker marker;
    //定位的坐标
    private LatLng latLng;
    private AMap aMap;

    private GetAddressView getAddressView;

    public AddressBizImpl(GetAddressView view) {
        this.getAddressView = view;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ConfigValues.VALUE_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ConfigValues.VALUE_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ConfigValues.VALUE_WRITE_TIMEOUT, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AddressApi.ADDRESS_CITY_ROOT_PATH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        addressApi = retrofit.create(AddressApi.class);
    }

    @Override
    public void initLocation(final AMap aMap) {
        if (!NetworkState.networkConnected(UIUtils.getContext())) {
            getAddressView.locationError(ConfigStateCode.STATE_NO_NETWORK);
            return;
        }
        this.aMap = aMap;
        //地图拖动的监听
        getAddressView.startLocationLoading();
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            private int num = 0;

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                marker.setPosition(cameraPosition.target);
            }

            @Override
            public void onCameraChangeFinish(final CameraPosition cameraPosition) {

                GeocodeSearch geocoderSearch = new GeocodeSearch(UIUtils.getContext());
                geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                        if (num != 0) {
                            AddressPositionAddress positionAddress = new AddressPositionAddress();
                            positionAddress.setLatLng(new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude,false) );
                            positionAddress.setCity(regeocodeResult.getRegeocodeAddress().getCity());
                            positionAddress.setDistrict(regeocodeResult.getRegeocodeAddress().getDistrict());
                            positionAddress.setProvince(regeocodeResult.getRegeocodeAddress().getProvince());
                            RxBus.getDefault().post(ConfigValues.VALUE_ADDRESS_RX_SEND_LOCATION, positionAddress);
                        }
                        num++;
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                    }
                });
                // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude),
                        5000f, GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);

            }
        });

        // 设置定位监听
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                Observable.just(onLocationChangedListener)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                getAddressView.startLocationLoading();
                            }
                        })
                        .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<OnLocationChangedListener>() {

                            @Override
                            public void accept(OnLocationChangedListener onLocationChangedListener) throws Exception {
                                startLocation(aMap);
                            }
                        });


            }

            @Override
            public void deactivate() {
                if (mlocationClient != null) {
                    mlocationClient.stopLocation();
                    mlocationClient.onDestroy();
                }
                mlocationClient = null;
            }
        });
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        aMap.setMapType(AMap.MAP_TYPE_NIGHT);
        aMap.setCustomMapStylePath(ConfigSdk.SDK_SAVE_CUSTOM_MAP_CONFIG_PATH);
        aMap.setMapCustomEnable(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    @Override
    public void startLocation(final AMap aMap) {
        //初始化定位
        mlocationClient = new AMapLocationClient(UIUtils.getContext());
        //初始化定位参数
        AMapLocationClientOption LocationOption = new AMapLocationClientOption();
        //设置定位回调监听
        mlocationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation mapLocation) {

                                if (mapLocation != null) {
                                    latLng = new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude(),false);
                                    if (mapLocation != null
                                            && mapLocation.getErrorCode() == 0) {
                                        MarkerOptions markerOption = new MarkerOptions();
                                        markerOption.position(latLng);
                                        markerOption.draggable(true);//设置Marker可拖动
                                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(
                                                BitmapFactory
                                                        .decodeResource(UIUtils.getContext().getResources(), R.drawable.icon_location))
                                        );
                                        //将Marker设置为贴地显示，可以双指下拉地图查看效果
                                        markerOption.setFlat(true);//设置marker平贴地图效果
                                        marker = aMap.addMarker(markerOption);
                                        getAddressView.locationSuccess(mapLocation);
                                        //设置希望展示的地图缩放级别
                                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                                    } else {
                                        String errText = "定位失败," + mapLocation.getErrorCode() + ": " + mapLocation.getErrorInfo();
                                        Log.e("AmapErr", errText);
                                    }
                                } else {
                                    getAddressView.locationError(ConfigStateCode.STATE_ERROE);
                                }


            }
        });
        //获取一次定位结果：
        //该方法默认为false。
        LocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        LocationOption.setMockEnable(true);
        //设置为高精度定位模式
        LocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        mlocationClient.setLocationOption(LocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient.startLocation();//启动定位
    }

    @Override
    public void resetLocation() {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    @Override
    public void destoryLocation() {
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    public void startInputSeatch(final String InputText,final String city  ) {
        if (!NetworkState.networkConnected(UIUtils.getContext())) {
            getAddressView.searchError(ConfigStateCode.STATE_NO_NETWORK);
            return;
        }
        InputtipsQuery inputquery = new InputtipsQuery(InputText,city);
        inputquery.setCityLimit(true);//限制在当前城市
        Inputtips inputTips = new Inputtips(UIUtils.getContext(), inputquery);
        getAddressView.startSearchLoading();
        inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(final List<Tip> list, final int i) {
                Observable.just(list)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                getAddressView.startSearchLoading();
                            }
                        })
                        .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Tip>>() {
                            @Override
                            public void accept(List<Tip> tips) throws Exception {
                                if (i == 1000) {
                                    if (list.size() != 0) {
                                        getAddressView.searchSuccess(list, InputText);
                                    } else {
                                        getAddressView.searchError(ConfigStateCode.STATE_DATA_EMPTY);
                                    }
                                } else {
                                    getAddressView.searchError(ConfigStateCode.STATE_ERROE);
                                }
                            }
                        });
            }
        });
        inputTips.requestInputtipsAsyn();

    }

    @Override
    public void initCityList() {
        if (!NetworkState.networkConnected(UIUtils.getContext()))
        {
            getAddressView.cityError(ConfigStateCode.STATE_NO_NETWORK);
        }
        addressApi.cityall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getAddressView.startCityLoading();
                    }
                })
                .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<AddressCity>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<List<AddressCity>> value) {
                            switch (value.getCode())
                            {
                                case ConfigStateCode.RESULT_CITY_SUCCESS:
                                    getAddressView.citySuccess(value.getResult());
                                    break;
                                default:
                                    getAddressView.cityError(value.getCode());
                            }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getAddressView.cityError(ConfigStateCode.STATE_ERROE);
                        ConfigStateCodeUtil.error(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
