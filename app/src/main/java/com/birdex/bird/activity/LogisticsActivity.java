package com.birdex.bird.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.TrackListAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.Track;
import com.birdex.bird.entity.Tracking;
import com.birdex.bird.util.JsonHelper;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/4/6.
 */
public class LogisticsActivity extends BaseActivity implements View.OnClickListener {

    // 电话号码
    @Bind(R.id.phone)
    TextView phone;

    // 轨迹列表
    @Bind(R.id.tracklist)
    RecyclerView tracklist;

    // 传递过来的order_code
    private String orderNo;

    // 轨迹图片
    @Bind(R.id.pic_tv_0)
    TextView pic_tv_0;
    @Bind(R.id.pic_iv_0)
    ImageView pic_iv_0;
    @Bind(R.id.pic_utv_0)
    TextView pic_utv_0;

    @Bind(R.id.pic_tv_1)
    TextView pic_tv_1;
    @Bind(R.id.pic_iv_1)
    ImageView pic_iv_1;
    @Bind(R.id.pic_utv_1)
    TextView pic_utv_1;

    @Bind(R.id.pic_tv_2)
    TextView pic_tv_2;
    @Bind(R.id.pic_iv_2)
    ImageView pic_iv_2;
    @Bind(R.id.pic_utv_2)
    TextView pic_utv_2;

    @Bind(R.id.pic_tv_3)
    TextView pic_tv_3;
    @Bind(R.id.pic_iv_3)
    ImageView pic_iv_3;
    @Bind(R.id.pic_utv_3)
    TextView pic_utv_3;

    @Bind(R.id.pic_tv_4)
    TextView pic_tv_4;
    @Bind(R.id.pic_iv_4)
    ImageView pic_iv_4;
    @Bind(R.id.pic_utv_4)
    TextView pic_utv_4;

    @Bind(R.id.pic_tv_5)
    TextView pic_tv_5;
    @Bind(R.id.pic_iv_5)
    ImageView pic_iv_5;
    @Bind(R.id.pic_utv_5)
    TextView pic_utv_5;

    // 订单号
    @Bind(R.id.order_oms_no)
    TextView order_oms_no;

    // 客户单号
    @Bind(R.id.order_no)
    TextView order_no;

    // 收件人
    @Bind(R.id.receiver_name)
    TextView receiver_name;

    // 状态
    @Bind(R.id.order_status)
    TextView order_status;

    // 已运送
    @Bind(R.id.transport)
    TextView transport;

    // 出库时间
    @Bind(R.id.out_storage)
    TextView out_storage;

    // 右上角文字
    @Bind(R.id.save)
    TextView save;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_logistics;
    }

    @Override
    public void initializeContentViews() {
        initData();
    }

    // 初始化数据
    private void initData() {
        save.setText(getString(R.string.track_after) + "\t\t");

        Drawable refresh = getResources().getDrawable(R.drawable.refresh);
        //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        refresh.setBounds(0, 0, refresh.getMinimumWidth(), refresh.getMinimumHeight());

        save.setCompoundDrawables(null, null, refresh, null);

        // 传过来的 order_code
        orderNo = getIntent().getExtras().getString("order_code");
        order_status.setText(getIntent().getExtras().getString("Status_name"));
        phone.setText(getIntent().getExtras().getString("Receiver_mobile"));
        getInterfaceData();

    }

    // 模拟数据
    private List<Tracking> getData() {
        List<Tracking> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Tracking tracking = new Tracking();
            tracking.setContext("宝贝已经完成出库" + i);
            tracking.setTime("2015-09-09 10:32:05");
            list.add(tracking);
        }
        return list;
    }

    // 获取到的 轨迹 data
    private Track track;

    // 获取接口数据
    private void getInterfaceData() {
        showLoading();

        // 获取接口数据
        RequestParams params = new RequestParams();
        params.add("order_code", orderNo);
        BirdApi.getTracking(MyApplication.getInstans(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if ("0".equals(response.getString("error"))) {
                        track = JsonHelper.parseObject((JSONObject) response.get("data"), Track.class);
                        if (track != null) {
                            // 将 track 中的值 set
                            setTrackData();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideLoading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong));
            }
        });
    }

    // 将 track 的值进行 set
    private void setTrackData() {
        order_oms_no.setText(track.getOrder_oms_no());
        order_no.setText(track.getOrder_no());
        receiver_name.setText(track.getReceiver_name());

        transport.setText(track.getTracking_length());
//        transport.setText("15天22小时");
        out_storage.setText(track.getCheckout_time());
//        out_storage.setText("2015-09-09 10:32:13");

        String status = track.getStatus();
        // 通过 status 绘制轨迹图片
        paintPic(status);

        // 轨迹详情
        tracklist.setLayoutManager(new LinearLayoutManager(this));
        TrackListAdapter adapter = new TrackListAdapter(this, track.getTrackings());
        tracklist.setAdapter(adapter);
    }

    // 绘制轨迹图片
    private void paintPic(String status) {
        int index = Integer.parseInt(status);
        if (index >= 0) {
            paint0();
        }
        if (index >= 1) {
            paint1();
        }
        if (index >= 2) {
            paint2();
        }
        if (index >= 3) {
            paint3();
        }
        if (index >= 4) {
            paint4();
        }
        if (index >= 5) {
            paint5();
        }
    }

    private void paint0() {
        pic_tv_0.setBackgroundColor(Color.parseColor("#13A7DF"));
        pic_iv_0.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.getorder_y));
        pic_utv_0.setTextColor(Color.parseColor("#13A7DF"));
    }

    private void paint1() {
        pic_tv_1.setBackgroundColor(Color.parseColor("#13A7DF"));
        pic_iv_1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.orderout_y));
        pic_utv_1.setTextColor(Color.parseColor("#13A7DF"));
    }

    private void paint2() {
        pic_tv_2.setBackgroundColor(Color.parseColor("#13A7DF"));
        pic_iv_2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fly_y));
        pic_utv_2.setTextColor(Color.parseColor("#13A7DF"));
    }

    private void paint3() {
        pic_tv_3.setBackgroundColor(Color.parseColor("#13A7DF"));
        pic_iv_3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.clear_y));
        pic_utv_3.setTextColor(Color.parseColor("#13A7DF"));
    }

    private void paint4() {
        pic_tv_4.setBackgroundColor(Color.parseColor("#13A7DF"));
        pic_iv_4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.transportation_y));
        pic_utv_4.setTextColor(Color.parseColor("#13A7DF"));
    }

    private void paint5() {
        pic_tv_5.setBackgroundColor(Color.parseColor("#13A7DF"));
        pic_iv_5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sign_y));
        pic_utv_5.setTextColor(Color.parseColor("#13A7DF"));
    }

    public static final int CALL_PHONE_REQUEST_CODE = 1;

    // 6.0 权限控制
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PHONE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 用户允许
                callPhone();
            } else {
                // Permission Denied
            }
        }
    }

    // 打电话
    private void callPhone(){
        String Telphone = phone.getText().toString();
        Uri uri = Uri.parse("tel:" + Telphone);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(uri);
        startActivity(intent);
    }

    @OnClick({R.id.phone, R.id.back, R.id.copy_logistics, R.id.save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请CALL_PHONE权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                            CALL_PHONE_REQUEST_CODE);
                } else {
                    callPhone();
                }

                break;
            //左上角返回键
            case R.id.back:
                finish();
                break;

            // 复制物流信息
            case R.id.copy_logistics:
                if (track != null) {
                    copyData();
                    T.showShort(this, getString(R.string.copy_tip));
                }
                break;

            // 刷新
            case R.id.save:
                // 重新加载数据
                getInterfaceData();
                break;
        }
    }

    // 复制物流信息到剪切板
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyData() {
        List<Tracking> trackings = track.getTrackings();
        StringBuffer sb = new StringBuffer();
        for (Tracking tr : trackings) {
            sb.append(tr.getTime() + "\n");
            sb.append(tr.getContext() + "\n");
        }
        ClipboardManager clip = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clip.setText(sb.toString());
    }
}
