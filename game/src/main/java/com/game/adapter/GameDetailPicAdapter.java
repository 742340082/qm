package com.game.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.baselibrary.base.adapter.CommonPagerAdapter;
import com.baselibrary.base.adapter.ViewHolder.ViewHolder;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.game.R;


/**
 * Created by 74234 on 2017/6/14.
 */

public class GameDetailPicAdapter extends CommonPagerAdapter<String> {
    public GameDetailPicAdapter(Context context) {
        super(context);
    }

    @Override
    public void convert(ViewHolder viewholder, String data, int position) {
        ImageView iv_new_tab_icon =  viewholder.getView(R.id.iv_tab_icon);
        Glide.with(UIUtils.getContext())
                .load(data)
                .placeholder(R.drawable.lufei)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.lufei).centerCrop()
                .into(iv_new_tab_icon);
    }

    @Override
    public int getLayouId() {
        return R.layout.item_branner_tab;
    }
}
