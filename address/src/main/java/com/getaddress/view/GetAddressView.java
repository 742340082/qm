package com.getaddress.view;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.help.Tip;
import com.getaddress.modle.AddressCity;

import java.util.List;

/**
 * Created by 74234 on 2017/4/28.
 */

public interface GetAddressView {
    void locationError(int errorCode);
    void searchError(int errorCode);
    void cityError(int errorCode);
    void startLocationLoading();
    void startSearchLoading();
    void startCityLoading();
    void  locationSuccess(AMapLocation mapLocation);
    void  searchSuccess(List<Tip> list, String searchText);
    void  citySuccess(List<AddressCity> list);
}
