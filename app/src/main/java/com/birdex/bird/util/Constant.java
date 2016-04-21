package com.birdex.bird.util;

import android.os.Environment;

import com.birdex.bird.R;

/**
 * Created by chuming.zhuang on 2016/3/18.
 */
public class Constant {
    public static final String NAME = "/Bird";
    public static final String BASEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + NAME;

    public static final int ICON_CHANGE = 1001;
    public static final int[] name = {R.string.tool1, R.string.tool2, R.string.tool3, R.string.tool5, R.string.tool6};

    public static final String MSG_ORDER_STOCK_EXCEPTION = "ORDER_STOCK_EXCEPTION";//订单库存异常
    public static final String MSG_ORDER_VERIFY_FAIL = "ORDER_VERIFY_FAIL";//订单审核不通过
    public static final String MSG_ORDER_IDCARD_EXCEPTION = "ORDER_IDCARD_EXCEPTION";//订单身份证异常
    public static final String MSG_ACCOUNT_EXCEPTION = "ACCOUNT_EXCEPTION";//账号异常
    public static final String MSG_STOCK_WARNING = "STOCK_WARNING";//库存预警

    public static final int[] time = {R.string.time_all, R.string.time_today, R.string.time_week, R.string.time_month, R.string.time_three_month, R.string.time_year};
    //数据库名
    public static final String DBName="BirdexData";
    //notifi启动的action
    public static final  String NotiAction1="com.birdex.msgdetail";
    //app的包名
    public static final String APPPackageName="com.birdex.bird";
    //SP文件里的推送设置
    public static final String SP_NAME = "MESSAGE_SETTING";
    public static final String TONE_SETTING = "tone";
    public static final String TIME_SETTING = "time";
    //SP个人信息
    public static final String SP_UserInfo="login";
    public static final String SP_UserInfo_Bind="bind_user_id";
    //设置系统的别名类型
    public static final String Alias_Type="com.birdex.alias";
}
