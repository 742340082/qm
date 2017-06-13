package com.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.baselibrary.basepager.CommonPagerAdapter;
import com.baselibrary.basepager.ViewHolder.ViewHolder;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.news.R;
import com.news.config.ConfigNews;
import com.news.mvp.detail.NewsDetailActivity;
import com.news.mvp.zhihu.modle.ZhihuTopStorie;

public class NewZhiHuTabAdapter
        extends CommonPagerAdapter<ZhihuTopStorie> {


    public NewZhiHuTabAdapter(Context context) {
        super(context);
    }

    public void convert(ViewHolder viewholder, final ZhihuTopStorie data, int position) {
        ImageView iv_new_tab_icon =  viewholder.getView(R.id.iv_new_tab_icon);
        Glide.with(UIUtils.getContext())
                .load(data.getImage())
                .placeholder(R.drawable.lufei)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.lufei).centerCrop()
                .into(iv_new_tab_icon);
        iv_new_tab_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ConfigNews.NEWS_SEND_NEWS_TYPE,ConfigNews.NEWS_ZHIHU_TYPE);
                intent.putExtra(ConfigNews.NEWS_SEND_ID,data.getTopstorie_id());
                intent.setClass(UIUtils.getContext(), NewsDetailActivity.class);
                UIUtils.startActivity(UIUtils.getContext(),intent);
            }
        });
    }
    public int getLayouId() {
        return R.layout.item_news_tab;
    }
}
