package com.we.mvp.user.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.config.ConfigSdk;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.MD5Util;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.ClearEditText;
import com.baselibrary.view.LoadingDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.we.R;
import com.we.R2;
import com.we.api.UserApi;
import com.we.config.ConfigUser;
import com.we.mvp.user.UserActivity;
import com.we.mvp.user.login.modle.QQLoginInfo;
import com.we.mvp.user.login.modle.QQUserInfo;
import com.we.mvp.user.login.modle.WeiboRrfreshToken;
import com.we.mvp.user.login.modle.WeiboUserInfo;
import com.we.mvp.user.login.presence.UserLoginPresence;
import com.we.mvp.user.login.view.IUserLoginView;
import com.we.mvp.user.modle.User;
import com.we.mvp.user.operate.OperateUserActivity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LoginFragement
        extends BaseFragmnet
        implements IUserLoginView {

    @BindView(R2.id.cb_password_visible_state)
    CheckBox cb_password_visible_state;
    @BindView(R2.id.civ_user_icon)
    CircleImageView civ_user_icon;
    @BindView(R2.id.btn_login)
    Button mBtn_login;
    @BindView(R2.id.et_password)
    ClearEditText mEt_password;
    @BindView(R2.id.et_username)
    ClearEditText mEt_username;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    private loginListener iUiListener;
    private Gson mGson;
    private LoadingDialog mDialog;
    private UserLoginPresence mLoginPresence;
    private Tencent mTencent;
    private SsoHandler mSsoHandler;


    @OnClick({R2.id.btn_login})
    void login() {
        this.mLoginPresence.login();
    }

    @OnClick(R2.id.ll_qq_login)
    void qqLigin() {
        mDialog.show();
        iUiListener = new loginListener();
        mTencent = Tencent.createInstance(ConfigSdk.TENCENT_APP_KEY, UIUtils.getContext());
        this.mTencent.login(this, "all", this.iUiListener);
    }

    @OnClick(R2.id.ll_weibo_login)
    void weiBoLigin() {
        mDialog.show();
        mSsoHandler.authorize(new AuthListener());
    }


    @OnClick(R2.id.tv_telephonecode_login)
    void telephoneLogin() {
        Intent intent = new Intent();
        intent.putExtra(ConfigUser.USER_LOGIN_TYPE, ConfigUser.USER_TELEPHONE_LOGIN);
        intent.setClass(getActivity(), OperateUserActivity.class);
        startActivityForResult(intent, ConfigStateCode.RESULT_LOGIN_SUCCESS);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_user_login;
    }

    @Override
    public void initData() {
        mLoginPresence = new UserLoginPresence(this);
        mGson = new Gson();
        mSsoHandler = new SsoHandler(getActivity());
    }

    @Override
    public void initListener() {
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ((UserActivity) LoginFragement.this.getActivity()).onBackPressed();
            }
        });
        this.cb_password_visible_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LoginFragement.this.mEt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    LoginFragement.this.mEt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        this.mEt_username.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence watcher, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence textWatcher, int start, int before, int count) {
                Observable.just(LoginFragement.this.mEt_username.getText().toString().trim())
                        .debounce(200L, TimeUnit.MILLISECONDS)
                        .switchMap(new Function<String, ObservableSource<String>>() {
                            @Override
                            public ObservableSource<String> apply(String str) throws Exception {
                                return Observable.just(str);
                            }
                        }).subscribe(new Consumer<String>() {
                    public void accept(@NonNull String str) {
                        String encodeString = MD5Util.encode(str);
                        Glide.with(getContext())
                                .load(UserApi.USER_ICON_ROOT_PSTH + str + "/" + encodeString + ".jpg")
                                .asBitmap()
                                .placeholder(R.drawable.lufei)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .error(R.drawable.lufei)
                                .centerCrop()
                                .into(LoginFragement.this.civ_user_icon);
                    }
                });
            }
        });
    }


    @Override
    public void initView() {
        UserActivity localUserActivity = (UserActivity) getActivity();
        localUserActivity.setSupportActionBar(toolbar);
        mDialog = new LoadingDialog(getActivity());
        mDialog.setTitle(R.string.user_login_loading);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.regist_menu, menu);
    }


    @Override
    public void loading() {
        this.mDialog.show();
    }

    @Override
    public void success(List<User> data) {
        this.mDialog.dismiss();
        getActivity().onBackPressed();
    }

    @Override
    public void bindAccountActivity(QQUserInfo qqUserInfo, WeiboUserInfo weiboUserInfo) {
        mDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra(ConfigUser.USER_BIND_QQUSERINFO, qqUserInfo);
        intent.putExtra(ConfigUser.USER_BIND_WEIBOUSERINFO, weiboUserInfo);
        intent.putExtra(ConfigUser.USER_REGIST_TYPE, ConfigUser.USER_TELEPHONE_REGIST);
        intent.setClass(getActivity(), OperateUserActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int i = menuItem.getItemId();
        if (i == R.id.menu_regist_user) {
            ((UserActivity) getActivity()).vp_user.setCurrentItem(1);

        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ConfigStateCode.RESULT_LOGIN_SUCCESS) {
            Executors.newCachedThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().onBackPressed();
                        }
                    });

                }
            });

        } else {
            Tencent.onActivityResultData(requestCode, resultCode, data, this.iUiListener);
        }
    }

    public void WeiboCallBack(int requestCode, int resultCode, Intent data) {

        if (requestCode == 32973) {
            if (mSsoHandler != null) {
                mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public String getPassword() {
        String password = this.mEt_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.makeShowToast(getContext(), "密码不能为空");
            return null;
        }
        if (password.length() < 6) {
            ToastUtils.makeShowToast(getContext(), "你输入的密码不能小于6位");
            return null;
        }
        return password;
    }

    @Override
    public String getUserName() {
        String account = this.mEt_username.getText().toString().trim();
        String password = this.mEt_password.getText().toString().trim();
        if ((TextUtils.isEmpty(account)) && (TextUtils.isEmpty(password))) {
            ToastUtils.makeShowToast(getContext(), "用户名和密码不能为空");
            return null;
        }
        if (TextUtils.isEmpty(account)) {
            ToastUtils.makeShowToast(getContext(), "用户名不能为空");
            return null;
        }
        return account;
    }

    @Override
    public void getWeiboToken(WeiboRrfreshToken weiboRrfreshToken) {
        Oauth2AccessToken oauth2AccessToken = AccessTokenKeeper.readAccessToken(getContext());
        mLoginPresence.weibologin(weiboRrfreshToken.getAccess_token(), oauth2AccessToken.getUid());
    }


    @Override
    public void error(int errorCode, String message) {
        mDialog.dismiss();
        if (errorCode != ConfigStateCode.STAT_OTHER) {
            ToastUtils.makeShowToast(getContext(), message);
        }
    }

    //QQ回调监听
    private class loginListener implements IUiListener {
        private loginListener() {
        }

        public void onCancel() {
            mDialog.dismiss();
        }

        public void onComplete(Object o) {
            QQLoginInfo qqLoginInfo = mGson.fromJson(o.toString(), QQLoginInfo.class);
            Logger.i("TAG", qqLoginInfo.getOpenid());
            mTencent.setOpenId(qqLoginInfo.getOpenid());
            mTencent.setAccessToken(qqLoginInfo.getAccess_token(), qqLoginInfo.getExpires_in());
            mLoginPresence.qqLogin(LoginFragement.this.mTencent);
        }

        public void onError(UiError uiError) {
            Logger.i("TAG", uiError.errorMessage);
        }
    }

    //微博回调监听
    class AuthListener implements WbAuthListener {


        @Override
        public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
            if (oauth2AccessToken.isSessionValid()) {
                mLoginPresence.weibologin(oauth2AccessToken.getToken(), oauth2AccessToken.getUid());
                AccessTokenKeeper.writeAccessToken(getActivity(), oauth2AccessToken); // 保存Token
            }
        }

        @Override
        public void cancel() {
            mDialog.dismiss();
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            ToastUtils.makeShowToast(UIUtils.getContext(), wbConnectErrorMessage.getErrorMessage());
        }
    }
}
