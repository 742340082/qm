package com.we.mvp.user.detail.biz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.baselibrary.api.Result;
import com.baselibrary.api.okhttp.ApiListenerManager;
import com.baselibrary.api.okhttp.ProgressRequestBody;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.MD5Util;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.RxBus;
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
import com.we.mvp.user.detail.view.UserDetailView;
import com.we.mvp.user.login.modle.QQUserInfo;
import com.we.mvp.user.login.modle.WeiboUserInfo;
import com.we.mvp.user.modle.User;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class UserDetailBizImpl
        implements UserDetailBiz {

    private final SdkApi mSdkApi;
    private UserDetailView mUserDetailView;
    private Gson mGson;
    UserApi mUserApi;
    private LogicOperateUser logicOperateUser;


    public UserDetailBizImpl(UserDetailView userDetailView) {
        this.mUserDetailView = userDetailView;
        logicOperateUser=new LogicOperateUser(userDetailView);
        mUserApi=logicOperateUser.mUserApi;
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
        this.mGson = new Gson();
    }


    /**
     * 裁剪失败
     * @param intent 裁剪失败的itent
     * @param takeTempSaceIconPath 拍照临时保存的图片位置
     */
    @Override
    public void handleCropError(Intent intent, String takeTempSaceIconPath) {
        deleteTempPhotoFile(takeTempSaceIconPath);
        if (intent != null) {
            if (UCrop.getError(intent) != null) {
                this.mUserDetailView.error(ConfigStateCode.STATE_CLIPPING_FAILURE,ConfigStateCode.STATE_CLIPPING_FAILURE_VALUE);
            } else {
                this.mUserDetailView.error(ConfigStateCode.STATE_MODIFY_AVATAR_FAILED,ConfigStateCode.STATE_MODIFY_AVATAR_FAILED_VALUE
                );
            }
        } else {
            this.mUserDetailView.error(ConfigStateCode.STATE_MODIFY_AVATAR_FAILED,ConfigStateCode.STATE_MODIFY_AVATAR_FAILED_VALUE);
        }
    }

    /**
     * 裁剪成功
     * @param intent 裁剪成功的itent
     * @param takeTempSaceIconPath 拍照临时保存的图片位置
     */
    @Override
    public void handleCropResult(Intent intent, String takeTempSaceIconPath,User mcbUser) {
        deleteTempPhotoFile(takeTempSaceIconPath);
        if (intent != null) {
            Uri uri = UCrop.getOutput(intent);
            if (uri != null){
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(UIUtils.getContext().getContentResolver(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadUserIcon(bitmap,mcbUser);

            }else {
                this.mUserDetailView.error(ConfigStateCode.STATE_CLIPPING_FAILURE,ConfigStateCode.STATE_CLIPPING_FAILURE_VALUE);
            }
        } else {
            this.mUserDetailView.error(ConfigStateCode.STATE_MODIFY_AVATAR_FAILED,ConfigStateCode.STATE_MODIFY_AVATAR_FAILED_VALUE);
        }
    }
    /**
     * 初始化用户信息
     */
    @Override
    public void initUserInfo() {
        String json = SaveConfigUserUtil.getString(UIUtils.getContext(), ConfigUser.USER_SAVE, null);
        User user = mGson.fromJson(json, User.class);
        logicOperateUser.obtainUser(user.getAccount());

    }

    @Override
    public void updateBindQQ(final Tencent tencent, final  String account) {
        new UserInfo(UIUtils.getContext(), tencent.getQQToken()).getUserInfo(new IUiListener() {
            public void onCancel() {
                tencent.logout(UIUtils.getContext());
            }

            public void onComplete(Object obj) {
                QQUserInfo qqUserInfo = mGson.fromJson(obj.toString(), QQUserInfo.class);
                tencent.logout(UIUtils.getContext());
                logicOperateUser.updateUser(
                        account,
                        null,
                        null,
                        null,
                        null,
                        null,
                        qqUserInfo,
                        null,
                        ConfigUser.USER_UPDATE_BIND_QQ
                );
            }

            public void onError(UiError uiError) {
                tencent.logout(UIUtils.getContext());
            }
        });
    }
    @Override
    public  void unBindQQ(String account)
    {
        logicOperateUser.updateUser(
                account,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                ConfigUser.USER_UPDATE_UNBIND_QQ);
    }

    @Override
    public void updateBindWeibo(String accessToken, String uid,final String account) {
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
                        logicOperateUser.updateUser(
                                account,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                weiboUserInfo,
                                ConfigUser.USER_UPDATE_BIND_WEIBO);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mUserDetailView.error(ConfigStateCode.STATE_ERROE,ConfigStateCode.STATE_ERROE_VALUE);
                        ConfigStateCodeUtil.error(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void unBindWeibo(String account) {
        logicOperateUser.updateUser(
                account,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                ConfigUser.USER_UPDATE_UNBIND_WEIBO);
    }

    /**
     * 删除图片临时保存的图片
     * @param takeTempSaceIconPath 临时保存的图片地址
     */

    public void deleteTempPhotoFile(String takeTempSaceIconPath) {
        File file = new File(takeTempSaceIconPath);
        if ((file.exists()) && (file.isFile())) {
            file.delete();
        }
    }




    /**
     * 上传图片
     * @param userIconBitmap 上传的图片
     */
    public void uploadUserIcon(final Bitmap userIconBitmap,User mcbUser) {
        String account = mcbUser.getAccount();
        final Uri localUri = ConfigUser.SAVE_USER_ICON_NETWORK_URI(account, ".jpg");
        if (NetworkState.networkConnected(UIUtils.getContext())) {
             File userIconFile = new File(ConfigUser.SAVE_USER_ICON_URI(account, ".jpg").getPath());
            ProgressRequestBody requestBody = new ProgressRequestBody(RequestBody.create(MediaType.parse("multipart/form-data"), userIconFile)
                    , new ApiListenerManager.onUploadImageFileProgressListener() {
                public void onProgress(long bytesWritten, long contentLength) {
                    UserDetailBizImpl.this.mUserDetailView.uploadProgress(bytesWritten, contentLength);
                }
            });
            String encodeAccount = MD5Util.encode(account);
            MultipartBody.Part formData = MultipartBody.Part.createFormData("photos.", encodeAccount + ".jpg", requestBody);
            this.mUserApi.uploadUserIcon(formData, account).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Result<List<User>>>() {
                public void onComplete() {
                }

                public void onError(Throwable throwable) {
                    mUserDetailView.error(ConfigStateCode.STATE_ERROE,ConfigStateCode.STATE_ERROE_VALUE);
                    ConfigStateCodeUtil.error(throwable);
                    UserDetailBizImpl.this.deleteTempPhotoFile(localUri.getPath());
                }

                public void onNext(Result<List<User>> result) {
                    switch (result.getCode())
                    {
                        case ConfigStateCode.RESULT_UPLOAD_FAILER:
                            UserDetailBizImpl.this.deleteTempPhotoFile(localUri.getPath());
                            mUserDetailView.error(ConfigStateCode.STATE_UPLOAD_AVATAR_FAILED,ConfigStateCode.STATE_UPLOAD_AVATAR_FAILED_VALUE);
                            break;
                        case ConfigStateCode.RESULT_UPLOAD_SUCCESS:
                            RxBus.getDefault().post(ConfigUser.USER_RX_ICON,userIconBitmap);
                            break;
                    }
                }

                public void onSubscribe(Disposable paramAnonymousDisposable) {
                }
            });
        }else {
            deleteTempPhotoFile(localUri.getPath());
            this.mUserDetailView.error(ConfigStateCode.STATE_UPLOAD_AVATAR_FAILED,ConfigStateCode.STATE_UPLOAD_AVATAR_FAILED_VALUE);
        }
    }


}
