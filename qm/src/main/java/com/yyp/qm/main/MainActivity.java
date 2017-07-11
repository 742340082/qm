package com.yyp.qm.main;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.SaveConfigValueUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.MyFragmentTabHost;
import com.yyp.qm.R;
import com.yyp.qm.R2;

import butterknife.BindView;

public class MainActivity
        extends BaseActivity {


    @BindView(R2.id.tabhost)
    MyFragmentTabHost mTabhost;

    private MainTab[] mMainTabSs;

    private long mSecondClickTime;
    private long mFirstClickTime;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }



    @Override
    public void initView() {


        if (Build.VERSION.SDK_INT >= 21) {
            hideToobarAnd(UIUtils.getColor(R.color.blue), UIUtils.getColor(R.color.transparent), true, false);
        }
        initBottomMenu();
    }

    @Override
    public void initData() {
        if (!SaveConfigValueUtil.getBoolean(this, ConfigValues.VALUE_STATE_ENTER_HOME, false)) {
            SaveConfigValueUtil.setBoolean(this, ConfigValues.VALUE_STATE_ENTER_HOME, true);
        }
    }

    private View getIndicaor(int position) {
      View main_tab_bottom_item = UIUtils.inflate(R.layout.item_main_tab);
        LinearLayout ll_tab_item = ((LinearLayout) main_tab_bottom_item.findViewById(R.id.ll_tab_item));
        TextView tv_tab_item = ((TextView) main_tab_bottom_item.findViewById(R.id.tv_tab_item));
        ImageView iv_tab_item = ((ImageView) main_tab_bottom_item.findViewById(R.id.iv_tab_item));
        iv_tab_item.setImageResource(this.mMainTabSs[position].getIcon());
        String str = UIUtils.getString(this.mMainTabSs[position].getResName());
        tv_tab_item.setText(str);
        return main_tab_bottom_item;
    }

    /**
     * 初始化底部菜单和内容区域
     */
    private void initBottomMenu() {
        mTabhost.setup(UIUtils.getContext(), getSupportFragmentManager(), R.id.fl_main_container);
        mMainTabSs = MainTab.values();
        int i = 0;
        while (i < mMainTabSs.length) {
            TabHost.TabSpec tabSpec = this.mTabhost.newTabSpec(UIUtils.getString(mMainTabSs[i].getResName()));
            tabSpec.setIndicator(getIndicaor(i));
            Bundle bundle = new Bundle();
            switch (i) {
//                case 0:
//                    bundle.putString(ConfigValues.VALUE_SEND_TITLE,UIUtils.getString(MainTab.TAKEOUT.getResName()) );
//                    break;
                case 1:
                    bundle.putString(ConfigValues.VALUE_SEND_TITLE, UIUtils.getString(MainTab.APP.getResName()));
                    break;
                case 2:
                    bundle.putString(ConfigValues.VALUE_SEND_TITLE, UIUtils.getString(MainTab.NEWS.getResName()));
                    break;
//                case 3:
//                    bundle.putString(ConfigValues.VALUE_SEND_TITLE, UIUtils.getString(MainTab.FROM.getResName()));
//                    break;
                case 4:
                    bundle.putString(ConfigValues.VALUE_SEND_TITLE, UIUtils.getString(MainTab.WE.getResName()));
                    break;
            }

            this.mTabhost.addTab(tabSpec, mMainTabSs[i].getFragmnet().getClass(), bundle);
            i++;
        }
        this.mTabhost.setCurrentTab(0);

    }



    /**
     * 设置双击退出
     *
     * @param keyCode
     * @param keyEvent
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {

        if (keyCode == keyEvent.KEYCODE_BACK) {

            if (mSecondClickTime != 0L && mSecondClickTime - mFirstClickTime < 2000) {
                killAll();
            } else {
                mSecondClickTime = System.currentTimeMillis();
                ToastUtils.makeShowToast(this, "再按一次退出");
            }
            return false;


        }
        return super.onKeyDown(keyCode, keyEvent);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        if (keyCode == keyEvent.KEYCODE_BACK) {
            this.mFirstClickTime = System.currentTimeMillis();
        }
        return super.onKeyUp(keyCode, keyEvent);
    }


}
