package com.game.adapter.gametop;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselibrary.model.game.Game;
import com.baselibrary.utils.GameUtil;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.utils.ViewUtil;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.Glide.with;

/**
 * Created by 74234 on 2017/6/12.
 */

public class GameTopAdapter extends BaseQuickAdapter<Game, BaseViewHolder> {
    public GameTopAdapter(int layoutResId, List<Game> data) {
        super(layoutResId, data);
    }

    public GameTopAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Game item) {

        String downNum = GameUtil.downloadCountSwitch(0, item.getNum_download());
        String size = GameUtil.byteSwitch(0, item.getSize_byte());
        helper.setText(R.id.tv_game_top_title, item.getAppname());
        helper.setText(R.id.tv_game_top_download_number, downNum);
        helper.setText(R.id.tv_game_top_tag_size, size);
        helper.setText(R.id.tv_game_top_type, item.getKind_name());

        ImageView iv_game_top_tag = helper.getView(R.id.iv_game_top_tag);
        TextView tv_game_top_tag = helper.getView(R.id.tv_game_top_tag);
        ImageView iv_game_top_icon = helper.getView(R.id.iv_game_top_icon);

        int layoutPosition = helper.getLayoutPosition();
        ViewUtil.initCutOff(helper, layoutPosition);

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
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),24,0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_game_top_icon);
    }

}
