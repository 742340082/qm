package com.getaddress.api;

import com.baselibrary.api.Result;
import com.getaddress.modle.GetAddressCity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface AddressApi {
    String ADDRESS_CITY_ROOT_PATH = "http://192.168.0.107:80/quickmeals/city/";


    @GET("cityall.action")
    Observable<Result<List<GetAddressCity>>> cityall();
}
