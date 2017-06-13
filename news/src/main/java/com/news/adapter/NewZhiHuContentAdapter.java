package com.news.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.news.R;
import com.news.mvp.zhihu.modle.ZhihuStorie;

import java.util.List;

public class NewZhiHuContentAdapter
        extends BaseQuickAdapter<ZhihuStorie, BaseViewHolder> {

    public NewZhiHuContentAdapter(int layoutId, List<ZhihuStorie> list) {
        super(layoutId, list);
    }


    protected void convert(BaseViewHolder viewHolder, ZhihuStorie data) {
        Glide.with(UIUtils.getContext())
                .load(data.getImageList().get(0))
                .placeholder(R.drawable.lufei)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.lufei).centerCrop()
                .into((ImageView) viewHolder.getView(R.id.iv_news_item_icon));
        ((TextView) viewHolder.getView(R.id.tv_news_item_title)).setText(data.getTitle());
    }
}
