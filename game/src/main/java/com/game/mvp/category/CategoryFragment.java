package com.game.mvp.category;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.ToastUtils;
import com.game.R;
import com.game.R2;
import com.game.adapter.CategoryAdapter;
import com.game.adapter.RecommendAdapter;
import com.game.mvp.category.modle.CategoryResult;
import com.game.mvp.category.presenter.CategoryPresenter;
import com.game.mvp.category.view.CategoryView;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/6/11.
 */

public class CategoryFragment extends BaseFragmnet implements CategoryView{
    @BindView(R2.id.ll_game_category)
    LinearLayout ll_game_category;
    @BindView(R2.id.srl_game_category)
    SwipeRefreshLayout srl_game_category;
    @BindView(R2.id.rv_category)
    RecyclerView rv_category;
    private CategoryPresenter mPresenter;
    private StatusLayoutManager mStatusLayoutManager;
    private CategoryResult categoryResult;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_category;
    }

    @Override
    public void initData() {
        mPresenter = new CategoryPresenter(this);
        mPresenter.initGameCategory();
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
                        mPresenter.initGameCategory();
                    }
                })
                .build();
        ll_game_category.addView(mStatusLayoutManager.getRootLayout(), ll_game_category.getChildCount() - 1);
    }

    @Override
    public void initListener() {
        srl_game_category.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.initGameCategory();
            }
        });
    }

    @Override
    public void error(int error, String errorMessage) {
        ToastUtils.makeShowToast(getContext(),errorMessage);
        ConfigStateCodeUtil.error(error,mStatusLayoutManager);
    }

    @Override
    public void loading() {
        if (categoryResult==null) {
            mStatusLayoutManager.showLoading();
        }else
        {
            srl_game_category.setRefreshing(true);
        }
    }

    @Override
    public void success(CategoryResult data) {
        categoryResult = data;
        srl_game_category.setRefreshing(false);
        mStatusLayoutManager.showContent();
        CategoryAdapter categoryAdapter=new CategoryAdapter(R.layout.item_category_content,data.getData());
        categoryAdapter.openLoadAnimation(RecommendAdapter.SLIDEIN_LEFT);
        rv_category.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rv_category.setAdapter(categoryAdapter);
    }
}
