package com.yyp.qm.main;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.base.adapter.ViewPagerFragmentAdapter;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.SaveConfigUserUtil;
import com.baselibrary.utils.SaveConfigValueUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.NoScrollViewPager;
import com.we.config.ConfigUser;
import com.yyp.qm.R;
import com.yyp.qm.R2;

import butterknife.BindView;

public class MainActivity
        extends BaseActivity {


    @BindView(R2.id.tl_main)
    TabLayout tl_main;
    @BindView(R2.id.vp_main)
    NoScrollViewPager vp_main;
    private MainTab[] mMainTabs;
    private long mSecondClickTime;
    private long mFirstClickTime;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }



    @Override
    public void initView() {

        mMainTabs=MainTab.values();

        initBottomNavigationBar();
    }

    private void initBottomNavigationBar() {
        ViewPagerFragmentAdapter viewPagerFragmentAdapter=new ViewPagerFragmentAdapter(getSupportFragmentManager());
        viewPagerFragmentAdapter.addTabPage(UIUtils.getString(MainTab.APP.getResName()),MainTab.APP.getFragmnet());
        viewPagerFragmentAdapter.addTabPage(UIUtils.getString(MainTab.NEWS.getResName()),MainTab.NEWS.getFragmnet());
        viewPagerFragmentAdapter.addTabPage(UIUtils.getString(MainTab.WE.getResName()),MainTab.WE.getFragmnet());
        vp_main.setAdapter(viewPagerFragmentAdapter);
        vp_main.setOffscreenPageLimit(mMainTabs.length);
        vp_main.setCurrentItem(0);
        tl_main.setupWithViewPager(vp_main);

        for (int i=0;i<tl_main.getTabCount();i++)
        {
            tl_main.getTabAt(i).setCustomView(getIndicaor(i));
        }
    }

    @Override
    public void initData() {
        if (!SaveConfigValueUtil.getBoolean(this, ConfigValues.VALUE_STATE_ENTER_HOME, false)) {
            SaveConfigValueUtil.setBoolean(this, ConfigValues.VALUE_STATE_ENTER_HOME, true);
        }
        boolean isNight = SaveConfigUserUtil.getBoolean(UIUtils.getContext(), ConfigUser.USER_NIGHT_SAVE, false);
        if (isNight)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private View getIndicaor(int position) {
      View main_tab_bottom_item = UIUtils.inflate(R.layout.item_main_tab);
        LinearLayout ll_tab_item = ((LinearLayout) main_tab_bottom_item.findViewById(R.id.ll_tab_item));
        TextView tv_tab_item = ((TextView) main_tab_bottom_item.findViewById(R.id.tv_tab_item));
        ImageView iv_tab_item = ((ImageView) main_tab_bottom_item.findViewById(R.id.iv_tab_item));
        iv_tab_item.setImageResource(this.mMainTabs[position].getIcon());
        String str = UIUtils.getString(this.mMainTabs[position].getResName());
        tv_tab_item.setText(str);
        return main_tab_bottom_item;
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
