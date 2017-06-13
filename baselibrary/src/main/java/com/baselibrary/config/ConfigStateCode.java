package com.baselibrary.config;

/**
 * Created by yangyupan on 2017/4/5.
 */

public interface ConfigStateCode {

    ////////////////////////////////本地状态码//////////////////////////////////////////
    //其他状态
    int STAT_OTHER=999;
    String STAT_OTHER_VALUE="";
    //连接超时
    int STATE_CONNECTION_TIMED_OUT=408;
    String STATE_CONNECTION_TIMED_OUT_VALUE="连接超时";
    //服务器连接失败
    int STATE_SERVER_CONNECTION_FAILED=415;
    String STATE_SERVER_CONNECTION_FAILED_VALUE="服务器连接失败";
    //错误
    int STATE_ERROE=333;
    String STATE_ERROE_VALUE="错误";
    //没有网络
    int STATE_NO_NETWORK=420;
    String STATE_NO_NETWORK_VALUE="没有网络";
    //异常
    int STATE_UNUSUAL=430;
    String STATE_UNUSUAL_VALUE="异常";
    //密码过期重新登录
    int STATE_SHELF_LIFE_HAS_BEEN_RELOGIN=440;
    String STATE_SHELF_LIFE_HAS_BEEN_RELOGIN_VALUE="密码过期重新登录";
    //裁剪失败
    int STATE_CLIPPING_FAILURE=440;
    String STATE_CLIPPING_FAILURE_VALUE="裁剪失败";
    //上传头像失败
    int STATE_UPLOAD_AVATAR_FAILED =460;
    String STATE_UPLOAD_AVATAR_FAILED_VALUE="上传头像失败";
    //修改头像失败
    int STATE_MODIFY_AVATAR_FAILED  =470;
    String STATE_MODIFY_AVATAR_FAILED_VALUE="修改头像失败";
    //文件找不到
    int STATE_FILE_NOT_FOUND=480;
    String STATE_FILE_NOT_FOUND_VALUE="文件找不到";
    //数据不完整
    int STATE_INCOMPLETE_DATA=490;
    String STATE_INCOMPLETE_DATA_VALUE="数据不完整";
    //数据为空
    int STATE_DATA_EMPTY=510;
    String STATE_DATA_EMPTY_VALUE="数据为空";
    //加载更多失败
    int STATE_LOAD_MORE_FAILURES=520;
    String STATE_LOAD_MORE_FAILURES_VALUE="加载更多失败";
    //服务器异常
    int STATE_ABNORMAL_SERVER=530;
    String STATE_ABNORMAL_SERVER_VALUE="服务器异常";

    //验证电话号码失败
    int RESULT_CONFIG_TELEPHONE_ERROR=300;
    String RESULT_CONFIG_TELEPHONE_ERROR_VALUE="输入的电话号码和验证的电话号码不匹配";
    //新密码和旧密码不一致
    int RESULT_NEW_PASSWORD_NOEQUESE_PASSWORD =290;
    String RESULT_NEW_PASSWORD_NOEQUESE_PASSWORD_VALUE="新密码和旧密码不一致";

    ////////////////////////////////服务端返回的状态码//////////////////////////////////////////
    //删除成功
    int RESULT_DELETE_SUCCESS = 1011;
    //删除失败
    int RESULT_DELETE_FAILER = 1012;

    //登录成功
    int RESULT_LOGIN_SUCCESS=100;
    //登录失败
    int RESULT_LOGIN_FAILER=110;

    //没有找到此账号
    int RESULT_NOT_FIND_ACCOUNT = 1008;
    //没有找到绑定此手机的用户
    int RESULT_NOT_FIND_TELEPHONE_ACCOUNT=1007;

    //登录成功
    int RESULT_QQ_LOGIN_SUCCESS=0x110;
    //登录失败
    int RESULT_QQ_LOGIN_FAILER=0x120;

    //注册失败
    int RESULT_REGIST_FAILER=130;
    //注册成功
    int RESULT_REGIST_SUCCESS=140;
    //改账号已经存在
    int RESULT_TELEPHONE_BIND_ACCOUNT =220;



    //上传失败
    int RESULT_UPLOAD_FAILER=160;
    //上传成功
    int RESULT_UPLOAD_SUCCESS=170;
    //获取用户信息失败
    int RESULT_OBTION_USER_FAILER = 180;
    //获取用户信息成功
    int RESULT_OBTION_USER_SUCCESS = 190;
    //获取用户信息成功
    int RESULT_PASSWORD_OR_ACCOUNT_DOES_NOT_EXIST=200;


    //第三方信息已经存在
    int RESULT_THIRD_INFO_EXIST =220;
    //第三方信息不存在
    int RESULT_THIRD_INFO_UNEXIST =230;

    //修改失败
    int RESULT_UPDATE_FAILER=240;
    //修改成功
    int RESULT_UPDATE_SUCCESS=250;
    //你修改的已被人用
    int RESULT_UPDATE_EXISTENCE = 260;
    //修改的密码和原来的密码一样
    int RESULT_UPDATE_PASSWORD_IS_ORIGINAL_PASSWORD=310;

    //密码和用户不匹配
    int RESULT_PASSWORD_ACCOUNT_NO_MATCH = 270;
    //用户和电话号码不匹配
    int RESULT_PASSWORD_TELEPHONE_NO_MATCH = 280;

    //获取城市成功
    int RESULT_CITY_SUCCESS = 1003;
    //获取城市失败
    int RESULT_CITY_FAILER = 1004;

    //收货地址为空
    int RESULT_RECEIVE_ADDRESS_EMPTY = 1000;
    //收货地址成功
    int RESULT_RECEIVE_ADDRESS_SUCCESS = 1001;
    //获取收货地址失败
    int RESULT_RECEIVE_ADDRESS_FAILER = 1002;

    //添加收货地址成功
    int RESULT_ADD_RECEIVE_ADDRESS_SUCCESS = 1005;
    //添加收货地址失败
    int RESULT_ADD_RECEIVE_ADDRESS_FAILER = 1006;
}
