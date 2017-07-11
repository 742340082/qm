package com.we.mvp.user.operate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.ClearEditText;
import com.baselibrary.view.LoadingDialog;
import com.we.R;
import com.we.R2;
import com.we.config.ConfigUser;
import com.we.mvp.user.login.modle.QQUserInfo;
import com.we.mvp.user.login.modle.WeiboUserInfo;
import com.we.mvp.user.modle.User;
import com.we.mvp.user.operate.presence.OperateUserPreence;
import com.we.mvp.user.operate.view.OperateUserView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class OperateUserActivity
        extends BaseActivity
        implements OperateUserView {

    @BindView(R2.id.btn_regist)
    Button btn_regist;
    @BindView(R2.id.et_user_code)
    ClearEditText et_user_code;
    @BindView(R2.id.et_user_telephone)
    ClearEditText et_user_telephone;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.tv_obtain_code)
    TextView tv_obtain_code;
    @BindView(R2.id.ll_voice_content)
    LinearLayout ll_voice_content;
    private OperateUserPreence mPreence;
    private LoadingDialog mDialog;
    private int mLoginType;
    private int mRegistType;
    private int mUpdateType;
    private QQUserInfo mQQUserInfo;
    private WeiboUserInfo mWeiboUserInfo;
    private String mAccount;
    private boolean isBindTelephone;
    private String mTelephone;

    private void coundDown() {
        Observable.interval(0L, 1L, TimeUnit.SECONDS).take(ConfigValues.VALUE_SMS_COUNTDOWN + 1)
                .observeOn(Schedulers.io())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        long l = aLong.longValue();
                        aLong.longValue();
                        return Integer.valueOf(Long.valueOf(ConfigValues.VALUE_SMS_COUNTDOWN - l).intValue());
                    }
                }).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Consumer<Disposable>() {
            public void accept(@NonNull Disposable disposable)
                    throws Exception {
                OperateUserActivity.this.setCodeEnabled(false);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            Disposable mDisposable;

            public void onComplete() {
                tv_obtain_code.setText("获取验证码");
                setCodeEnabled(true);
                ll_voice_content.setVisibility(View.VISIBLE);
            }

            public void onError(Throwable paramAnonymousThrowable) {
                OperateUserActivity.this.setCodeEnabled(true);
            }

            public void onNext(Integer paramAnonymousInteger) {
                if (isBindTelephone) {
                    isBindTelephone = false;
                    mDisposable.dispose();
                } else {
                    OperateUserActivity.this.tv_obtain_code.setText("剩余" + paramAnonymousInteger + "s");
                }
            }

            public void onSubscribe(Disposable disposable) {
                mDisposable = disposable;
            }
        });
    }


    @OnClick({R2.id.btn_regist})
    void regist() {
        if (mLoginType != -1) {
            if (this.mPreence.confirmation(null, null)) {
                mDialog.show();
                mPreence.submitVerificationCode();
            }
        } else if (mRegistType != -1) {
            if (this.mPreence.confirmation(mQQUserInfo, mWeiboUserInfo)) {
                mDialog.show();
                mPreence.submitVerificationCode();
            }
        } else if (mUpdateType != -1) {
            if (this.mPreence.confirmation(null, null)) {
                mDialog.show();
                mPreence.submitVerificationCode();
            }
        }
    }


    @OnClick({R2.id.tv_obtain_code})
    void sendSmsCode() {
        String telephone = this.et_user_telephone.getText().toString().trim();
        if (NetworkState.networkConnected(this)) {
            this.mPreence.sendSmsCode(telephone);
            coundDown();
        } else {
            ToastUtils.makeShowToast(this, "没有网络");
        }
    }

    @OnClick(R2.id.tv_voice_code)
    void sendVoiceCode() {
        String telephone = getUserTelephone();
        if (telephone != null) {
            et_user_code.setVisibility(View.VISIBLE);
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("语音验证码");
            dialog.setMessage("我们将以电话的方式告知你验证码请注意听");
            dialog.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            mPreence.sendVoiceCode(telephone);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_user_code_regist;
    }

    @Override
    public void initData() {
        this.mPreence = new OperateUserPreence(this);
        Intent intent = getIntent();
        mLoginType = intent.getIntExtra(ConfigUser.USER_LOGIN_TYPE, -1);
        mRegistType = intent.getIntExtra(ConfigUser.USER_REGIST_TYPE, -1);
        mUpdateType = intent.getIntExtra(ConfigUser.USER_UPDATE_TYPE, -1);

        mQQUserInfo = (QQUserInfo) intent.getSerializableExtra(ConfigUser.USER_BIND_QQUSERINFO);
        mWeiboUserInfo = (WeiboUserInfo) intent.getSerializableExtra(ConfigUser.USER_BIND_WEIBOUSERINFO);
        mAccount = intent.getStringExtra(ConfigUser.USER_ACCOUNT);
        if (mLoginType != -1) {
            toolbar.setTitle(UIUtils.getString(R.string.user_code_login));
            btn_regist.setText(UIUtils.getString(R.string.user_login));
            mDialog.setTitle(R.string.user_login_loading);
            et_user_telephone.setEnabled(true);
        }
        if (mRegistType != -1) {
            toolbar.setTitle(UIUtils.getString(R.string.user_bind_telephone));
            btn_regist.setText(UIUtils.getString(R.string.user_bind));
            mDialog.setTitle(R.string.user_regist_loading);
            et_user_telephone.setEnabled(true);
        }
        if (mUpdateType != -1) {
            tv_obtain_code.setEnabled(true);
            toolbar.setTitle(UIUtils.getString(R.string.user_witchbind_telephone));
            btn_regist.setText(UIUtils.getString(R.string.user_switch_telephone));
            mDialog.setTitle(R.string.user_config_loading);
            mTelephone = String.valueOf(intent.getLongExtra(ConfigUser.USER_TELEPHONE, -1l));
            et_user_telephone.setText(mTelephone);
        }
        mPreence.initMobSms(mLoginType, mRegistType, mUpdateType, mTelephone);
        if (mUpdateType == -1 && mRegistType == -1 && mLoginType == -1) {
            confirmationJudge();
            isBindTelephone = false;
            return;
        }
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        this.et_user_telephone.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String phone = OperateUserActivity.this.et_user_telephone.getText().toString().trim();
                String telephone = phone.replace(" ", "");
                if (!tv_obtain_code.getText().toString().contains("s")) {
                    if ((telephone.startsWith("1")) && (telephone.length() == 11)) {
                        tv_obtain_code.setEnabled(true);
                    } else {
                        OperateUserActivity.this.tv_obtain_code.setEnabled(false);
                    }
                }
            }
        });
    }


    @Override
    public void initView() {
        setSupportActionBar(this.toolbar);
        this.tv_obtain_code.setEnabled(false);
        mDialog = new LoadingDialog(this);
    }


    @Override
    public void onDestroy() {
        if (mPreence != null) {
            mPreence.destroyMobSms();
        }
        super.onDestroy();
    }

    @Override
    public void loading() {
        mDialog.show();
    }

    @Override
    public void success(List<User> data) {
        mDialog.dismiss();
        if (mLoginType != -1) {
            setResult(ConfigStateCode.RESULT_LOGIN_SUCCESS);
            onBackPressed();
        }else {
            if (mUpdateType != -1) {
            } else if (mRegistType != -1) {
            } else {
                ToastUtils.makeShowToast(UIUtils.getContext(), "绑定成功");
            }
            onBackPressed();
        }
    }

    @Override
    public void confirmationJudge() {
        isBindTelephone = true;
        mRegistType = -1;
        mLoginType = -1;
        mUpdateType = -1;
        et_user_telephone.setEnabled(true);
        et_user_telephone.setText("");
        et_user_code.setText("");

        if (mPreence != null) {
            mPreence.destroyMobSms();
        }

        mPreence.initMobSms(mLoginType, mRegistType, mUpdateType, mTelephone);
        et_user_code.setVisibility(View.VISIBLE);
        et_user_telephone.setHint("请输入新手机号码");
        tv_obtain_code.setText(UIUtils.getString(R.string.get_code));
        tv_obtain_code.setEnabled(false);
        mDialog.dismiss();
        ll_voice_content.setVisibility(View.GONE);
        toolbar.setTitle(UIUtils.getString(R.string.user_bind_telephone));
        btn_regist.setText(UIUtils.getString(R.string.user_bind));
        mDialog.setTitle(R.string.user_bind_loading);
    }


    @Override
    public void setCodeEnabled(boolean paramBoolean) {
        this.tv_obtain_code.setClickable(paramBoolean);
        this.tv_obtain_code.setFocusable(paramBoolean);
        this.tv_obtain_code.setEnabled(paramBoolean);
        this.tv_obtain_code.setFocusableInTouchMode(paramBoolean);
    }


    @Override
    public String getCountDown() {
        return this.tv_obtain_code.getText().toString().trim();
    }

    @Override
    public String getUserCode() {
        String code = this.et_user_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.makeShowToast(this, "你的验证码不能为空");
            return null;
        }
        return code;
    }

    @Override
    public String getAccount() {
        return mAccount;
    }

    @Override
    public String getUserTelephone() {
        String telephone = this.et_user_telephone.getText().toString().trim();
        if ((TextUtils.isEmpty(telephone)) || (telephone == "")) {
            ToastUtils.makeShowToast(this, "你的电话号码不能为空");
            return null;
        }
        if (!telephone.startsWith("1")) {
            ToastUtils.makeShowToast(this, "电话号码规则错误");
            return null;
        }
        if (telephone.length() != 11) {
            ToastUtils.makeShowToast(this, "电话号码规则错误");
            return null;
        }
        return telephone;
    }

    @Override
    public void isBrainConfirmation(boolean intelligence) {
        ToastUtils.makeShowToast(UIUtils.getContext(), "发送成功");
    }

    @Override
    public void error(int errorCode, String message) {
        mDialog.dismiss();
        if (errorCode != ConfigStateCode.STAT_OTHER) {
            ToastUtils.makeShowToast(this, message);
        }


    }
}
