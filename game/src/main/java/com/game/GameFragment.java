package com.game;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.baselibrary.basepager.ViewPagerFragmentAdapter;
import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.utils.UIUtils;

import butterknife.BindView;


/**
 * Created by 74234 on 2017/5/18.
 */

public class GameFragment extends BaseFragmnet {
    @BindView(R2.id.tb_game)
    TabLayout tb_game;
    @BindView(R2.id.vp_game)
    ViewPager vp_game;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_game;
    }

    @Override
    public void initData() {
        ViewPagerFragmentAdapter mAdapter = new ViewPagerFragmentAdapter(getFragmentManager());

        Bundle bundle = getBundle(UIUtils.getString(GameTab.RECOMMEND.getResName()));
        mAdapter.addTabPage(UIUtils.getString(GameTab.RECOMMEND.getResName()),GameTab.RECOMMEND.getFragmnet(),bundle);

        bundle = getBundle(UIUtils.getString(GameTab.CATEGORY.getResName()));
        mAdapter.addTabPage(UIUtils.getString(GameTab.CATEGORY.CATEGORY.getResName()),GameTab.CATEGORY.getFragmnet(),bundle);

        bundle = getBundle(UIUtils.getString(GameTab.TOP.getResName()));
        mAdapter.addTabPage(UIUtils.getString(GameTab.TOP.getResName()),GameTab.TOP.getFragmnet(),bundle);

//        bundle = getBundle(UIUtils.getString(GameTab.CONSULTATION.getResName()));
//        mAdapter.addTabPage(UIUtils.getString(GameTab.CONSULTATION.getResName()),GameTab.CONSULTATION.getFragmnet(),bundle);

        vp_game.setAdapter(mAdapter );
        vp_game.setOffscreenPageLimit(1);
        vp_game.setCurrentItem(0);
        tb_game.setupWithViewPager(vp_game,true);
    }


}
