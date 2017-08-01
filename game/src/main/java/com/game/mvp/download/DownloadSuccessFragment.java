package com.game.mvp.download;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baselibrary.api.download.DownloadInfo;
import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.utils.RxBus;
import com.game.R;
import com.game.R2;
import com.game.adapter.GameDownloadIngAdapter;
import com.game.adapter.GameDownloadSuccessAdapter;
import com.game.config.ConfigGame;
import com.game.mvp.download.presenter.DownloadPresenter;
import com.game.mvp.download.view.DownloadSuccessView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by 74234 on 2017/7/31.
 */

public class DownloadSuccessFragment extends BaseFragmnet implements DownloadSuccessView{
    @BindView(R2.id.rv_game_download)
    RecyclerView rv_game_download;
    private DownloadPresenter mPresenter;
    @Override
    public int getLayoutResId() {
        return R.layout.frgament_game_download_total;
    }

    @Override
    public void initData() {
        mPresenter=new DownloadPresenter(this);
        mPresenter.initDownloadSuccess();


    }

    @Override
    public void error(int error, String errorMessage) {

    }

    @Override
    public void loading() {

    }

    @Override
    public void success(List<DownloadInfo> data) {
        final GameDownloadSuccessAdapter gameDownloadAdapter = new GameDownloadSuccessAdapter(R.layout.item_game_top_total,data,getContext());
        gameDownloadAdapter.openLoadAnimation(GameDownloadIngAdapter.ALPHAIN);
        rv_game_download.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_game_download.setAdapter(gameDownloadAdapter);

        RxBus.getDefault().toObservable(ConfigGame.GAME_SEND_DOWNLOAD_DOWNLOADINFO_SUCCESS,DownloadInfo.class)
                .subscribe(new Consumer<DownloadInfo>() {
                    @Override
                    public void accept(DownloadInfo downloadInfo) throws Exception {
                        gameDownloadAdapter.getData().add(0,downloadInfo);
                        gameDownloadAdapter.notifyItemInserted(0);
                    }
                });
    }
}
