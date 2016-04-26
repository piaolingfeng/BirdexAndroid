package com.birdex.bird.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.birdex.bird.AliPay.PayResult;
import com.birdex.bird.AliPay.SignUtils;
import com.birdex.bird.R;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.EncryptEntity;
import com.birdex.bird.util.Constant;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Switch;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.Bind;

/**
 * 充值
 */
public class RechargeFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,View.OnClickListener,TextWatcher{
    @Bind(R.id.rb_recharge_tran)
    public RadioButton rb_tran;
    @Bind(R.id.rb_recharge_tax)
    public RadioButton rb_tax;
    @Bind(R.id.btn_pay_recharge)
    public Button btn_pay;
    //输入金额
    @Bind(R.id.et_recharge_inputmoney)
    public EditText et_money;
    // 商户PID
    public static final String PARTNER = "";
    // 商户收款账号
    public static final String SELLER = "";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";
    private static final int SDK_PAY_FLAG = 1;
    //获取服务器端的加密信息
    private RequestParams params=null;
    private final  String tag="RechargeFragment";
    //获取信息
    private SharedPreferences sharedPreferences=null;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_LONG).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_recharge;
    }

    @Override
    public void initializeContentViews() {
        et_money.addTextChangedListener(this);
        sharedPreferences=getActivity().getSharedPreferences(Constant.SP_UserInfo, Activity.MODE_PRIVATE);
        rb_tran.setOnCheckedChangeListener(this);
        rb_tax.setOnCheckedChangeListener(this);
        rb_tran.setOnClickListener(this);
        rb_tax.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        //设置接口的参数
        params=new RequestParams();
        //params.put("WalletType","default");
        //http://payorder.testsite.com.cn/gotobank.aspx?a=in&WalletType=default&ud=2（用户ID）&p=6&m=0.01（金额）&islink=1
        params.put("a","in");
        params.put("islink","1");
        params.put("p","6");
    }

    @Override
    protected void lazyLoad() {

    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_recharge_tran:
                rb_tran.setChecked(true);
                rb_tax.setChecked(false);
                break;
            case R.id.rb_recharge_tax:
                rb_tran.setChecked(false);
                rb_tax.setChecked(true);
                break;
            case R.id.btn_pay_recharge:
//                pay(v);
                if(!rb_tran.isChecked()&&!rb_tax.isChecked()){
                    T.showShort(getActivity(),R.string.recharge_tip1);
                    return;
                }
                String money=et_money.getText().toString().trim();
                if(TextUtils.isEmpty(money)){
                    T.showShort(getActivity(),R.string.recharge_tip2);
                    return;
                }
                float moneyNum=0.00f;
                try {
                    moneyNum=Float.parseFloat(money);
                }catch (Exception ex){
                    T.showShort(getActivity(),R.string.recharge_tip2);
                    return;
                }
                if(moneyNum==0){
                    T.showShort(getActivity(),R.string.recharge_tip7);
                    return;
                }
                moneyNum=Float.parseFloat(money);
                String bindID=sharedPreferences.getString(Constant.SP_UserInfo_Bind, "").trim();
                if(TextUtils.isEmpty(bindID)){
                    T.showShort(getActivity(),R.string.recharge_tip4);
                    return;
                }
                //添加用户的绑定id
                params.put("ud",bindID);
                //设置充值账号
                if(rb_tran.isChecked()){
                    params.put("WalletType","default");
                }else if(rb_tax.isChecked()){
                    params.put("WalletType","tax");
                }
                params.put("m",money);

                getEncryptInfo();
                break;
            default:
                break;
        }
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(getActivity());
        String version = payTask.getVersion();
        Toast.makeText(getActivity(), version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
//    public void h5Pay(View v) {
//        Intent intent = new Intent(this, H5PayDemoActivity.class);
//        Bundle extras = new Bundle();
//        /**
//         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
//         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
//         * 商户可以根据自己的需求来实现
//         */
//        String url = "http://m.meituan.com";
//        // url可以是一号店或者美团等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
//        extras.putString("url", url);
//        intent.putExtras(extras);
//        startActivity(intent);
//
//    }

    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public void getEncryptInfo(){
        showLoading();
        JsonHttpResponseHandler handler=new JsonHttpResponseHandler(){

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                T.showShort(getActivity(),R.string.recharge_tip5);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                EncryptEntity encryptEntity= GsonHelper.getPerson(response.toString(),EncryptEntity.class);
                if(encryptEntity!=null){
                    if("SUCCESS".equalsIgnoreCase(encryptEntity.getCode())){
                        //加密成功
                        startPay(encryptEntity.getMessage());
                    }else {
                        T.showShort(getActivity(),R.string.recharge_tip5);
                    }
                }else{
                    T.showShort(getActivity(),R.string.recharge_tip5);
                }
            }

        };

        handler.setTag(tag);
        BirdApi.getEncryptInfo(getActivity(),params,handler);
    }
    private void startPay(final String payInfo){
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String money=s.toString();
        if (money.contains(".")) {
            int position=money.indexOf(".");
            int lenght=money.length();
            if (lenght-(position+1)>2) {
                s = money.subSequence(0, position+ 3);
                et_money.setText(s);
                et_money.setSelection(s.length());
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
