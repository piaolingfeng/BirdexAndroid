package com.birdex.bird.api;

import android.content.Context;

import com.birdex.bird.MyApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Created by chuming.zhuang on 2016/3/18.
 * 请求接口
 */
public class BirdApi {

    public static String SERVER_ADDRESS = "192.168.1.207";
    public static String PORT = "8089";//8002
    public static String BASE_URL = "http://" + SERVER_ADDRESS + ":" + PORT;//

    /**
     * @param context
     * @param url
     * @param params
     * @param jsonHttpResponseHandler
     */
    public static void post(Context context, String url, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        MyApplication.ahc.post(context, BASE_URL + "/" + url, params, jsonHttpResponseHandler);
    }

    public static void post(Context context, String url, RequestParams params, TextHttpResponseHandler textHttpResponseHandler) {
        MyApplication.ahc.post(context, BASE_URL + "/" + url, params, textHttpResponseHandler);
    }
    /**
     *取消特定的tag请求
     */
    public static void cancelRequestWithTag(Object tag){
        MyApplication.ahc.cancelRequestsByTAG(tag, true);
    }
    /**
     * @param context
     * @param url
     * @param params
     * @param jsonHttpResponseHandler
     */
    private static void get(Context context, String url, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        MyApplication.ahc.get(context, BASE_URL + "/" + url, params, jsonHttpResponseHandler);
    }

    private static void get(Context context, String url, RequestParams params, TextHttpResponseHandler textHttpResponseHandler) {
        MyApplication.ahc.get(context, BASE_URL + "/" + url, params, textHttpResponseHandler);
    }

    /**
     * 请求接口方法
     * @param context
     * @param params
     * @param jsonHttpResponseHandler
     */
    public static void CheckUpdate(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        post(context, "UpdateCenter/CheckVersion", params, jsonHttpResponseHandler);
    }



    public static void getWalletRecord(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        StringBuilder url=new StringBuilder("Wallet/getRecord");
        if(MyApplication.user!=null){
            url.append("?app_debug=1");
            url.append("&user_code=").append(MyApplication.user.getUser_code());
            url.append("&company_code=").append(MyApplication.user.getCompany_code());
        }
        post(context, url.toString(), params, jsonHttpResponseHandler);
    }

    public static void login(Context context, RequestParams params, TextHttpResponseHandler textHttpResponseHandler) {
        post(context, "Public/login", params, textHttpResponseHandler);
    }

    public static void getTodayData(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        get(context, "company/stat", params, jsonHttpResponseHandler);
    }
    // 获取更新信息
    public static void upDateMessage(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        MyApplication.ahc.get(context, "http://192.168.1.201:8099/sanfangcang.html", params, jsonHttpResponseHandler);
    }

    // 获取公司信息
    public static void getCompanyMes(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        get(context, "Company/get", params, jsonHttpResponseHandler);
    }

    // 获取配置信息
    public static void getConfig(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        get(context, "Public/getConfig", params, jsonHttpResponseHandler);
    }
}