package com.birdex.bird.api;

import android.content.Context;

import com.birdex.bird.MyApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by chuming.zhuang on 2016/3/18.
 * 请求接口
 */
public class BirdApi {
    public static String SERVER_ADDRESS = "api.beta1.b.birdex.cn";
//    public static String SERVER_ADDRESS = "192.168.1.207";
//    public static String PORT = "8089";//8002
    public static String BASE_URL = "http://" + SERVER_ADDRESS ;//+ ":" + PORT;//

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

    public static void post(Context context, String url, Header[] headers, RequestParams params, String contentType, ResponseHandlerInterface responseHandler){
        MyApplication.ahc.post(context, BASE_URL + "/" + url, params, responseHandler);
    }

    /**
     * 取消特定的tag请求
     */
    public static void cancelRequestWithTag(Object tag) {
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

    public static void cancelAllRequest() {
        MyApplication.ahc.cancelAllRequests(false);
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

    //消费记录
    public static void getWalletRecord(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        StringBuilder url=new StringBuilder("Wallet/getRecord");
//        if(MyApplication.user!=null){
//            url.append("?app_debug=1");
//            url.append("&user_code=").append(MyApplication.user.getUser_code());
//            url.append("&company_code=").append(MyApplication.user.getCompany_code());
//        }
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
//        MyApplication.ahc.get(context, "http://192.168.1.201:8099/sanfangcang.html", params, jsonHttpResponseHandler);
        MyApplication.ahc.get(context, "http://app.birdex.cn/sanfangcang.html", params, jsonHttpResponseHandler);
    }

    // 获取公司信息
    public static void getCompanyMes(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        params.add("app_debug", 1 + "");
//        params.add("user_code", MyApplication.user.getUser_code());
//        params.add("company_code", MyApplication.user.getCompany_code());
        get(context, "Company/get", params, jsonHttpResponseHandler);
    }

    // 获取配置信息
    public static void getConfig(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        get(context, "Public/getConfig", params, jsonHttpResponseHandler);
    }

    //获取仓库，库存
    public static void getInventory(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        StringBuilder url=new StringBuilder("stock/all");
//        if(MyApplication.user!=null){
//            url.append("?app_debug=1");
//            url.append("&user_code=").append(MyApplication.user.getUser_code());
//            url.append("&company_code=").append(MyApplication.user.getCompany_code());
//        }
        post(context, url.toString(), params, jsonHttpResponseHandler);
    }

    // 获取订单所有状态
    public static void getOrderListState(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        params.add("app_debug", 1 + "");
//        params.add("user_code", MyApplication.user.getUser_code());
//        params.add("company_code", MyApplication.user.getCompany_code());
        get(context, "order/getOrderStatusList", params, jsonHttpResponseHandler);
    }

    // 获取订单列表
    public static void getOrderList(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        params.add("app_debug", 1 + "");
//        params.add("user_code", MyApplication.user.getUser_code());
//        params.add("company_code", MyApplication.user.getCompany_code());
        get(context, "order/all", params, jsonHttpResponseHandler);
    }

    // 获取单个订单
    public static void getOrderDetail(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        params.add("app_debug", 1 + "");
//        params.add("user_code", MyApplication.user.getUser_code());
//        params.add("company_code", MyApplication.user.getCompany_code());
        get(context, "Order/get", params, jsonHttpResponseHandler);
    }

    // 获取所有仓库
    public static void getAllWarehouse(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        params.add("app_debug", 1 + "");
//        params.add("user_code", MyApplication.user.getUser_code());
//        params.add("company_code", MyApplication.user.getCompany_code());
        get(context, "Warehouse/companyAll", params, jsonHttpResponseHandler);
    }


    // 获取物流轨迹
    public static void getTracking(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        params.add("app_debug", 1 + "");
//        params.add("user_code", MyApplication.user.getUser_code());
//        params.add("company_code", MyApplication.user.getCompany_code());
        get(context, "Order/getTracking", params, jsonHttpResponseHandler);
    }

    // 获取账户余额
    public static void getBalance(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        params.add("app_debug", 1 + "");
//        params.add("user_code", MyApplication.user.getUser_code());
//        params.add("company_code", MyApplication.user.getCompany_code());
        get(context, "Wallet/get", params, jsonHttpResponseHandler);
    }

    // 上传公司 logo
    public static void upLoadLogo(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        MyApplication.ahc.post("http://file.beta1.b.birdex.cn/upload/companyLogo", params, jsonHttpResponseHandler);
//        MyApplication.ahc.post("http://192.168.1.207:8090/upload/companyLogo", params, jsonHttpResponseHandler);
    }

    // 修改公司信息
    public static void companyEdit(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        post(context, "Company/edit", params, jsonHttpResponseHandler);
    }

    // 获取预报列表
    public static void getPredicitionList(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        params.add("app_debug", 1 + "");
//        params.add("user_code", MyApplication.user.getUser_code());
//        params.add("company_code", MyApplication.user.getCompany_code());
        post(context, "Storage/all", params, jsonHttpResponseHandler);
    }

    // 获取预报列表
    public static void getPredicitionStatus(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        params.add("app_debug", 1 + "");
//        params.add("user_code", MyApplication.user.getUser_code());
//        params.add("company_code", MyApplication.user.getCompany_code());
        post(context, "storage/getStorageStatusList", params, jsonHttpResponseHandler);
    }

    // 测试 消息头
    public static void testHeader(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        post(context, "Public/testApp", params, jsonHttpResponseHandler);
    }

    public static void getOrder(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        post(context, "Order/get", params, jsonHttpResponseHandler);
    }

    public static void getStorageDetail(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        post(context, "Storage/get", params, jsonHttpResponseHandler);
    }

    //确认入库
    public static void setConfirmStorage(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        post(context, "storage/confirm", params, jsonHttpResponseHandler);
    }

    //发起复核
    public static void setReviewStorage(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        post(context, "storage/review", params, jsonHttpResponseHandler);
    }

    // 上传身份证图片
    public static void uploadIDCardPic(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
//        MyApplication.ahc.post("http://192.168.1.207:8090/upload/IDCard", params, jsonHttpResponseHandler);
        MyApplication.ahc.post("http://file.beta1.b.birdex.cn/upload/IDCard", params, jsonHttpResponseHandler);
    }

    // 删除身份证， 需要调用 修改订单 接口
    public static void uploadIDCard(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        post(context, "Order/edit", params, jsonHttpResponseHandler);
    }

    // 修改订单
    public static void modOrder(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        post(context, "Order/edit", params, jsonHttpResponseHandler);
    }
    public static void getProductDetail(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        post(context, "Product/get", params, jsonHttpResponseHandler);
    }

    // 未读消息统计
    public static void getMessageStat(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        get(context, "Message/stat", params, jsonHttpResponseHandler);
    }

    // 获取公用配置当前版本
    public static void getConfigVersion(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        get(context, "Public/getConfigVersion", params, jsonHttpResponseHandler);
    }
    public static void getMsgList(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        post(context, "Message/all", params, jsonHttpResponseHandler);
    }
    //支付宝加密接口
    public static void getEncryptInfo(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        MyApplication.ahc.get(context,"http://payorder.testsite.com.cn/gotobank.aspx", params, jsonHttpResponseHandler);
    }
}
