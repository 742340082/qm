package com.game.adapter.gamenewgame;

import com.baselibrary.model.game.newgame.NewGameLink;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 74234 on 2017/8/3.
 */

public class NewGameHeaderAdapter extends BaseQuickAdapter<NewGameLink,BaseViewHolder> {
    public NewGameHeaderAdapter(int layoutResId, List<NewGameLink> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewGameLink item) {
        helper.setText(R.id.tv_game_category_header_title,item.getName());
        CircleImageView circleImageView = helper.getView(R.id.iv_game_category_header_icon);
        Glide.with(UIUtils.getContext())
                .load(item.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(circleImageView);
    }
}
