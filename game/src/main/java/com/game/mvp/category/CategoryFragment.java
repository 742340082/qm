package com.game.mvp.category;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.game.R;
import com.game.R2;
import com.game.adapter.CategoryAdapter;
import com.game.adapter.CategoryHeaderAdapter;
import com.game.adapter.IndexAdapter;
import com.game.config.ConfigGame;
import com.game.mvp.category.detail.CategoryDetailActivity;
import com.game.mvp.category.modle.CategoryLink;
import com.game.mvp.category.modle.CategoryResult;
import com.game.mvp.category.presenter.CategoryPresenter;
import com.game.mvp.category.view.CategoryView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/6/11.
 */

public class CategoryFragment extends BaseFragmnet implements CategoryView {
    @BindView(R2.id.ll_game_category)
    LinearLayout ll_game_category;
    @BindView(R2.id.srl_game_category)
    SwipeRefreshLayout srl_game_category;
    @BindView(R2.id.rv_game_category)
    RecyclerView rv_category;


    private CategoryPresenter mPresenter;
    private StatusLayoutManager mStatusLayoutManager;
    private CategoryResult categoryResult;
    private CategoryAdapter categoryAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_game_category;
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
        ToastUtils.makeShowToast(getContext(), errorMessage);
        ConfigStateCodeUtil.error(error, mStatusLayoutManager);
    }

    @Override
    public void loading() {
        if (categoryResult == null) {
            mStatusLayoutManager.showLoading();
        } else {
            srl_game_category.setRefreshing(true);
        }
    }

    @Override
    public void success(CategoryResult data) {
        categoryResult = data;
        srl_game_category.setRefreshing(false);
        mStatusLayoutManager.showContent();
        categoryAdapter.addData(categoryResult.getData());
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void initHeader( List<CategoryLink> categoryLinks) {
        categoryAdapter = new CategoryAdapter(R.layout.item_game_category_content);
        categoryAdapter.openLoadAnimation(IndexAdapter.ALPHAIN);
        rv_category.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv_category.setAdapter(categoryAdapter);


        View headView = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_game_category_header, null);
        RecyclerView rl_game_category_header = (RecyclerView) headView.findViewById(R.id.rl_game_category_header);
        CategoryHeaderAdapter categoryHeaderAdapter = new CategoryHeaderAdapter(R.layout.item_game_category_header_top,categoryLinks);
        rl_game_category_header.setLayoutManager(new GridLayoutManager(UIUtils.getContext(),4));
        rl_game_category_header.setAdapter(categoryHeaderAdapter);
        categoryAdapter.addHeaderView(headView);

        categoryHeaderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryLink categoryLink = (CategoryLink) adapter.getItem(position);

                Intent intent = new Intent(UIUtils.getContext(), CategoryDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                switch (position)
                {
                    case 0:
                    case 4:
                        intent.putExtra(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID,categoryLink.getLink_id());
                        break;
                    case 1:
                    case 2:
                        intent.putExtra(ConfigGame.GAME_SEND_CATEGORY_DETAID_TAGID,categoryLink.getLink_id());
                        break;
                }

                intent.putExtra(ConfigGame.GAME_SEND_CATEGORY_TAG,"全部");
                intent.putExtra(ConfigGame.GAME_SEND_CATEGORY_TITLE,categoryLink.getName());
                intent.putExtra(ConfigGame.GAME_SEND_CATEGORY_TYPE,categoryLink.getType());
                UIUtils.getContext().startActivity(intent);
            }
        });

    }
}
