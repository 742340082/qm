package com.we.api;

import com.baselibrary.api.Result;
import com.we.config.ConfigUser;
import com.we.mvp.user.modle.User;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserApi {
    String USER_HOST="http://120.24.89.76:80/";
    String USER_ICON_ROOT_PSTH = USER_HOST+"image/";
    String USER_ROOT_PATH = USER_HOST+"quickmeals/user/";


    @GET("login.action")
    Observable<Result<List<User>>> login(@Query(ConfigUser.USER_ACCOUNT) String account,
                                         @Query(ConfigUser.USER_PASSWORD) String password,
                                         @Query(ConfigUser.USER_TELEPHONE) String telephone,
                                         @Query(ConfigUser.USER_LOGIN_TYPE) int loginType);


    @POST("isThirdLogin.action")
    Observable<Result<List<User>>> isThirdLogin(
            @Body HashMap<String, String> hashMap,
            @Query(ConfigUser.USER_LOGIN_TYPE) int loginType);

    @POST("regist.action")
    Observable<Result<List<User>>> regist(@Query(ConfigUser.USER_TELEPHONE) String telephone,
                                          @Query(ConfigUser.USER_PASSWORD) String password,
                                          @Body HashMap<String, String> hashMap,
                                          @Query(ConfigUser.USER_REGIST_TYPE) int registType);

    @PATCH("update.action")
    Observable<Result<List<User>>> update(@Query(ConfigUser.USER_ACCOUNT) String account,
                                          @Query(ConfigUser.USER_NAME) String userName,
                                          @Query(ConfigUser.USER_PASSWORD) String password,
                                          @Query(ConfigUser.USER_NEW_PASSWOD) String newPassword,
                                          @Query(ConfigUser.USER_TELEPHONE) String telephone,
                                          @Query(ConfigUser.USER_PAY_PASSWORD) String payPassword,
                                          @Body HashMap<String, String> hashMap,
                                          @Query(ConfigUser.USER_UPDATE_TYPE) int updateType);



    @GET("getUserInfoByAccount.action")
    Observable<Result<List<User>>> getUserInfoByAccount(@Query(ConfigUser.USER_ACCOUNT) String account);

    @Multipart
    @POST("uploadUserIcon.action")
    Observable<Result<List<User>>> uploadUserIcon(@Part MultipartBody.Part paramPart,
                                                  @Query(ConfigUser.USER_ACCOUNT) String paramString);
}
