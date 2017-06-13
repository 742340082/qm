package com.we.mvp.user.operate.biz;

import android.text.TextUtils;
import android.widget.Toast;

import com.baselibrary.config.ConfigSdk;
import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.config.ConfigValues;
import com.baselibrary.utils.Logger;
import com.baselibrary.utils.NetworkState;
import com.baselibrary.utils.UIUtils;
import com.we.config.ConfigUser;
import com.we.mvp.user.baseLogic.LogicOperateUser;
import com.we.mvp.user.login.modle.QQUserInfo;
import com.we.mvp.user.login.modle.WeiboUserInfo;
import com.we.mvp.user.modle.User;
import com.we.mvp.user.operate.view.OperateUserView;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OperateUserBizImpl
        implements OperateUserBiz {
    private String countDown;
    private EventHandler mEventHandler;
    private OperateUserView mOperateRegistView;
    private User mUser;
    private QQUserInfo qqUserInfo;
    private WeiboUserInfo mWeiboUserInfo;
    private LogicOperateUser logicOperateUser;

    public OperateUserBizImpl(OperateUserView operateUserView) {
        this.mOperateRegistView = operateUserView;
        logicOperateUser = new LogicOperateUser(operateUserView);
    }


    @Override
    public boolean confirmation(QQUserInfo qqUserInfo, WeiboUserInfo weiboUserInfo) {
        String telephone = this.mOperateRegistView.getUserTelephone();
        this.countDown = this.mOperateRegistView.getCountDown();
        String code = null;
        //获取到初始化的值
        if (telephone != null) {
            code = mOperateRegistView.getUserCode();
        }
        //对初始化值进行检验
        if ((telephone == null) || (code == null)) {
            return false;
        }
        if (!NetworkState.networkConnected(UIUtils.getContext())) {
            mOperateRegistView.error(ConfigStateCode.STATE_NO_NETWORK, ConfigStateCode.STATE_NO_NETWORK_VALUE);
            return false;
        }
        mOperateRegistView.loading();
        this.mUser = new User(mOperateRegistView.getAccount());
        mUser.setPhone(Long.parseLong(telephone));
        this.qqUserInfo = qqUserInfo;
        this.mWeiboUserInfo = weiboUserInfo;
        return true;
    }


    @Override
    public void destroyMobSms() {
        SMSSDK.unregisterEventHandler(mEventHandler);
    }

    @Override
    public void sendVoiceCode(String telePhone) {
        SMSSDK.getVoiceVerifyCode("86", telePhone);
    }

    @Override
    public void initMobSms(final int loginType, final int registType, final int updateType, final String phone) {
        SMSSDK.initSDK(UIUtils.getContext(), ConfigSdk.SDK_SMSSDK_KEY, ConfigSdk.SDK_SMSSDK_SECRET);
        mEventHandler = new EventHandler() {
            public void afterEvent(final int eventHandler, final int result, final Object data) {
                UIUtils.runOnUIThread(new Runnable() {
                    public void run() {
                        //成功的时候
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //成功的时候

                            if (eventHandler == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                noFlag(loginType, registType, updateType, phone);

                            }
                            if (eventHandler == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                //发送验证码成功
                                //mFlag true表示智能验证 false短信验证
                                boolean flag = (boolean) data;
                                OperateUserBizImpl.this.mOperateRegistView.isBrainConfirmation(flag);
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
                            mOperateRegistView.error(ConfigStateCode.STAT_OTHER, ConfigStateCode.STAT_OTHER_VALUE);
                            if (!TextUtils.isEmpty(countDown)) {
                                if (!countDown.contains("s")) {
                                    mOperateRegistView.setCodeEnabled(true);
                                } else {
                                    mOperateRegistView.setCodeEnabled(false);
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
        SMSSDK.submitVerificationCode("86", mOperateRegistView.getUserTelephone(), mOperateRegistView.getUserCode());
    }

    private void noFlag(int loginType, int registType, int updateType, String phone) {
        //校验验证码成功
        if (loginType != -1) {
            if (mUser != null) {
                logicOperateUser.loginUser(mUser.getAccount(), mUser.getPassword(), mOperateRegistView.getUserTelephone(), loginType);
            }
        } else if (registType != -1) {

            if (mUser != null && qqUserInfo != null && mWeiboUserInfo != null) {
                logicOperateUser.registUser(mUser.getPhone().toString(),
                        mUser.getPassword(),
                        qqUserInfo,
                        mWeiboUserInfo,
                        registType);
            }

        } else if (updateType != -1) {
            if (!mOperateRegistView.getUserTelephone().equals(phone)) {
                mOperateRegistView.error(ConfigStateCode.RESULT_CONFIG_TELEPHONE_ERROR, ConfigStateCode.RESULT_CONFIG_TELEPHONE_ERROR_VALUE);
            } else {
                Observable.just(phone)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                mOperateRegistView.loading();
                            }
                        })
                        .delay(ConfigValues.VALUE_DEFAULT_WAIT, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                mOperateRegistView.confirmationJudge();
                            }
                        });
            }
        } else {
            logicOperateUser.updateUser(mUser.getAccount(),
                    null,
                    null,
                    null,
                    mOperateRegistView.getUserTelephone(),
                    null,
                    null,
                    null,
                    ConfigUser.USER_UPDATE_TELEPHONE
            );
        }
    }
    @Override
    public void sendSmsCode(String telePhone) {
        SMSSDK.getVerificationCode("86", telePhone);
    }


}
