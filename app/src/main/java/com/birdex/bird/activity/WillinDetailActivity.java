package com.birdex.bird.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.InventoryActivityEntity;
import com.birdex.bird.entity.ProductEntity;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.TimeUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by huwei on 16/4/12.
 */
public class WillinDetailActivity extends  BaseActivity implements View.OnClickListener{
    private String tag="InventoryInnerDetailActivity";
    private Bundle bundle=null;
    private InventoryActivityEntity entity = null;
    @Bind(R.id.tv_willin_detail_createtime)
    public TextView tv_createtime;
    @Bind(R.id.tv_willin_detail_productcode)
    public TextView tv_productcode;
    @Bind(R.id.tv_willin_detail_ininventorycode)
    public TextView tv_ininventorycode;
    @Bind(R.id.tv_willin_detail_transcode)
    public TextView tv_transcode;
    @Bind(R.id.tv_willin_detail_transstayle)
    public TextView tv_transstayle;
    @Bind(R.id.tv_willin_detail_inventory)
    public TextView tv_inventory;
    @Bind(R.id.tv_willin_detail_num)
    public TextView tv_num;
    @Bind(R.id.tv_willin_detail_status)
    public TextView tv_status;
    private RequestParams params=null;
    private ProductEntity productEntity=null;
    @Bind(R.id.iv_willin_detail_close)
    public ImageView iv_close;
    @Bind(R.id.tv_willin_detail_name)
    public TextView tv_name;
    @Bind(R.id.detail_willin_viewother)
    public TextView tv_viewothers;
    //设置时间
    @Override
    public int getContentLayoutResId() {
        return R.layout.willin_detail_layout;
    }

    @Override
    public void initializeContentViews() {
        iv_close.setOnClickListener(this);
        tv_viewothers.setOnClickListener(this);
        params=new RequestParams();
        bundle=getIntent().getExtras();
        entity=(InventoryActivityEntity)bundle.getSerializable("inventory_willin");
        params.put("product_code",entity.getProduct_code());
        showBar();
        startHttpRequest();
    }
    private void startHttpRequest(){
        JsonHttpResponseHandler handler=new JsonHttpResponseHandler(){
            @Override
            public void onFinish() {
                super.onFinish();
                hideBar();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                productEntity = GsonHelper.getPerson(response.toString(), ProductEntity.class);
                if(productEntity!=null&productEntity.getError()==0){
                    addData();
                }else{

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        };
        handler.setTag(tag);
        BirdApi.getProductDetail(this, params, handler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
    private void addData(){
        ProductEntity.ProductDetail detail=productEntity.getData();
        try{
            if(detail.getCreated_time()!=null&&!"".equals(detail.getCreated_time().trim())&&!"null".equals(detail.getCreated_time().trim())){
                tv_createtime.setText(TimeUtil.long2Date(Long.parseLong(detail.getCreated_time())));
            }
        }catch (Exception ex){
        }
        tv_productcode.setText(entity.getExternal_no());
        tv_name.setText(entity.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_willin_detail_close:
                finish();
                break;
            case R.id.detail_willin_viewother:
                break;
        }
    }
}
