package com.we.api;


import com.baselibrary.config.ConfigSdk;
import com.we.mvp.user.login.modle.AlipayOpenAuthToken;
import com.we.mvp.user.login.modle.WeiboRrfreshToken;
import com.we.mvp.user.login.modle.WeiboUserInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yangyupan on 2017/4/12.
 */

public interface SdkApi {
    //新浪微博获取用户信息的根路径
    String SDK_WEIBO_ROOT_PATH ="https://api.weibo.com/";
    @GET("oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=refresh_token&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&refresh_token={accessToken}")
    Observable<WeiboRrfreshToken> getAccessToken(@Path("accessToken") String accessToken);

    @GET("2/users/show.json?forcelogin=true")
    Observable<WeiboUserInfo> weiboUserInfo(@Query(ConfigSdk.SDK_ACCESS_TOKEN) String accessToken,
                                            @Query(ConfigSdk.SDK_ACCESS_UID) String accessUid);
    //////////////////////////////////////支付宝//////////////////////////////////////////
    String SDK_ALIPAY_BEBUG_ROOT_PATH="https://openauth.alipaydev.com/oauth2/";
    @GET("appToAppAuth.htm")
    Observable<AlipayOpenAuthToken> obtionAuthToken(@Query("app_id") String app_id,@Query("redirect_uri") String redirect_uri);
}
