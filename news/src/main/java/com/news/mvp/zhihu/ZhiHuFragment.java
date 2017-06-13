package com.news.mvp.zhihu;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.news.R;
import com.news.R2;
import com.news.adapter.NewZhiHuContentAdapter;
import com.news.adapter.NewZhiHuTabAdapter;
import com.news.config.ConfigNews;
import com.news.mvp.detail.NewsDetailActivity;
import com.news.mvp.zhihu.modle.Storie;
import com.news.mvp.zhihu.modle.TopStorie;
import com.news.mvp.zhihu.presenter.ZhiHuPresenter;
import com.news.mvp.zhihu.view.ZhiHuView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class ZhiHuFragment
        extends BaseFragmnet
        implements ZhiHuView, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R2.id.fab)
    FloatingActionButton fab;
    @BindView(R2.id.new_fl_container)
    FrameLayout new_fl_container;
    @BindView(R2.id.rv_news_content)
    RecyclerView rv_news_content;
    @BindView(R2.id.srl_news_content)
    SwipeRefreshLayout srl_news_content;
    public Calendar mInstance;
    private NewZhiHuContentAdapter zhihuAdapter;
    public ZhiHuPresenter mPresenter;
    private StatusLayoutManager mStatusLayoutManager;
    private List<Storie> storieS;


    @Override
    public void click(View paramView, int paramInt) {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.news_content;
    }

    @Override
    public void initData() {
        if (this.mPresenter == null) {
            mInstance = Calendar.getInstance();
            this.mPresenter = new ZhiHuPresenter(getContext(), this);
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
                        mPresenter.selectreTimefreshData(mInstance.getTimeInMillis());
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
                        mPresenter.selectreTimefreshData(mInstance.getTimeInMillis());
                    }
                }).build();
        this.new_fl_container.addView(mStatusLayoutManager.getRootLayout(),new_fl_container.getChildCount()-1);
        this.rv_news_content.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView paramAnonymousRecyclerView, int paramAnonymousInt) {
                super.onScrollStateChanged(paramAnonymousRecyclerView, paramAnonymousInt);
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    ZhiHuFragment.this.fab.hide();
                    return;
                }
                ZhiHuFragment.this.fab.show();
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
    public void error(int error, String errorMessage) {
        this.srl_news_content.setRefreshing(false);
        ToastUtils.makeShowToast(getContext(), errorMessage);
        switch (error) {
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
                this.zhihuAdapter.loadMoreFail();
                int day = mInstance.get(Calendar.DAY_OF_MONTH);
                mInstance.set(mInstance.get(Calendar.YEAR),
                        mInstance.get(Calendar.MONTH),
                        ++day);
                break;
        }
    }

    @Override
    public void loading() {

        if (storieS ==null) {
            mStatusLayoutManager.showLoading();
        }else
        {
            this.srl_news_content.setRefreshing(true);
        }
    }

    @Override
    public void success(List<Storie> data) {
        this.srl_news_content.setRefreshing(false);
        mStatusLayoutManager.showContent();
        List<Storie> data1 = zhihuAdapter.getData();
        data1.clear();
        zhihuAdapter.addData(data);
        zhihuAdapter.notifyDataSetChanged();
    }


    @Override
    public void showContent(List<Storie> stories,List<TopStorie> topStories, boolean isLoadMore) {
        this.storieS = stories;
        mStatusLayoutManager.showContent();
        srl_news_content.setRefreshing(false);
        if (!isLoadMore) {



            zhihuAdapter = new NewZhiHuContentAdapter(R.layout.item_news_content,stories);
            zhihuAdapter.openLoadAnimation(NewZhiHuContentAdapter.SLIDEIN_LEFT);
            zhihuAdapter.setEnableLoadMore(true);
            zhihuAdapter.setOnLoadMoreListener(this, rv_news_content);
            initTop(topStories);
            rv_news_content.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
            rv_news_content.setAdapter(zhihuAdapter);

        } else {
            zhihuAdapter.addData(stories);
            zhihuAdapter.loadMoreComplete();
        }
        zhihuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Storie item = (Storie) adapter.getItem(position);
                Intent intent=new Intent();
                intent.putExtra(ConfigNews.NEWS_SEND_ID,item.getStorie_id());
                intent.putExtra(ConfigNews.NEWS_SEND_NEWS_TYPE,ConfigNews.NEWS_ZHIHU_TYPE);
                intent.setClass(getContext(), NewsDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initTop(List<TopStorie> topStories) {

        ViewPager viewPager = new ViewPager(getContext());
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(200)));
        NewZhiHuTabAdapter zhiHuTabAdapter = new NewZhiHuTabAdapter(getContext());
        zhiHuTabAdapter.addTabPage(topStories);
        viewPager.setAdapter(zhiHuTabAdapter);
        zhihuAdapter.addHeaderView(viewPager,0, LinearLayout.VERTICAL);
    }
}
