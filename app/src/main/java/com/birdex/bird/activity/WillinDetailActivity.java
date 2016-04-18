package com.birdex.bird.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.adapter.WillinDetailAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.InventoryActivityEntity;
import com.birdex.bird.entity.ProductEntity;
import com.birdex.bird.entity.SimpleWillinEntity;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.TimeUtil;
import com.birdex.bird.widget.TitleView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by huwei on 16/4/12.
 */
public class WillinDetailActivity extends BaseActivity implements View.OnClickListener, ExpandableListView.OnGroupClickListener {
    private String tag = "InventoryInnerDetailActivity";
    private Bundle bundle = null;
    private InventoryActivityEntity entity = null;
    private RequestParams params = null;
    private ProductEntity productEntity = null;
    private LayoutInflater inflater = null;
    @Bind(R.id.elv_willin_detail)
    public ExpandableListView elv_detail;
    @Bind(R.id.tiv_detail_willin_title)
    public TitleView tiv_head;
    private ArrayList<SimpleWillinEntity> list = null;
    private WillinDetailAdapter adapter = null;

    //设置时间
    @Override
    public int getContentLayoutResId() {
        return R.layout.willin_detail_layout;
    }

    @Override
    public void initializeContentViews() {
        inflater = LayoutInflater.from(this);
        tiv_head.setInventoryDetail(getString(R.string.inventory_inner_detail_title), R.color.gray1);
        elv_detail.setOnGroupClickListener(this);
        params = new RequestParams();
        bundle = getIntent().getExtras();
        entity = (InventoryActivityEntity) bundle.getSerializable("inventory_willin");
        params.put("product_code", entity.getProduct_code());
//        showBar();
//        startHttpRequest();
        initHead();
        initData();
        adapter = new WillinDetailAdapter(this, list);
        elv_detail.setAdapter(adapter);
        expande();
    }

    private void initData() {
        list = new ArrayList<>();
        if (entity.getStock() != null) {
            for (InventoryActivityEntity.InventoryStockEntity entity1 : entity.getStock()) {
                SimpleWillinEntity entity2 = new SimpleWillinEntity();
                entity2.setWarehouse_code(entity1.getWarehouse_code());
                entity2.setWarehouse_name(entity1.getWarehouse_name());
                if (entity1.getDetail() != null) {
                    int stock = 0;
                    for (InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity entity3 : entity1.getDetail()) {
                        if (entity3.getStorages() != null) {
                            entity2.tranlist = new ArrayList<>();
                            for (Map.Entry<String, InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity.InventoryTransInfo> entry : entity3.getStorages().entrySet()) {
                                SimpleWillinEntity.InventoryTransInfo info = entity2.new InventoryTransInfo();
                                info.setBatch_no(entry.getValue().getBatch_no());
                                info.setCreated_time(entry.getValue().getCreated_time());
                                info.setNums(entry.getValue().getNums());
                                //设置状态信息
                                info.setStatus(entity3.getStatus());
                                info.setStatus_name(entity3.getStatus_name());
                                info.setStorage_code(entry.getValue().getStorage_code());
                                info.setStorage_no(entry.getValue().getStorage_no());
                                info.setTrack_no(entry.getValue().getTrack_no());
                                info.setTrack_type(entry.getValue().getTrack_type());
                                info.setTrack_type_name(entry.getValue().getTrack_type_name());
                                entity2.tranlist.add(info);
                            }
                        }
                        if (entity3.getStock() != null) {
                            if (!"".equals(entity3.getStock().trim()) && !"null".equals(entity3.getStock().trim())) {
                                stock += Integer.parseInt(entity3.getStock().trim());
                            }
                        }
                    }
                    entity2.setCount(stock);
                    list.add(entity2);
                }
            }
        }
    }

    //    private void startHttpRequest(){
//        JsonHttpResponseHandler handler=new JsonHttpResponseHandler(){
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideBar();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                productEntity = GsonHelper.getPerson(response.toString(), ProductEntity.class);
//                if(productEntity!=null&productEntity.getError()==0){
//                    addData();
//                }else{
//
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//            }
//        };
//        handler.setTag(tag);
//        BirdApi.getProductDetail(this, params, handler);
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BirdApi.cancelRequestWithTag(tag);
    }

    //    private void addData(){
//        ProductEntity.ProductDetail detail=productEntity.getData();
//        try{
//            if(detail.getCreated_time()!=null&&!"".equals(detail.getCreated_time().trim())&&!"null".equals(detail.getCreated_time().trim())){
//                tv_createtime.setText(TimeUtil.long2Date(Long.parseLong(detail.getCreated_time())));
//            }
//        }catch (Exception ex){
//        }
//        tv_productcode.setText(entity.getExternal_no());
//        tv_name.setText(entity.getName());
//    }
    private void initHead() {
        View headview = inflater.inflate(R.layout.willin_detail_head_layout, null, false);
        TextView tv_productname = (TextView) headview.findViewById(R.id.tv_willin_detail_name);
        TextView tv_productcode = (TextView) headview.findViewById(R.id.tv_willin_detail_productcode);
        if (entity.getName() != null) {
            tv_productname.setText(entity.getName());
        } else {
            tv_productname.setText(R.string.unkonw_product);
        }
        if (entity.getExternal_no() == null) {
            tv_productcode.setText("");
        } else {
            tv_productcode.setText(entity.getExternal_no());
        }
        elv_detail.addHeaderView(headview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_willin_viewother:
                break;
        }
    }

    private void expande() {
        for (int i = 0; i < adapter.getGroupCount(); i++) {

            elv_detail.expandGroup(i);

        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        //禁止收缩
        return true;
    }
}
