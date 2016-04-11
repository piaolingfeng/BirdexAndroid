package com.birdex.bird.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.AccountDetail;
import com.birdex.bird.entity.Wallet;
import com.birdex.bird.glide.GlideUtils;
import com.birdex.bird.util.Constant;
import com.birdex.bird.util.JsonHelper;
import com.birdex.bird.util.T;
import com.birdex.bird.widget.RoundImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/3/28.
 */
public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    // 顶部头像
    @Bind(R.id.head_icon)
    RoundImageView head;

    // 顶部文字
    @Bind(R.id.head_tv)
    TextView head_tv;

    @Bind(R.id.account_manager)
    TextView accountManager;

    // 运费余额
    @Bind(R.id.freight_balance)
    TextView freight_balance;

    // 信用
    @Bind(R.id.credit)
    TextView credit;

    // 可用
    @Bind(R.id.left_credit)
    TextView left_credit;

    // 关税余额
    @Bind(R.id.tariff_balance)
    TextView tariff_balance;

//    private String path;
    // logo 地址
    private String logo = "";

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_myaccount;
    }

    @Override
    public void initializeContentViews() {
        initData();
    }

    // 初始化数据
    private void initData() {
        getInterfactData();
    }

    // 获取接口数据
    private void getInterfactData() {
        showLoading();
        RequestParams params = new RequestParams();
        BirdApi.getBalance(MyApplication.getInstans(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                hideLoading();
                try {
                    if ("0".equals(response.getString("error"))) {
                        AccountDetail accountDetail = JsonHelper.parseObject((JSONObject) response.get("data"), AccountDetail.class);
                        if (accountDetail != null) {
                            // 将接口数据设置上去
                            setInterfaceData(accountDetail);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideLoading();
                T.showShort(getApplication(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(getApplication(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(getApplication(), getString(R.string.tip_myaccount_getdatawrong));
            }

        });

        // 获取头像跟 company name
        BirdApi.getCompanyMes(MyApplication.getInstans(), new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if ("0".equals(response.getString("error"))) {
                        if (!TextUtils.isEmpty(((JSONObject) response.get("data")).getString("logo"))) {
                            logo = ((JSONObject) response.get("data")).getString("logo");
                            GlideUtils.setImageToLocalPathForMyaccount(head, logo);
                        }
                        head_tv.setText(((JSONObject) response.get("data")).getString("company_name") + "，欢迎您！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                T.showShort(getApplication(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                T.showShort(getApplication(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                T.showShort(getApplication(), getString(R.string.tip_myaccount_getdatawrong));
            }
        });
    }

    // 将接口数据设置上去
    private void setInterfaceData(AccountDetail accountDetail) {
        List<Wallet> wallets = accountDetail.getWallets();
        credit.setText(accountDetail.getCredit());
        left_credit.setText(accountDetail.getLeft_credit());
        for (Wallet wallet : wallets) {
            if ("运费账户".equals(wallet.getName())) {
                freight_balance.setText("￥" + wallet.getBalance());
            }
            if ("关税账户".equals(wallet.getName())) {
                tariff_balance.setText("￥" + wallet.getBalance());
            }
        }
    }

    @OnClick({R.id.head_icon, R.id.account_manager, R.id.back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_icon:
                // 点击头像，进入修改头像界面
                Intent intent = new Intent(this, IconChangeActivity.class);
                Bundle b = new Bundle();
                b.putString("logo", logo);
                intent.putExtras(b);
                startActivityForResult(intent, Constant.ICON_CHANGE);
                break;

            // 点击账户管理
            case R.id.account_manager:

                break;

            // 点击左上角返回
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 修改头像
            case Constant.ICON_CHANGE:
                if (resultCode == RESULT_OK) {
//                    // 返回的图片路径
////                    path = data.getExtras().getString("path");
////                    GlideUtils.setImageToLocalPath(head,path);
//                    if (data.getExtras() != null) {
////                        Bitmap resultBitmap = (Bitmap) data.getExtras().get("bitmap");
//                        String uriStr = (String) data.getExtras().get("bitmap");
//                        if (uriStr != null) {
//                            Uri uri = Uri.parse(uriStr);
//                            try {
//                                head.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(uri)));
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }

                    // 返回后，需要重新获取数据
                    initData();
                }
                break;

        }
    }

    // 将 drawable 转换成 bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
