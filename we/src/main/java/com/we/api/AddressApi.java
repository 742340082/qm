package com.we.api;

import com.baselibrary.api.Result;
import com.we.config.ConfigReceiveAddress;
import com.we.config.ConfigUser;
import com.we.mvp.receiveaddress.modle.ReceiveAddress;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AddressApi {
    String ADDRESS_LIST_ROOT_PATH = "http://120.24.89.76:80/quickmeals/receiveaddress/";
    @POST("info.action")
    Observable<Result<List<ReceiveAddress>>> receiveAddressInfo(@Query(ConfigUser.USER_ACCOUNT) String account,
                                                                @Query(ConfigReceiveAddress.RECEIVEADDRESS_ID) String id,
                                                                @Body HashMap<String, String> hashMap,
                                                                @Query(ConfigReceiveAddress.RECEIVEADDRESS_OPERATE_TYOE) int operateType);

}
