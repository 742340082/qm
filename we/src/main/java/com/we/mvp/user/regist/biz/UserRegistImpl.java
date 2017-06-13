package com.we.mvp.user.regist.biz;

import android.text.TextUtils;
import android.widget.Toast;

import com.baselibrary.config.ConfigSdk;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.we.config.ConfigUser;
import com.we.mvp.user.baseLogic.LogicOperateUser;
import com.we.mvp.user.modle.User;
import com.we.mvp.user.regist.view.RegistView;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class UserRegistImpl
        implements UserRegist {
    private String countDown;
    private EventHandler mEventHandler;
    private RegistView mRegistView;
    private User mUser;
    private LogicOperateUser logicOperateUser;

    public UserRegistImpl(RegistView registView) {
        this.mRegistView = registView;
        logicOperateUser = new LogicOperateUser(registView);
    }


    @Override
    public boolean confirmation() {
        String telephone = this.mRegistView.getUserTelephone();
        this.countDown = this.mRegistView.getCountDown();
        String password = null;
        String code = null;
        //获取到初始化的值
        if (telephone != null) {
            code = mRegistView.getUserCode();
            if (code != null) {
                password = this.mRegistView.getPassword();
            }
        }
        //不是智能验证
        if ((telephone == null) || (password == null) || (code == null)) {
            return false;
        }
        if (!NetworkState.networkConnected(UIUtils.getContext())) {
            mRegistView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            return false;
        }
        mRegistView.loading();
        this.mUser = new User();
        mUser.setPhone(Long.parseLong(telephone));
        mUser.setPassword(password);
        return true;


    }

    @Override
    public void destroyMobSms() {
        SMSSDK.unregisterEventHandler(this.mEventHandler);
    }

    @Override
    public void initMobSms() {
        SMSSDK.initSDK(UIUtils.getContext(), ConfigSdk.SDK_SMSSDK_KEY, ConfigSdk.SDK_SMSSDK_SECRET);
        this.mEventHandler = new EventHandler() {
            public void afterEvent(final int eventHandler, final int result, final Object data) {
                UIUtils.runOnUIThread(new Runnable() {

                    public void run() {

                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //成功的时候

                            if (eventHandler == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                //校验验证码成功

                                logicOperateUser.registUser(mUser.getPhone().toString(), mUser.getPassword(), null, null, ConfigUser.USER_TELEPHONE_REGIST);

                            }
                            if (eventHandler == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                //发送验证码成功
                                //mFlag true表示智能验证 false短信验证
                                boolean flag = (boolean) data;
                                mRegistView.isBrainConfirmation(flag);
                            }
                        } else if (result == SMSSDK.RESULT_ERROR) {
                            //失败的时候
                            try {
                                Throwable throwable = (Throwable) data;
                                throwable.printStackTrace();
                                JSONObject object = new JSONObject(throwable.getMessage());
                                String des = object.optString("detail");//错误描述
                                int status = object.optInt("status");//错误代码
                                if (status > 0 && !TextUtils.isEmpty(des)) {
                                    Toast.makeText(UIUtils.getContext(), des, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mRegistView.error(ConfigStateCode.STAT_OTHER, ConfigStateCode.STAT_OTHER_VALUE);
                            if (!TextUtils.isEmpty(countDown)) {
                                if (!countDown.contains("s")) {
                                    mRegistView.setCodeEnabled(true);
                                } else {
                                    mRegistView.setCodeEnabled(false);
                                }
                            }
                        }
                    }
                });
            }

            public void beforeEvent(int eventHandler, Object data) {
                Logger.i("TAG", eventHandler + "/" + data);
                super.beforeEvent(eventHandler, data);
            }
        };
        SMSSDK.registerEventHandler(this.mEventHandler);
    }

    @Override
    public void submitVerificationCode() {
        SMSSDK.submitVerificationCode("86", mRegistView.getUserTelephone(), mRegistView.getUserCode());
    }

    @Override
    public void sendSmsCode(String telePhone) {
        SMSSDK.getVerificationCode("86", telePhone);
    }

    @Override
    public void sendVoiceCode(String telePhone) {
        SMSSDK.getVoiceVerifyCode("86", telePhone);
    }

}
