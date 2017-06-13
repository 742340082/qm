package com.yyp.qm.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.baselibrary.animation.RotateDownTransformer;
import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.SaveConfigValueUtil;
import com.baselibrary.utils.UIUtils;
import com.yyp.qm.R;
import com.yyp.qm.R2;
import com.yyp.qm.adapter.GuideAdpater;
import com.yyp.qm.main.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;


public class GuideActivity
        extends BaseActivity
        implements GuideAdpater.OnFinishGuideActivityListener {
    private ArrayList<View> guideS = new ArrayList();
    @BindView(R2.id.guide_viewpager)
    ViewPager mGuideViewPager;

    public void finishGuideActivity() {
        UIUtils.startActivity(this, MainActivity.class);
        if (!SaveConfigValueUtil.getBoolean(this, ConfigValues.VALUE_STATE_ENTER_HOME, false)) {
            SaveConfigValueUtil.setBoolean(this, ConfigValues.VALUE_STATE_ENTER_HOME, true);
        }
        onBackPressed();
    }

    public int getLayoutResId() {
        return R.layout.activity_guide;
    }

    public void initData() {
        GuideAdpater localGuideAdpater = new GuideAdpater(this);
        localGuideAdpater.setOnFinishGuideActivityListener(this);
        localGuideAdpater.setViews(this.guideS);
        this.mGuideViewPager.setPageTransformer(true, new RotateDownTransformer());
        this.mGuideViewPager.setAdapter(localGuideAdpater);
    }


    public void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            hideToobarAnd( Color.TRANSPARENT, Color.TRANSPARENT,true, true);
        }
        this.guideS.add(UIUtils.inflate(R.layout.guide_item4));
        this.guideS.add(UIUtils.inflate(R.layout.guide_item3));
        this.guideS.add(UIUtils.inflate(R.layout.guide_item2));
        this.guideS.add(UIUtils.inflate(R.layout.guide_item1));
    }
}
