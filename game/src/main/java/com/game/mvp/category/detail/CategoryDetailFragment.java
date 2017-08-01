package com.game.mvp.category.detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselibrary.base.adapter.ViewPagerFragmentAdapter;
import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.model.game.category.detail.CategoryDetailResult;
import com.baselibrary.model.game.category.detail.CategoryDetailSizeOption;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.UIUtils;
import com.baselibrary.view.FlowLayout;
import com.game.R;
import com.game.R2;
import com.game.config.ConfigGame;
import com.game.mvp.category.detail.top.CateoryDetailTopFragment;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by 74234 on 2017/6/15.
 */

public class CategoryDetailFragment extends BaseFragmnet {
    @BindView(R2.id.vp_game_category_detail_tag)
    ViewPager vp_game_category_detail_tag;
    @BindView(R2.id.tl_game_category_detail)
    TabLayout tl_game_category_detail;
    @BindView(R2.id.tv_game_selcted_size)
    TextView tv_game_selcted_size;

    @BindView(R2.id.iv_game_selcted_size_icon)
    ImageView iv_game_selcted_size_icon;
    @BindView(R2.id.fl_game_category_detail)
    FlowLayout fl_game_category_detail;
    @BindView(R2.id.rl_game_category_detail)
    RelativeLayout rl_game_category_detail;
    @BindView(R2.id.ll_game_category_detail)
    LinearLayout ll_game_category_detail;
    private CategoryDetailSizeOption categoryDetailSizeOption;
    private String selectedSizeOption;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_game_category_detail;
    }

    @Override
    public void initData() {
        initTopTag();
        initChangerData();



    }

    private void initChangerData() {

        RxBus.getDefault().toObservable(ConfigGame.GAME_SEND_RX_CATEGORY,
                CategoryDetailResult.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CategoryDetailResult>() {
                    @Override
                    public void accept(CategoryDetailResult result) throws Exception {
                            initSizeOptionData(result);

                    }


                });
    }

    private void initTopTag( ) {

        ViewPagerFragmentAdapter mAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager());


        int kId = getArguments().getInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID);
        int tagId = getArguments().getInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_TAGID);


        Bundle bundle1 = new Bundle();
        bundle1.putInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID, kId);
        bundle1.putInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_TAGID, tagId);
        bundle1.putString(ConfigGame.GAME_SEND_CATEGORY_DETAID_TOP, "rec");

        CateoryDetailTopFragment cateoryDetailTopFragment = new CateoryDetailTopFragment();
        Logger.i("TAG", cateoryDetailTopFragment.toString());
        mAdapter.addTabPage(UIUtils.getString(CategoryDetailTopTab.REC.getResName()), cateoryDetailTopFragment, bundle1);


        Bundle bundle2 = new Bundle();
        bundle2.putInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID, kId);
        bundle2.putInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_TAGID, tagId);
        bundle2.putString(ConfigGame.GAME_SEND_CATEGORY_DETAID_TOP, "hot");

        CateoryDetailTopFragment cateoryDetailTopFragment1 = new CateoryDetailTopFragment();
        Logger.i("TAG", cateoryDetailTopFragment1.toString());
        mAdapter.addTabPage(UIUtils.getString(CategoryDetailTopTab.HOT.getResName()), cateoryDetailTopFragment1, bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_KID, kId);
        bundle3.putInt(ConfigGame.GAME_SEND_CATEGORY_DETAID_TAGID, tagId);
        bundle3.putString(ConfigGame.GAME_SEND_CATEGORY_DETAID_TOP, "new");

        CateoryDetailTopFragment cateoryDetailTopFragment2 = new CateoryDetailTopFragment();
        Logger.i("TAG", cateoryDetailTopFragment2.toString());
        mAdapter.addTabPage(UIUtils.getString(CategoryDetailTopTab.NEW.getResName()), cateoryDetailTopFragment2, bundle3);

        vp_game_category_detail_tag.setAdapter(mAdapter);
        vp_game_category_detail_tag.setOffscreenPageLimit(1);
        vp_game_category_detail_tag.setCurrentItem(0);
        tl_game_category_detail.setupWithViewPager(vp_game_category_detail_tag, true);
    }
    private void initSizeOptionData(CategoryDetailResult result) {
      final   List<CategoryDetailSizeOption> sizeOptions = result.getSizeOptions();
        if (fl_game_category_detail.getChildCount() == 0) {
            for (int i = 0; i < sizeOptions.size(); i++) {

               final View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_game_category_detail_selector_size, null);

                final TextView tv_game_category_detail_selector_size_title = (TextView) view.findViewById(R.id.tv_game_category_detail_selector_size_title);
              final   CategoryDetailSizeOption categoryDetailSizeOption = sizeOptions.get(i);

                tv_game_category_detail_selector_size_title.setText(categoryDetailSizeOption.getTitle());
                tv_game_category_detail_selector_size_title.setOnClickListener(new View.OnClickListener() {



                    @Override
                    public void onClick(View v) {
                        selectedSizeOption = tv_game_category_detail_selector_size_title.getText().toString();
                        tv_game_selcted_size.setText(selectedSizeOption);
                        tv_game_category_detail_selector_size_title.setSelected(true);
                        for (int j = 0; j < fl_game_category_detail.getChildCount(); j++) {
                            View childAt = fl_game_category_detail.getChildAt(j);
                             TextView textView = (TextView) childAt.findViewById(R.id.tv_game_category_detail_selector_size_title);
                            if (textView!=tv_game_category_detail_selector_size_title) {
                                textView.setSelected(false);
                            }
                        }
                        CategoryDetailFragment.this.categoryDetailSizeOption = categoryDetailSizeOption;
                        RxBus.getDefault().post(ConfigGame.GAME_SEND_RX_CATEGORY_DETAIL_SIZEOPTION,
                                CategoryDetailFragment.this.categoryDetailSizeOption);

                        fl_game_category_detail.setVisibility(View.GONE);
                    }
                });
                if (i==0)
                {
                    tv_game_category_detail_selector_size_title.setSelected(true);
                }
                fl_game_category_detail.addView(view);
            }
        }
    }


    @Override
    public void initListener() {
        selectedSizeOption= tv_game_selcted_size.getText().toString();
        rl_game_category_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fl_game_category_detail.getVisibility() == View.VISIBLE) {
                    iv_game_selcted_size_icon.setImageResource(R.drawable.arrow_up);
                    fl_game_category_detail.setVisibility(View.GONE);
                    tv_game_selcted_size.setText(selectedSizeOption);
                } else {
                    iv_game_selcted_size_icon.setImageResource(R.drawable.arrow_down);
                    fl_game_category_detail.setVisibility(View.VISIBLE);
                    tv_game_selcted_size.setText("关闭");
                }
            }
        });
        vp_game_category_detail_tag.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (categoryDetailSizeOption!=null) {
                    RxBus.getDefault().post(ConfigGame.GAME_SEND_RX_CATEGORY_DETAIL_SIZEOPTION, categoryDetailSizeOption);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
