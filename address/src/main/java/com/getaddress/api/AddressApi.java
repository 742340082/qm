package com.getaddress.api;

import com.baselibrary.api.Result;
import com.getaddress.modle.AddressCity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface AddressApi {
    String ADDRESS_CITY_ROOT_PATH = "http://120.24.89.76:80/quickmeals/city/";


    @GET("cityall.action")
    Observable<Result<List<AddressCity>>> cityall();
}
