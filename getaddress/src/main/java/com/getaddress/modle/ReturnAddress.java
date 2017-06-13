package com.getaddress.modle;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.core.LatLonPoint;

/**
 * Created by 74234 on 2017/5/3.
 */

public class ReturnAddress implements Parcelable{
    private LatLonPoint latLng;
    private String city;
    private String province;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public ReturnAddress() {

    }

    public static final Creator<ReturnAddress> CREATOR = new Creator<ReturnAddress>() {
        @Override
        public ReturnAddress createFromParcel(Parcel in) {
            return new ReturnAddress(in);
        }

        @Override
        public ReturnAddress[] newArray(int size) {
            return new ReturnAddress[size];
        }
    };

    public ReturnAddress(Parcel in) {
        title=in.readString();
        address=in.readString();
    }

    public LatLonPoint getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLonPoint latLng) {
        this.latLng = latLng;
    }

    String title;
    String address;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.address);
    }
}
