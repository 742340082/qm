package com.getaddress.presenter;

import com.amap.api.maps.AMap;
import com.getaddress.biz.AddressBiz;
import com.getaddress.biz.AddressBizImpl;
import com.getaddress.view.GetAddressView;

/**
 * Created by 74234 on 2017/4/28.
 */

public class AddressPresenter {
    private AddressBiz getAddressBiz;

    public AddressPresenter(GetAddressView view) {
        getAddressBiz = new AddressBizImpl(view);
    }

    public void initLocation(AMap aMap) {
        getAddressBiz.initLocation(aMap);
    }

    public void resetLocation() {
        getAddressBiz.resetLocation();
    }

    public void destoryLocation() {
        getAddressBiz.destoryLocation();
    }

    public void startInputSeatch(String InputText, String city) {
        getAddressBiz.startInputSeatch(InputText, city);
    }

    public void startLocation(AMap aMap) {
        getAddressBiz.startLocation(aMap);
    }

    public void initCityList() {
        getAddressBiz.initCityList();
    }
}
