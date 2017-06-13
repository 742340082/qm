package com.we.mvp.user.detail.biz;

import android.text.TextUtils;
import android.widget.Toast;

import com.baselibrary.config.ConfigSdk;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.we.config.ConfigUser;
import com.we.mvp.user.baseLogic.LogicOperateUser;
import com.we.mvp.user.detail.view.UpdatePasswordView;
import com.we.mvp.user.modle.User;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * Created by yangyupan on 2017/4/19.
 */

public class UpdatePasswordBizImpl implements UpdatePasswordBiz {
    private UpdatePasswordView mPasswordView;
    private EventHandler mEventHandler;
    private boolean mFlag;
    private String mCountDown;
    private User mUser;
    private String mTelePhone;
    private LogicOperateUser logicOperateUser;

    public UpdatePasswordBizImpl(UpdatePasswordView view) {
        mPasswordView = view;
        logicOperateUser = new LogicOperateUser(view);
    }

    @Override
    public void updatePassword(int updateType) {

        switch (updateType) {
            case ConfigUser.USER_UPDATE_ADD_PASSWORD:
                logicOperateUser.updateUser(
                        mUser.getAccount(),
                        null,
                        null,
                        mPasswordView.getNewPassWord(),
                        null,
                        null,
                        null,
                        null,
                        updateType);
                break;
            case ConfigUser.USER_UPDATE_PWD_PASSWORD:
                logicOperateUser.updateUser(
                        mUser.getAccount(),
                        null,
                        mPasswordView.getPassWord(),
                        mPasswordView.getNewPassWord(),
                        null,
                        null,
                        null,
                        null,
                        updateType);

                break;
        }


    }


    @Override
    public void destroyMobSms() {
        SMSSDK.unregisterEventHandler(this.mEventHandler);
    }

    @Override
    public boolean confirmation(String account, int updateType) {
        String newPassword = null;
        String telephone = mPasswordView.getTelephone();
        boolean isFlag = false;
        switch (updateType) {
            case ConfigUser.USER_UPDATE_ADD_PASSWORD:
                newPassword = mPasswordView.getNewPassWord();
                if (newPassword != null) {
                    mUser = new User(account);
                    isFlag = true;
                } else {
                    isFlag = false;
                }
                break;
            case ConfigUser.USER_UPDATE_TELEPHONE_PASSWORD:
                mCountDown = mPasswordView.getCountDown();
                String code = null;
                //获取到初始化的值
                if (telephone != null) {

                    //不是智能验证
                    code = mPasswordView.getUserCode();
                    if (code != null) {
                        newPassword = this.mPasswordView.getNewPassWord();
                    }

                }
                //对初始化值进行检验

                //不是智能验证
                if ((telephone == null) || (newPassword == null) || (code == null)) {
                    isFlag = false;
                }
                if (!NetworkState.networkConnected(UIUtils.getContext())) {
                    mPasswordView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
                    isFlag = false;
                }
                SMSSDK.submitVerificationCode("86", telephone, code);
                mPasswordView.loading();
                mUser = new User(account);
                isFlag = true;


                break;
            case ConfigUser.USER_UPDATE_PWD_PASSWORD:
                //密码修改验证
                String passWord = mPasswordView.getPassWord();

                if (passWord != null) {
                    newPassword = mPasswordView.getNewPassWord();
                    if (newPassword != null) {
                        if (!passWord.equals(newPassword)) {
                            mPasswordView.loading();
                            mUser = new User(account);
                            isFlag = true;
                        } else {
                            mPasswordView.error(ConfigStateCode.RESULT_NEW_PASSWORD_NOEQUESE_PASSWORD, ConfigStateCode.RESULT_NEW_PASSWORD_NOEQUESE_PASSWORD_VALUE);
                            isFlag = false;
                        }
                    }
                }
                break;
        }
        return isFlag;
    }

    @Override
    public void initMobSms(final String account, final int updateType) {
        SMSSDK.initSDK(UIUtils.getContext(), ConfigSdk.SDK_SMSSDK_KEY, ConfigSdk.SDK_SMSSDK_SECRET);
        mEventHandler = new EventHandler() {
            public void afterEvent(final int eventHandler, final int result, final Object data) {
                UIUtils.runOnUIThread(new Runnable() {

                    public void run() {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //成功的时候
                            if (eventHandler == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                //校验验证码成功

                                logicOperateUser.updateUser(
                                        account,
                                        null,
                                        null,
                                        mPasswordView.getNewPassWord(),
                                        mPasswordView.getTelephone(),
                                        null,
                                        null,
                                        null,
                                        updateType
                                );


                            }
                            if (eventHandler == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                //发送验证码成功
                                //mFlag true表示智能验证 false短信验证
                                boolean flag = (boolean) data;
                                mPasswordView.isBrainConfirmation(flag);
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
                            mPasswordView.error(ConfigStateCode.STAT_OTHER, ConfigStateCode.STAT_OTHER_VALUE);
                            if (!TextUtils.isEmpty(mCountDown)) {
                                if (!mCountDown.contains("s")) {
                                    mPasswordView.setCodeEnabled(true);
                                } else {
                                    mPasswordView.setCodeEnabled(false);
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
    public void sendSmsCode(String telePhone) {
        SMSSDK.getVerificationCode("86", telePhone);
    }

    @Override
    public void sendVoiceCode(String telePhone) {
        SMSSDK.getVoiceVerifyCode("86", telePhone);
    }
}
