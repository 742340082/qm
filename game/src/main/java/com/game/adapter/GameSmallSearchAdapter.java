package com.game.adapter;

import com.baselibrary.model.game.Game;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.List;

/**
 * Created by 74234 on 2017/6/19.
 */

public class GameSmallSearchAdapter extends BaseQuickAdapter<Game,BaseViewHolder> {
    public GameSmallSearchAdapter(int layoutResId) {
        super(layoutResId);
    }

    public GameSmallSearchAdapter(int layoutResId, List<Game> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Game item) {
        helper.setText(R.id.tv_game_small_search_title,item.getAppname());
    }
}
