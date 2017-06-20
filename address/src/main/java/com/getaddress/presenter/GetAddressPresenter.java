package com.getaddress.presenter;

import com.amap.api.maps.AMap;
import com.getaddress.biz.GetAddressBiz;
import com.getaddress.biz.GetAddressBizImpl;
import com.getaddress.view.GetAddressView;

/**
 * Created by 74234 on 2017/4/28.
 */

public class GetAddressPresenter {
    private GetAddressBiz getAddressBiz;

    public GetAddressPresenter(GetAddressView view) {
        getAddressBiz = new GetAddressBizImpl(view);
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

    public void startInputSeatch(String InputText,String city) {
        getAddressBiz.startInputSeatch(InputText,city);
    }
    public      void  startLocation(AMap aMap)
    {
        getAddressBiz.startLocation(aMap);
    }
    public      void  initCityList()
    {
        getAddressBiz.initCityList();
    }
}
