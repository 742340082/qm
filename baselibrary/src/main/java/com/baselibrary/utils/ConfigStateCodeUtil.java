package com.baselibrary.utils;

import com.baselibrary.config.ConfigStateCode;
import com.baselibrary.statusutils.StatusLayoutManager;
import com.google.gson.JsonSyntaxException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;

/**
 * Created by yangyupan on 2017/4/16.
 */

public class ConfigStateCodeUtil {
    public  static  int error(Throwable throwable)
    {
        throwable.printStackTrace();
        int errorCode=0;
        if ((throwable instanceof ConnectException)) {
            errorCode= ConfigStateCode.STATE_SERVER_CONNECTION_FAILED;
        }
        if ((throwable instanceof HttpException)) {
            errorCode=ConfigStateCode.STATE_ABNORMAL_SERVER;
        }
        if ((throwable instanceof EOFException)) {
            errorCode=ConfigStateCode.STATE_UNUSUAL;
        }
        if ((throwable instanceof TimeoutException)) {
            errorCode=ConfigStateCode.STATE_CONNECTION_TIMED_OUT;
        }
        if ((throwable instanceof SocketTimeoutException)) {
            errorCode=ConfigStateCode.STATE_CONNECTION_TIMED_OUT;
        }
        if (throwable instanceof JsonSyntaxException) {
            errorCode=ConfigStateCode.STATE_UNUSUAL;
        }
        errorValue(errorCode);
        return errorCode;
    }

    private   static  void errorValue(int errorcode)
    {
        switch (errorcode) {

            case ConfigStateCode.STATE_SERVER_CONNECTION_FAILED:
                ToastUtils.makeShowToast(UIUtils.getContext(), "服务器连接失败");
                break;
            case ConfigStateCode.STATE_CONNECTION_TIMED_OUT:
                ToastUtils.makeShowToast(UIUtils.getContext(), "连接超时");
                break;
            case ConfigStateCode.STATE_UNUSUAL:
                ToastUtils.makeShowToast(UIUtils.getContext(), "异常");
                break;
            case ConfigStateCode.STATE_ERROE:
            case ConfigStateCode.STATE_ABNORMAL_SERVER:
                ToastUtils.makeShowToast(UIUtils.getContext(), "服务器异常");
                break;
            default:
                ToastUtils.makeShowToast(UIUtils.getContext(), "未知错误");
        }
    }

    public static void error(int errorCode, StatusLayoutManager statusLayoutManager)
    {
        switch (errorCode)
        {
            case ConfigStateCode.STATE_NO_NETWORK:
                statusLayoutManager.showNetWorkError();
                break;
            case ConfigStateCode.STATE_DATA_EMPTY:
                statusLayoutManager.showEmptyData();
                break;
            case ConfigStateCode.STATE_ERROE:
                statusLayoutManager.showError();
                break;
        }
    }
}
