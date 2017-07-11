package com.game.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselibrary.utils.FileUtil;
import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;
import com.game.mvp.model.Game;

import java.util.List;

/**
 * Created by 74234 on 2017/6/19.
 */

public class SearchAdapter extends BaseQuickAdapter<Game,BaseViewHolder> {
    public SearchAdapter(int layoutResId, List<Game> data) {
        super(layoutResId, data);
    }

    public SearchAdapter(List<Game> data) {
        super(data);
    }

    public SearchAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Game game) {
        ImageView iv_game_icon = helper.getView(R.id.iv_game_icon);
        TextView tv_game_title = helper.getView(R.id.tv_game_title);
        TextView tv_download_count = helper.getView(R.id.tv_game_download_count);
        TextView tv_download_size = helper.getView(R.id.tv_game_download_size);
        TextView tv_game_desc = helper.getView(R.id.tv_game_desc);
        TextView tv_game_vedio = helper.getView(R.id.tv_game_vedio);
        Button btn_downlaod = helper.getView(R.id.btn_game_downlaod);



        if (StringUtil.isEmpty(game.getVideo_url())) {
            tv_game_vedio.setVisibility(View.GONE);
        }
        Glide.with(UIUtils.getContext())
                .load(game.getIcopath())
                .placeholder(R.drawable.lufei)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.lufei).centerCrop()
                .into(iv_game_icon);
        tv_game_title.setText(game.getAppname());
        tv_download_count.setText(FileUtil.downloadCountSwitch(0, game.getNum_download()));
        tv_download_size.setText(FileUtil.byteSwitch(2, game.getSize_byte()));
        tv_game_desc.setText(game.getReview());
    }
}
