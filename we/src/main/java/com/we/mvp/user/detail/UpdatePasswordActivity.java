package com.we.mvp.user.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.we.mvp.user.detail.presence.UpdatePasswordPresence;
import com.we.mvp.user.detail.view.UpdatePasswordView;
import com.we.mvp.user.modle.User;

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

/**
 * Created by yangyupan on 2017/4/15.
 */

public class UpdatePasswordActivity extends BaseActivity implements UpdatePasswordView {

    @BindView(R2.id.tv_send_telephone)
    TextView tv_send_telephone;
    @BindView(R2.id.et_user_password)
    ClearEditText et_user_password;
    @BindView(R2.id.et_user_new_password)
    ClearEditText et_user_new_password;
    @BindView(R2.id.et_user_code)
    ClearEditText et_user_code;
    @BindView(R2.id.cb_password_visible_state)
    CheckBox cb_password_visible_state;
    @BindView(R2.id.cb_new_password_visible_state)
    CheckBox cb_new_password_visible_state;
    @BindView(R2.id.tv_obtain_code)
    TextView tv_obtain_code;

    @BindView(R2.id.ll_user_update_pwd_password)
    LinearLayout ll_user_update_pwd_password;
    @BindView(R2.id.ll_user_update_telephone_password)
    LinearLayout ll_user_update_telephone_password;
    @BindView(R2.id.ll_voice_content)
    LinearLayout ll_voice_content;

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    private String mTelephone;
    private UpdatePasswordPresence mPreence;
    private LoadingDialog mDialog;
    private String mAccount;
    private int mUpdateType;

    @OnClick(R2.id.btn_update_user_password)
    void updatePassword() {
        if (mPreence.confirmation(mAccount,mUpdateType)) {
            mPreence.updatePassword(mUpdateType);
        }
    }

    @OnClick(R2.id.tv_obtain_code)
    void sendCode() {
        if (NetworkState.networkConnected(this)) {
            mPreence.sendSmsCode(mTelephone);
            coundDown();
        } else {
            ToastUtils.makeShowToast(this, "没有网络");
        }
    }

    @OnClick(R2.id.tv_voice_code)
    void sendVoiceCode() {
        String telephone = getTelephone();
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
    public void initData() {
        mPreence = new UpdatePasswordPresence(this);
        Intent intent = getIntent();
        mUpdateType = (int) intent.getIntExtra(ConfigUser.USER_UPDATE_TYPE, -1);
        mAccount = intent.getStringExtra(ConfigUser.USER_ACCOUNT);

        mPreence.initMobSms(mAccount, mUpdateType);
        switch (mUpdateType) {
            case ConfigUser.USER_UPDATE_PWD_PASSWORD:
                ll_user_update_pwd_password.setVisibility(View.VISIBLE);
                break;
            case ConfigUser.USER_UPDATE_TELEPHONE_PASSWORD:
                ll_user_update_telephone_password.setVisibility(View.VISIBLE);
                mTelephone = String.valueOf(intent.getLongExtra(ConfigUser.USER_TELEPHONE, -1));
                tv_send_telephone.setText("验证码已发送至手机" + mTelephone);
                tv_send_telephone.setVisibility(View.VISIBLE);
                sendCode();

                break;
            case ConfigUser.USER_UPDATE_ADD_PASSWORD:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        if (mPreence != null) {
            mPreence.destroyMobSms();
        }
        super.onDestroy();
    }

    @Override
    public void setCodeEnabled(boolean isVisable) {
        tv_obtain_code.setClickable(isVisable);
        tv_obtain_code.setFocusable(isVisable);
        tv_obtain_code.setEnabled(isVisable);
        tv_obtain_code.setFocusableInTouchMode(isVisable);
    }

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
                }).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    public void accept(@NonNull Disposable disposable)
                            throws Exception {
                        setCodeEnabled(false);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            public void onComplete() {
                tv_obtain_code.setText("获取验证码");
                setCodeEnabled(true);
                if (mTelephone == null) {
                    tv_obtain_code.setEnabled(false);
                }
            }

            public void onError(Throwable throwable) {
                setCodeEnabled(true);
            }

            public void onNext(Integer paramAnonymousInteger) {

                tv_obtain_code.setText("剩余" + paramAnonymousInteger + "s");
            }

            public void onSubscribe(Disposable paramAnonymousDisposable) {
            }
        });
    }


    @Override
    public int getLayoutResId() {
        return R.layout.user_update_password;
    }

    @Override
    public void initView() {
        toolbar.setTitle("设置密码");
        setSupportActionBar(toolbar);
        mDialog = new LoadingDialog(this);
        mDialog.setTitle(UIUtils.getString(R.string.updateloading));

    }


    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cb_password_visible_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_user_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    et_user_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        cb_new_password_visible_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_user_new_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    et_user_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.menu_skip) {
            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getCountDown() {
        return tv_obtain_code.getText().toString().trim();
    }

    @Override
    public String getNewPassWord() {

        String newPassWord = et_user_new_password.getText().toString().trim();
        if ((TextUtils.isEmpty(newPassWord)) || (newPassWord == "")) {
            ToastUtils.makeShowToast(this, "你的新密码不能为空");
            return null;
        }
        if (newPassWord.length() < 6) {
            ToastUtils.makeShowToast(this, "你的新密码不能小于6位");
            return null;
        }
        return newPassWord;
    }

    @Override
    public String getPassWord() {

        String password = this.et_user_password.getText().toString().trim();
        if ((TextUtils.isEmpty(password)) || (password == "")) {
            ToastUtils.makeShowToast(this, "你的旧密码不能为空");
            return null;
        }
        if (password.length() < 6) {
            ToastUtils.makeShowToast(this, "你的旧密码不能小于6位");
            return null;
        }
        return password;
    }

    @Override
    public String getTelephone() {
        return mTelephone;
    }

    @Override
    public String getUserCode() {
        String code = et_user_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.makeShowToast(this, "验证码不能为空");
            return null;
        }
        return code;
    }

    @Override
    public void loading() {
        mDialog.show();
    }

    @Override
    public void success(List<User> data) {
        mDialog.dismiss();
        ToastUtils.makeShowToast(UIUtils.getContext(), "修改成功");
        onBackPressed();
    }


    @Override
    public void error(int errorCode,String message) {
        mDialog.dismiss();
        if (errorCode!= ConfigStateCode.STAT_OTHER) {
            ToastUtils.makeShowToast(UIUtils.getContext(), message);
        }
    }

    @Override
    public void isBrainConfirmation(boolean isVisable) {
        ToastUtils.makeShowToast(UIUtils.getContext(), "发送成功");
    }
}
