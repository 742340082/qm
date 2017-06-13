package com.we.mvp.user.detail;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;


import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.ClearEditText;
import com.baselibrary.view.LoadingDialog;
import com.we.R;
import com.we.R2;
import com.we.config.ConfigUser;
import com.we.mvp.user.detail.presence.UpdateNamePresence;
import com.we.mvp.user.detail.view.UpdateNameView;
import com.we.mvp.user.modle.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by yangyupan on 2017/4/15.
 */

public class UpdateNameActivity extends BaseActivity implements UpdateNameView {
    @BindView(R2.id.et_user_name)
    ClearEditText et_user_name;
    @BindView(R2.id.btn_update_user_name)
    Button btn_update_user_name;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    private UpdateNamePresence mPresence;
    private LoadingDialog mDialog;

    @OnClick(R2.id.btn_update_user_name)
    public void updateUserName()
    {
        String account = getIntent().getStringExtra(ConfigUser.USER_ACCOUNT);
        Logger.e("TAG","account="+account);
        mPresence.updateUserName(account);
    }
    @Override
    public void initData() {
        mPresence = new UpdateNamePresence(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.user_update_name;
    }

    @Override
    public void initView() {
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
        et_user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = et_user_name.getText().toString().trim();
                Observable.just(userName)
                        .debounce(200L, TimeUnit.MILLISECONDS)
                        .switchMap(new Function<String, ObservableSource<String>>() {
                            @Override
                            public ObservableSource<String> apply(String str) throws Exception {
                                return Observable.just(str);
                            }
                        })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String username) throws Exception {
                                    if(username.length()>=1)
                                    {
                                        btn_update_user_name.setEnabled(true);
                                    }else
                                    {
                                        btn_update_user_name.setEnabled(false);

                                    }
                            }
                        });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public String getUserName() {
        return et_user_name.getText().toString();
    }

    @Override
    public void error(int errorCode,String message) {
        mDialog.dismiss();
        if (errorCode!= ConfigStateCode.STAT_OTHER) {
            ToastUtils.makeShowToast(UIUtils.getContext(), message);
        }
    }

    @Override
    public void loading() {
        mDialog.show();
    }

    @Override
    public void success(List<User> data) {
        mDialog.dismiss();
        onBackPressed();
    }
}
