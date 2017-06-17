package com.we.mvp.user;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.baselibrary.animation.FixedSpeedScroller;
import com.baselibrary.animation.TabletTransformer;
import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.base.adapter.ViewPagerFragmentAdapter;
import com.baselibrary.config.ConfigSdk;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.view.NoScrollViewPager;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.we.R;
import com.we.R2;
import com.we.mvp.user.login.LoginFragement;
import com.we.mvp.user.regist.RegistFragment;

import java.lang.reflect.Field;

import butterknife.BindView;

public class UserActivity
        extends BaseActivity
{
    private ViewPagerFragmentAdapter mAdapter;
    @BindView(R2.id.vp_user)
    public NoScrollViewPager vp_user;

    private void ReflexUpdateViewPagerTouchSpeed()
    {
        Field localField = null;
        try {
            localField =ViewPager.class.getDeclaredField("mScroller");
            localField.setAccessible(true);
            FixedSpeedScroller localFixedSpeedScroller = new FixedSpeedScroller(this.vp_user.getContext());
            localField.set(vp_user, localFixedSpeedScroller);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getLayoutResId()
    {
        return R.layout.activity_user;
    }

    public void initData()
    {
        AuthInfo authInfo = new AuthInfo(this, ConfigSdk.WEIBO_APP_KEY, ConfigSdk.WEIBO_REDIRECT_URL, ConfigSdk.WEIBO_SCOPE);
        WbSdk.install(this,authInfo);

        this.mAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        Bundle bundle = getBundle("登录");
        this.mAdapter.addFragement(new LoginFragement(),bundle);
        bundle=getBundle("注册");
        this.mAdapter.addFragement(new RegistFragment(),bundle);
        this.vp_user.setPageTransformer(true, new TabletTransformer());
        this.vp_user.setAdapter(this.mAdapter);
        this.vp_user.setOffscreenPageLimit(1);
        this.vp_user.setCurrentItem(0);
    }

    public void initListener() {}

    public Bundle getBundle(String title) {
        Bundle localBundle = new Bundle();
        localBundle.putString(ConfigValues.VALUE_SEND_TITLE, title);
        return localBundle;
    }
    public void initView()
    {

        ReflexUpdateViewPagerTouchSpeed();
    }

}
