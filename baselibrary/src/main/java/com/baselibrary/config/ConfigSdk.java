package com.baselibrary.config;

import com.baselibrary.utils.UIUtils;

import java.io.File;

/**
 * Created by yangyupan on 2017/4/11.
 */

public class ConfigSdk {
///////////////////////////////////////////////高度地图sdk/////////////////////////////////////////////////////////

    public static final String SDK_GAODE_KEY="4f22e3b4f572e7931292ad628e3370a7";

    public static final String SDK_SAVE_CUSTOM_MAP_CONFIG_PATH= File.separator + "data" + File.separator + "data" + File.separator + UIUtils.getContext().getPackageName() + File.separator+"mapcustomconfig.json";
    ///////////////////////////////////////////////////mob短信验证/////////////////////////////////////////////////////////////
    public static final String SDK_SMSSDK_KEY="1ee050647e52a";
    public static final String SDK_SMSSDK_SECRET="d0e5a287018a11db6c76aeddc8dc1dc0";

    //////////////////////////////////////////////支付宝///////////////////////////////////////////////////////////
    //支付宝回到地址
    public static final String SDK_ALIPAY_DEBUG_REDIRECT_URL = "http://192.168.0.107/quickmeals/alipay/obtainauthcode.action";
    //APPID 即创建应用后生成
    public static final String SDK_ALIPAY_DEBUG_APP_ID ="2016080300159237";
    //APPID 即创建应用后生成
    public static final String SDK_ALIPAY_RELEASE_APP_ID="2017051407233455";

    /////////////////////////////////////////////////////////////////QQ//////////////////////////////////////////////////////////
    public static final String TENCENT_APP_KEY      = "101391682";

    /////////////////////////////////////////////////////////////////微信/////////////////////////////////////////////////////////
    public static final String SDK_WEIXIN_APP_ID      = "wx750f1c770b5a6eec";
    public static final String SDK_WEIXIN_APP_SECRET      = "e71c94582b98fcdca27970cc83012f43";

    ////////////////////////////////////////////////////////微博////////////////////////////////////////////
    public static final String WEIBO_APP_KEY      = "836102818";
    public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static final String WEIBO_SCOPE =
            " ";

    //新浪微博的access_token
    public static final String SDK_ACCESS_TOKEN="access_token";

    //新浪微博的uid
    public static final String SDK_ACCESS_UID="uid";
}
