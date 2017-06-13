package com.we.mvp.receiveaddress.logic;

import com.baselibrary.BaseView;
import com.baselibrary.api.Result;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.SaveConfigReceiveAddresUtil;
import com.baselibrary.utils.UIUtils;
import com.google.gson.Gson;
import com.we.api.AddressApi;
import com.we.config.ConfigReceiveAddress;
import com.we.mvp.receiveaddress.modle.ReceiveAddress;

import java.util.HashMap;
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

/**
 * Created by 74234 on 2017/5/6.
 */

public class LogicReceiveAddress implements LogicReceiveAddressInterInterface {
    protected final Gson mGson;
    protected final AddressApi mcbReceiveAddressApi;
    protected BaseView<List<ReceiveAddress>> baseView;

    protected  LogicReceiveAddress(BaseView<List<ReceiveAddress>> baseView)
    {
     this.baseView=baseView;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ConfigValues.VALUE_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ConfigValues.VALUE_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ConfigValues.VALUE_WRITE_TIMEOUT, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AddressApi.ADDRESS_LIST_ROOT_PATH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        mGson = new Gson();
        mcbReceiveAddressApi = retrofit.create(AddressApi.class);
    }
    @Override
    public void operateReceiveAddress(String account,String receiveaddressID, ReceiveAddress receiveAddress,final int operateReceiveaAddressType) {
        if (!NetworkState.networkConnected(UIUtils.getContext())) {
            baseView.error(ConfigStateCode.STATE_NO_NETWORK,ConfigStateCode.STATE_NO_NETWORK_VALUE);
        }
        HashMap<String, String> hashMap = new HashMap<>();
        if (receiveAddress!=null) {
            String ReceiveAddress = mGson.toJson(receiveAddress);
            hashMap.put(ConfigReceiveAddress.RECEIVEADDRESS_INFO,ReceiveAddress);
        }

        mcbReceiveAddressApi.receiveAddressInfo(account,receiveaddressID,hashMap,operateReceiveaAddressType)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        baseView.loading();
                    }
                })
                .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<ReceiveAddress>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<List<ReceiveAddress>> value) {
                        switch (value.getCode()) {
                            case ConfigStateCode.RESULT_ADD_RECEIVE_ADDRESS_SUCCESS:
                                SaveConfigReceiveAddresUtil.setInt(UIUtils.getContext(),ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_TYOE,operateReceiveaAddressType);
                                RxBus.getDefault().post(ConfigReceiveAddress.RECEIVEADDRESS_RX_SEND_RECEIVE_ADDRESS, value.getResult().get(0));
                                baseView.success(value.getResult());
                                break;
                            case ConfigStateCode.RESULT_RECEIVE_ADDRESS_SUCCESS:
                                SaveConfigReceiveAddresUtil.setInt(UIUtils.getContext(),ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_TYOE,operateReceiveaAddressType);
//                                RxBus.getDefault().post(ConfigReceiveAddress.RECEIVEADDRESS_RX_SEND_RECEIVE_ADDRESS, value.getResult().get(0));
                                baseView.success(value.getResult());
                                break;
                            case ConfigStateCode.RESULT_UPDATE_SUCCESS:
                                SaveConfigReceiveAddresUtil.setInt(UIUtils.getContext(),ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_TYOE,operateReceiveaAddressType);
                                RxBus.getDefault().post(ConfigReceiveAddress.RECEIVEADDRESS_RX_SEND_RECEIVE_ADDRESS, value.getResult().get(0));
                                baseView.success(value.getResult());
                                break;
                            case ConfigStateCode.RESULT_DELETE_SUCCESS:
                                SaveConfigReceiveAddresUtil.setInt(UIUtils.getContext(),ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_TYOE,operateReceiveaAddressType);
                                RxBus.getDefault().post(ConfigReceiveAddress.RECEIVEADDRESS_RX_SEND_RECEIVE_ADDRESS, value.getResult().get(0));
                                baseView.success(value.getResult());
                                break;
                            default:
                                baseView.error(value.getCode(),value.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        baseView.error(ConfigStateCode.STATE_ERROE,ConfigStateCode.STATE_ERROE_VALUE);
                        ConfigStateCodeUtil.error(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
