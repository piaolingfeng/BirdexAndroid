package com.birdex.bird;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.birdex.bird.entity.User;
import com.birdex.bird.util.Constant;
import com.birdex.bird.util.CrashHandler;
import com.loopj.android.http.AsyncHttpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/3/18.
 */
public class MyApplication extends Application {
    private static MyApplication instants;
    public static AsyncHttpClient ahc;
    public static String appName = "bird";

    public static List<Activity> activityList = new ArrayList<>();

    // 登录的相关信息 user
    public static User user;


    // 清除 activity 栈
    public void clearActivities(){
        for(Activity activity: activityList){
            if(activity != null ){
                activity.finish();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instants = this;
        initAsyncHttpClient();
        initFile();
//        iniCrash();
    }

    private void initFile(){
        File file = new File(Constant.BASEPATH);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 异常扑捉器
     */
    private void iniCrash() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

    }

    public static MyApplication getInstans() {
        if (instants == null) {
            instants = new MyApplication();
        }
        return instants;
    }

    //线程同步网络请求
    private synchronized void initAsyncHttpClient() {
        ahc = new AsyncHttpClient();//获取网络连接超时
        ahc.setTimeout(8 * 1000);//设置30秒超时
        ahc.setConnectTimeout(4 * 1000);//设置30秒超时
        ahc.setMaxConnections(5);
    }

}
