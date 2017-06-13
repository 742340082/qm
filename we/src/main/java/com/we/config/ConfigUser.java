package com.we.config;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.baselibrary.utils.MD5Util;
import com.baselibrary.utils.UIUtils;

import java.io.File;

public class ConfigUser
{
    //保存的用户
    public static final String USER_SAVE = "USER_SAVE";
    //保存是否是夜间模式
    public static final String USER_NIGHT_SAVE = "USER_NIGHT_SAVE";
    //传递的用户
    public static final int USER_RX_SEND =0x333 ;

    //传递的用户图片
    public static final int USER_RX_ICON =0x334 ;

    //登录的方式
    public static final String USER_LOGIN_TYPE = "USER_LOGIN_TYPE";
    //QQ登录
    public static final int USER_QQ_LOGIN = 0x140;
    //微信登录
    public static final int USER_WEIXIN_LOGIN = 0x150;
    //电话登录
    public static final int USER_TELEPHONE_LOGIN = 0x585;
    //微博登录
    public static final int USER_WEIBO_LOGIN = 0x160;
    //普通登录
    public static final int USER_COMMON_LOGIN = 0x170;


    //注册的方式
    public static final String USER_REGIST_TYPE = "USER_REGIST_TYPE";
    //邮箱注册
    public static final int USER_EMAIL_REGUST = 0x110;
    //电话注册
    public static final int USER_TELEPHONE_REGIST= 0x120;
    //普通注册
    public static final int USER_COMMON_REGIST= 0x130;
    //QQ注册
    public static final int USER_QQ_REGIST = 0x190;
    //微信注册
    public static final int USER_WEIXIN_REGIST = 0x200;
    //微博注册
    public static final int USER_WEIBO_REGIST = 0x1210;

    //修改的类型
    public static  final String USER_UPDATE_TYPE="USER_UPDATE_TYPE";
    //修改昵称
    public static  final int USER_UPDATE_NAME=0x1111;
    //修改绑定的电话
    public static  final int USER_UPDATE_TELEPHONE=0x1112;
    //修改绑定的QQ
    public static  final int USER_UPDATE_BIND_QQ=0x1113;
    //修改绑定的微博
    public static  final int USER_UPDATE_BIND_WEIBO=0x1114;
    //修改绑定的微信
    public static  final int USER_UPDATE_BIND_WEIXIN=0x1115;
    //通过旧密码改密码
    public static  final int USER_UPDATE_PWD_PASSWORD=0x1116;
    //通过电话号码改密码
    public static  final int USER_UPDATE_TELEPHONE_PASSWORD=0x1121;
    public static final int USER_UPDATE_ADD_PASSWORD =0x1122;
    //修改支付密码
    public static  final int USER_UPDATE_PAY_PASSWORD=0x1117;
    //解除绑定的QQ
    public static  final int USER_UPDATE_UNBIND_QQ=0x1118;
    //解除绑定的微博
    public static  final int USER_UPDATE_UNBIND_WEIBO=0x1119;
    //解除绑定的微信
    public static  final int USER_UPDATE_UNBIND_WEIXIN=0x1120;



    //是不是密码登录
    public static final String USER_IS_PASSWORD_LOGIN = "VALUE_IS_PASSWORD_LOGIN";

    //传递的用户
    public static final String USER_ACCOUNT="account";
    //传递的密码
    public static final String USER_PASSWORD="password";
    //传递的昵称
    public static final String USER_NAME="user_name";
    //传递的新密码
    public static final String USER_NEW_PASSWOD="USER_NEW_PASSWOD";
    //传递的电话号码
    public static final String USER_TELEPHONE="USER_TELEPHONE";
    //传递的支付密码
    public static final String USER_PAY_PASSWORD="USER_PAY_PASSWORD";
    //传递QQ用户
    public static final String USER_BIND_QQUSERINFO="USER_BIND_QQUSERINFO";
    //传递QQ用户
    public static final String USER_BIND_WEIBOUSERINFO="USER_BIND_WEIBOUSERINFO";
    //保存用户的图片
    public static final String USER_ICON = "icon_we_user";
    //用户在没有网络的时候拍照保存的路径
    public static final String USER_ICON_NETWORK_URI = File.separator + "data" + File.separator + "data" + File.separator + UIUtils.getContext().getPackageName() + File.separator + "UserCropError";
    //用户在有网络的时候拍照保存的路径
    public static final String USER_ICON_PATH = File.separator + "data" + File.separator + "data" + File.separator + UIUtils.getContext().getPackageName() + File.separator + "UserIcon";
    //用户缓存
    public static final String USER_CACHE_TIME = "SAVE_USER_TIME";
    //用户缓存的时间
    public static final long USER_TIME_CONTENT = 7*24*60*60*1000;

    //根据用户获取用户在没有网络的时候拍照保存的路径
    public static Uri SAVE_USER_ICON_NETWORK_URI(String account, String suffix)
    {
        File localFile = new File(USER_ICON_NETWORK_URI);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        account = MD5Util.encode(account);
        return Uri.fromFile(new File(localFile, account + suffix));
    }
    //根据用户获取用户在有网络的时候拍照保存的路径
    public static Uri SAVE_USER_ICON_TEMP_URI(String account, String suffix)
    {
        Context localContext = UIUtils.getContext();
        account = MD5Util.encode(account);
       File file = new File(localContext.getExternalCacheDir(), account + suffix);
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(localContext, "com.mcb.image", file);
        }
        return Uri.fromFile(file);
    }
    //根据用户获取头像的保存路径
    public static Uri SAVE_USER_ICON_URI(String account, String suffix)
    {
        File localFile = new File(USER_ICON_PATH);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        account = MD5Util.encode(account);
        return Uri.fromFile(new File(localFile, account + suffix));
    }

}
