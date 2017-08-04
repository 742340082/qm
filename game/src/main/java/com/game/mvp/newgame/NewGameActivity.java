package com.game.mvp.newgame;

import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.model.game.newgame.NewGameResult;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ToastUtils;
import com.game.R;
import com.game.R2;
import com.game.adapter.gamenewgame.NewGameAdapter;
import com.game.adapter.gamenewgame.NewGameHeaderAdapter;
import com.game.mvp.newgame.presenter.NewGamePresenter;
import com.game.mvp.newgame.view.NewGameView;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/8/2.
 */

public class NewGameActivity extends BaseActivity implements NewGameView {

    @BindView(R2.id.srl_game_newgame)
    SwipeRefreshLayout srl_game_newgame;
    @BindView(R2.id.rv_game_newgame)
    RecyclerView rv_game_newgame;
    @BindView(R2.id.ll_game_newgame)
    LinearLayout ll_game_newgame;

    NewGamePresenter mPresenter;
    private StatusLayoutManager mStatusLayoutManager;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_game_newgame;
    }

    @Override
    public void initView() {
        mStatusLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        mPresenter.initNewGame();
                    }
                })
                .build();
        ll_game_newgame.addView(mStatusLayoutManager.getRootLayout(), ll_game_newgame.getChildCount() - 1);
    }

    @Override
    public void initData() throws RemoteException {
        mPresenter=new NewGamePresenter(this);
        mPresenter.initNewGame();

    }

    @Override
    public void initListener() {
        srl_game_newgame.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.initNewGame();
            }
        });
    }


    @Override
    public void error(int error, String errorMessage) {
        ToastUtils.makeShowToast(this,errorMessage);
    }

    @Override
    public void loading() {
        mStatusLayoutManager.showLoading();
    }

    @Override
    public void success(NewGameResult data) {
        mStatusLayoutManager.showContent();
        srl_game_newgame.setRefreshing(false);
        NewGameAdapter newGameAdapter=new NewGameAdapter(R.layout.item_game_newgame_tag,data.getData());
        newGameAdapter.openLoadAnimation(NewGameAdapter.ALPHAIN);


        //添加新游戏头
        View headerView = LayoutInflater.from(this).inflate(R.layout.item_game_category_header, new FrameLayout(this));
        RecyclerView rl_game_category_header = (RecyclerView) headerView.findViewById(R.id.rl_game_category_header);
        rl_game_category_header.setLayoutManager(new GridLayoutManager(this,3));
        NewGameHeaderAdapter newGameHeaderAdapter=new NewGameHeaderAdapter(R.layout.item_game_category_header_tag,data.getLinks());
        newGameHeaderAdapter.openLoadAnimation(NewGameHeaderAdapter.ALPHAIN);
        rl_game_category_header.setAdapter(newGameHeaderAdapter);
        newGameAdapter.addHeaderView(headerView);

        //添加新游戏数据
        rv_game_newgame.setLayoutManager(new LinearLayoutManager(this));
        rv_game_newgame.setAdapter(newGameAdapter);

    }
}
