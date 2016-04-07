package com.birdex.bird.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.TrackListAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.Tracking;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/4/6.
 */
public class LogisticsActivity extends BaseActivity implements View.OnClickListener{

    // 电话号码
    @Bind(R.id.phone)
    TextView phone;

    // 轨迹列表
    @Bind(R.id.tracklist)
    RecyclerView tracklist;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_logistics;
    }

    @Override
    public void initializeContentViews() {
        initData();
    }

    // 初始化数据
    private void initData(){

        getInterfaceData();

        tracklist.setLayoutManager(new LinearLayoutManager(this));
        TrackListAdapter adapter = new TrackListAdapter(this,getData());
        tracklist.setAdapter(adapter);

    }

    // 模拟数据
    private List<Tracking> getData(){
        List<Tracking> list = new ArrayList<>();
        for(int i=0;i<8;i++){
            Tracking tracking = new Tracking();
            tracking.setContext("宝贝已经完成出库" + i);
            tracking.setTime("2015-09-09 10:32:05");
            list.add(tracking);
        }
        return list;
    }

    // 获取接口数据
    private void getInterfaceData(){
        showLoading();

        // 获取接口数据
        RequestParams params = new RequestParams();
        params.put("order_code","BH151110254100");
        BirdApi.getTracking(MyApplication.getInstans(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


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


    @OnClick({R.id.phone})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phone:
                String Telphone=phone.getText().toString();
                Uri uri= Uri.parse("tel:" + Telphone);
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(uri);
                startActivity(intent);
                break;
        }
    }
}
