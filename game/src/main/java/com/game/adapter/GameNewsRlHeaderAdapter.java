package com.game.adapter;

import com.baselibrary.model.game.gamenews.GameNewsLink;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 74234 on 2017/6/16.
 */

public class GameNewsRlHeaderAdapter extends BaseQuickAdapter<GameNewsLink,BaseViewHolder> {
    public GameNewsRlHeaderAdapter(int layoutResId, List<GameNewsLink> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameNewsLink item) {
        helper.setText(R.id.iv_game_category_header_title,item.getName());
        CircleImageView circleImageView = helper.getView(R.id.iv_game_category_header_icon);
        Glide.with(UIUtils.getContext())
                .load(item.getImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(circleImageView);
    }
}
