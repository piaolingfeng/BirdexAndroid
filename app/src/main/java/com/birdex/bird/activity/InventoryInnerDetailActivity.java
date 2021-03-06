package com.birdex.bird.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.adapter.InventoryInnerDetailAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.InventoryActivityEntity;
import com.birdex.bird.entity.OrderDetailEntity;
import com.birdex.bird.entity.ProductEntity;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.widget.TitleView;
import com.birdex.bird.widget.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;

public class InventoryInnerDetailActivity extends BaseActivity {

    @Bind(R.id.tiv_detail_inner_title)
    public TitleView tiv_head;
    @Bind(R.id.xrv_detail_inner)
    public XRecyclerView xrv_inner;
    private InventoryActivityEntity entity = null;
    private Bundle bundle=null;
    private InventoryInnerDetailAdapter adapter=null;
    private String tag="InventoryInnerDetailActivity";
    private RequestParams params=null;
    private ProductEntity productEntity=null;
    @Override
    public int getContentLayoutResId() {
        return R.layout.detail_inner_layout;
    }

    @Override
    public void initializeContentViews() {
        tiv_head.setInventoryDetail(getString(R.string.inventory_inner_detail_title), R.color.gray1);
        //禁止上下拉
        xrv_inner.setPullRefreshEnabled(false);
        xrv_inner.setLoadingMoreEnabled(false);
        xrv_inner.setLayoutManager(new LinearLayoutManager(this));
        bundle=getIntent().getExtras();
        entity=(InventoryActivityEntity)bundle.getSerializable("inventory_inner");
        adapter=new InventoryInnerDetailAdapter(this,entity);

        params=new RequestParams();
        params.put("product_code",entity.getProduct_code());
        showBar();
        startHttp();
    }
    private void startHttp(){
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
                initHead();
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
    private void initHead(){
        if(productEntity!=null){
            ProductEntity.ProductDetail detail=productEntity.getData();
            LayoutInflater inflater=LayoutInflater.from(this);
            View view=inflater.inflate(R.layout.detail_inner_head_layout, null, false);
            TextView tv_productname=(TextView)view.findViewById(R.id.tv_inner_productname);
            tv_productname.setText(detail.getName());
            TextView tv_upccode=(TextView)view.findViewById(R.id.tv_inner_upccode);
            tv_upccode.setText(detail.getUpc());
            TextView tv_productcode=(TextView)view.findViewById(R.id.tv_inner_productcode);
            tv_productcode.setText(detail.getExternal_no());
            TextView tv_customscode=(TextView)view.findViewById(R.id.tv_inner_customscode);
            tv_customscode.setText(detail.getCustoms_record_id());
            TextView tv_weight=(TextView)view.findViewById(R.id.tv_inner_weight);
            tv_weight.setText(detail.getWeight()+detail.getWeight_unit());
            TextView tv_capacity=(TextView)view.findViewById(R.id.tv_inner_capacity);
            tv_capacity.setText(detail.getProduct_length()+"*"+detail.getWidth()+"*"+detail.getHeight());
            TextView tv_declareprice=(TextView)view.findViewById(R.id.tv_inner_declareprice);
            tv_declareprice.setText(detail.getPrice()+detail.getPrice_unit());
            TextView tv_productcomposition=(TextView)view.findViewById(R.id.tv_inner_productcomposition);
            tv_productcomposition.setText(detail.getIngredients());
            TextView tv_productdescribte=(TextView)view.findViewById(R.id.tv_inner_productdescribte);
            tv_productdescribte.setText(detail.getDetail());
            TextView tv_availablecount=(TextView)view.findViewById(R.id.tv_inner_availablecount);
            int availableCount = 0;
            for (InventoryActivityEntity.InventoryStockEntity entity1 : entity.getStock()) {
                if(entity1.getDetail()!=null){
                    for (InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity entity2 : entity1.getDetail()) {
                        //可用=总入库-总出库-损耗-盘亏-占用-丢失-过期-破损+盘盈+允许超收数量
                        int in_stock =0,out_stock=0,spoilage_stock=0,shortage_stock=0,block_stock=0,lose_stock=0,expire_stock=0,damage_stock=0,overage_stock=0,overdraft_stock=0;
                        //可用=总入库-总出库-损耗-盘亏-占用-丢失-过期-破损+盘盈+允许超收数量
                        if(entity2.getIn_stock()!=null){
                            in_stock = Integer.parseInt(entity2.getIn_stock());
                        }
                        if(entity2.getOut_stock()!=null){
                            out_stock = Integer.parseInt(entity2.getOut_stock());
                        }
                        if(entity2.getSpoilage_stock()!=null){
                            spoilage_stock = Integer.parseInt(entity2.getSpoilage_stock());
                        }
                        if(entity2.getShortage_stock()!=null){
                            shortage_stock = Integer.parseInt(entity2.getShortage_stock());
                        }
                        if(entity2.getBlock_stock()!=null){
                            block_stock = Integer.parseInt(entity2.getBlock_stock());
                        }
                        if(entity2.getLose_stock()!=null){
                            lose_stock = Integer.parseInt(entity2.getLose_stock());
                        }
                        if(entity2.getExpire_stock()!=null){
                            expire_stock = Integer.parseInt(entity2.getExpire_stock());
                        }
                        if(entity2.getDamage_stock()!=null){
                            damage_stock = Integer.parseInt(entity2.getDamage_stock());
                        }
                        if(entity2.getOverage_stock()!=null){
                            overage_stock = Integer.parseInt(entity2.getOverage_stock());
                        }
                        if(entity2.getOverdraft_stock()!=null){
                            overdraft_stock = Integer.parseInt(entity2.getOverdraft_stock());
                        }
                        availableCount += (in_stock - out_stock - spoilage_stock - shortage_stock - block_stock - lose_stock - expire_stock - damage_stock - overage_stock + overdraft_stock);
//                    //占用=
//                    if(entity2.getBlock_stock()!=null){
//                        occupancyCount += Integer.parseInt(entity2.getBlock_stock());
//                    }else{
//                        occupancyCount += 0;
//                    }
                    }
                }
            }
            tv_availablecount.setText(String.valueOf(availableCount));
            xrv_inner.addHeaderView(view);
            xrv_inner.setAdapter(adapter);
        }
    }
}
