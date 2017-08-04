package com.news.adapter;

import android.widget.ImageView;

import com.baselibrary.utils.Logger;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.utils.ViewUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.news.R;
import com.news.mvp.doubian.model.DouBianPosts;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class NewDouBianContentAdapter
        extends BaseMultiItemQuickAdapter<DouBianPosts, BaseViewHolder> {
    public   static  final int NEWS_DOUBIAN_NOIMG=1;
    public  static  final int NEWS_DOUBIAN_NORMAL=2;

    public NewDouBianContentAdapter(List<DouBianPosts> list) {
        super(list);
        addItemType(NEWS_DOUBIAN_NOIMG, R.layout.item_news_doubian_noimg);
        addItemType(NEWS_DOUBIAN_NORMAL, R.layout.item_news_doubian_normal);
    }


    protected void convert(BaseViewHolder viewHolder, DouBianPosts douBianBean) {
        ViewUtil.initCutOff(viewHolder,viewHolder.getLayoutPosition());
        Logger.i("TAG","哈哈哈哈");
        switch (douBianBean.getItemType()) {
            case NEWS_DOUBIAN_NOIMG:
                viewHolder.setText(R.id.tv_news_item_title, douBianBean.getTitle());
                return;
            case NEWS_DOUBIAN_NORMAL:
                viewHolder.setText(R.id.tv_news_item_title, douBianBean.getTitle());
                Glide.with(UIUtils.getContext())
                        .load(douBianBean.getLarge_url())
                        .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),10,0))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into((ImageView) viewHolder.getView(R.id.iv_news_item_icon));
                break;
        }

    }
}
