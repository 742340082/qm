package com.baselibrary.base.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.Window;

import com.baselibrary.config.ConfigValues;

import java.util.LinkedList;

import butterknife.ButterKnife;

public abstract class BaseActivity
        extends AppBaseActivity {
    protected static BaseActivity sForegroundActivity;
    public static final LinkedList<BaseActivity> mActivities = new LinkedList();
    protected Bundle mBundle;
    public static final   BaseActivity getForegroundActivity() {
        return sForegroundActivity;
    }
    public Bundle getBundle(String title) {
        Bundle localBundle = new Bundle();
        localBundle.putString(ConfigValues.VALUE_SEND_TITLE, title);
        return localBundle;
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void hideToobarAnd(int statusColor, int nagivebarColor, boolean isStateBar, boolean isNagiveBar) {
        Window window = getWindow();
        View localView = window.getDecorView();
        int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (!isStateBar && !isNagiveBar) {
            return;
        }
        if ((isStateBar) && (isNagiveBar)) {

            options = options | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.setStatusBarColor(statusColor);
            window.setNavigationBarColor(nagivebarColor);
            localView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        } else {
            if (isStateBar) {
                options = options | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                window.setStatusBarColor(statusColor);
            }
            if (isNagiveBar) {
                options = options | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                window.setNavigationBarColor(nagivebarColor);
            }
        }
        localView.setSystemUiVisibility(options);
    }

    /**
     * 结束所有没有销毁的 Activity, 结束当前进程
     */
    public void killAll() {
        for (BaseActivity activity : mActivities) {
            // 结束当前 Activity, 也可以使用广播
            activity.onBackPressed();
        }
        // 杀死当前的进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    //判斷當前的activity是否存活
    public static boolean isActivityDestory(Class<? extends Activity> clazz)
    {
        boolean isActivityExist=false;
        for (BaseActivity itemActivity : mActivities) {
            if (clazz.getSimpleName().equals( itemActivity.getClass().getSimpleName())) {
                isActivityExist =true;
                break;
            }
        }
        return isActivityExist;
    }
    /**
     * 銷毀當前activity
     */
    public void killActivity(Class<? extends Activity> clazz) {
        for (BaseActivity itemActivity : mActivities) {
            // 结束当前 Activity, 也可以使用广播
            if (clazz.getSimpleName().equals( itemActivity.getClass().getSimpleName())) {
                itemActivity.onBackPressed();
            }
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            default:
                click(view, view.getId());
                break;
        }
    }



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mBundle =bundle;
        mActivities.add(this);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        initView();
        try {
            initData();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        initListener();

    }


    protected void onDestroy() {
        this.mActivities.remove(this);
        super.onDestroy();
    }

    protected void onPause() {
        if (sForegroundActivity == this) {
            sForegroundActivity = null;
        }
        super.onPause();
    }

    protected void onResume() {
        sForegroundActivity = this;
        super.onResume();
    }

}
