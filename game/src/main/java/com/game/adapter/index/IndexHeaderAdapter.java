package com.game.adapter.index;

import com.baselibrary.model.game.index.IndexRecBlock;
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

public class IndexHeaderAdapter extends BaseQuickAdapter<IndexRecBlock,BaseViewHolder> {
    public IndexHeaderAdapter(int layoutResId, List<IndexRecBlock> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexRecBlock item) {
        helper.setText(R.id.iv_game_index_category_title,item.getName());
        CircleImageView iv_index_category_icon =  helper.getView(R.id.iv_game_index_category_icon);
        Glide.with(UIUtils.getContext())
                .load(item.getPoster())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(iv_index_category_icon);
    }
}
