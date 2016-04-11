package com.birdex.bird.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.CommonSimpleAdapter;
import com.birdex.bird.adapter.InventoryAdapter;
import com.birdex.bird.adapter.InventoryWillInAdapter;
import com.birdex.bird.adapter.OrderWareHouseAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.biz.InventoryBiz;
import com.birdex.bird.entity.InventoryEntity;
import com.birdex.bird.entity.OrderManagerEntity;
import com.birdex.bird.entity.WarehouseEntity;
import com.birdex.bird.fragment.IndexFragment;
import com.birdex.bird.helper.OnShowGoTopListener;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.SafeProgressDialog;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
import com.birdex.bird.widget.ClearEditText;
import com.birdex.bird.widget.HeaderView;
import com.birdex.bird.widget.RotateLoading;
import com.birdex.bird.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by huwei on 16/4/5.
 */
public class InventoryActivity extends BaseActivity implements XRecyclerView.LoadingListener, HeaderView.OnHeadViewClickLister, View.OnClickListener, TabLayout.OnTabSelectedListener,OnShowGoTopListener {
    private RequestParams params = null;
    //当前页数，首页为1
    private int currentPage = 1;
    @Bind(R.id.rv_inventory)
    public XRecyclerView rv_inventory = null;
    @Bind(R.id.tl_inventory_change)
    public TabLayout tl_items;
    @Bind(R.id.hv_invenyory)
    public HeaderView hv_title;
    //搜索
    @Bind(R.id.et_search)
    public ClearEditText et_search;
    @Bind(R.id.tv_inventory_alltxt)
    public TextView tv_count;
    //跳顶按钮
    @Bind(R.id.fab_inventory_gotop)
    public FloatingActionButton fab_gotop;
    //适配器
    private InventoryAdapter adapter = null;
    private InventoryWillInAdapter willInAdapter = null;
    //所有条目总数
    private int countNum = 0;
    //总页数
    private int countPage = 0;
    //解析数据业务
    private InventoryBiz biz = null;
    //数据集合
    private ArrayList<InventoryEntity> list = null;

    @Override
    public void onShowFloatAtionButton(boolean isShow) {
        if(fab_gotop.getVisibility()==View.GONE&&isShow){
            fab_gotop.setVisibility(View.VISIBLE);
        }else if(fab_gotop.getVisibility()==View.VISIBLE&&!isShow){
            fab_gotop.setVisibility(View.GONE);
        }
    }

    //默认为在库库存
    //设置显示的类型
    public enum Type {
        //在库，待入库
        Inner, Willin,OutofWarning
    }

    private Type type = Type.Inner;
    //设置请求的tag
    private String tag = "InventoryActivity";
    private WarehouseEntity warehouseEntity = null;//所有仓库列表
    //弹出框
    private PopupWindow mPopupWindow = null;
    private PopupWindow menuPop=null;
    //popwindow的adapter
    private OrderWareHouseAdapter wareAdapter = null;
    @Bind(R.id.tv_inventory_all)
    public TextView tv_allInventory = null;
    @Bind(R.id.tv_inventory_sort_available)
    public TextView tv_sortavailable = null;
    @Bind(R.id.tv_inventory_sort_time)
    public TextView tv_sorttime;
    //获取屏幕宽度
    private int width = 0;
    private String countTxt="";
    @Override
    public int getContentLayoutResId() {
        return R.layout.inventory_layout;
    }

    @Override
    public void initializeContentViews() {
        initSystemBar(R.color.blue_head_1);
        EventBus.getDefault().register(this);
        countTxt=getString(R.string.inventory_all_data);
        //
        hv_title.setOnHeadViewClickLister(this);
        //初始化解析业务
        biz = new InventoryBiz();
        tv_allInventory.setOnClickListener(this);
        tv_sortavailable.setOnClickListener(this);
        tv_sorttime.setOnClickListener(this);
        //获取屏幕宽度
        width = getWindowManager().getDefaultDisplay().getWidth();
        //网络请求参数
        params = new RequestParams();
        //商品类型，20表示物料，默认10表示商品
        params.put("product_type", 10);
        //40表示发往仓库，1表示在库，10表示正常，20表示库存紧张，30表示断货
        //默认进入为“待入库”
        params.put("stock_status", 1);
        rv_inventory = (XRecyclerView) findViewById(R.id.rv_inventory);
        rv_inventory.setLoadingMoreEnabled(true);
        rv_inventory.setPullRefreshEnabled(false);
        rv_inventory.setLoadingListener(this);
        rv_inventory.setLayoutManager(new LinearLayoutManager(this));
        this.list = new ArrayList<>();
        //初始化适配器
        adapter = new InventoryAdapter(this, this.list);
        adapter.setOnGoTopListener(this);
        willInAdapter = new InventoryWillInAdapter(this, this.list);
        willInAdapter.setOnGoTopListener(this);
        rv_inventory.setAdapter(adapter);
        //tablayout
//        tl_items.addTab(tl_items.newTab().setText(R.string.inventory_item_1));
//        tl_items.addTab(tl_items.newTab().setText(R.string.inventory_item_2));
//        tl_items.setOnTabSelectedListener(this);
        //显示数量
        tv_count.setText(countTxt.replace("@", "0"));
        //初始化搜索框
        initSearch();
        //初始化弹框
        initPopWindow();
        //显示加载动画
        showBar();
        //请求正式数据
        startRequest();
        initStatus();
        //设置至顶按钮
        fab_gotop.setOnClickListener(this);
    }
    /*
    *初始化状态
    */
    private void initStatus(){
        String [] status=getResources().getStringArray(R.array.inventory_all_status);
        LayoutInflater inflater=LayoutInflater.from(this);
        String str="";
        for(int i=0;i<status.length;i++){
            str=status[i];
            View view=inflater.inflate(R.layout.status_layout,null,false);
            tl_items.addTab(tl_items.newTab().setCustomView(view));
            TextView tv_name=(TextView)view.findViewById(R.id.tv_inventory_status_name);
            TextView tv_num=(TextView)view.findViewById(R.id.tv_inventory_status_num);
            tv_name.setText(str);
            if(i==0||i==1){
                tv_num.setVisibility(View.GONE);
            }else{
                tv_num.setText("0");
                tv_num.setVisibility(View.GONE);
            }
        }
        tl_items.setOnTabSelectedListener(this);
    }
    private void startRequest() {
        params.put("page_no", currentPage);
        JsonHttpResponseHandler responeHandler = new JsonHttpResponseHandler() {
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
                if (statusCode == 200) {
                    int code = -1;
                    try {
                        code = response.getInt("error");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code == 0) {
                        JSONObject jobj = null;
                        try {
                            jobj = response.getJSONObject("data");
                            countNum = jobj.getInt("count");
                            countPage = jobj.getInt("page_num");
                            Object proObj=jobj.get("products");
                            if((proObj instanceof JSONArray)&&currentPage==1){
                                JSONArray jroot = jobj.getJSONArray("products");
                                //搜索或者限制条件导致没有数据
                                if (type == Type.Inner) {
                                    adapter.clearDataSource();
                                } else if (type == Type.Willin) {
                                    willInAdapter.clearDataSource();
                                }else if(type==Type.OutofWarning){
                                    adapter.clearDataSource();
                                }
                            }else if(proObj instanceof JSONObject){
                                JSONObject jroot = jobj.getJSONObject("products");
                                list = biz.parseJson2List(jroot);
                                if (list != null && list.size() > 0) {
                                    if (currentPage == 1) {
                                        //显示数量
                                        tv_count.setText(countTxt.replace("@",countNum+""));
                                        //首页为重新加载
                                        if (type == Type.Inner) {
                                            adapter.setDataSource(list);
                                        } else if (type == Type.Willin) {
                                            willInAdapter.setDataSource(list);
                                        }else if(type==Type.OutofWarning){
                                            adapter.setDataSource(list);
                                        }
                                    } else {
                                        //其他页面则为添加
                                        if (type == Type.Inner) {
                                            adapter.addDataSource(list);
                                        } else if (type == Type.Willin) {
                                            willInAdapter.addDataSource(list);
                                        }else if(type==Type.OutofWarning){
                                            adapter.addDataSource(list);
                                        }

                                    }
                                    //下次页数加1
                                    currentPage++;
                                }else{
                                    T.showShort(InventoryActivity.this, R.string.inventory_tip_3);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        T.showShort(InventoryActivity.this, R.string.inventory_tip_2);
                    }
                } else {
                    T.showShort(InventoryActivity.this, R.string.inventory_tip_1);
                }
                //结束动画
                stopHttpAnim();
            }
        };
        responeHandler.setTag(tag);
        BirdApi.getInventory(this, params, responeHandler);
    }

    /*
     *停止动画
     */
    private void stopHttpAnim() {
        hideBar();
        rv_inventory.refreshComplete();
        rv_inventory.loadMoreComplete();
    }


    @Override
    public void onRefresh() {
        currentPage = 1;

        startRequest();
    }

    @Override
    public void onLoadMore() {
//        if(countPage>=currentPage){
        //非最后一页
        startRequest();
//        }
//        else{
//            T.showShort(this, R.string.inventory_tip_3);
//            stopHttpAnim();
//        }
    }

    @Override
    public void onLeftClick(View view) {
        finish();
    }

    @Override
    public void onRightClick(View view) {
        showMenuPopupWindow(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消当前activity的所有请求
        BirdApi.cancelRequestWithTag(tag);
    }

    /**
     * 获取所有的仓库
     */
    private void getAllCompanyWarehouse() {
        RequestParams wareParams = new RequestParams();
        BirdApi.getAllWarehouse(MyApplication.getInstans(), wareParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                warehouseEntity = GsonHelper.getPerson(response.toString(), WarehouseEntity.class);
                WarehouseEntity.WarehouseDetail detail = new WarehouseEntity().new WarehouseDetail();
                detail.setName("全部仓库");
//                nowSelectedWarehouse = detail;//默认选中全部
                warehouseEntity.getData().add(0, detail);
                //显示弹框
                wareAdapter.setDataSource(warehouseEntity.getData());
                mPopupWindow.showAsDropDown(tv_allInventory, 0, 0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                T.showLong(MyApplication.getInstans(), "error:" + responseString.toString());
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void initPopWindow() {
        final View popWindow = LayoutInflater.from(this).inflate(R.layout.common_recycleview_layout, null);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(this));
        wareAdapter = new OrderWareHouseAdapter(this, null);
        wareAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                //点击时获取点击的仓库码
                String wareCode = warehouseEntity.getData().get(position).getWarehouse_code();
                //防止为空
                wareCode = StringUtils.getString(wareCode);
                params.put("warehouse_code", wareCode);
                String name = warehouseEntity.getData().get(position).getName();
                if (name == null || "".equals(name.trim())) {
                    tv_allInventory.setText(warehouseEntity.getData().get(0).getName());
                } else {
                    tv_allInventory.setText(name);
                }
                //开始请求网络
                //显示加载动画
                showBar();
                currentPage = 1;
                startRequest();
            }
        });
        rcy.setAdapter(wareAdapter);
        mPopupWindow = new PopupWindow(popWindow,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
//        mPopupWindow.showAsDropDown(view, width / 2, 0);
    }
    /**
     * 展示弹出框
     */
    public void showMenuPopupWindow(View viewID) {
//        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popWindow = LayoutInflater.from(this).inflate(R.layout.common_recycleview_layout, null);
//        popWindow.setBackgroundColor(Color.TRANSPARENT);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> list=new ArrayList<>();
        for(String s:getResources().getStringArray(R.array.manage_mode_items)){
            list.add(s);
        }
        CommonSimpleAdapter adapter = new CommonSimpleAdapter(this, list);
        rcy.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (menuPop.isShowing()) {
                    menuPop.dismiss();
                }
            }
        });
        int width = getWindowManager().getDefaultDisplay().getWidth();
        menuPop = new PopupWindow(popWindow, width / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
        menuPop.setFocusable(true);
        menuPop.setOutsideTouchable(true);
        menuPop.setBackgroundDrawable(new BitmapDrawable());
        menuPop.update();
        menuPop.showAsDropDown(viewID, width / 2, 0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_inventory_all:
                if (warehouseEntity != null) {
                    if (warehouseEntity.getData() != null) {
                        if (mPopupWindow != null) {
                            if (!mPopupWindow.isShowing()) {
                                //显示弹框
                                mPopupWindow.showAsDropDown(tv_allInventory, 0, 0);
                            }
                        } else {
                            initPopWindow();
                            //显示弹框
                            mPopupWindow.showAsDropDown(tv_allInventory, 0, 0);
                        }
                        return;
                    }
                }
                //请求所有仓库
                getAllCompanyWarehouse();
                break;
            case R.id.tv_inventory_sort_available:

                break;
            case R.id.tv_inventory_sort_time:

                break;
            case R.id.fab_inventory_gotop:
                if (type == Type.Inner) {
                    rv_inventory.smoothScrollToPosition(1);
                } else if (type == Type.Willin) {
                    rv_inventory.smoothScrollToPosition(1);
                }else if(type==Type.OutofWarning) {
                    rv_inventory.smoothScrollToPosition(1);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化搜索
     */
    private void initSearch() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {//系统键盘搜索入口
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    params.put("keyword", v.getText().toString());
//                    currentPage = 1;
                    reStartHttp();
                }
                return false;
            }
        });
        et_search.setOnClearTextListener(new ClearEditText.OnClearTextListener() {
            @Override
            public void clearTextListenr() {
                params.put("keyword", "");
//                currentPage = 1;
                reStartHttp();
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //40表示发往仓库，1表示在库，10表示正常，20表示库存紧张，30表示断货
        switch (tab.getPosition()) {
            case 0:
                type = Type.Inner;
                tv_allInventory.setText(R.string.inventory_all);
                adapter = new InventoryAdapter(this, null);
                adapter.setOnGoTopListener(this);
//                adapter.clearDataSource();
//                adapter.notifyDataSetChanged();
                rv_inventory.setAdapter(adapter);
                clearParams();
                //商品类型，20表示物料，默认10表示商品
                params.put("product_type", 10);
                params.put("stock_status", 1);
                //显示加载动画
                reStartHttp();

                break;
            case 1:
                type = Type.Willin;
                tv_allInventory.setText(R.string.inventory_all);
                willInAdapter = new InventoryWillInAdapter(this, null);
                willInAdapter.setOnGoTopListener(this);
//                willInAdapter.clearDataSource();
                rv_inventory.setAdapter(willInAdapter);
                clearParams();
                //商品类型，20表示物料，默认10表示商品
                params.put("product_type", 10);
                params.put("stock_status", 40);
                //显示加载动画
                reStartHttp();
                break;
            case 2:
                type = Type.OutofWarning;
                tv_allInventory.setText(R.string.inventory_all);
                adapter = new InventoryAdapter(this, null);
                adapter.setOnGoTopListener(this);
//                adapter.clearDataSource();
//                adapter.notifyDataSetChanged();
                rv_inventory.setAdapter(adapter);
                clearParams();
                //商品类型，20表示物料，默认10表示商品
                params.put("product_type", 10);
                params.put("stock_status", 20);
                //显示加载动画
                reStartHttp();
                break;
            default:

                break;
        }
    }

    private void clearParams() {
        //清除所有的参数
        params.remove("keyword");
        params.remove("product_type");
        params.remove("stock_status");
        params.remove("page_no");
        params.remove("warehouse_code");
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }



    private void reStartHttp() {
        fab_gotop.setVisibility(View.GONE);
        //显示数量
        tv_count.setText(countTxt.replace("@","0"));
        //显示加载动画
        showBar();
        currentPage = 1;
        startRequest();
    }
}
