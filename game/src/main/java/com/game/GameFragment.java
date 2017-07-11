package com.game;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baselibrary.base.adapter.ViewPagerFragmentAdapter;
import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.utils.UIUtils;
import com.game.mvp.search.GameSearchActivity;

import butterknife.BindView;


/**
 * Created by 74234 on 2017/5/18.
 */

public class GameFragment extends BaseFragmnet {
    @BindView(R2.id.tb_game)
    TabLayout tb_game;
    @BindView(R2.id.vp_game)
    ViewPager vp_game;
    @BindView(R2.id.tv_game_search)
    TextView tv_game_search;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_game;
    }

    @Override
    public void initData() {
        ViewPagerFragmentAdapter mAdapter = new ViewPagerFragmentAdapter(getFragmentManager());

        Bundle bundle = getBundle(UIUtils.getString(GameTab.INDEX.getResName()));
        mAdapter.addTabPage(UIUtils.getString(GameTab.INDEX.getResName()),GameTab.INDEX.getFragmnet(),bundle);

        bundle = getBundle(UIUtils.getString(GameTab.CATEGORY.getResName()));
        mAdapter.addTabPage(UIUtils.getString(GameTab.CATEGORY.CATEGORY.getResName()),GameTab.CATEGORY.getFragmnet(),bundle);

        bundle = getBundle(UIUtils.getString(GameTab.TOP.getResName()));
        mAdapter.addTabPage(UIUtils.getString(GameTab.TOP.getResName()),GameTab.TOP.getFragmnet(),bundle);

        bundle = getBundle(UIUtils.getString(GameTab.GAMENEWS.getResName()));
        mAdapter.addTabPage(UIUtils.getString(GameTab.GAMENEWS.getResName()),GameTab.GAMENEWS.getFragmnet(),bundle);

        vp_game.setAdapter(mAdapter );
        vp_game.setOffscreenPageLimit(1);
        vp_game.setCurrentItem(0);
        tb_game.setupWithViewPager(vp_game,true);
    }


    @Override
    public void initView() {
        toolbar.inflateMenu(R.menu.game_menu_download);
    }

    @Override
    public void initListener() {
        tv_game_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP)
                {
                    UIUtils.startActivityAndMaterAnimator(GameFragment.this.getContext()
                            ,"共享搜索",v,GameSearchActivity.class);
                }else
                {
                    UIUtils.startActivity(GameFragment.this.getContext(),GameSearchActivity.class);
                }

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId==R.id.menu_game_download)
        {

        }
        return super.onOptionsItemSelected(item);
    }
}
