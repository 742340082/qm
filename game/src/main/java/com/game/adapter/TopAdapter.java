package com.game.adapter;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselibrary.utils.FileUtil;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;
import com.game.mvp.model.Game;

import java.util.List;

import static com.bumptech.glide.Glide.with;

/**
 * Created by 74234 on 2017/6/12.
 */

public class TopAdapter extends BaseQuickAdapter<Game, BaseViewHolder> {
    public TopAdapter(int layoutResId, List<Game> data) {
        super(layoutResId, data);
    }

    public TopAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Game item) {

        String downNum = FileUtil.downloadCountSwitch(0, item.getNum_download());
        String size = FileUtil.byteSwitch(0, item.getSize_byte());
        helper.setText(R.id.tv_game_top_title, item.getAppname());
        helper.setText(R.id.tv_game_top_download_number, downNum);
        helper.setText(R.id.tv_game_top_tag_size, size);
        helper.setText(R.id.tv_game_top_type, item.getKind_name());

        ImageView iv_game_top_tag = helper.getView(R.id.iv_game_top_tag);
        TextView tv_game_top_tag = helper.getView(R.id.tv_game_top_tag);
        ImageView iv_game_top_icon = helper.getView(R.id.iv_game_top_icon);

        int layoutPosition = helper.getLayoutPosition();
        if(layoutPosition==0)
        {
            LinearLayout linearLayout = (LinearLayout) helper.getConvertView();
            ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams)
            {
                ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) layoutParams;
                params.setMargins(0,UIUtils.dip2px(10),0,0);
                linearLayout.setLayoutParams(params);
            }
        }else
        {
            LinearLayout linearLayout = (LinearLayout) helper.getConvertView();
            ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams)
            {
                ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) layoutParams;
                params.setMargins(0,0,0,0);
                linearLayout.setLayoutParams(params);
            }
        }

        if (layoutPosition <= 2) {
            tv_game_top_tag.setVisibility(View.GONE);
            iv_game_top_tag.setVisibility(View.VISIBLE);
            DrawableTypeRequest<Integer> load = null;
            switch (layoutPosition) {
                case 0:
                    load = with(UIUtils.getContext()).load(R.drawable.icon_game_top_number1);
                    break;
                case 1:
                    load = with(UIUtils.getContext()).load(R.drawable.icon_game_top_number2);
                    break;
                case 2:
                    load = Glide.with(UIUtils.getContext()).load(R.drawable.icon_game_top_number3);
                    break;
            }
            load.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(iv_game_top_tag);
        } else {
            tv_game_top_tag.setVisibility(View.VISIBLE);
            iv_game_top_tag.setVisibility(View.GONE);
            if (layoutPosition >= 99) {
                Typeface font = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL);
                tv_game_top_tag.setTypeface(font);
                tv_game_top_tag.setTextSize(12);
                tv_game_top_tag.setText(layoutPosition+1 + "");
            } else {
                Typeface font = Typeface.create(Typeface.DEFAULT,Typeface.BOLD);
                tv_game_top_tag.setTypeface(font);
                tv_game_top_tag.setTextSize(14);
                tv_game_top_tag.setText(layoutPosition+1 + "");
            }
        }

        with(UIUtils.getContext())
                .load(item.getIcopath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(iv_game_top_icon);
    }
}
