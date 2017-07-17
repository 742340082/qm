/*
 * Copyright 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.UIUtils;
import com.news.config.ConfigNews;

import butterknife.BindView;

/**
 * Created by Lizhaotailang on 2016/8/30.
 */

public class InnerBrowserActivity extends BaseActivity {
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.wv_news)
    WebView wv_news;
    @BindView(R2.id.pb_news)
    ProgressBar pb_news;
    @BindView(R2.id.ll_new_inner_browser)
    LinearLayout ll_new_inner_browser;
    private StatusLayoutManager mStatusLayoutManager;
    private String newsurl;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_inner_browser;
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            hideToobarAnd(UIUtils.getColor(R.color.colorPrimary), UIUtils.getColor(R.color.colorPrimary), true, true);
        }
        setSupportActionBar(toolbar);
        //能够和js交互
        wv_news.getSettings().setJavaScriptEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        wv_news.getSettings().setBuiltInZoomControls(false);
        //缓存
        wv_news.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启DOM storage API功能
        wv_news.getSettings().setDomStorageEnabled(true);
        //开启application Cache功能
        wv_news.getSettings().setAppCacheEnabled(false);

        mStatusLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error).onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        wv_news.reload();
                    }
                }).build();
        this.ll_new_inner_browser.addView(mStatusLayoutManager.getRootLayout(), ll_new_inner_browser.getChildCount() - 1);
    }

    @Override
    public void initData() {
        newsurl = getIntent().getStringExtra(ConfigNews.NEWS_SEND_NEWS_URL);
        wv_news.loadUrl(newsurl);
    }

    @Override
    public void initListener() {
        // 设置在本WebView内可以通过按下返回上一个html页面
        wv_news.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && wv_news.canGoBack()) {
                        wv_news.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
        wv_news.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根

                if (newProgress == 100) {
                    toolbar.setTitle(wv_news.getTitle());
                    mStatusLayoutManager.showContent();
                    pb_news.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    pb_news.setProgress(newProgress);//设置进度值
                    if (pb_news.getVisibility()== View.GONE) {
                        pb_news.setVisibility(View.VISIBLE);
                        mStatusLayoutManager.showLoading();
                    }
                }

            }
        });
        wv_news.setWebViewClient(new WebViewClient(){

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view1, WebResourceRequest request, WebResourceError error) {
                Logger.e("TAG","errorcode:"+error.getErrorCode());
                if (error.getErrorCode()!=-2&&error.getErrorCode()!=-6)
                {
                    mStatusLayoutManager.showNetWorkError();
                }

            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.menu_news_open_browser) {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(wv_news.getUrl())));
        } else if (id == R.id.menu_refresh) {
            wv_news.reload();
        }
        return super.onOptionsItemSelected(item);
    }
}
