package com.news.mvp.detail;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.news.InnerBrowserActivity;
import com.news.R;
import com.news.R2;
import com.news.config.ConfigNews;
import com.news.mvp.detail.modle.DoubanDetail;
import com.news.mvp.detail.modle.GuokeDetail;
import com.news.mvp.detail.modle.ZhiHuDetail;
import com.news.mvp.detail.presenter.NewsDetailPresenter;
import com.news.mvp.detail.view.NewsDetailView;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/5/12.
 */

public class NewsDetailActivity extends BaseActivity implements NewsDetailView {
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.wv_news)
    WebView wv_news;
    @BindView(R2.id.ll_new_nsw_container)
    LinearLayout rl_new_nsw_container;
    @BindView(R2.id.pb_news)
    ProgressBar pb_news;
    @BindView(R2.id.ctl_news)
    CollapsingToolbarLayout ctl_news;
    @BindView(R2.id.iv_news_item_icon)
    ImageView iv_news_item_icon;

    private NewsDetailPresenter mPresenter;
    private StatusLayoutManager mStatusLayoutManager;
    private GuokeDetail mGuokeDetail;
    private DoubanDetail mDoubanDetail;
    private ZhiHuDetail mZhiHuDetail;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= 21) {
            hideToobarAnd(UIUtils.getColor(R.color.transparent), UIUtils.getColor(R.color.transparent), true, true);
        }

        mStatusLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error).onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        refreshData();
                    }
                }).build();
        this.rl_new_nsw_container.addView(mStatusLayoutManager.getRootLayout(), rl_new_nsw_container.getChildCount() - 1);

        WebSettings settings = wv_news.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        //开启DOM storage API功能
        settings.setDomStorageEnabled(true);
        //开启application Cache功能
        settings.setAppCacheEnabled(false);
        settings.setBuiltInZoomControls(false);
        if (NetworkState.networkConnected(this)) {
            //有网络连接，设置默认缓存模式
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //无网络连接，设置本地缓存模式
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }


    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        wv_news.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根

                if (newProgress == 100) {
                    mStatusLayoutManager.showContent();
                    pb_news.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    pb_news.setProgress(newProgress);//设置进度值
                }

            }
        });
        wv_news.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Intent intent = new Intent();
                intent.putExtra(ConfigNews.NEWS_SEND_NEWS_URL, url);
                intent.setClass(NewsDetailActivity.this, InnerBrowserActivity.class);
                startActivity(intent);
                return true;
            }

        });
    }

    @Override
//设置回退
//覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv_news.canGoBack()) {
            wv_news.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        int newsType = intent.getIntExtra(ConfigNews.NEWS_SEND_NEWS_TYPE, -1);
        if (newsType != -1) {

            mPresenter = new NewsDetailPresenter(this, newsType);
            refreshData();
        }
    }

    @Override
    public void onBackPressed() {
        if (mZhiHuDetail!=null||mDoubanDetail!=null||mGuokeDetail!=null)
        {
            super.onBackPressed();
        }

    }

    private void refreshData() {
        Intent intent = getIntent();
        int newsType = intent.getIntExtra(ConfigNews.NEWS_SEND_NEWS_TYPE, -1);
        long newsID = intent.getLongExtra(ConfigNews.NEWS_SEND_ID, -1);
        if (newsID != -1) {
            mPresenter.initNewsInfo(newsID + "", newsType);
        }
    }

    // to change the title's font size of toolbar layout
    private void setCollapsingToolbarLayoutTitle(String title) {
        ctl_news.setTitle(title);
        ctl_news.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        ctl_news.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        ctl_news.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        ctl_news.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }


    @Override
    public void error(int error, String errorMessage) {
        mZhiHuDetail=new ZhiHuDetail();
        mDoubanDetail=new DoubanDetail();
        mGuokeDetail=new GuokeDetail();
        ToastUtils.makeShowToast(this, errorMessage);
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
        }
    }

    @Override
    public void loading() {
        pb_news.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
        mStatusLayoutManager.showLoading();
    }

    @Override
    public void success(String data) {

    }

    @Override
    public void success(GuokeDetail guokeDetail) {
        mGuokeDetail = guokeDetail;

        Glide.with(this)
                .load(guokeDetail.getImage())
                .asBitmap()
                .placeholder(R.drawable.lufei)
                .centerCrop()
                .error(R.drawable.lufei)
                .into(iv_news_item_icon);
        setCollapsingToolbarLayoutTitle(guokeDetail.getTitle());
        wv_news.loadDataWithBaseURL("x-data://base", mPresenter.convertGuokrContent(guokeDetail.getBody()), "text/html", "utf-8", null);
    }

    @Override
    public void success(ZhiHuDetail zhiHuDetail) {
        mZhiHuDetail = zhiHuDetail;
            setCollapsingToolbarLayoutTitle(zhiHuDetail.getTitle());
            if (zhiHuDetail.getBody() == null) {
                wv_news.loadUrl(zhiHuDetail.getShare_url());
            } else {
                wv_news.loadDataWithBaseURL("x-data://base", mPresenter.convertZhihuContent(zhiHuDetail.getBody()), "text/html", "utf-8", null);
            }
                Glide.with(this)
                        .load(zhiHuDetail.getImage())
                        .asBitmap()
                        .placeholder(R.drawable.lufei)
                        .centerCrop()
                        .error(R.drawable.lufei)
                        .into(iv_news_item_icon);
    }

    @Override
    public void success(DoubanDetail doubanDetail) {
        mDoubanDetail = doubanDetail;
        if (mDoubanDetail!=null) {
            Glide.with(this)
                    .load(doubanDetail.getLarge_url())
                    .asBitmap()
                    .placeholder(R.drawable.lufei)
                    .centerCrop()
                    .error(R.drawable.lufei)
                    .into(iv_news_item_icon);
            setCollapsingToolbarLayoutTitle(doubanDetail.getTitle());
            wv_news.loadDataWithBaseURL("x-data://base", mPresenter.convertDoubanContent(doubanDetail), "text/html", "utf-8", null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
