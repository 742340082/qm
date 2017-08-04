package com.game.mvp.index;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.model.game.Game;
import com.baselibrary.model.game.index.AdListGame;
import com.baselibrary.model.game.index.IndexRecBlock;
import com.baselibrary.model.game.index.PosterBlock;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.game.R;
import com.game.R2;
import com.game.adapter.gameindex.IndexAdapter;
import com.game.adapter.gameindex.IndexHeaderAdapter;
import com.game.config.ConfigGame;
import com.game.mvp.detail.GameDetailActivity;
import com.game.mvp.index.presenter.IndexPresenter;
import com.game.mvp.index.view.IndexView;
import com.game.mvp.newgame.NewGameActivity;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by 74234 on 2017/5/18.
 */

public class IndexFragment extends BaseFragmnet implements IndexView {
    @BindView(R2.id.rv_game_index)
    RecyclerView rv_game_index;
    @BindView(R2.id.ll_game_index)
    LinearLayout ll_game_index;
    @BindView(R2.id.srl_game_index)
    SwipeRefreshLayout srl_game_index;
    private IndexPresenter presenter;
    private StatusLayoutManager mStatusLayoutManager;
    private List<AdListGame> dataS;
    private IndexAdapter recommendAdapter;
    private int k;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_game_index;
    }

    @Override
    public void initData() {
        presenter = new IndexPresenter(this);
        presenter.initRecommendGame();
    }

    @Override
    public void initView() {
        mStatusLayoutManager = StatusLayoutManager.newBuilder(getContext())
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        presenter.initRecommendGame();
                    }
                })
                .build();
        ll_game_index.addView(mStatusLayoutManager.getRootLayout(), ll_game_index.getChildCount() - 1);
    }

    @Override
    public void initListener() {
        srl_game_index.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.initRecommendGame();
            }
        });
    }

    @Override
    public void error(int error, String errorMessage) {
        ToastUtils.makeShowToast(getContext(), errorMessage);

        ConfigStateCodeUtil.error(error, mStatusLayoutManager);
    }

    @Override
    public void loading() {
        if (dataS == null) {
            mStatusLayoutManager.showLoading();
        } else {
            srl_game_index.setRefreshing(true);
        }
    }

    @Override
    public void success(List<AdListGame> data) {
        dataS = data;
        mStatusLayoutManager.showContent();
        srl_game_index.setRefreshing(false);
        recommendAdapter.addData(data);
        recommendAdapter.notifyDataSetChanged();
    }


    @Override
    public void initHeader(PosterBlock posterBlock) {
        //出初始化内容数据
        recommendAdapter = new IndexAdapter(null);
        recommendAdapter.openLoadAnimation(IndexAdapter.ALPHAIN);
        rv_game_index.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_game_index.setAdapter(recommendAdapter);

        //添加头部
        View headView = View.inflate(UIUtils.getContext(), R.layout.item_game_index_header, new FrameLayout(UIUtils.getContext()));
        ImageView iv_game_top_icon1 = (ImageView) headView.findViewById(R.id.iv_game_top_icon1);
        ImageView iv_game_top_icon2 = (ImageView) headView.findViewById(R.id.iv_game_top_icon2);
        Glide.with(UIUtils.getContext())
                .load(posterBlock.getRecPoster().get(0).getPoster())
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),10,0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_game_top_icon1);
        Glide.with(UIUtils.getContext())
                .load(posterBlock.getRecPoster().get(1).getPoster())
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(),10,0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_game_top_icon2);

        List<IndexRecBlock> recBlock = posterBlock.getRecBlock();
        IndexHeaderAdapter indexHeaderAdapter = new IndexHeaderAdapter(R.layout.item_game_index_header_tag, recBlock);
        RecyclerView rl_index_header = (RecyclerView) headView.findViewById(R.id.rl_game_index_header);
        rl_index_header.setLayoutManager(new GridLayoutManager(UIUtils.getContext(), 4));
        rl_index_header.setAdapter(indexHeaderAdapter);

        //头部tag点击
        indexHeaderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IndexRecBlock item = (IndexRecBlock) adapter.getItem(position);
                switch (Integer.parseInt(item.getType()))
                {
                    case 2:
                        //男生或者迷你
                        break;
                    case 6:
                        //新游
                        Intent intent = new Intent(IndexFragment.this.getContext(), NewGameActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        //无敌
                        break;
                    case 12:
                        //单机
                        break;
                }
            }
        });

        recommendAdapter.addHeaderView(headView);
        recommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AdListGame adListGame = (AdListGame) adapter.getItem(position);
                Game game = adListGame.getGame();
                if (game != null) {
                    Intent intent = new Intent(getContext(), GameDetailActivity.class);
                    intent.putExtra(ConfigGame.GAME_SEND_GAMEDETAIL_ID, game.getGame_id());
                    intent.putExtra(ConfigGame.GAME_SEND_GAMEDETAIL_APPNAME, game.getAppname());
                    intent.putExtra(ConfigGame.GAME_SEND_GAMEDETAIL_PACKAGE, game.getPackag());
                    startActivity(intent);
                }
            }
        });
    }
}
