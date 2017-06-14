package com.game.mvp.category;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
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
import com.game.adapter.CategoryAdapter;
import com.game.adapter.IndexAdapter;
import com.game.mvp.category.modle.CategoryLink;
import com.game.mvp.category.modle.CategoryResult;
import com.game.mvp.category.presenter.CategoryPresenter;
import com.game.mvp.category.view.CategoryView;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private CategoryAdapter categoryAdapter;
    private int k;
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
        categoryAdapter.addData(categoryResult.getData());
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void initHeader(List<CategoryLink> categoryLinks) {
        categoryAdapter = new CategoryAdapter(R.layout.item_category_content);
        categoryAdapter.openLoadAnimation(IndexAdapter.SLIDEIN_LEFT);
        rv_category.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rv_category.setAdapter(categoryAdapter);

        FrameLayout headView = (FrameLayout) LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_category_header, new FrameLayout(UIUtils.getContext()));
        LinearLayout linearLayout = (LinearLayout) headView.getChildAt(0);
        k=0;
        for (int i=0;i<linearLayout.getChildCount();i++)
        {
            LinearLayout linearLayout2 = (LinearLayout) linearLayout.getChildAt(i);
            for (int j=0;j<linearLayout2.getChildCount();j++)
            {
                LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(j);
                for (int t=0;t<linearLayout3.getChildCount();t++) {
                    View childAt = linearLayout3.getChildAt(t);
                    if (childAt instanceof CircleImageView) {
                        CircleImageView circleImageView = (CircleImageView) childAt;
                        Glide.with(UIUtils.getContext())
                                .load(categoryLinks.get(k).getIcon())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .into(circleImageView);
                    }
                    if (childAt instanceof TextView) {
                        TextView textView = (TextView) childAt;
                        textView.setText(categoryLinks.get(k).getName());
                    }
                }
                k++;
            }

        }
        categoryAdapter.addHeaderView(headView);
    }
}
