package com.game.adapter.gamedetail;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselibrary.model.game.Game;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by 74234 on 2017/7/28.
 */

public class GameDetailRecommend extends BaseQuickAdapter<Game,BaseViewHolder> {
    public GameDetailRecommend(int layoutResId, List<Game> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Game item) {
        ImageView iv_game_detail_recommend_game_icon = helper.getView(R.id.iv_game_detail_recommend_game_icon);
        TextView tv_game_detail_recommend_game_title = helper.getView(R.id.tv_game_detail_recommend_game_title);
        Button btn_game_detail_recommend_download = helper.getView(R.id.btn_game_detail_recommend_download);

        Glide.with(UIUtils.getContext())
                .load(item.getIcopath())
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),24,0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_game_detail_recommend_game_icon);

        tv_game_detail_recommend_game_title.setText(item.getAppname());
    }
}
