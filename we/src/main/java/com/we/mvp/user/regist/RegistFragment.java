package com.we.mvp.user.regist;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.ClearEditText;
import com.baselibrary.view.LoadingDialog;
import com.we.R;
import com.we.R2;
import com.we.mvp.user.UserActivity;
import com.we.mvp.user.modle.User;
import com.we.mvp.user.regist.presence.UserRegistPreence;
import com.we.mvp.user.regist.view.RegistView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RegistFragment
        extends BaseFragmnet
        implements RegistView {

    @BindView(R2.id.btn_regist)
    Button btn_regist;
    @BindView(R2.id.cb_password_visible_state)
    CheckBox cb_password_visible_state;
    @BindView(R2.id.civ_user_icon)
    CircleImageView civ_user_icon;
    @BindView(R2.id.et_user_code)
    ClearEditText et_user_code;
    @BindView(R2.id.et_user_password)
    ClearEditText et_user_password;
    @BindView(R2.id.et_user_telephone)
    ClearEditText et_user_telephone;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.tv_obtain_code)
    TextView tv_obtain_code;
    @BindView(R2.id.ll_voice_content)
    LinearLayout ll_voice_content;
    private UserRegistPreence mPreence;
    private LoadingDialog mDialog;

    private void coundDown() {
        Observable.interval(0L, 1L, TimeUnit.SECONDS).take(ConfigValues.VALUE_SMS_COUNTDOWN+1)
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
                RegistFragment.this.setCodeEnabled(false);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            public void onComplete() {
                RegistFragment.this.tv_obtain_code.setText("获取验证码");
                setCodeEnabled(true);
                ll_voice_content.setVisibility(View.VISIBLE);
            }

            public void onError(Throwable paramAnonymousThrowable) {
                RegistFragment.this.setCodeEnabled(true);
            }

            public void onNext(Integer paramAnonymousInteger) {
                RegistFragment.this.tv_obtain_code.setText("剩余" + paramAnonymousInteger + "s");
            }

            public void onSubscribe(Disposable paramAnonymousDisposable) {
            }
        });
    }
    @OnClick({R2.id.btn_regist})
    void regist() {
        if (this.mPreence.confirmation()) {
            this.mPreence.submitVerificationCode();
        }
    }

    @OnClick({R2.id.tv_obtain_code})
    void sendSmsCode() {
        String telephone = this.et_user_telephone.getText().toString().trim();
        if (NetworkState.networkConnected(getContext())) {
            this.mPreence.sendSmsCode(telephone);
            coundDown();
        }else {
            ToastUtils.makeShowToast(getContext(),"没有网络");
        }
    }
    @OnClick(R2.id.tv_voice_code)
    void sendVoiceCode()
    {
        String telephone = getUserTelephone();
        if(telephone!=null) {
            et_user_code.setVisibility(View.VISIBLE);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
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
        return R.layout.activity_user_regist;
    }

    @Override
    public void initData() {
        this.mPreence = new UserRegistPreence(this);
        this.mPreence.initMobSms();
    }
    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity activity = (UserActivity) getActivity();
                activity.onBackPressed();
            }
        });
        this.cb_password_visible_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_user_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    et_user_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        this.et_user_telephone.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String phone = RegistFragment.this.et_user_telephone.getText().toString().trim();
                String telephone = phone.replace(" ","");
                if (!tv_obtain_code.getText().toString().contains("s")) {
                    if ((telephone.startsWith("1")) && (telephone.length() == 11)) {
                        tv_obtain_code.setEnabled(true);
                    } else {
                        RegistFragment.this.tv_obtain_code.setEnabled(false);
                    }
                }
            }
        });
    }
    @Override
    public void initView() {
        ((UserActivity) getActivity()).setSupportActionBar(this.toolbar);
        this.tv_obtain_code.setEnabled(false);
        mDialog = new LoadingDialog(getContext());
        mDialog.setTitle(R.string.user_regist_loading);
    }

    @Override
    public void onResume() {
        if(mPreence!=null) {
            mPreence.initMobSms();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if(mPreence!=null) {
            mPreence.destroyMobSms();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(mPreence!=null) {
                mPreence.destroyMobSms();
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.login_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int i = menuItem.getItemId();
        if (i == R.id.menu_login_user) {
            ((UserActivity) getActivity()).vp_user.setCurrentItem(0);

        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void loading() {
        mDialog.show();
    }

    @Override
    public void success(List<User> data) {
        mDialog.dismiss();
        ToastUtils.makeShowToast(UIUtils.getContext(), "登录成功");
        getActivity().onBackPressed();
    }


    @Override
    public void setCodeEnabled(boolean isVisable) {
        this.tv_obtain_code.setClickable(isVisable);
        this.tv_obtain_code.setFocusable(isVisable);
        this.tv_obtain_code.setEnabled(isVisable);
        this.tv_obtain_code.setFocusableInTouchMode(isVisable);
    }

    @Override
    public String getCountDown() {
        return this.tv_obtain_code.getText().toString().trim();
    }
    @Override
    public String getPassword() {
        String password = this.et_user_password.getText().toString().trim();
        if ((TextUtils.isEmpty(password)) || (password == "")) {
            ToastUtils.makeShowToast(getContext(), "你的密码不能为空");
            return null;
        }
        if (password.length() < 6) {
            ToastUtils.makeShowToast(getContext(), "你的密码不能小于6位");
            return null;
        }
        return password;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!getUserVisibleHint()) {

        }
    }

    @Override
    public String getUserCode() {
        String code = this.et_user_code.getText().toString().trim();
        String password = this.et_user_password.getText().toString().trim();
        if ((TextUtils.isEmpty(code)) && (TextUtils.isEmpty(password))) {
            ToastUtils.makeShowToast(getContext(), "你的验证码密码不能为空");
            return  null;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.makeShowToast(getContext(), "你的验证码不能为空");
            return  null;
        }
        return code;
    }
    @Override
    public String getUserTelephone() {
        String telephone = this.et_user_telephone.getText().toString().trim();
        String code = this.et_user_code.getText().toString().trim();
        String password = this.et_user_password.getText().toString().trim();
        String phone = telephone.replace(" ", "");
        if ((TextUtils.isEmpty(code)) && (TextUtils.isEmpty(phone)) && (TextUtils.isEmpty(password))) {
            ToastUtils.makeShowToast(getContext(), "你的电话号码验证码密码不能为空");
            return null;
        }
        if ((TextUtils.isEmpty(phone)) || (telephone == "")) {
            ToastUtils.makeShowToast(getContext(), "你的电话号码不能为空");
            return null;
        }
        if (!phone.startsWith("1")) {
            ToastUtils.makeShowToast(getContext(), "电话号码规则错误");
            return null;
        }
        if (phone.length()!=11) {
            ToastUtils.makeShowToast(getContext(), "电话号码规则错误");
            return null;
        }
        return phone;
    }
    @Override
    public void isBrainConfirmation(boolean intelligence) {
        ToastUtils.makeShowToast(UIUtils.getContext(), "发送成功");
    }

    @Override
    public void error(int errorCode,String message) {
        mDialog.dismiss();
        if (errorCode!= ConfigStateCode.STAT_OTHER) {
            ToastUtils.makeShowToast(getContext(), message);
        }
    }
}
