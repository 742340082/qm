package com.yyp.qm.splash;

import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigSdk;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.FileUtil;
import com.baselibrary.utils.SaveConfigValueUtil;
import com.baselibrary.utils.UIUtils;
import com.yyp.qm.R;
import com.yyp.qm.activity.GuideActivity;
import com.yyp.qm.main.MainActivity;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

public class SplashActivity
        extends BaseActivity
{
    private boolean isLastHome = false;
    @BindView(R.id.iv_splash)
    ImageView mIv_splash;

    private void animateImage()
    {
//        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(this.mIv_splash, "scaleX", new float[] { 1.0F, 1.13F });
//        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(this.mIv_splash, "scaleY", new float[] { 1.0F, 1.13F });
//        AnimatorSet localAnimatorSet = new AnimatorSet();
//        localAnimatorSet.setDuration(8000L);
//        localAnimatorSet.play(localObjectAnimator1).with(localObjectAnimator2);
//        localAnimatorSet.start();
    }

    private void enterGuideOrHome()
    {
        boolean state_enter_home = SaveConfigValueUtil.getBoolean(this, ConfigValues.VALUE_STATE_ENTER_HOME, false);
        Class clazz =state_enter_home==true?MainActivity.class:GuideActivity.class;
            UIUtils.startActivity(this,clazz);
            finish();
    }

    public int getLayoutResId()
    {
        return R.layout.activity_splash;
    }

    public void initData() {
        try {
            InputStream open = getAssets().open("style_json.json");
            FileUtil.saveFile(ConfigSdk.SDK_SAVE_CUSTOM_MAP_CONFIG_PATH,open);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initListener()
    {
    }


    public void initView()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            hideToobarAnd(Color.TRANSPARENT,Color.TRANSPARENT,true, false);
        }
    }

    public void onBackPressed() {}

    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            enterGuideOrHome();
        }
        return super.onTouchEvent(event);
    }
}
