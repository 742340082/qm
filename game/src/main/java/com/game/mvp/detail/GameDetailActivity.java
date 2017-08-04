package com.game.mvp.detail;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.base.adapter.ViewPagerFragmentAdapter;
import com.baselibrary.model.game.detail.summary.GameSummaryResult;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.ProgressHorizontal;
import com.game.R;
import com.game.R2;
import com.game.adapter.gamedetail.GameDetailPicAdapter;
import com.game.config.ConfigGame;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by 74234 on 2017/6/23.
 */

public class GameDetailActivity extends BaseActivity   {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.vp_game_detail_top)
    ViewPager vp_game_detail_top;
    @BindView(R2.id.vp_game_detail)
    ViewPager vp_game_detail;
    @BindView(R2.id.tb_game_detail)
    TabLayout tb_game_detail;

    @BindView(R2.id.cl_game_detail)
    CoordinatorLayout cl_game_detail;
    @BindView(R2.id.abl_game_detail)
    AppBarLayout abl_game_detail;
    @BindView(R2.id.ctl_game_detail)
    CollapsingToolbarLayout ctl_game_detail;

    @BindView(R2.id.pbh_gamedonwload)
    ProgressHorizontal pbh_gamedonwload;
    private String mGameId;
    private String mPackageName;
    private String mAppName;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_game_detail;
    }
    @Override
    public void initData() throws RemoteException {

        mGameId = getIntent().getStringExtra(ConfigGame.GAME_SEND_GAMEDETAIL_ID);
        mPackageName = getIntent().getStringExtra(ConfigGame.GAME_SEND_GAMEDETAIL_PACKAGE);
        mAppName = getIntent().getStringExtra(ConfigGame.GAME_SEND_GAMEDETAIL_APPNAME);

        toolbar.setTitle(mAppName);

        ViewPagerFragmentAdapter viewPagerFragmentAdapter=new ViewPagerFragmentAdapter(getSupportFragmentManager());
        Bundle SUMMARY = new Bundle();
        SUMMARY.putString(ConfigGame.GAME_SEND_GAMEDETAIL_ID,mGameId);
        SUMMARY.putString(ConfigGame.GAME_SEND_GAMEDETAIL_PACKAGE,mPackageName);
        viewPagerFragmentAdapter.addTabPage(UIUtils.getString(GameDetailTab.SUMMARY.getResName()),
                GameDetailTab.SUMMARY.getFragmnet(),SUMMARY);

        Bundle COMMENT = getBundle(UIUtils.getString(GameDetailTab.COMMENT.getResName()));
        COMMENT.putString(ConfigGame.GAME_SEND_GAMEDETAIL_ID,mGameId);
        COMMENT.putString(ConfigGame.GAME_SEND_GAMEDETAIL_PACKAGE,mPackageName);
        viewPagerFragmentAdapter.addTabPage(UIUtils.getString(GameDetailTab.COMMENT.getResName()),
                GameDetailTab.COMMENT.getFragmnet(),COMMENT);
        vp_game_detail.setAdapter(viewPagerFragmentAdapter);
        vp_game_detail.setOffscreenPageLimit(2);
        tb_game_detail.setupWithViewPager(vp_game_detail);

        RxBus.getDefault().toObservable(ConfigGame.GAME_SEND_RX_DETAIL,GameSummaryResult.class)
                .subscribe(new Consumer<GameSummaryResult>() {
                    @Override
                    public void accept(GameSummaryResult data) throws Exception {
                        GameDetailPicAdapter gameDetailPicAdapter = new GameDetailPicAdapter(UIUtils.getContext());
                        gameDetailPicAdapter.addTabPage(data.getScreenpath());
                        vp_game_detail_top.setAdapter(gameDetailPicAdapter);

                    }
                });
    }

    @Override
    public void initView() {
        pbh_gamedonwload.setCenterText("下载");
        if (Build.VERSION.SDK_INT >= 21) {
            hideToobarAnd(Color.TRANSPARENT, Color.TRANSPARENT, true, false);
        }

    }

    @Override
    public void initListener() {
        abl_game_detail.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -vp_game_detail_top.getHeight() / 2) {
                    ctl_game_detail.setTitle(mAppName);
                } else {
                    ctl_game_detail.setTitle(" ");
                }
            }
        });
    }

}
