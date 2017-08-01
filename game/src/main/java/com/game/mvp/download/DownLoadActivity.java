package com.game.mvp.download;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.base.adapter.ViewPagerFragmentAdapter;
import com.game.R;
import com.game.R2;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/7/20.
 */

public class DownLoadActivity extends BaseActivity {
    @BindView(R2.id.tl_game_download)
   TabLayout tl_game_download;
    @BindView(R2.id.vp_game_download)
    ViewPager vp_game_download;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_game_download;
    }

    @Override
    public void initData()   {

        ViewPagerFragmentAdapter viewPagerFragmentAdapter=new ViewPagerFragmentAdapter(getSupportFragmentManager());
        Bundle downloadIngBundle = getBundle(GameDetailTab.DOWNLOADING.getResName());
        viewPagerFragmentAdapter.addTabPage(GameDetailTab.DOWNLOADING.getResName(),GameDetailTab.DOWNLOADING.getFragmnet()
        ,downloadIngBundle);
        Bundle downloadSuccessBundle = getBundle(GameDetailTab.DOWNLOADSUCCESS.getResName());
        viewPagerFragmentAdapter.addTabPage(GameDetailTab.DOWNLOADSUCCESS.getResName(),GameDetailTab.DOWNLOADSUCCESS.getFragmnet()
                ,downloadSuccessBundle);
        Bundle installAppBundle = getBundle(GameDetailTab.INSTALLAPP.getResName());
        viewPagerFragmentAdapter.addTabPage(GameDetailTab.INSTALLAPP.getResName(),GameDetailTab.INSTALLAPP.getFragmnet()
                ,installAppBundle);
        vp_game_download.setAdapter(viewPagerFragmentAdapter);
        vp_game_download.setOffscreenPageLimit(0);
        tl_game_download.setupWithViewPager(vp_game_download);
    }
}
