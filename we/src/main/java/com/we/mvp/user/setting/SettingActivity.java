package com.we.mvp.user.setting;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.utils.UIUtils;
import com.we.R;
import com.we.R2;
import com.we.config.ConfigUser;
import com.we.mvp.user.UserActivity;
import com.we.mvp.user.detail.UpdatePasswordActivity;
import com.we.mvp.user.detail.UserDetailActivity;
import com.we.mvp.user.modle.User;
import com.we.mvp.user.setting.presence.SettingPresence;
import com.we.mvp.user.setting.view.SettingView;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity
        extends BaseActivity
        implements SettingView
{
    @BindView(R2.id.btn_exit_login)
    Button btn_exit_login;
    @BindView(R2.id.rl_abount)
    RelativeLayout rl_abount;
    @BindView(R2.id.rl_common)
    RelativeLayout rl_common;
    @BindView(R2.id.rl_user_account_safe)
    RelativeLayout rl_user_account_safe;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    private User mMcbUser;
    private SettingPresence mPresence;


    @OnClick({R2.id.btn_exit_login})
    void exitLogin() {
        this.mPresence.deleteSaveUser(mMcbUser.getAccount());
        if (mMcbUser.getPassword() == null)
        {
            Intent intent=new Intent();
            intent.setClass(this,UpdatePasswordActivity.class);
            intent.putExtra(ConfigUser.USER_UPDATE_TYPE, ConfigUser.USER_UPDATE_ADD_PASSWORD);
            intent.putExtra(ConfigUser.USER_ACCOUNT,mMcbUser.getAccount());
            startActivity(intent);
        }
        onBackPressed();
    }

    public int getLayoutResId()
    {
        return R.layout.activity_user_setting;
    }

    @Override
    protected void onStart() {
        mPresence=new SettingPresence(this);
        mPresence.isLogin();
        super.onStart();
    }

    public void initListener()
    {
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                SettingActivity.this.onBackPressed();
            }
        });
    }


    public void initView() {
        setSupportActionBar(this.toolbar);
    }

    public void onBackPressed()
    {
        super.onBackPressed();
    }

    void setExitLoginVisible(boolean isLogin)
    {
        if (isLogin)
        {
            this.btn_exit_login.setVisibility(View.VISIBLE);
        }else {
            this.btn_exit_login.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.rl_user_account_safe)
    void userAccountSafe()
    {
        Class clazz = mMcbUser != null?UserDetailActivity.class:UserActivity.class;
            UIUtils.startActivity(this, clazz);
    }

    @Override
    public void IsLogin(User user) {
        mMcbUser = user;
        setExitLoginVisible(mMcbUser==null?false:true);
    }
}
