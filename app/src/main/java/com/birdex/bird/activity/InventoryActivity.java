package com.birdex.bird.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.birdex.bird.R;
import com.birdex.bird.adapter.InventoryAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.biz.InventoryBiz;
import com.birdex.bird.entity.InventoryEntity;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by huwei on 16/4/5.
 */
public class InventoryActivity extends AppCompatActivity {
    private RequestParams params=null;
    //当前页数，首页为1
    private int currentPage=1;
    public RecyclerView rv_inventory=null;
    //适配器
    private InventoryAdapter adapter=null;
    //所有条目总数
    private int countNum=0;
    //总页数
    private int countPage=0;
    //解析数据业务
    private InventoryBiz biz=null;
    //数据集合
    private ArrayList<InventoryEntity> list=null;
    //默认为在库库存
    private InventoryAdapter.Type type= InventoryAdapter.Type.Inner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_layout);
        //初始化解析业务
        biz=new InventoryBiz();
        //网络请求参数
        params=new RequestParams();
        //商品类型，20表示物料，默认10表示商品
        params.put("product_type",10);
        //40表示发往仓库，1表示在库，10表示正常，20表示库存紧张，30表示断货
        //默认进入为“待入库”
        params.put("stock_status",40);
//        params.put("page_num",2);
        initView();
        startRequest();
    }
    //初始化view
    private void initView(){
        rv_inventory=(RecyclerView)findViewById(R.id.rv_inventory);
        rv_inventory.setLayoutManager(new LinearLayoutManager(this));
        this.list=new ArrayList<>();
        //初始化适配器
        adapter=new InventoryAdapter(this,this.list,type);
        rv_inventory.setAdapter(adapter);
    }
    private void startRequest(){
        params.put("page_no",currentPage);
        BirdApi.getInventory(this,params,new JsonHttpResponseHandler(){
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                //结束动画
                stopHttpAnim();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(statusCode==200){
                    int code=-1;
                    try {
                        code=response.getInt("error");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(code==0){
                        JSONObject jobj= null;
                        try {
                            jobj = response.getJSONObject("data");
                            countNum=jobj.getInt("count");
                            countPage=jobj.getInt("page_num");
                            JSONObject jroot=jobj.getJSONObject("products");
                            list=biz.parseJson2List(jroot);
                            if(currentPage==1){
                                //首页为重新加载
                                adapter.setDataSource(list);
                            }else {
                                //其他页面则为添加
                                adapter.addDataSource(list);
                            }
                            //下次页数加1
                            currentPage++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        T.showShort(InventoryActivity.this,R.string.inventory_tip_2);
                    }
                }else{
                    T.showShort(InventoryActivity.this,R.string.inventory_tip_1);
                }
                //结束动画
                stopHttpAnim();
            }
        });
    }
    /*
     *停止动画
     */
    private void stopHttpAnim(){

    }


}
