package com.birdex.bird.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.birdex.bird.R;
import com.birdex.bird.adapter.StockDetailAdapter;
import com.birdex.bird.entity.InventoryActivityEntity;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by huwei on 16/4/21.
 */
public class StockDetailActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.iv_stockdetail_close)
    public ImageView iv_close;
    @Bind(R.id.rv_stockdetail_data)
    public RecyclerView rv_data;
    private StockDetailAdapter adapter=null;
    private ArrayList<InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity> detaillist=null;
    private ArrayList<InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity.InventoryOrderEntity> orderlist=null;
    @Override
    public int getContentLayoutResId() {
        return R.layout.stock_detail_layout;
    }

    @Override
    public void initializeContentViews() {
        iv_close.setOnClickListener(this);
        Bundle bundle= getIntent().getExtras();
        orderlist=new ArrayList<>();
        detaillist=(ArrayList<InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity>)bundle.getSerializable("stocklist");
        for(InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity entity1:detaillist){
            if(entity1.getStock_detail()!=null){
                for(InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity.InventoryOrderEntity entity2:entity1.getStock_detail()){
                    orderlist.add(entity2);
                }
            }
        }
        adapter=new StockDetailAdapter(this,orderlist);
        rv_data.setLayoutManager(new LinearLayoutManager(this));
        rv_data.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.iv_stockdetail_close:
                finish();
                break;
        }
    }
}
