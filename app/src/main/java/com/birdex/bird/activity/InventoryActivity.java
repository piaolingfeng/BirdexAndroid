package com.birdex.bird.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.birdex.bird.R;
import com.birdex.bird.adapter.InventoryAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.biz.InventoryBiz;
import com.birdex.bird.entity.InventoryEntity;
import com.birdex.bird.util.SafeProgressDialog;
import com.birdex.bird.util.T;
import com.birdex.bird.widget.RotateLoading;
import com.birdex.bird.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by huwei on 16/4/5.
 */
public class InventoryActivity extends BaseActivity implements XRecyclerView.LoadingListener{
    private RequestParams params=null;
    //当前页数，首页为1
    private int currentPage=1;
    @Bind(R.id.rv_inventory)
    public XRecyclerView rv_inventory=null;
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
//    ProgressDialog bar;
//    Dialog loadingDialog;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.inventory_layout);
//
//        //初始化解析业务
//        biz=new InventoryBiz();
//        //网络请求参数
//        params=new RequestParams();
//        //商品类型，20表示物料，默认10表示商品
//        params.put("product_type",10);
//        //40表示发往仓库，1表示在库，10表示正常，20表示库存紧张，30表示断货
//        //默认进入为“待入库”
//        params.put("stock_status", 1);
////        params.put("page_num",2);
//        initView();
//        //
//        showBar();
//        startRequest();
//    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.inventory_layout;
    }

    @Override
    public void initializeContentViews() {
        //初始化解析业务
        biz=new InventoryBiz();
        //网络请求参数
        params=new RequestParams();
        //商品类型，20表示物料，默认10表示商品
        params.put("product_type",10);
        //40表示发往仓库，1表示在库，10表示正常，20表示库存紧张，30表示断货
        //默认进入为“待入库”
        params.put("stock_status", 1);
        rv_inventory=(XRecyclerView)findViewById(R.id.rv_inventory);
        rv_inventory.setLoadingMoreEnabled(true);
        rv_inventory.setPullRefreshEnabled(true);
        rv_inventory.setLoadingListener(this);
        rv_inventory.setLayoutManager(new LinearLayoutManager(this));
        this.list=new ArrayList<>();
        //初始化适配器
        adapter=new InventoryAdapter(this,this.list,type);
        rv_inventory.setAdapter(adapter);
    }

//    //初始化view
//    private void initView(){
//        rv_inventory=(XRecyclerView)findViewById(R.id.rv_inventory);
//        rv_inventory.setLoadingMoreEnabled(true);
//        rv_inventory.setPullRefreshEnabled(true);
//        rv_inventory.setLoadingListener(this);
//        rv_inventory.setLayoutManager(new LinearLayoutManager(this));
//        this.list=new ArrayList<>();
//        //初始化适配器
//        adapter=new InventoryAdapter(this,this.list,type);
//        rv_inventory.setAdapter(adapter);
//        //
//        bar = new ProgressDialog(this);
//        bar.setCanceledOnTouchOutside(false);
//    }
    private void startRequest(){
        params.put("page_no",currentPage);
        BirdApi.getInventory(this,params,new JsonHttpResponseHandler(){
            @Override
            public void onFinish() {
                super.onFinish();
                //结束动画
                stopHttpAnim();
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
        hideBar();
        rv_inventory.refreshComplete();
        rv_inventory.loadMoreComplete();
    }


    @Override
    public void onRefresh() {
        currentPage=1;

        startRequest();
    }

    @Override
    public void onLoadMore() {
        if(countPage>=currentPage){
            //非最后一页
            startRequest();
        }else{
            T.showShort(this, R.string.inventory_tip_3);
            stopHttpAnim();
        }
    }
//    public void showBar() {
//        bar.setMessage("加载中...");
//        bar.show();
//    }
//
//    public void hideBar() {
//        bar.dismiss();
//    }
//
//    public void showBarCommit() {
//        bar.setMessage("正在提交...");
//        bar.show();
//    }
//
//    public void showLoading() {
//        loadingDialog = new SafeProgressDialog(this, R.style.semester_dialog);// 创建自定义样式dialog
////        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
////        loadingDialog.setCanceledOnTouchOutside(false);
//        View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
//        loadingDialog.setContentView(view);// 设置布局
//        final RotateLoading loading = (RotateLoading) view.findViewById(R.id.rotateloading);
//        loading.start();
//        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                loading.stop();
//            }
//        });
//        loadingDialog.show();
//    }
}
