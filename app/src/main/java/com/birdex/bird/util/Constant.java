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

    public static final int ICON_CHANGE =  1001;
    public static final int[] name = {R.string.tool1, R.string.tool2, R.string.tool3, R.string.tool5, R.string.tool6};
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
}
