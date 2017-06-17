package com.game.mvp.category.detail.top;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.game.R;
import com.game.R2;
import com.game.adapter.CategoryDetailTopAdapter;
import com.game.adapter.IndexAdapter;
import com.game.config.ConfigGame;
import com.game.mvp.category.detail.modle.CategoryDetailResult;
import com.game.mvp.category.detail.modle.CategoryDetailSizeOption;
import com.game.mvp.category.detail.top.presenter.CategoryDetailTopPresenter;
import com.game.mvp.category.detail.top.view.CategoryDetailTopView;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by 74234 on 2017/6/15.
 */

public class CateoryDetailTopFragment extends BaseFragmnet implements CategoryDetailTopView, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R2.id.ll_game_top)
    LinearLayout ll_game_top;
    @BindView(R2.id.srl_game_top)
    SwipeRefreshLayout srl_game_top;
    @BindView(R2.id.rv_game_top)
    RecyclerView rv_game_top;
    private StatusLayoutManager mStatusLayoutManager;
    private CategoryDetailTopPresenter mPresenter;
    private CategoryDetailResult categoryDetailResult;
    private int kId;
    private int tagId;
    private String top;
    private int startSize;
    private int endSize;
    private CategoryDetailTopAdapter categoryDetailTopAdapter;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_total;
    }

    @Override
    public void initData() {
        mPresenter = new CategoryDetailTopPresenter(this);
        kId = getArguments().getInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID);
        tagId = getArguments().getInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_TAGID);
        top = getArguments().getString(ConfigGame.GAME_SEND_CATEGORY_DETAID_TOP);

        mPresenter.initCategoryDetail(kId, startSize, endSize, 1, top, tagId);


        RxBus.getDefault().toObservable(ConfigGame.GAME_SEND_RX_CATEGORY_DETAIL_SIZEOPTION,
                CategoryDetailSizeOption.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CategoryDetailSizeOption>() {

                    @Override
                    public void accept(CategoryDetailSizeOption categoryDetailSizeOption) throws Exception {
                        String[] split = categoryDetailSizeOption.getValue().split(",");
                        int startSize = Integer.parseInt(split[0]);
                        int endSize = Integer.parseInt(split[1]);
                        if (CateoryDetailTopFragment.this.startSize != startSize ||
                                CateoryDetailTopFragment.this.endSize != endSize) {
                            if (isVisible) {
                                mPresenter.initCategoryDetail(kId, startSize, endSize, 1, top, tagId);
                                CateoryDetailTopFragment.this.startSize = startSize;
                                CateoryDetailTopFragment.this.endSize = endSize;
                            }
                        }

                    }
                });
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
                        mPresenter.initCategoryDetail(kId, startSize, endSize, 1, top, tagId);
                    }
                })
                .build();
        ll_game_top.addView(mStatusLayoutManager.getRootLayout(), ll_game_top.getChildCount() - 1);
    }

    @Override
    public void initListener() {
        srl_game_top.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.initCategoryDetail(kId, startSize, endSize, 1, top, tagId);
            }
        });
    }

    @Override
    public void error(int error, String errorMessage) {
        ToastUtils.makeShowToast(UIUtils.getContext(), errorMessage);
        ConfigStateCodeUtil.error(error, mStatusLayoutManager);
        if (error == ConfigStateCode.STATE_LOAD_MORE_FAILURES) {
            categoryDetailTopAdapter.loadMoreFail();
        }
    }

    @Override
    public void loading() {
        if (categoryDetailResult == null) {
            mStatusLayoutManager.showLoading();
        }
    }

    @Override
    public void success(CategoryDetailResult data) {
        categoryDetailResult = data;
        mStatusLayoutManager.showContent();
        srl_game_top.setRefreshing(false);

        if (data.getPage() == 1) {
            categoryDetailTopAdapter = new CategoryDetailTopAdapter(R.layout.item_index_normal, categoryDetailResult.getData());
            categoryDetailTopAdapter.openLoadAnimation(IndexAdapter.SLIDEIN_LEFT);
            categoryDetailTopAdapter.loadMoreEnd(true);
            categoryDetailTopAdapter.setOnLoadMoreListener(this, rv_game_top);
            rv_game_top.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_game_top.setAdapter(categoryDetailTopAdapter);
        } else {
            categoryDetailTopAdapter.addData(data.getData());
            categoryDetailTopAdapter.loadMoreComplete();
        }

    }

    @Override
    public void onLoadMoreRequested() {
        if (categoryDetailTopAdapter.getItemCount() >= categoryDetailResult.getCount()) {
            categoryDetailTopAdapter.loadMoreEnd();
        } else {
            mPresenter.initCategoryDetail(kId, 0, 0, categoryDetailResult.getStartKey(), top, tagId);
        }
    }


}
