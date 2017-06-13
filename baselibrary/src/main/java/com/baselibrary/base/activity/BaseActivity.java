package com.baselibrary.base.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity
        extends AppBaseActivity {
    public static BaseActivity sForegroundActivity;
    public static final LinkedList<BaseActivity> mActivities = new LinkedList();

    public static final   BaseActivity getForegroundActivity() {
        return sForegroundActivity;
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
        // 复制了一份mActivities 集合
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new LinkedList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            // 结束当前 Activity, 也可以使用广播
            activity.finish();
        }
        // 杀死当前的进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 结束所有没有销毁的 Activity, 结束当前进程
     */
    public void killActivity(Class clazz) {
        // 复制了一份mActivities 集合
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new LinkedList<BaseActivity>(mActivities);
        }
        for (BaseActivity itemActivity : copy) {
            // 结束当前 Activity, 也可以使用广播
            if (clazz.getSimpleName() == itemActivity.getClass().getSimpleName()) {
                itemActivity.finish();
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
        mActivities.add(this);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        initView();
        initData();
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
