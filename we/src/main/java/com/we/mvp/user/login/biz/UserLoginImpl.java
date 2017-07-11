package com.we.mvp.user.login.biz;

import com.baselibrary.api.Result;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.SaveConfigUserUtil;
import com.baselibrary.utils.UIUtils;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.we.api.SdkApi;
import com.we.api.UserApi;
import com.we.config.ConfigUser;
import com.we.mvp.user.baseLogic.LogicOperateUser;
import com.we.mvp.user.login.modle.QQUserInfo;
import com.we.mvp.user.login.modle.WeiboRrfreshToken;
import com.we.mvp.user.login.modle.WeiboUserInfo;
import com.we.mvp.user.login.view.IUserLoginView;
import com.we.mvp.user.modle.User;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLoginImpl
        implements UserLogin {
    private IUserLoginView loginView;
    private User mUser;
    UserApi mUserApi;
    private final SdkApi mSdkApi;
    private Gson mGson;
    public LogicOperateUser logicOperateUser;


    public UserLoginImpl(IUserLoginView loginView) {
        this.loginView = loginView;
        logicOperateUser=new LogicOperateUser(loginView);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ConfigValues.VALUE_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ConfigValues.VALUE_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ConfigValues.VALUE_WRITE_TIMEOUT, TimeUnit.SECONDS).build();
        Retrofit retrofitWeibo = new Retrofit.Builder()
                .baseUrl(SdkApi.SDK_WEIBO_ROOT_PATH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        mSdkApi = retrofitWeibo.create(SdkApi.class);
        mUserApi=logicOperateUser.mUserApi;
        mGson=new Gson();
    }

    @Override
    public void login() {
        if (confirmation()) {
            logicOperateUser.loginUser(mUser.getAccount(), mUser.getPassword(),null, ConfigUser.USER_COMMON_LOGIN);
        }
    }

    @Override
    public void weibologin(String accessToken, String uid) {
        mSdkApi.weiboUserInfo(accessToken, uid)
                .subscribeOn(Schedulers.io())
                .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeiboUserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WeiboUserInfo weiboUserInfo) {
                        Logger.i("TAG", weiboUserInfo.getScreen_name() + "---" + weiboUserInfo.toString());
                        isThirdRegist(null, weiboUserInfo, ConfigUser.USER_WEIBO_LOGIN, ConfigUser.USER_WEIBO_REGIST);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void qqLogin(final Tencent tencent) {
        new UserInfo(UIUtils.getContext(), tencent.getQQToken()).getUserInfo(new IUiListener() {
            public void onCancel() {
                tencent.logout(UIUtils.getContext());
            }

            public void onComplete(Object obj) {
                QQUserInfo qqUserInfo = mGson.fromJson(obj.toString(), QQUserInfo.class);
                isThirdRegist(qqUserInfo, null, ConfigUser.USER_QQ_LOGIN, ConfigUser.USER_QQ_REGIST);
            }

            public void onError(UiError uiError) {
                tencent.logout(UIUtils.getContext());
            }
        });

    }

    @Override
    public void alipayLogin() {
    }

    @Override
    public  void  getRefresgToken(String refreshToken) {
        mSdkApi.getAccessToken(refreshToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeiboRrfreshToken>() {
                    @Override
                    public void accept(WeiboRrfreshToken weiboRrfreshToken) throws Exception {
                        loginView.getWeiboToken(weiboRrfreshToken);
                    }
                });
    }


    public void isThirdRegist(final QQUserInfo qqUserInfo, final WeiboUserInfo weiboUserInfo, final int loginType, final int registType) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (weiboUserInfo!=null)
        {
            hashMap.put(ConfigUser.USER_BIND_WEIBOUSERINFO, mGson.toJson(weiboUserInfo));
        }
        if (qqUserInfo!=null)
        {
            hashMap.put(ConfigUser.USER_BIND_QQUSERINFO, mGson.toJson(qqUserInfo));
        }


        mUserApi.isThirdLogin(hashMap, loginType)
                .subscribeOn(Schedulers.io())
                .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<User>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        SaveConfigUserUtil.setInt(UIUtils.getContext(), ConfigUser.USER_LOGIN_TYPE, loginType);
                    }

                    @Override
                    public void onNext(Result<List<User>> result) {
                        switch (result.getCode()) {
                            case ConfigStateCode.RESULT_THIRD_INFO_EXIST:
                                User user = result.getResult().get(0);
                                logicOperateUser.loginUser(user.getAccount(),user.getPassword(),null,loginType);
                                break;
                            case ConfigStateCode.RESULT_THIRD_INFO_UNEXIST:
                                SaveConfigUserUtil.setInt(UIUtils.getContext(), ConfigUser.USER_REGIST_TYPE, registType);
                                loginView.bindAccountActivity(qqUserInfo, weiboUserInfo);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        loginView.error(ConfigStateCode.STATE_ERROE,ConfigStateCode.STATE_ERROE_VALUE);
                        ConfigStateCodeUtil.error(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 数据验证
     *
     * @return true验证成功 false 验证失败
     */
    public boolean confirmation() {
        String account = this.loginView.getUserName();
        String password = null;
        if (account != null) {
            password = this.loginView.getPassword();
        }
        if ((account == null) || (password == null)) {
            return false;
        }

        if (NetworkState.networkConnected(UIUtils.getContext())) {
            mUser = new User(account, password);
            return true;
        } else {
            this.loginView.error(ConfigStateCode.STATE_NO_NETWORK,ConfigStateCode.STATE_NO_NETWORK_VALUE);
            return false;
        }
    }


}
