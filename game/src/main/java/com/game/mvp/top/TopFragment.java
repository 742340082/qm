package com.game.mvp.top;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.basepager.ViewPagerFragmentAdapter;
import com.baselibrary.utils.UIUtils;
import com.game.R;
import com.game.R2;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/6/12.
 */

public class TopFragment extends BaseFragmnet {
    @BindView(R2.id.tl_game_top)
    TabLayout tl_game_top;
    @BindView(R2.id.vp_game_top)
    ViewPager vp_game_top;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_top;
    }

    @Override
    public void initData() {

        ViewPagerFragmentAdapter mAdapter = new ViewPagerFragmentAdapter(getFragmentManager());

        Bundle bundle = getBundle(UIUtils.getString(TOPTab.NEW.getResName()));
        mAdapter.addTabPage(UIUtils.getString(TOPTab.NEW.getResName()),TOPTab.NEW.getFragmnet(),bundle);

        bundle = getBundle(UIUtils.getString(TOPTab.NET.getResName()));
        mAdapter.addTabPage(UIUtils.getString(TOPTab.NET.getResName()),TOPTab.NET.getFragmnet(),bundle);

        bundle = getBundle(UIUtils.getString(TOPTab.SINGLE.getResName()));
        mAdapter.addTabPage(UIUtils.getString(TOPTab.SINGLE.getResName()),TOPTab.SINGLE.getFragmnet(),bundle);

        bundle = getBundle(UIUtils.getString(TOPTab.HOT.getResName()));
        mAdapter.addTabPage(UIUtils.getString(TOPTab.HOT.getResName()),TOPTab.HOT.getFragmnet(),bundle);

        vp_game_top.setAdapter(mAdapter );
        vp_game_top.setOffscreenPageLimit(1);
        vp_game_top.setCurrentItem(0);
        tl_game_top.setupWithViewPager(vp_game_top,true);
    }
}
