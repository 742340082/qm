package com.baselibrary.config;

public abstract interface ConfigValues {

    //--------------------------------------RXBUS传递的数据------------------------
    //getaddres传递的title
    int VALUE_ADDRESS_RX_SEND_LOCATION = 3004;
    //getaddres传递的title
    int VALUE_ADDRESS_RX_SEND_RETURNADDRESS = 3003;

    //--------------------------------------普通的配置数据------------------------
    String VALUE_SELECT_TAB_TITLE="VALUE_SELECT_TAB_TITLE";
    String VALUE_SAVE_FRAGMENT_VISABLE_STATE="VALUE_SAVE_FRAGMENT_VISABLE_STATE";
    //默认连接超时时间单位毫秒
    //轮播条切换时间
    long VALUE_NEWS_CHANGE_TIME = 3000;
    //默认连接超时时间单位秒
    int VALUE_CONNECT_TIMEOUT = 30;
    //默认读取超时时间单位秒
    int VALUE_READ_TIMEOUT = 10;
    //默认写入超时时间单位秒
    int VALUE_WRITE_TIMEOUT = 10;
    //打开相册的requestcode
    int VALUE_FOLLOW_PHOTO_OBTAIN_PICTURE = 352;
    //打开相机的requestcode
    int VALUE_FOLLOW_TAKE_OBTAIN_PICTURE = 368;
    //短信发送倒计时
    int VALUE_SMS_COUNTDOWN = 60;
    int VALUE_SPLASH_BACK_TIME = 3;

    //默认连接超时时间单位毫秒
    int VALUE_DEFAULT_WAIT = 0;

    //打开联系人列表的requestcode
    int VALUE_OPEN_CONTACTS_LIST = 3000;

    //是否进入主界面
    String VALUE_STATE_ENTER_HOME = "VALUE_STATE_ENTER_HOME";

    //getaddres传递的查询参数
    String VALUE_ADDRESS_SEND_QUERYCONTENT = "VALUE_ADDRESS_SEND_QUERYCONTENT";
    //getaddres传递的查询参数
    String VALUE_SEND_TITLE = "VALUE_SEND_TITLE";

    //getaddres传递的查询参数
    String VALUE_ADDRESS_SEND_CITY = "VALUE_ADDRESS_SEND_CITY";


}
