package com.game.mvp.category.detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.base.adapter.ViewPagerFragmentAdapter;
import com.baselibrary.listener.OnRetryListener;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.baselibrary.utils.ConfigStateCodeUtil;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.ToastUtils;
import com.baselibrary.utils.UIUtils;
import com.game.R;
import com.game.R2;
import com.game.config.ConfigGame;
import com.game.mvp.category.detail.modle.CategoryDetailResult;
import com.game.mvp.category.detail.modle.CategoryDetailTag;
import com.game.mvp.category.detail.top.presenter.CategoryDetailTopPresenter;
import com.game.mvp.category.detail.top.view.CategoryDetailTopView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 74234 on 2017/6/15.
 */

public class CategoryDetailActivity extends BaseActivity implements CategoryDetailTopView {
    @BindView(R2.id.tl_game_category_detail_tag)
    TabLayout tl_game_category_detail_tag;
    @BindView(R2.id.vp_game_category_detail)
    ViewPager vp_game_category_detail;
    @BindView(R2.id.ll_game_category)
    LinearLayout ll_game_category;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    private int curretnTagIndex;
    private String categorytag;
    private int kId;
    private int tagId;
    private CategoryDetailTopPresenter mPresenter;
    private StatusLayoutManager mStatusLayoutManager;
    private int category_type;
    private String title;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_game_category_detail;
    }

    @Override
    public void initData() {
        title = getIntent().getStringExtra(ConfigGame.GAME_SEND_CATEGORY_TITLE);
        kId = getIntent().getIntExtra(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID, 0);
        category_type = getIntent().getIntExtra(ConfigGame.GAME_SEND_CATEGORY_TYPE, -1);
        tagId = getIntent().getIntExtra(ConfigGame.GAME_SEND_CATEGORY_DETAID_TAGID, 0);
        categorytag = getIntent().getStringExtra(ConfigGame.GAME_SEND_CATEGORY_TAG);
        switch (category_type)
        {
            case -1:
            case 3:
                mPresenter = new CategoryDetailTopPresenter(this);
                mPresenter.initCategoryDetail(kId, 0, 0, 1, "rec", 0);
                break;
            case 1:
                mPresenter = new CategoryDetailTopPresenter(this);
                mPresenter.initCategoryDetail(kId, 0, 0, 1, "rec", tagId);
                break;

        }

        toolbar.setTitle(title);
    }

    @Override
    public void initView() {
        toolbar.inflateMenu(R.menu.game_menu_download);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()== R.id.menu_game_download)
                {

                }
                return false;
            }
        });
        mStatusLayoutManager = StatusLayoutManager.newBuilder(this)
                .emptyDataView(R.layout.state_empty)
                .errorView(R.layout.state_error)
                .loadingView(R.layout.state_loading)
                .netWorkErrorView(R.layout.state_network_error)
                .retryViewId(R.id.iv_content_error)
                .onRetryListener(new OnRetryListener() {
                    @Override
                    public void retry() {
                        mPresenter.initCategoryDetail(kId, 0, 0, 1, "rec", 0);
                    }
                })
                .build();
        ll_game_category.addView(mStatusLayoutManager.getRootLayout(), ll_game_category.getChildCount() - 1);
    }

    @Override
    public void error(int error, String errorMessage) {
        ToastUtils.makeShowToast(UIUtils.getContext(), errorMessage);
        ConfigStateCodeUtil.error(error, mStatusLayoutManager);
    }

    @Override
    public void loading() {
        mStatusLayoutManager.showLoading();
    }

    @Override
    public void success(CategoryDetailResult data) {
        tl_game_category_detail_tag.setVisibility(View.VISIBLE);
        mStatusLayoutManager.showContent();
        switch (category_type) {
            case -1:
            case 3:
                ViewPagerFragmentAdapter fragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
                CategoryDetailFragment categoryDetailFragment = null;
                List<CategoryDetailTag> tags = data.getTags();
                CategoryDetailTag categoryDetailTag1 = new CategoryDetailTag();
                categoryDetailTag1.setIcon_tag("0");
                categoryDetailTag1.setTag_id("0");
                categoryDetailTag1.setName("全部");
                tags.add(0, categoryDetailTag1);
                for (int i = 0; i < tags.size(); i++) {
                    CategoryDetailTag categoryDetailTag = tags.get(i);
                    Bundle bundle = new Bundle();
                    bundle.putInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_TAGID, Integer.parseInt(categoryDetailTag.getTag_id()));
                    bundle.putInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID, kId);
                    categoryDetailFragment = new CategoryDetailFragment();
                    Logger.i("TAG", categoryDetailFragment.toString());
                    fragmentAdapter.addTabPage(categoryDetailTag.getName(), categoryDetailFragment, bundle);
                    if (categorytag.equals(categoryDetailTag.getName())) {
                        curretnTagIndex = i;
                    }
                }
                vp_game_category_detail.setAdapter(fragmentAdapter);
                vp_game_category_detail.setOffscreenPageLimit(1);

                vp_game_category_detail.setCurrentItem(curretnTagIndex);

                tl_game_category_detail_tag.setupWithViewPager(vp_game_category_detail, true);
                break;
            case 1:
                ViewPagerFragmentAdapter CategoryType1adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
                Bundle bundle = new Bundle();
                bundle.putInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_TAGID, tagId);
                bundle.putInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID, kId);
                CategoryDetailFragment   categoryDetailFragmentType1 = new CategoryDetailFragment();
                CategoryType1adapter.addTabPage("全部", categoryDetailFragmentType1, bundle);
                vp_game_category_detail.setAdapter(CategoryType1adapter);
                vp_game_category_detail.setOffscreenPageLimit(1);

                vp_game_category_detail.setCurrentItem(curretnTagIndex);

                tl_game_category_detail_tag.setupWithViewPager(vp_game_category_detail, true);
                tl_game_category_detail_tag.setVisibility(View.GONE);
                break;
        }

    }
}
