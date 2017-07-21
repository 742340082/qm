package com.game.mvp.download;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baselibrary.api.download.DownloadInfo;
import com.baselibrary.api.download.HttpDownload;
import com.baselibrary.base.activity.BaseActivity;
import com.game.R;
import com.game.R2;
import com.game.adapter.GameDownloadAdapter;
import com.game.mvp.model.Game;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/7/20.
 */

public class DownLoadActivity extends BaseActivity {
    private ArrayList<DownloadInfo<Game>> downLoadInfos;
    @BindView(R2.id.rv_game_download)
    RecyclerView rv_game_download;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_game_download;
    }

    @Override
    public void initData() {
        downLoadInfos=new ArrayList<>();
        ConcurrentHashMap<String, DownloadInfo> downloadInfoMap = HttpDownload.getInstance().getDownloadInfoMap();
        for (Map.Entry<String,DownloadInfo> entry:downloadInfoMap.entrySet())
        {
            DownloadInfo value = entry.getValue();
            downLoadInfos.add(value);
        }

        GameDownloadAdapter gameDownloadAdapter = new GameDownloadAdapter(R.layout.item_game_top_total, downLoadInfos);
        gameDownloadAdapter.openLoadAnimation(GameDownloadAdapter.ALPHAIN);
        rv_game_download.setLayoutManager(new LinearLayoutManager(this));
        rv_game_download.setAdapter(gameDownloadAdapter);

    }


}
