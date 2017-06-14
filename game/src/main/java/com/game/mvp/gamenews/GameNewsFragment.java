package com.game.mvp.gamenews;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.game.R;
import com.game.R2;
import com.game.adapter.GameNewsAdapter;
import com.game.adapter.GameNewsTopAdapter;
import com.game.adapter.IndexAdapter;
import com.game.mvp.gamenews.model.GameNewsResult;
import com.game.mvp.gamenews.presenter.GameNewsPresneter;
import com.game.mvp.gamenews.view.GameNewsView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private Handler mHandler;
    private static long TOP_NEWS_CHANGE_TIME = 3000;
    private int k;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_gamenews;
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
        gameNewsAdapter = new GameNewsAdapter(R.layout.item_gamenews);
        gameNewsAdapter.openLoadAnimation(IndexAdapter.SLIDEIN_LEFT);
        gameNewsAdapter.loadMoreEnd(true);
        gameNewsAdapter.setOnLoadMoreListener(this, rv_game_gamenews);
        rv_game_gamenews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv_game_gamenews.setAdapter(gameNewsAdapter);

        FrameLayout viewHeader = (FrameLayout) LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_gamenews_header, new FrameLayout(UIUtils.getContext()));
        LinearLayout linearLayout = (LinearLayout) viewHeader.getChildAt(0);
        final ViewPager viewPager = (ViewPager) linearLayout.getChildAt(0);
        if (mHandler == null) {

            mHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    int item = viewPager.getCurrentItem();
                    if (item < gameNewsResult.getGallary().size() - 1) {
                        item++;
                    } else {// 判断是否到达最后一个
                        item = 0;
                    }
                    // Log.d(TAG, "轮播条:" + item);
                    viewPager.setCurrentItem(item);
                    mHandler.sendMessageDelayed(Message.obtain(),
                            TOP_NEWS_CHANGE_TIME);
                }
            };
        }
        mHandler.sendMessageDelayed(Message.obtain(), TOP_NEWS_CHANGE_TIME);// 延时4s发送消息
        GameNewsTopAdapter gameNewsTopAdapter = new GameNewsTopAdapter(UIUtils.getContext());
        gameNewsTopAdapter.addTabPage(gameNewsResult.getGallary());
        viewPager.setAdapter(gameNewsTopAdapter);


        LinearLayout linearLayoutChildAt = (LinearLayout) linearLayout.getChildAt(1);
        k=0;
        for (int j=0;j<linearLayoutChildAt.getChildCount();j++) {
            LinearLayout linearLayout1 = (LinearLayout) linearLayoutChildAt.getChildAt(j);
            for (int i = 0; i < linearLayout1.getChildCount(); i++) {
                View childAt = linearLayout1.getChildAt(i);
                if (childAt instanceof CircleImageView) {
                    CircleImageView circleImageView = (CircleImageView) childAt;
                    Glide.with(UIUtils.getContext())
                            .load(gameNewsResult.getLinks().get(k).getImg())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .into(circleImageView);
                }
                if (childAt instanceof TextView) {
                    TextView textView = (TextView) childAt;
                    textView.setText(gameNewsResult.getLinks().get(k).getName());
                }
            }
            k++;

        }
        gameNewsAdapter.addHeaderView(viewHeader);

    }
}
