package com.game.adapter;

import android.widget.ImageView;

import com.baselibrary.utils.DateFormatter;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;
import com.game.mvp.gamenews.model.GameNewsData;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

        helper.setText(R.id.tv_gamenews_title,item.getTitle());
        helper.setText(R.id.tv_gamenews_desc,item.getDesc());
        helper.setText(R.id.tv_gamenews_publishtime,timeTag);

        with(UIUtils.getContext())
                .load(item.getImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into((ImageView) helper.getView(R.id.iv_gamenews_icon));
    }
}
