package com.we.mvp.user.detail;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigSdk;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.config.PermissionS;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.LoadingDialog;
import com.baselibrary.view.ProgressDialog;
import com.baselibrary.view.SelectPictureDialog;
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
import com.we.config.ConfigUser;
import com.we.mvp.user.detail.presence.UserDetailPresence;
import com.we.mvp.user.detail.view.UserDetailView;
import com.we.mvp.user.login.modle.QQLoginInfo;
import com.we.mvp.user.modle.User;
import com.we.mvp.user.operate.OperateUserActivity;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCrop.Options;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class UserDetailActivity extends BaseActivity implements UserDetailView, SelectPictureDialog.OnDialogClickListener {
    @BindView(R2.id.civ_user_icon)
    CircleImageView civ_user_icon;
    @BindView(R2.id.ll_user_account_and_safe)
    LinearLayout ll_user_account_and_safe;
    @BindView(R2.id.ll_user_detail)
    LinearLayout ll_user_detail;
    @BindView(R2.id.sv_user_detail)
    ScrollView sv_user_detail;
    @BindView(R2.id.tv_user_account)
    TextView tv_user_account;
    @BindView(R2.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R2.id.tv_user_telephone)
    TextView tv_user_telephone;
    @BindView(R2.id.tv_user_weibo)
    TextView tv_user_weibo;
    @BindView(R2.id.tv_user_qq)
    TextView tv_user_qq;
    @BindView(R2.id.tv_user_login_password)
    TextView tv_user_login_password;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    private User mUser;
    private UserDetailPresence mPresence;
    private Uri mSaveUserIconPath;
    private Uri mTakeTempSaveIconUri;
    private LoadingDialog mDialog;
    private Gson mGson;
    private Tencent mTencent;
    private loginListener iUiListener;
    private SsoHandler mSsoHandler;
    private StatusLayoutManager mStatusLayoutManager;
    private ProgressDialog mProgressDialog;

    @OnClick(R2.id.rl_user_name)
    void updateUserName()
    {
        Intent intent=new Intent();
        intent.putExtra(ConfigUser.USER_ACCOUNT, mUser.getAccount());
        intent.setClass(this,UpdateNameActivity.class);
        startActivity(intent);
    }
    @OnClick(R2.id.rl_user_login_password)
    void updateUserPassword()
    {
        String trim = tv_user_login_password.getText().toString().trim();
        if(trim!=null)
        {
            if(trim.equals("修改"))
            {
                AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                dialog.setTitle(UIUtils.getString(R.string.password_update_type));
                dialog.setItems(UIUtils.getStringS(R.array.user_update_password_type),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent();
                                if (!TextUtils.isEmpty(mUser.getAccount())) {
                                    switch (which) {
                                        case 0:
                                            intent.putExtra(ConfigUser.USER_ACCOUNT, mUser.getAccount());
                                            intent.putExtra(ConfigUser.USER_UPDATE_TYPE, ConfigUser.USER_UPDATE_PWD_PASSWORD);
                                            intent.setClass(UserDetailActivity.this, UpdatePasswordActivity.class);
                                            startActivity(intent);
                                            break;
                                        case 1:
                                            if (mUser.getPhone() != null) {
                                                intent.putExtra(ConfigUser.USER_ACCOUNT, mUser.getAccount());
                                                intent.putExtra(ConfigUser.USER_TELEPHONE, mUser.getPhone());
                                                intent.putExtra(ConfigUser.USER_UPDATE_TYPE, ConfigUser.USER_UPDATE_TELEPHONE_PASSWORD);
                                                intent.setClass(UserDetailActivity.this, UpdatePasswordActivity.class);
                                                startActivity(intent);
                                            } else {
                                                ToastUtils.makeShowToast(UserDetailActivity.this, "你还没有绑定手机");
                                            }
                                            break;
                                    }
                                }else
                                {
                                    ToastUtils.makeShowToast(UIUtils.getContext(),"你不能修改");
                                }

                            }
                        });
                dialog.show();
            }
            if(trim.equals("未设置"))
            {
                Intent intent=new Intent();
                intent.setClass(this,UpdatePasswordActivity.class);
                intent.putExtra(ConfigUser.USER_UPDATE_TYPE, ConfigUser.USER_UPDATE_ADD_PASSWORD);
                intent.putExtra(ConfigUser.USER_ACCOUNT, mUser.getAccount());
                startActivity(intent);
            }
        }

    }


    @OnClick(R2.id.rl_user_telephone)
    void updateUserTelephone()
    {
        if (!TextUtils.isEmpty(mUser.getAccount())) {
            if (tv_user_telephone.isEnabled()) {
                Intent intent = new Intent();
                intent.putExtra(ConfigUser.USER_UPDATE_TYPE, ConfigUser.USER_UPDATE_TELEPHONE);
                intent.putExtra(ConfigUser.USER_ACCOUNT, mUser.getAccount());
                intent.putExtra(ConfigUser.USER_TELEPHONE, mUser.getPhone());
                intent.setClass(this, OperateUserActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtra(ConfigUser.USER_ACCOUNT, mUser.getAccount());
                intent.setClass(this, OperateUserActivity.class);
                startActivity(intent);
            }
        }else
        {
            ToastUtils.makeShowToast(this,"你不能修改");
        }
    }

    @OnClick(R2.id.rl_user_qq)
    void updateUserBindQQ()
    {
        if(tv_user_qq.isEnabled())
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setTitle(UIUtils.getString(R.string.unbind));
            dialog.setMessage(UIUtils.getString(R.string.unbind_QQ));
            dialog.setNegativeButton(UIUtils.getString(R.string.cancle), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setPositiveButton(UIUtils.getString(R.string.unbind), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.show();
                    mPresence.unBindQQ(mUser.getAccount());
                }
            });
            dialog.show();
        }else {
            mDialog.show();
            iUiListener = new loginListener();
            mTencent = Tencent.createInstance(ConfigSdk.TENCENT_APP_KEY, UIUtils.getContext());
                this.mTencent.login(this, "all", this.iUiListener);
        }
    }
    @OnClick(R2.id.rl_user_weibo)
    void updateUserBindWeibo()
    {
        if(tv_user_weibo.isEnabled())
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(this);
            dialog.setTitle(UIUtils.getString(R.string.unbind));
            dialog.setMessage(UIUtils.getString(R.string.unbind_Weibo));
            dialog.setNegativeButton(UIUtils.getString(R.string.cancle), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setPositiveButton(UIUtils.getString(R.string.unbind), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.show();
                    mPresence.unBindWeibo(mUser.getAccount());
                }
            });
            dialog.show();
        }else {
            mDialog.show();
            mSsoHandler.authorize(new AuthListener());
        }
    }
    @Override
    public int getLayoutResId() {
        return R.layout.activity_user_detail;
    }

    @Override
    public void initData() {
        mGson = new Gson();
        mSsoHandler = new SsoHandler(this);

        this.mPresence = new UserDetailPresence(this);
        if (this.mUser == null) {
            this.ll_user_account_and_safe.setEnabled(true);
        }
        RxBus.getDefault().toObservable(ConfigUser.USER_RX_SEND, User.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        resetUI(user);
                    }
                });
        RxBus.getDefault().toObservable(ConfigUser.USER_RX_ICON, Bitmap.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap userIconBitmap) throws Exception {
                        if (userIconBitmap != null) {
                            civ_user_icon.setImageBitmap(userIconBitmap);
                        }
                    }
                });
        mPresence.initUserInfo();
    }



    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        mDialog =new LoadingDialog(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(UIUtils.getString(R.string.upload_loading));
        mDialog.setTitle(UIUtils.getString(R.string.updateloading));
        mStatusLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_receive_address_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        mPresence.initUserInfo();
                    }
                })
                .build();
        ll_user_detail.addView(mStatusLayoutManager.getRootLayout(),ll_user_detail.getChildCount()-1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConfigValues.VALUE_FOLLOW_PHOTO_OBTAIN_PICTURE:
                if (data != null) {
                    startCropActivity(data.getData());
                }
                break;
            case ConfigValues.VALUE_FOLLOW_TAKE_OBTAIN_PICTURE:
                startCropActivity(mTakeTempSaveIconUri);
                break;
            case UCrop.REQUEST_CROP:
                mPresence.handleCropResult(data, mTakeTempSaveIconUri.getPath(), mUser);
                break;
            case UCrop.RESULT_ERROR:
                mPresence.handleCropError(data, mTakeTempSaveIconUri.getPath());
                break;
        }
        Tencent.onActivityResultData(requestCode, resultCode, data, this.iUiListener);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    /**
     * 点击头像弹出dialog
     */
    @OnClick({R2.id.civ_user_icon})
    void showDialog() {
        SelectPictureDialog localSelectPictureDialog = new SelectPictureDialog(this);
        localSelectPictureDialog.setOnDialogClickListener(this);
        localSelectPictureDialog.show();
    }

    @Override
    public void dialogClick(View view, int viewid) {
        if (AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            if (viewid == R.id.tv_take) {
                showTake();

            } else if (viewid == R.id.tv_photo) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, ConfigValues.VALUE_FOLLOW_PHOTO_OBTAIN_PICTURE);

            }
        } else {
            AndPermission.with(this).requestCode(PermissionS.CAMERA)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .send();
        }
    }

    /**
     * 打开相机
     */
    private void showTake() {
        Intent localIntent = new Intent();
        localIntent.setAction("android.media.action.IMAGE_CAPTURE");
        localIntent.putExtra("output", mTakeTempSaveIconUri);
        startActivityForResult(localIntent, ConfigValues.VALUE_FOLLOW_TAKE_OBTAIN_PICTURE);
    }
    /**
     * 开始裁剪图片
     * @param uri 图片的uri地址
     */
    public void startCropActivity(Uri uri) {
        String account = this.mUser.getAccount();
        Options options = new UCrop.Options();
        if (NetworkState.networkConnected(this)) {
            mSaveUserIconPath = ConfigUser.SAVE_USER_ICON_URI(account, ".jpg");
        } else {
            mSaveUserIconPath = ConfigUser.SAVE_USER_ICON_NETWORK_URI(account, ".jpg");
        }
        options.setHideBottomControls(true);
        options.setAllowedGestures(1, 2, 3);
        options.setCircleDimmedLayer(true);
        options.setToolbarColor(UIUtils.getColor(R.color.blue));
        options.setToolbarTitle("裁剪头像");
        options.setStatusBarColor(UIUtils.getColor(R.color.blue));
        options.setShowCropFrame(false);
        options.setCompressionQuality(100);
        options.setShowCropGrid(false);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        UCrop.of(uri, mSaveUserIconPath)
                .withOptions(options)
                .withAspectRatio(1.0F, 1.0F)
                .withMaxResultSize(256, 256)
                .start(this);
    }

    /**
     * 申请权限失败
     * @param paramList 权限的集合
     */
    @PermissionNo(PermissionS.CAMERA)
    private void getLocationNo(List<String> paramList) {
        if (AndPermission.hasAlwaysDeniedPermission(this, paramList)) {
            AndPermission.defaultSettingDialog(this, PermissionS.CAMERA).show();
        }
    }

    /**
     * 申请权限成功
     * @param paramList 权限的集合
     */
    @PermissionYes(PermissionS.CAMERA)
    private void getLocationYes(List<String> paramList) {
        for (String permission : paramList) {
            switch (permission) {
                case Manifest.permission.CAMERA:
                    showTake();
                    break;
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt) {
        AndPermission.onRequestPermissionsResult(this, paramInt, paramArrayOfString, paramArrayOfInt);
    }



    /**
     * 错误码
     * @param errorCode 错误的id
     */
    @Override
    public void error(int errorCode,String message) {
        mDialog.dismiss();

        switch (errorCode) {
            case ConfigStateCode.RESULT_OBTION_USER_FAILER:
                mStatusLayoutManager.showContent();
                break;
            case ConfigStateCode.STATE_ERROE:
                mStatusLayoutManager.showError();
                mDialog.dismiss();
                break;
            case ConfigStateCode.STATE_NO_NETWORK:
                ToastUtils.makeShowToast(this, message);
                mStatusLayoutManager.showNetWorkError();
                break;
            default:
                if (errorCode!= ConfigStateCode.STAT_OTHER) {
                    ToastUtils.makeShowToast(UIUtils.getContext(), message);
                }
        }

    }

    @Override
    public void loading() {
        View rootLayout = mStatusLayoutManager.getRootLayout();
        if (rootLayout.getVisibility()==View.GONE) {
            mDialog.show();
        }else {
            mStatusLayoutManager.showLoading();
        }
    }

    @Override
    public void success(List<User> data) {

        View rootLayout = mStatusLayoutManager.getRootLayout();
        if (rootLayout.getVisibility()==View.GONE) {
            ToastUtils.makeShowToast(UIUtils.getContext(), "成功");
            mDialog.dismiss();
        }else
        {
            mStatusLayoutManager.showContent();
        }
    }

    /**
     * 上传的进度
     * @param bytesWritten 每次上传的大小
     * @param contentLength 上传的总大小
     */
    @Override
    public void uploadProgress(final long bytesWritten,final long contentLength) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float penccent=(float) bytesWritten/contentLength;
                int progress =(int) (penccent*100);
                if(!mProgressDialog.isShowing())
                {
                    mProgressDialog.show();
                }
                if (progress==100)
                {
                    mProgressDialog.dismiss();
                }
                mProgressDialog.setProgress(progress);
            }
        });

    }


    public void resetUI(User user)
    {
        Logger.e("TAG","我是只被调用一次吗？"+user.toString());
        if(user==null|| TextUtils.isEmpty(user.getAccount()))
        {
            this.mUser = null;
            tv_user_name.setText(UIUtils.getString(R.string.user_name));
            tv_user_account.setText(UIUtils.getString(R.string.user_telephone));
            civ_user_icon.setImageResource(R.drawable.lufei);
        }else
        {
            mUser = user;
            mTakeTempSaveIconUri = ConfigUser.SAVE_USER_ICON_TEMP_URI(mUser.getAccount(), ".jpg");
            tv_user_name.setText(user.getName());
            tv_user_account.setText(user.getAccount());
            if(user.getPhone()==null)
            {
                tv_user_telephone.setText("未绑定");
                tv_user_telephone.setEnabled(false);
            }else
            {
                tv_user_telephone.setText(user.getPhone()+"");
                tv_user_telephone.setEnabled(true);
            }
            if(user.getAbqid()==0)
            {
                tv_user_qq.setText("未绑定");
                tv_user_qq.setEnabled(false);
            }else
            {
                tv_user_qq.setText("已绑定");
                tv_user_qq.setEnabled(true);
            }
            if(user.getAbwid()==0)
            {
                tv_user_weibo.setText("未绑定");
                tv_user_weibo.setEnabled(false);
            }else
            {
                tv_user_weibo.setText("已绑定");
                tv_user_weibo.setEnabled(true);
            }
            if(!TextUtils.isEmpty(user.getPassword()))
            {
                tv_user_login_password.setText("修改");
            }else
            {
                tv_user_login_password.setText("未设置");
            }

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
            mTencent.setOpenId(qqLoginInfo.getOpenid());
            mTencent.setAccessToken(qqLoginInfo.getAccess_token(), qqLoginInfo.getExpires_in());
            mPresence.updateBindQQ(mTencent, mUser.getAccount());
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
                mPresence.updateBindWeibo(oauth2AccessToken.getToken(), oauth2AccessToken.getUid(), mUser.getAccount());
                AccessTokenKeeper.writeAccessToken(UserDetailActivity.this, oauth2AccessToken); // 保存Token
            }
        }

        @Override
        public void cancel() {
        mDialog.dismiss();
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            ToastUtils.makeShowToast(UIUtils.getContext(),wbConnectErrorMessage.getErrorMessage());
        }
    }
}
