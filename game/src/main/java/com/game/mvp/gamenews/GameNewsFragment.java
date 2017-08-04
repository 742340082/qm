package com.game.mvp.gamenews;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.model.game.gamenews.GameNewsLink;
import com.baselibrary.model.game.gamenews.GameNewsResult;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.CarouselViewPager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.game.R;
import com.game.R2;
import com.game.adapter.gameindex.IndexAdapter;
import com.game.adapter.gamenews.GameNewsAdapter;
import com.game.adapter.gamenews.GameNewsRlHeaderAdapter;
import com.game.adapter.gamenews.GameNewsVpHeaderAdapter;
import com.game.mvp.gamenews.presenter.GameNewsPresneter;
import com.game.mvp.gamenews.view.GameNewsView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/6/13.
 */

public class GameNewsFragment extends BaseFragmnet implements GameNewsView, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R2.id.ll_game_gamenews)
    LinearLayout ll_game_gamenews;
    @BindView(R2.id.rv_game_gamenews)
    RecyclerView rv_game_gamenews;
    @BindView(R2.id.srl_game_gamenews)
    SwipeRefreshLayout srl_game_gamenews;
    private StatusLayoutManager mStatusLayoutManager;
    private GameNewsPresneter mPresenter;
    private GameNewsResult gameNewsResult;
    private GameNewsAdapter gameNewsAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_game_gamenews;
    }

    @Override
    public void initData() {
        mPresenter = new GameNewsPresneter(this);
        mPresenter.initGameNews(1);
    }

    @Override
    public void initView() {
        mStatusLayoutManager = StatusLayoutManager.newBuilder(getContext())
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        mPresenter.initGameNews(1);
                    }
                })
                .build();
        ll_game_gamenews.addView(mStatusLayoutManager.getRootLayout(), ll_game_gamenews.getChildCount() - 1);
    }

    @Override
    public void initListener() {
        srl_game_gamenews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.initGameNews(1);
            }
        });
    }

    @Override
    public void error(int error, String errorMessage) {
        ToastUtils.makeShowToast(UIUtils.getContext(), errorMessage);
        ConfigStateCodeUtil.error(error, mStatusLayoutManager);
        if (error == ConfigStateCode.STATE_LOAD_MORE_FAILURES) {
            gameNewsAdapter.loadMoreFail();
        }
    }

    @Override
    public void loading() {
        if (gameNewsResult == null) {
            mStatusLayoutManager.showLoading();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (gameNewsAdapter.getItemCount() >= gameNewsResult.getCount()) {
            gameNewsAdapter.loadMoreEnd();
        } else {
            mPresenter.initGameNews(gameNewsResult.getStartKey());
        }
    }

    @Override
    public void success(GameNewsResult data) {
        gameNewsResult = data;
        srl_game_gamenews.setRefreshing(false);
        mStatusLayoutManager.showContent();
        if (gameNewsResult.getPage() == 1) {
            gameNewsAdapter.addData(gameNewsResult.getData());
            gameNewsAdapter.notifyDataSetChanged();
        } else {
            gameNewsAdapter.addData(gameNewsResult.getData());
            gameNewsAdapter.loadMoreComplete();
        }
    }


    @Override
    public void initHeader(final GameNewsResult gameNewsResult) {
        gameNewsAdapter = new GameNewsAdapter(R.layout.item_game_gamenews_tag);
        gameNewsAdapter.openLoadAnimation(IndexAdapter.ALPHAIN);
        gameNewsAdapter.loadMoreEnd(true);
        gameNewsAdapter.setOnLoadMoreListener(this, rv_game_gamenews);
        rv_game_gamenews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv_game_gamenews.setAdapter(gameNewsAdapter);

        View viewHeader =  LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_game_gamenews_header, new FrameLayout(getContext()));
        final CarouselViewPager viewPager = (CarouselViewPager) viewHeader.findViewById(R.id.vp_game_gamenews);

        GameNewsVpHeaderAdapter gameNewsTopAdapter = new GameNewsVpHeaderAdapter(UIUtils.getContext());
        gameNewsTopAdapter.addTabPage(gameNewsResult.getGallary());
        //设置Page间间距
        viewPager.setPageMargin(20);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(gameNewsTopAdapter);
        int position = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % gameNewsResult.getGallary().size());
        viewPager.setCurrentItem(position);


        List<GameNewsLink> links = gameNewsResult.getLinks();
        GameNewsRlHeaderAdapter gameNewsRlHeaderAdapter = new GameNewsRlHeaderAdapter(R.layout.item_game_category_header_tag,links);
        RecyclerView rl_gamenews_header = (RecyclerView) viewHeader.findViewById(R.id.rl_game_gamenews_header);
        rl_gamenews_header.setLayoutManager(new GridLayoutManager(UIUtils.getContext(),4));
        rl_gamenews_header.setAdapter(gameNewsRlHeaderAdapter);
        gameNewsAdapter.addHeaderView(viewHeader);

    }
}
