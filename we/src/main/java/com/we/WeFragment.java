package com.we;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.baselibrary.base.fragment.BaseFragmnet;
import com.baselibrary.utils.RxBus;
import com.baselibrary.utils.StringUtil;
import com.baselibrary.utils.UIUtils;
import com.we.config.ConfigUser;
import com.we.mvp.receiveaddress.ReceiveAddressActivity;
import com.we.mvp.user.UserActivity;
import com.we.mvp.user.detail.UserDetailActivity;
import com.we.mvp.user.modle.User;
import com.we.mvp.user.setting.SettingActivity;
import com.we.presenter.WePresenter;
import com.we.view.UserMenuView;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by 74234 on 2017/5/4.
 */

public class WeFragment extends BaseFragmnet implements UserMenuView {

    //头部的控件
    @BindView(R2.id.civ_user_icon)
    CircleImageView civ_user_icon;
    @BindView(R2.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R2.id.tv_user_telephone)
    TextView tv_user_telephone;




    //菜单控件
    @BindView(R2.id.ll_user_take_local)
    LinearLayout ll_user_take_local;

    @BindView(R2.id.ll_setting)
    LinearLayout ll_setting;
    @BindView(R2.id.switch_night)
    Switch switch_night;

    private WePresenter presenter;
    private User mUser;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_we;
    }


    @Override
    public void initListener() {

        ll_user_take_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser!=null)
                {
                    Intent intent=new Intent();
                    intent.putExtra(ConfigUser.USER_ACCOUNT, mUser.getAccount());
                    intent.setClass(getContext(),ReceiveAddressActivity.class);
                    startActivity(intent);
                }else
                {
                    UIUtils.startActivity(getContext(),UserActivity.class);
                }

            }
        });
        ll_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startActivity(getContext(), SettingActivity.class);
            }
        });
        civ_user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterActivity();
            }
        });
        tv_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterActivity();
            }
        });

    }
    public void enterActivity() {
        if (this.mUser != null) {
                UIUtils.startActivity(getContext(), UserDetailActivity.class);
        }else {
            UIUtils.startActivity(getContext(), UserActivity.class);
        }

    }
    @Override
    public void initData() {
        RxBus.getDefault().toObservable(ConfigUser.USER_RX_SEND, User.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {

                        resetUI(user);
                    }
                });
        RxBus.getDefault().toObservable(ConfigUser.USER_RX_ICON, Bitmap.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap userIconBitmap) throws Exception {
                        if (userIconBitmap != null) {
                            civ_user_icon.setImageBitmap(userIconBitmap);
                        }
                    }
                });

        presenter = new WePresenter(this);
        presenter.initSaveLogin();

    }


    @Override
    public void resetShowUi() {
        resetUI(null);
    }

    public void resetUI(User user) {

        if (user == null || TextUtils.isEmpty(user.getAccount())) {
            mUser=null;
            tv_user_name.setText(UIUtils.getString(R.string.user_name));
            tv_user_telephone.setText(UIUtils.getString(R.string.user_telephone));
            civ_user_icon.setImageResource(R.drawable.lufei);
        } else {
            mUser=user;
            tv_user_name.setText(user.getName());
            if (StringUtil.isEmpty(user.getPhone()+""))
            {
                tv_user_telephone.setText("");
            }else
            {
                tv_user_telephone.setText(user.getPhone() + "");
            }

        }
    }

    @Override
    public void error(int error, String errorMessage) {

    }

    @Override
    public void loading() {

    }

    @Override
    public void success(List<User> data) {

    }
}
