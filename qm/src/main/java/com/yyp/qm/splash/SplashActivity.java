package com.yyp.qm.splash;

import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigSdk;
import com.baselibrary.utils.FileUtil;
import com.baselibrary.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yyp.qm.R;
import com.yyp.qm.main.MainActivity;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

public class SplashActivity
        extends BaseActivity {
    @BindView(R.id.iv_splash)
    ImageView mIv_splash;


    private void enterGuideOrHome() {
        UIUtils.startActivity(this, MainActivity.class);
        finish();
    }

    public int getLayoutResId() {
        return R.layout.activity_splash;
    }

    public void initData() {
        try {
            InputStream open = getAssets().open("style_json.json");
            FileUtil.saveFile(ConfigSdk.SDK_SAVE_CUSTOM_MAP_CONFIG_PATH, open);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            hideToobarAnd(Color.TRANSPARENT, Color.TRANSPARENT, true, false);
        }


            Glide.with(UIUtils.getContext())
                    .load(R.drawable.splash_lufei)
                    .placeholder(R.drawable.lufei)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.lufei).centerCrop()
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mIv_splash.setBackgroundDrawable(resource);
                        }
                    });

    }


    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            enterGuideOrHome();
        }
        return super.onTouchEvent(event);
    }
}
