package com.game.mvp.index;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.game.R;
import com.game.R2;
import com.game.adapter.IndexAdapter;
import com.game.mvp.index.modle.IndexPosterBlock;
import com.game.mvp.index.modle.IndexSubjectGame;
import com.game.mvp.index.presenter.IndexPresenter;
import com.game.mvp.index.view.IndexView;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 74234 on 2017/5/18.
 */

public class IndexFragment extends BaseFragmnet implements IndexView {
    @BindView(R2.id.rv_recommend)
    RecyclerView rv_recommend;
    @BindView(R2.id.ll_game_recommend)
    LinearLayout ll_game_recommend;
    @BindView(R2.id.srl_game_recommend)
    SwipeRefreshLayout srl_game_recommend;
    private IndexPresenter presenter;
    private StatusLayoutManager mStatusLayoutManager;
    private List<IndexSubjectGame> dataS;
    private IndexAdapter recommendAdapter;
    private int k;

    @Override
    public int getLayoutResId() {
        return R.layout.fragement_recomend;
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
        ll_game_recommend.addView(mStatusLayoutManager.getRootLayout(), ll_game_recommend.getChildCount() - 1);
    }

    @Override
    public void initListener() {
        srl_game_recommend.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
            srl_game_recommend.setRefreshing(true);
        }
    }

    @Override
    public void success(List<IndexSubjectGame> data) {
        dataS = data;
        mStatusLayoutManager.showContent();
        srl_game_recommend.setRefreshing(false);
        recommendAdapter.addData(data);
        recommendAdapter.notifyDataSetChanged();
    }


    @Override
    public void initHeader(IndexPosterBlock posterBlock) {
        recommendAdapter = new IndexAdapter(null);
        recommendAdapter.openLoadAnimation(IndexAdapter.SLIDEIN_LEFT);
        rv_recommend.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_recommend.setAdapter(recommendAdapter);

        FrameLayout headView = (FrameLayout) View.inflate(UIUtils.getContext(), R.layout.item_index_header, new FrameLayout(UIUtils.getContext()));
        LinearLayout linearLayout = (LinearLayout) headView.getChildAt(0);
        LinearLayout topHeaderView = (LinearLayout) linearLayout.getChildAt(0);
        ImageView imageView1 = (ImageView) topHeaderView.getChildAt(0);
        ImageView imageView2 = (ImageView) topHeaderView.getChildAt(1);
        Glide.with(UIUtils.getContext())
                .load(posterBlock.getRecPoster().get(0).getPoster())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView1);
        Glide.with(UIUtils.getContext())
                .load(posterBlock.getRecPoster().get(1).getPoster())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView2);


        k = 0;
        for (int t = 1; t < linearLayout.getChildCount(); t++) {
            LinearLayout categoryTagsView = (LinearLayout) linearLayout.getChildAt(t);
            for (int i = 0; i < categoryTagsView.getChildCount(); i++) {
                View childAt = categoryTagsView.getChildAt(i);
                if (childAt instanceof LinearLayout) {
                    LinearLayout linearLayoutTag = (LinearLayout) childAt;
                    for (int j = 0; j < linearLayoutTag.getChildCount(); j++) {
                        View childView = linearLayoutTag.getChildAt(j);
                        if (childView instanceof CircleImageView) {
                            CircleImageView circleImageView = (CircleImageView) childView;
                            Glide.with(UIUtils.getContext())
                                    .load(posterBlock.getRecBlock().get(k).getPoster())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .into(circleImageView);
                        }
                        if (childView instanceof TextView) {
                            TextView textView = (TextView) childView;
                            textView.setText(posterBlock.getRecBlock().get(k).getName());
                        }

                    }
                    k++;
                }
            }
        }
        recommendAdapter.addHeaderView(headView);
    }
}
