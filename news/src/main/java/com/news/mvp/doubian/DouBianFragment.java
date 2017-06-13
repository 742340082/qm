package com.news.mvp.doubian;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.news.R;
import com.news.R2;
import com.news.adapter.NewDouBianContentAdapter;
import com.news.adapter.NewZhiHuContentAdapter;
import com.news.config.ConfigNews;
import com.news.mvp.detail.NewsDetailActivity;
import com.news.mvp.doubian.bean.Posts;
import com.news.mvp.doubian.presenter.DouBianPresenter;
import com.news.mvp.doubian.view.DouBianView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class DouBianFragment
        extends BaseFragmnet
        implements DouBianView, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R2.id.fab)
    FloatingActionButton fab;
    @BindView(R2.id.new_fl_container)
    FrameLayout new_fl_container;
    @BindView(R2.id.rv_news_content)
    RecyclerView rv_news_content;
    @BindView(R2.id.srl_news_content)
    SwipeRefreshLayout srl_news_content;


    public Calendar mInstance;
    private NewDouBianContentAdapter mNewContentAdapter;
    public DouBianPresenter mPresenter;
    private StatusLayoutManager mStatusLayoutManager;
    private List<Posts> postses;


    @Override
    public int getLayoutResId() {
        return R.layout.news_content;
    }

    @Override
    public void initData() {
        if (this.mPresenter == null) {
            mInstance = Calendar.getInstance();
            this.mPresenter = new DouBianPresenter(this);
        }
        refresh();
    }

    @Override
    public void initListener() {
        this.srl_news_content.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mInstance=Calendar.getInstance();
                mPresenter.loadingData(mInstance.getTimeInMillis());
            }
        });
        this.fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar instance = Calendar.getInstance();
                instance.set(mInstance.get(Calendar.YEAR), mInstance.get(Calendar.MONTH), mInstance.get(Calendar.DAY_OF_MONTH));
                DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        mInstance = Calendar.getInstance();
                        mInstance.clear();
                        mInstance.set(year, monthOfYear, dayOfMonth);
                        mPresenter.loadingData(mInstance.getTimeInMillis());
                    }
                }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
                dialog.setMaxDate(Calendar.getInstance());
                Calendar localCalendar = Calendar.getInstance();
                localCalendar.set(2013, 5, 20);
                dialog.setMinDate(localCalendar);
                dialog.vibrate(false);
                dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
            }
        });
    }

    @Override
    public void initView() {
        fab.show();
        mStatusLayoutManager = StatusLayoutManager.newBuilder(getContext())
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error).onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        refresh();
                    }
                }).build();
        this.new_fl_container.addView(mStatusLayoutManager.getRootLayout());
        this.rv_news_content.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView paramAnonymousRecyclerView, int paramAnonymousInt) {
                super.onScrollStateChanged(paramAnonymousRecyclerView, paramAnonymousInt);
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    DouBianFragment.this.fab.hide();
                    return;
                }
                DouBianFragment.this.fab.show();
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        srl_news_content.setRefreshing(false);
        int day = mInstance.get(Calendar.DAY_OF_MONTH);
            mInstance.set(mInstance.get(Calendar.YEAR),
                    mInstance.get(Calendar.MONTH),
                    --day);
            mPresenter.loadMore(mInstance.getTimeInMillis());

    }

    private void refresh() {
        mPresenter.loadingData(mInstance.getTimeInMillis());
    }

    @Override
    public void showContent(List<Posts> postses, boolean isLoadMore) {
        this.postses = postses;
        mStatusLayoutManager.showContent();
        srl_news_content.setRefreshing(false);
        if (!isLoadMore) {
            mNewContentAdapter = new NewDouBianContentAdapter(postses);
            mNewContentAdapter.setOnLoadMoreListener(this, this.rv_news_content);
            mNewContentAdapter.openLoadAnimation(NewZhiHuContentAdapter.SLIDEIN_LEFT);
            rv_news_content.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
            rv_news_content.setAdapter(this.mNewContentAdapter);
            mNewContentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Posts item = (Posts) adapter.getItem(position);
                    Intent intent = new Intent();
                    intent.putExtra(ConfigNews.NEWS_SEND_ID, item.getPosts_id());
                    intent.putExtra(ConfigNews.NEWS_SEND_NEWS_TYPE, ConfigNews.NEWS_DOUBIAN_TYPE);
                    intent.setClass(getContext(), NewsDetailActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            this.mNewContentAdapter.addData(postses);
            this.mNewContentAdapter.loadMoreComplete();
        }
    }

    @Override
    public void error(int uiCode, String message) {
        this.srl_news_content.setRefreshing(false);
        ToastUtils.makeShowToast(getContext(), message);
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
            case ConfigStateCode.STATE_LOAD_MORE_FAILURES:
                this.mNewContentAdapter.loadMoreFail();
                int day = mInstance.get(Calendar.DAY_OF_MONTH);
                mInstance.set(mInstance.get(Calendar.YEAR),
                        mInstance.get(Calendar.MONTH),
                        ++day);
                break;
        }
    }

    @Override
    public void loading() {
        if(postses==null)
        {
            mStatusLayoutManager.showLoading();
        }else
        {
            srl_news_content.setRefreshing(true);
        }


    }

    @Override
    public void success(List<Posts> postses) {

    }


}
