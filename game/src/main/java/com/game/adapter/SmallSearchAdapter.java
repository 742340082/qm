package com.game.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;
import com.game.mvp.model.Game;

import java.util.List;

/**
 * Created by 74234 on 2017/6/19.
 */

public class SmallSearchAdapter extends BaseQuickAdapter<Game,BaseViewHolder> {
    public SmallSearchAdapter(int layoutResId) {
        super(layoutResId);
    }

    public SmallSearchAdapter(int layoutResId, List<Game> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Game item) {
        helper.setText(R.id.tv_small_search_title,item.getAppname());
    }
}
