package com.game.adapter;

import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;
import com.game.mvp.category.modle.CategoryLink;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 74234 on 2017/6/16.
 */

public class CategoryHeaderAdapter extends BaseQuickAdapter<CategoryLink,BaseViewHolder>{
    public CategoryHeaderAdapter(int layoutResId, List<CategoryLink> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryLink item) {
        helper.setText(R.id.iv_game_category_header_title,item.getName());
        CircleImageView circleImageView = helper.getView(R.id.iv_game_category_header_icon);
        Glide.with(UIUtils.getContext())
                .load(item.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(circleImageView);
    }
}
