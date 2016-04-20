package com.birdex.bird.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.SameOrderAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.PredicitionDetailEntity;
import com.birdex.bird.entity.SimpleWillinEntity;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by huwei on 16/4/18.
 */
public class SameOrderOtherActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.iv_sameorder_close)
    public ImageView iv_close;
    @Bind(R.id.xrv_sameorder_others)
    public RecyclerView xrv_sameorder;
    private SimpleWillinEntity entity = null;
    private SimpleWillinEntity.InventoryTransInfo transInfo=null;
    private PredicitionDetailEntity predicentity=null;
    //解析的
    private Intent intent = null;
    private Bundle bundle = null;
    //索引
    private int cindex = 0;
    private final  String tag="SameOrderOtherActivity";
    private RequestParams params=null;
    private SameOrderAdapter adapter=null;
    //数据显示器
    private ArrayList<PredicitionDetailEntity.PredicitionData.PredicitionDetailProduct> list=null;
    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_sameorder_other;
    }

    @Override
    public void initializeContentViews() {
        params=new RequestParams();
        intent = getIntent();
        bundle = intent.getExtras();
        params=new RequestParams();
        entity = (SimpleWillinEntity) bundle.getSerializable("entity");
        cindex = bundle.getInt("cindex");
        iv_close.setOnClickListener(this);
//        xrv_sameorder.setPullRefreshEnabled(false);
//        xrv_sameorder.setLoadingMoreEnabled(false);
        if (entity != null) {
            transInfo=entity.getTranlist().get(cindex);
            adapter=new SameOrderAdapter(this,inithead());
            xrv_sameorder.setLayoutManager(new LinearLayoutManager(this));
            xrv_sameorder.setAdapter(adapter);
            params.add("storage_code", transInfo.getStorage_code());
            startHttp();
        }
    }

    private View inithead() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.sameorder_head_layout, null, false);
        TextView tv_inventorycode = (TextView) view.findViewById(R.id.tv_sameorder_inventorycode);
        tv_inventorycode.setText(String.valueOf(transInfo.getStorage_no()));
        TextView tv_currentstatus = (TextView) view.findViewById(R.id.tv_sameorder_currentstatu);
        tv_currentstatus.setText(String.valueOf(transInfo.getStatus_name()));
        TextView tv_warehouse = (TextView) view.findViewById(R.id.tv_sameorder_warehouse);
        tv_warehouse.setText(String.valueOf(entity.getWarehouse_code()));
        TextView tv_transcode = (TextView) view.findViewById(R.id.tv_sameorder_transcode);
        tv_transcode.setText(String.valueOf(transInfo.getTrack_no()));
        TextView tv_transstyle = (TextView) view.findViewById(R.id.tv_sameorder_transstyle);
        String transstyle=getString(R.string.sameorder_others_transstyle);
        transstyle=transstyle.replace("@",transInfo.getTrack_type_name());
        tv_transstyle.setText(transstyle);
        return view;
//        xrv_sameorder.addHeaderView(view);
    }

    private void startHttp(){
        showLoading();
        JsonHttpResponseHandler handler= new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                hideLoading();
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                predicentity = GsonHelper.getPerson(response.toString(), PredicitionDetailEntity.class);
                if (predicentity != null&&predicentity.getError()==0){
                    adapter.setDataSource((ArrayList)predicentity.getData().getProducts());
                }
                else {
                    try {
                        if (response.get("data") != null)
                            T.showLong(SameOrderOtherActivity.this, response.get("data").toString() + "请重新登录");
                        else
                            T.showLong(SameOrderOtherActivity.this, getString(R.string.parse_error));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse != null)
                    T.showLong(MyApplication.getInstans(), "error:" + errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
        handler.setTag(tag);
        BirdApi.getStorageDetail(this, params, handler);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sameorder_close:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
