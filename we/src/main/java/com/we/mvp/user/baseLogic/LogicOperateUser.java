package com.we.mvp.user.baseLogic;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.baselibrary.BaseView;
import com.baselibrary.api.Result;
import com.baselibrary.api.okhttp.ApiListenerManager;
import com.baselibrary.api.okhttp.OkHttp;
import com.baselibrary.api.okhttp.ProgressRequestBody;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.MD5Util;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.SaveConfigUserUtil;
import com.baselibrary.utils.UIUtils;
import com.google.gson.Gson;
import com.we.api.UserApi;
import com.we.config.ConfigUser;
import com.we.mvp.user.login.modle.QQUserInfo;
import com.we.mvp.user.login.modle.WeiboUserInfo;
import com.we.mvp.user.modle.User;
import com.we.utils.DateUtil;
import com.we.utils.PresenceUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 74234 on 2017/5/6.
 */

public class LogicOperateUser implements LogicOperateUserInterface {
    private BaseView<List<User>> baseView;
    public UserApi mUserApi;
    private Gson mGson;
    public LogicOperateUser(BaseView<List<User>> baseView)
    {
        this.baseView=baseView;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ConfigValues.VALUE_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ConfigValues.VALUE_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ConfigValues.VALUE_WRITE_TIMEOUT, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserApi.USER_ROOT_PATH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        this.mUserApi = retrofit.create(UserApi.class);
        mGson=new Gson();
    }


    @Override
    public void obtainUser(String account) {
        if (!NetworkState.networkConnected(UIUtils.getContext()))
        {
            baseView.error(ConfigStateCode.STATE_NO_NETWORK,ConfigStateCode.STATE_NO_NETWORK_VALUE);
        }else
        {
            mUserApi.getUserInfoByAccount(account)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            baseView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Result<List<User>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Result<List<User>> value) {
                            switch (value.getCode())
                            {
                                case ConfigStateCode.RESULT_OBTION_USER_SUCCESS:
                                    Bitmap userIconBitmap = PresenceUtil.obtainBitmap(value.getResult().get(0).getAccount());
                                    RxBus.getDefault().post(ConfigUser.USER_RX_ICON,userIconBitmap);
                                    RxBus.getDefault().post(ConfigUser.USER_RX_SEND,value.getResult().get(0));
                                    baseView.success(value.getResult());
                                    break;
                                default:
                                    baseView.error(value.getCode(),value.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            baseView.error(ConfigStateCode.STATE_ERROE,ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }


    }

    @Override
    public void loginUser(String account, String password,String telephone,final int loginType) {
        if (!NetworkState.networkConnected(UIUtils.getContext()))
        {
            baseView.error(ConfigStateCode.STATE_NO_NETWORK,ConfigStateCode.STATE_NO_NETWORK_VALUE);
        }else {
            mUserApi.login(account, password, telephone, loginType)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            baseView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result<List<User>>>() {
                public void onComplete() {
                }

                public void onError(Throwable throwable) {
                    baseView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                    ConfigStateCodeUtil.error(throwable);
                }

                public void onNext(final Result<List<User>> result) {

                    switch (result.getCode()) {
                        case ConfigStateCode.RESULT_LOGIN_SUCCESS:
                            final User user = result.getResult().get(0);
                            final File file = new File(ConfigUser.SAVE_USER_ICON_URI(user.getAccount(), ".jpg").getPath());
                            downloadUserIcon(user);
                            Observable.just(user)
                                    .observeOn(Schedulers.io())
                                    .map(new Function<User, Long>() {
                                        @Override
                                        public Long apply(User user) throws Exception {
                                            return OkHttp.getInstance().obtainFileContentLengByUrl(user.getPicturepath());
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Long>() {
                                        @Override
                                        public void accept(Long contentLeng) throws Exception {
                                            while (true) {
                                                if (file.length() == contentLeng) {

                                                    Bitmap userIconBitmap = PresenceUtil.obtainBitmap(user.getAccount());
                                                    RxBus.getDefault().post(ConfigUser.USER_RX_SEND, user);
                                                    RxBus.getDefault().post(ConfigUser.USER_RX_ICON, userIconBitmap);
                                                    String json = new Gson().toJson(user);
                                                    SaveConfigUserUtil.setString(UIUtils.getContext(), ConfigUser.USER_SAVE, json);
                                                    SaveConfigUserUtil.setInt(UIUtils.getContext(), ConfigUser.USER_LOGIN_TYPE, loginType);
                                                    SaveConfigUserUtil.setLong(UIUtils.getContext(), ConfigUser.USER_CACHE_TIME, DateUtil.SaveUserTime());
                                                    break;
                                                } else {
                                                    continue;
                                                }
                                            }
                                            baseView.success(result.getResult());
                                        }
                                    });

                            break;
                        default:
                            baseView.error(result.getCode(), result.getMessage());
                    }

                }

                public void onSubscribe(Disposable disposable) {
                }
            });
        }
    }

    @Override
    public void registUser(String account, String password, QQUserInfo qqUserInfo, WeiboUserInfo weiboUserInfo, final int registType) {
        if (!NetworkState.networkConnected(UIUtils.getContext()))
        {
            baseView.error(ConfigStateCode.STATE_NO_NETWORK,ConfigStateCode.STATE_NO_NETWORK_VALUE);
        }else {
            HashMap<String, String> hashMap = new HashMap<>();
            if (qqUserInfo != null) {
                hashMap.put(ConfigUser.USER_BIND_QQUSERINFO, mGson.toJson(qqUserInfo));
            }
            if (weiboUserInfo != null) {
                hashMap.put(ConfigUser.USER_BIND_WEIBOUSERINFO, mGson.toJson(weiboUserInfo));
            }

            mUserApi.regist(account, password, hashMap, registType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            baseView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result<List<User>>>() {
                public void onComplete() {
                }

                public void onError(Throwable throwable) {
                    baseView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                    ConfigStateCodeUtil.error(throwable);
                }

                public void onNext(final Result<List<User>> result) {
                    switch (result.getCode()) {
                        case ConfigStateCode.RESULT_REGIST_SUCCESS:
                            final User user = result.getResult().get(0);
                            final File file = new File(ConfigUser.SAVE_USER_ICON_URI(user.getAccount(), ".jpg").getPath());
                            downloadUserIcon(user);
                            Observable.just(user)
                                    .observeOn(Schedulers.io())
                                    .map(new Function<User, Long>() {
                                        @Override
                                        public Long apply(User user) throws Exception {

                                            return OkHttp.getInstance().obtainFileContentLengByUrl(user.getPicturepath());
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Long>() {
                                        @Override
                                        public void accept(Long contentLeng) throws Exception {
                                            while (true) {
                                                if (file.length() == contentLeng) {
                                                    String json = new Gson().toJson(user);
                                                    uploadUserIcon(user);
                                                    Bitmap userIconBitmap = PresenceUtil.obtainBitmap(user.getAccount());
                                                    RxBus.getDefault().post(ConfigUser.USER_RX_SEND, user);
                                                    RxBus.getDefault().post(ConfigUser.USER_RX_ICON, userIconBitmap);
                                                    SaveConfigUserUtil.setString(UIUtils.getContext(), ConfigUser.USER_SAVE, json);
                                                    SaveConfigUserUtil.setInt(UIUtils.getContext(), ConfigUser.USER_REGIST_TYPE, registType);
                                                    SaveConfigUserUtil.setLong(UIUtils.getContext(), ConfigUser.USER_CACHE_TIME, DateUtil.SaveUserTime());
                                                    break;
                                                } else {
                                                    continue;
                                                }
                                            }
                                            baseView.success(result.getResult());
                                        }
                                    });
                            break;
                        default:
                            baseView.error(result.getCode(), result.getMessage());
                    }
                }

                public void onSubscribe(Disposable paramAnonymousDisposable) {
                }
            });
        }
    }

    @Override
    public void updateUser(String account, String userName, String password, String newPassword, String telephone, String payPassword, QQUserInfo qqUserInfo, WeiboUserInfo weiboUserInfo, int updateType) {
        if (!NetworkState.networkConnected(UIUtils.getContext()))
        {
            baseView.error(ConfigStateCode.STATE_NO_NETWORK,ConfigStateCode.STATE_NO_NETWORK_VALUE);
        }else {
            HashMap<String, String> hashMap = new HashMap<>();
            if (qqUserInfo != null) {
                hashMap.put(ConfigUser.USER_BIND_QQUSERINFO, mGson.toJson(qqUserInfo));
            }
            if (weiboUserInfo != null) {
                hashMap.put(ConfigUser.USER_BIND_WEIBOUSERINFO, mGson.toJson(weiboUserInfo));
            }
            mUserApi.update(account,
                    userName,
                    password,
                    newPassword,
                    telephone,
                    payPassword,
                    hashMap,
                    updateType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            baseView.loading();
                        }
                    })
                    .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Result<List<User>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Result<List<User>> value) {
                            Logger.e("TAG", "修改成功");
                            switch (value.getCode()) {
                                case ConfigStateCode.RESULT_UPDATE_SUCCESS:
                                    User user = value.getResult().get(0);
                                    RxBus.getDefault().post(ConfigUser.USER_RX_SEND, user);
                                    String json = new Gson().toJson(user);
                                    SaveConfigUserUtil.setString(UIUtils.getContext(), ConfigUser.USER_SAVE, json);
                                    baseView.success(value.getResult());
                                    break;
                                default:
                                    baseView.error(value.getCode(), value.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            baseView.error(ConfigStateCode.STATE_ERROE, ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    /**
     * 下载头像
     *
     * @param user 用户对象
     */
    private void downloadUserIcon(User user) {
        Observable.just(user).
                subscribeOn(Schedulers.io()).
                observeOn(Schedulers.io()).
                subscribe(new Consumer<User>() {
            public void accept(@NonNull User mcbUser)
                    throws Exception {
                if (!TextUtils.isEmpty(mcbUser.getPicturepath())) {
                    OkHttp.getInstance().asyncUserImageByServer(mcbUser.getPicturepath(),
                            mcbUser.getAccount(),ConfigUser.USER_ICON_PATH,
                            new ApiListenerManager.onDownloadFileListener() {
                        public void onError(Exception throwable) {
                            baseView.error(ConfigStateCode.STATE_ERROE,ConfigStateCode.STATE_ERROE_VALUE);
                            ConfigStateCodeUtil.error(throwable);
                        }

                        public void onResponst(int downloadCode) {
                            Logger.i("TAG", downloadCode + "");
                            switch (downloadCode) {
                                case 103:
                                    baseView.error(ConfigStateCode.STATE_UNUSUAL,ConfigStateCode.STATE_UNUSUAL_VALUE);
                                    break;
                                case 101:
                                    baseView.error(ConfigStateCode.STATE_FILE_NOT_FOUND,ConfigStateCode.STATE_FILE_NOT_FOUND_VALUE);
                                    break;
                            }

                        }
                    });
                }
            }
        });

    }
    /**
     * 上传图片
     */
    private void uploadUserIcon(User user) {
        String account = user.getAccount();
        final File userIconFile = new File(ConfigUser.SAVE_USER_ICON_URI(account, ".jpg").getPath());
        ProgressRequestBody requestBody = new ProgressRequestBody(RequestBody.create(MediaType.parse("multipart/form-data"), userIconFile), new ApiListenerManager.onUploadImageFileProgressListener() {
            public void onProgress(long bytesWritten, long contentLength) {
            }
        });
        String encodeAccount = MD5Util.encode(account);
        MultipartBody.Part formData = MultipartBody.Part.createFormData("photos.", encodeAccount + ".jpg", requestBody);
        mUserApi.uploadUserIcon(formData, account).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<User>>>() {
                    public void onComplete() {
                    }

                    public void onError(Throwable throwable) {
                        baseView.error(ConfigStateCode.STATE_ERROE,ConfigStateCode.STATE_ERROE_VALUE);
                        ConfigStateCodeUtil.error(throwable);
                    }

                    public void onNext(Result<List<User>> result) {
                        switch (result.getCode()) {
                            case ConfigStateCode.RESULT_UPLOAD_FAILER:
                                break;
                            case ConfigStateCode.RESULT_UPLOAD_SUCCESS:
//                        mBindRegistView.locationError(ConfigStateCode.STATE_UNUSUAL);
                                break;
                        }
                    }

                    public void onSubscribe(Disposable paramAnonymousDisposable) {
                    }
                });
    }
}
