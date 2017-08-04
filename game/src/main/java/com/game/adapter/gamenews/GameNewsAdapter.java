package com.game.adapter.gamenews;

import android.widget.ImageView;

import com.baselibrary.model.game.gamenews.GameNewsData;
import com.baselibrary.utils.DateFormatter;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.utils.ViewUtil;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.Glide.with;

/**
 * Created by 74234 on 2017/6/13.
 */

public class GameNewsAdapter extends BaseQuickAdapter<GameNewsData,BaseViewHolder> {
    public GameNewsAdapter(int layoutResId) {
        super(layoutResId);
    }

    public GameNewsAdapter(int layoutResId, List<GameNewsData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GameNewsData item) {
        ViewUtil.initCutOff(helper,helper.getLayoutPosition());

        Calendar instance = Calendar.getInstance(Locale.CHINA);
        long currentTime = instance.getTimeInMillis();
        long publishTime = Long.parseLong(item.getDateline())*1000;

        long gapTime=currentTime-publishTime;
        String timeTag=null;
        if (gapTime<0)
        {
            timeTag="刚刚";
        }else
        {
            timeTag = DateFormatter.formatGapTime(gapTime);
        }

        helper.setText(R.id.tv_game_gamenews_title,item.getTitle());
        helper.setText(R.id.tv_game_gamenews_desc,item.getDesc());
        helper.setText(R.id.tv_game_gamenews_publishtime,timeTag);

        with(UIUtils.getContext())
                .load(item.getImg())
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),10,0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) helper.getView(R.id.iv_game_gamenews_icon));
    }
}
