package com.getaddress.modle;

import com.amap.api.maps.model.LatLng;

/**
 * Created by 74234 on 2017/4/30.
 */

public class GetAddressPositionAddress {
    private LatLng latLng;
    private String city;
    private String district;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    private String province;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

}
