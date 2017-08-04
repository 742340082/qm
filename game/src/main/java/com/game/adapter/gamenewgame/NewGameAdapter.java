package com.game.adapter.gamenewgame;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.baselibrary.model.game.newgame.NewGameData;
import com.baselibrary.utils.DateFormatter;
import com.baselibrary.utils.UIUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.game.R;

import java.util.List;

/**
 * Created by 74234 on 2017/8/2.
 */

public class NewGameAdapter extends BaseQuickAdapter<NewGameData,BaseViewHolder> {
    public NewGameAdapter(int layoutResId, List<NewGameData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewGameData item) {
        TextView textView = helper.getView(R.id.tv_game_newgame_tag);
        RecyclerView rv_game_newgame_tag = helper.getView(R.id.rv_game_newgame_tag);

        NewGameTagAdapter newGameTagAdapter=new NewGameTagAdapter(R.layout.item_game_index_normal,item.getGames());
        newGameTagAdapter.openLoadAnimation(NewGameTagAdapter.ALPHAIN);
        rv_game_newgame_tag.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        rv_game_newgame_tag.setAdapter(newGameTagAdapter);

        String time= DateFormatter.DoubanDateFormat(item.getDay()*1000);
        textView.setText(time);
    }
}
