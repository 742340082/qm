package com.yyp.qm.splash;

import android.graphics.Color;
import android.os.Build;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.baselibrary.base.activity.BaseActivity;
import com.baselibrary.config.ConfigSdk;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.GameUtil;
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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity
        extends BaseActivity {
    @BindView(R.id.iv_splash)
    ImageView mIv_splash;


    private void enterGuideOrHome() {
        Observable.interval(0L, 1L, TimeUnit.SECONDS).take(ConfigValues.VALUE_SPLASH_BACK_TIME + 1)
                .observeOn(Schedulers.io())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        long l = aLong.longValue();
                        aLong.longValue();
                        return Integer.valueOf(Long.valueOf(ConfigValues.VALUE_SPLASH_BACK_TIME - l).intValue());
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if (integer==0) {
                    UIUtils.startActivity(SplashActivity.this, MainActivity.class);
                    finish();
                }
            }
        });

    }

    public int getLayoutResId() {
        return R.layout.activity_splash;
    }

    public void initData() {
        try {
            InputStream open = getAssets().open("style_json.json");
            GameUtil.saveFile(ConfigSdk.SDK_SAVE_CUSTOM_MAP_CONFIG_PATH, open);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            hideToobarAnd(Color.TRANSPARENT, Color.TRANSPARENT, true, false);
        }
        enterGuideOrHome();

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
