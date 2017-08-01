package com.game.adapter;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselibrary.base.adapter.CommonShakeAdapter;
import com.baselibrary.model.game.search.GameSearchHotWord;
import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.game.R;

import java.util.List;
import java.util.Random;

/**
 * Created by 74234 on 2017/6/19.
 */

public class GameSearchFirstAdapter extends CommonShakeAdapter<GameSearchHotWord> {
    public GameSearchFirstAdapter(int layoutResId, List<GameSearchHotWord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(GameSearchHotWord gameSearchHotWord, View view) {
        ImageView iv_search_tag_icon = (ImageView) view.findViewById(R.id.iv_game_search_tag_icon);
        TextView tv_search_tag_title = (TextView) view.findViewById(R.id.tv_game_search_tag_title);

        // 设置随机文字大小
        Random random = new Random();
        int size = 16 + random.nextInt(10);// 产生16-25的随机数
        tv_search_tag_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);// 以sp为单位设置文字大小

        // 设置随机文字颜色
        int r = 30 + random.nextInt(210);// 产生30-239的随机颜色, 绕过0-29,
        // 240-255的值,避免颜色过暗或者过亮
        int g = 30 + random.nextInt(210);
        int b = 30 + random.nextInt(210);
        tv_search_tag_title.setTextColor(Color.rgb(r, g, b));
        tv_search_tag_title.setText(gameSearchHotWord.getWord());

        if (StringUtil.isEmpty(gameSearchHotWord.getIcopath())){
            iv_search_tag_icon.setVisibility(View.GONE);
        }else
        {
            Glide.with(UIUtils.getContext())
                    .load(gameSearchHotWord.getIcopath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(iv_search_tag_icon);
        }
    }
}
