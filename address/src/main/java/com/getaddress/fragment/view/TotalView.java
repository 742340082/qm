package com.getaddress.fragment.view;

import com.amap.api.services.poisearch.PoiResult;

/**
 * Created by 74234 on 2017/4/27.
 */

public interface TotalView {
    void  error(int errorCode);
    void  startLoading();
    void  success(PoiResult poiResult, boolean isloadmore);
}
