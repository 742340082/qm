package com.getaddress.modle;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.help.Tip;

/**
 * Created by 74234 on 2017/4/30.
 */

public class AddressTipMapLocation {
    private Tip tip;
    private AMapLocation aMapLocation;
    private String searchText;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public AMapLocation getaMapLocation() {
        return aMapLocation;
    }

    public void setaMapLocation(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
    }

    public Tip getTip() {

        return tip;
    }

    public void setTip(Tip tip) {
        this.tip = tip;
    }
}
