package com.getaddress.biz;

import com.amap.api.maps.AMap;

/**
 * Created by 74234 on 2017/4/28.
 */

public interface GetAddressBiz {
    void initLocation(AMap aMap);
    void resetLocation();
    void  destoryLocation();
    void startInputSeatch(String InputText, String city);
    void  startLocation(AMap aMap);
    void  initCityList();
}
