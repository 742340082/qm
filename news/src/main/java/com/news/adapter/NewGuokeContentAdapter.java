package com.news.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.baselibrary.utils.UIUtils;
import com.baselibrary.utils.ViewUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.news.R;
import com.news.mvp.guoke.model.GuoKeResult;

import java.util.List;

public class NewGuokeContentAdapter
        extends BaseQuickAdapter<GuoKeResult, BaseViewHolder> {

    public NewGuokeContentAdapter(int layoutId, List<GuoKeResult> list) {
        super(layoutId, list);
    }

    protected void convert(BaseViewHolder viewHolder, GuoKeResult guoKeResult) {
        ViewUtil.initCutOff(viewHolder,viewHolder.getLayoutPosition());
        Glide.with(UIUtils.getContext())
                .load(guoKeResult.getHeadline_img())
                .placeholder(R.drawable.lufei)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.lufei).centerCrop()
                .into((ImageView) viewHolder.getView(R.id.iv_news_item_icon));
        ((TextView) viewHolder.getView(R.id.tv_news_item_title)).setText(guoKeResult.getTitle());
    }
}
