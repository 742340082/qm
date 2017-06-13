package com.news.mvp.guoke;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.news.R;
import com.news.R2;
import com.news.adapter.NewGuokeContentAdapter;
import com.news.adapter.NewZhiHuContentAdapter;
import com.news.config.ConfigNews;
import com.news.mvp.detail.NewsDetailActivity;
import com.news.mvp.guoke.bean.GuoKeResult;
import com.news.mvp.guoke.presenter.GuoKeDailyPresenter;
import com.news.mvp.guoke.view.GuokeView;

import java.util.List;

import butterknife.BindView;

public class GuoKeFragment
        extends BaseFragmnet
        implements GuokeView {
    @BindView(R2.id.fab)
    FloatingActionButton fab;
    @BindView(R2.id.new_fl_container)
    FrameLayout new_fl_container;
    @BindView(R2.id.rv_news_content)
    RecyclerView rv_news_content;
    @BindView(R2.id.srl_news_content)
    SwipeRefreshLayout srl_news_content;

    private NewGuokeContentAdapter mNewContentAdapter;
    public GuoKeDailyPresenter mPresenter;
    private StatusLayoutManager mStatusLayoutManager;
    private List<GuoKeResult> guoKeResults;


    @Override
    public int getLayoutResId() {
        return R.layout.news_content;
    }

    @Override
    public void initData() {
        if (this.mPresenter == null) {
            this.mPresenter = new GuoKeDailyPresenter(this);
        }
        mPresenter.loadingData();
    }

    @Override
    public void initListener() {
        this.srl_news_content.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadingData();
            }
        });
    }

    @Override
    public void initView() {
        mStatusLayoutManager = StatusLayoutManager.newBuilder(getContext())
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error).onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        mPresenter.loadingData();
                    }
                }).build();
        this.new_fl_container.addView(mStatusLayoutManager.getRootLayout(),new_fl_container.getChildCount()-1);
    }


    @Override
    public void loading() {
        if (guoKeResults==null) {
            mStatusLayoutManager.showLoading();
        }else
        {
            srl_news_content.setRefreshing(true);
        }

    }

    @Override
    public void success(List<GuoKeResult> data) {
        guoKeResults = data;
        mStatusLayoutManager.showContent();
        srl_news_content.setRefreshing(false);
        mNewContentAdapter = new NewGuokeContentAdapter(R.layout.item_news_content, data);
        mNewContentAdapter.openLoadAnimation(NewZhiHuContentAdapter.SLIDEIN_LEFT);
        rv_news_content.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        rv_news_content.setAdapter(this.mNewContentAdapter);
        mNewContentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GuoKeResult item = (GuoKeResult) adapter.getItem(position);
                Intent intent=new Intent();
                intent.putExtra(ConfigNews.NEWS_SEND_ID,item.getGuokeresult_id());
                intent.putExtra(ConfigNews.NEWS_SEND_NEWS_TYPE,ConfigNews.NEWS_GUOKE_TYPE);
                intent.setClass(getContext(), NewsDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void error(int uiCode, String errorMessage) {
        this.srl_news_content.setRefreshing(false);
        ToastUtils.makeShowToast(getContext(), errorMessage);
        switch (uiCode) {
            case ConfigStateCode.STATE_NO_NETWORK:
                this.mStatusLayoutManager.showNetWorkError();
                break;
            case ConfigStateCode.STATE_DATA_EMPTY:
                this.mStatusLayoutManager.showEmptyData();
                break;
            case ConfigStateCode.STATE_ERROE:

                this.mStatusLayoutManager.showError();
                break;
        }
    }

}
