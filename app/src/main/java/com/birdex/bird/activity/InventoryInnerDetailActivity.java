package com.birdex.bird.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.birdex.bird.R;
import com.birdex.bird.adapter.InventoryInnerDetailAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.InventoryActivityEntity;
import com.birdex.bird.widget.TitleView;
import com.birdex.bird.xrecyclerview.XRecyclerView;
import com.loopj.android.http.RequestParams;

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
        xrv_inner.setAdapter(adapter);
        params=new RequestParams();
        params.put("","");
    }
    private void startHttp(){

    }

}
