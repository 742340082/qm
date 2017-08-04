package com.game.adapter.gamenewgame;

import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselibrary.model.game.Game;
import com.baselibrary.utils.GameUtil;
import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.utils.ViewUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by 74234 on 2017/8/3.
 */

public class NewGameTagAdapter extends BaseQuickAdapter<Game,BaseViewHolder>
{
    public NewGameTagAdapter(int layoutResId, List<Game> data) {
        super(layoutResId, data);
    }



     @Override
    protected void convert(BaseViewHolder helper, Game game) {
        FrameLayout convertView = (FrameLayout) helper.getConvertView();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            LinearLayout LinearLayout = (android.widget.LinearLayout) convertView.getChildAt(0);
            LinearLayout.setElevation(1f);
        }

        ImageView iv_game_icon = helper.getView(R.id.iv_game_icon);
        TextView tv_game_title = helper.getView(R.id.tv_game_title);
        TextView tv_download_count = helper.getView(R.id.tv_game_download_count);
        TextView tv_download_size = helper.getView(R.id.tv_game_download_size);
        TextView tv_game_desc = helper.getView(R.id.tv_game_desc);
        TextView tv_game_vedio = helper.getView(R.id.tv_game_vedio);
        Button btn_downlaod = helper.getView(R.id.btn_game_downlaod);

        ViewUtil.initCutOff(helper,helper.getLayoutPosition());

        if (StringUtil.isEmpty(game.getVideo_url())) {
            tv_game_vedio.setVisibility(View.GONE);
        }
        Glide.with(UIUtils.getContext())
                .load(game.getIcopath())
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),24,0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_game_icon);
        tv_game_title.setText(game.getAppname());
        tv_download_count.setText(GameUtil.downloadCountSwitch(0, game.getNum_download()));
        tv_download_size.setText(GameUtil.byteSwitch(2, game.getSize_byte()));
        tv_game_desc.setText(game.getReview());
    }
}
