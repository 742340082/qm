package com.game.mvp.top.total;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.game.R;
import com.game.R2;
import com.game.adapter.IndexAdapter;
import com.game.adapter.TopAdapter;
import com.game.mvp.top.modle.TotalResult;
import com.game.mvp.top.total.presenter.TotalPresenter;
import com.game.mvp.top.total.view.TotalView;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/6/12.
 */

public class TotalFragment extends BaseFragmnet implements TotalView, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R2.id.ll_game_top)
    LinearLayout ll_game_top;
    @BindView(R2.id.srl_game_top)
    SwipeRefreshLayout srl_game_top;
    @BindView(R2.id.rv_game_top)
    RecyclerView rv_game_top;

    private StatusLayoutManager mStatusLayoutManager;
    private TotalPresenter mPresenter;
    private String topType;
    private TotalResult totalResult;
    private TopAdapter topAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_game_total;
    }

    @Override
    public void initData() {
        mPresenter = new TotalPresenter(this);
        Bundle bundle = getArguments();
        topType = bundle.getString(ConfigValues.VALUE_SEND_TITLE);
        mPresenter.initGameTopTotal(1, topType);
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
                        mPresenter.initGameTopTotal(1, topType);
                    }
                })
                .build();
        ll_game_top.addView(mStatusLayoutManager.getRootLayout(), ll_game_top.getChildCount() - 1);


    }

    @Override
    public void initListener() {
        srl_game_top.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.initGameTopTotal(1, topType);
            }
        });
    }

    @Override
    public void error(int error, String errorMessage) {
        ToastUtils.makeShowToast(UIUtils.getContext(), errorMessage);
        ConfigStateCodeUtil.error(error, mStatusLayoutManager);
        if (error == ConfigStateCode.STATE_LOAD_MORE_FAILURES)
        {
            topAdapter.loadMoreFail();
        }
    }

    @Override
    public void loading() {
        if (totalResult == null) {
            mStatusLayoutManager.showLoading();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (topAdapter.getItemCount()>=totalResult.getCount()){
            topAdapter.loadMoreEnd();
        }else
        {
            mPresenter.initGameTopTotal(totalResult.getStartKey(), topType);
        }
    }

    @Override
    public void success(TotalResult data) {
        totalResult = data;
        srl_game_top.setRefreshing(false);
        mStatusLayoutManager.showContent();

        if (data.getPage() == 1) {
            topAdapter = new TopAdapter(R.layout.item_game_top_total,totalResult.getData());
            topAdapter.openLoadAnimation(IndexAdapter.ALPHAIN);
            topAdapter.loadMoreEnd(true);
            topAdapter.setOnLoadMoreListener(this, rv_game_top);
            rv_game_top.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_game_top.setAdapter(topAdapter);
        } else {
            topAdapter.addData(data.getData());
            topAdapter.loadMoreComplete();
        }

    }

}
