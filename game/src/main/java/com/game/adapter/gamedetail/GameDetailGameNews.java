package com.game.adapter.gamedetail;

import android.widget.TextView;

import com.baselibrary.model.game.detail.summary.GameSummaryRelate;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.List;

/**
 * Created by 74234 on 2017/7/28.
 */

public class GameDetailGameNews extends BaseQuickAdapter<GameSummaryRelate,BaseViewHolder> {

    private int[] gameNewsBackgroundS={R.drawable.bg_round_blue_shape,
            R.drawable.bg_round_green_shape,R.drawable.bg_round_red_shape,
            R.drawable.bg_round_yellow_shape,R.drawable.bg_round_orange_shape,
            R.drawable.bg_round_pink_shape,R.drawable.bg_round_purple_shape};

    public GameDetailGameNews(int layoutResId, List<GameSummaryRelate> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameSummaryRelate item) {
        TextView tv_game_detail_gamenews_title = helper.getView(R.id.tv_game_detail_gamenews_title);
        TextView tv_game_detail_gamenews_content = helper.getView(R.id.tv_game_detail_gamenews_content);
        tv_game_detail_gamenews_content.setText(item.getTitle());
        switch (Integer.parseInt(item.getType()))
        {
            case 2:
                tv_game_detail_gamenews_title.setText("活动");
                tv_game_detail_gamenews_title.setBackgroundResource(gameNewsBackgroundS[0]);
                break;
            case 4:
                tv_game_detail_gamenews_title.setText("评测");
                tv_game_detail_gamenews_title.setBackgroundResource(gameNewsBackgroundS[1]);
                break;
            case 5:
                tv_game_detail_gamenews_title.setText("新闻");
                tv_game_detail_gamenews_title.setBackgroundResource(gameNewsBackgroundS[3]);
                break;
        }
    }
}
