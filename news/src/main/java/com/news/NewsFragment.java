package com.news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.base.adapter.ViewPagerFragmentAdapter;
import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.utils.UIUtils;
import com.news.mvp.doubian.DouBianFragment;
import com.news.mvp.guoke.GuoKeFragment;
import com.news.mvp.zhihu.ZhiHuFragment;

import butterknife.BindView;

public class NewsFragment
        extends BaseFragmnet
{
    @BindView(R2.id.vp_news)
    ViewPager vp_news;
    @BindView(R2.id.tl_news)
    TabLayout tl_news;

//    @BindView(R2.id.news_toolbar)
//    Toolbar news_toolbar;


    private void initFragmentData()
    {
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getChildFragmentManager());
        Bundle bundle = getBundle("我是知乎");
        ZhiHuFragment zhiHuFragment = new ZhiHuFragment();
        adapter.addTabPage(UIUtils.getString(R.string.new_zhihu),zhiHuFragment, bundle);

        bundle = getBundle("我是DouBian");
        DouBianFragment bianFragment = new DouBianFragment();
        adapter.addTabPage(UIUtils.getString(R.string.new_doubian), bianFragment, bundle);

        bundle = getBundle("我是果壳");
        GuoKeFragment guoKeFragment = new GuoKeFragment();
        adapter.addTabPage(UIUtils.getString(R.string.new_guoke),guoKeFragment, bundle);

        vp_news.setAdapter(adapter);
        vp_news.setOffscreenPageLimit(1);

        vp_news.setCurrentItem(0);
        tl_news.setupWithViewPager(vp_news);

    }
    public Bundle getBundle(String content)
    {
        Bundle localBundle = new Bundle();
        localBundle.putString("title", content);
        return localBundle;
    }

    public int getLayoutResId()
    {
        return R.layout.fragment_news;
    }


    public void initView()
    {
        BaseActivity baseActivity = (BaseActivity)getActivity();
//        this.news_toolbar.setTitle("新闻");
//        this.news_toolbar.setTitleTextColor(-1);
//        localMainActivity.setSupportActionBar(this.news_toolbar);
        initFragmentData();
    }


}
