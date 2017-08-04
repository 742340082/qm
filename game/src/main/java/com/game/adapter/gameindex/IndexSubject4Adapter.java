package com.game.adapter.gameindex;

import android.widget.ImageView;
import android.widget.TextView;

import com.baselibrary.model.game.Game;
import com.baselibrary.utils.GameUtil;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.List;

/**
 * Created by 74234 on 2017/8/1.
 */

public class IndexSubject4Adapter extends BaseQuickAdapter<Game, BaseViewHolder> {
    public IndexSubject4Adapter(int layoutResId, List<Game> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Game game) {
        ImageView iv_game_index_subject_icon = helper.getView(R.id.iv_game_index_subject_icon);
        TextView tv_subject_tag_title = helper.getView(R.id.tv_game_index_subject_title);
        TextView tv_game_index_subject_download_title = helper.getView(R.id.tv_game_index_subject_download_title);
        Glide.with(UIUtils.getContext())
                .load(game.getIcopath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(iv_game_index_subject_icon);
        tv_subject_tag_title.setText(game.getAppname());
        tv_game_index_subject_download_title.setText(GameUtil.byteSwitch(0,game.getSize_byte()));
    }
}
