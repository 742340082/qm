package com.baselibrary.config;

public abstract interface ConfigValues
{

    //--------------------------------------RXBUS传递的数据------------------------
    //getaddres传递的title
    public static final int VALUE_ADDRESS_RX_SEND_LOCATION = 3004;
    //getaddres传递的title
    public static final int VALUE_ADDRESS_RX_SEND_RETURNADDRESS = 3003;

    //--------------------------------------普通的配置数据------------------------
    //默认连接超时时间单位秒
    public static final int VALUE_CONNECT_TIMEOUT=30;
    //默认读取超时时间单位秒
    public static final int VALUE_READ_TIMEOUT=10;
    //默认写入超时时间单位秒
    public static final int VALUE_WRITE_TIMEOUT=10;
    //打开相册的requestcode
    public static final int VALUE_FOLLOW_PHOTO_OBTAIN_PICTURE = 352;
    //打开相机的requestcode
    public static final int VALUE_FOLLOW_TAKE_OBTAIN_PICTURE = 368;
    //短信发送倒计时
    public static final int VALUE_SMS_COUNTDOWN = 60;

    //默认连接超时时间单位毫秒
    public static final int VALUE_DEFAULT_WAIT=500;

    //打开联系人列表的requestcode
    int VALUE_OPEN_CONTACTS_LIST=3000;

    //是否进入主界面
    public static final String VALUE_STATE_ENTER_HOME = "VALUE_STATE_ENTER_HOME";

    //getaddres传递的查询参数
    public static final String VALUE_ADDRESS_SEND_QUERYCONTENT = "VALUE_ADDRESS_SEND_QUERYCONTENT";
    //getaddres传递的查询参数
    public static final String VALUE_SEND_TITLE = "VALUE_SEND_TITLE";

    //getaddres传递的查询参数
    public static final String  VALUE_ADDRESS_SEND_CITY = "VALUE_ADDRESS_SEND_CITY";
    //返回城市的requestCode
    public static final int VALUE_REQUESR_CITY_CODE =3001 ;
    //返回城市的resultCode
    public static final int VALUE_RESULT_CITY_CODE =3002 ;
    //getaddres传递的查询参数
    public static final String VALUE_GETADDRESS_RETURN_ADDRESS= "VALUE_GETADDRESS_RETURN_ADDRESS";
    //getaddres传递的查询参数
    public static final String VALUE_GETADDRESS_SEND_LATITUDE= "VALUE_GETADDRESS_SEND_LATITUDE";





}
