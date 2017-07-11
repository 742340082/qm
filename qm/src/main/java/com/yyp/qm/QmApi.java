package com.yyp.qm;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by 74234 on 2017/6/22.
 */

public interface QmApi {
    @GET("bing_pic")
    Call<String> getPicture();
}
