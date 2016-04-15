package com.birdex.bird.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.activity.MipcaActivityCapture;
import com.birdex.bird.adapter.InventoryAdapter;
import com.birdex.bird.adapter.InventoryWillInAdapter;
import com.birdex.bird.adapter.OrderWareHouseAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.biz.InventoryBiz;
import com.birdex.bird.entity.InventoryActivityEntity;
import com.birdex.bird.entity.WarehouseEntity;
import com.birdex.bird.util.recycleviewhelper.OnLoadingImgListener;
import com.birdex.bird.util.recycleviewhelper.OnShowGoTopListener;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
import com.birdex.bird.widget.ClearEditText;
import com.birdex.bird.widget.xrecyclerview.XRecyclerView;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/4/11.
 */
public class InventoryFragment extends BaseFragment implements XRecyclerView.LoadingListener , View.OnClickListener, TabLayout.OnTabSelectedListener,OnShowGoTopListener,OnLoadingImgListener,CompoundButton.OnCheckedChangeListener{
    private RequestParams params = null;
    //当前页数，首页为1
    private int currentPage = 1;
    @Bind(R.id.rv_inventory)
    public XRecyclerView rv_inventory = null;
    @Bind(R.id.tl_inventory_change)
    public TabLayout tl_items;
    //搜索
    @Bind(R.id.et_search)
    public ClearEditText et_search;
    @Bind(R.id.tv_inventory_alltxt)
    public TextView tv_count;
    //跳顶按钮
    @Bind(R.id.fab_inventory_gotop)
    public FloatingActionButton fab_gotop;
    //扫码搜索
    @Bind(R.id.iv_inventory_scan_code)
    public ImageView iv_scan;
    //适配器
    private InventoryAdapter adapter = null;
    private InventoryWillInAdapter willInAdapter = null;
    //所有条目总数
    private int countNum = 0;
    //解析数据业务
    private InventoryBiz biz = null;
    //数据集合
    private ArrayList<InventoryActivityEntity> list = null;
    private final  int SCANNIN_GREQUEST_CODE = 13;
    @Override
    public void onShowFloatAtionButton(boolean isShow) {
        if(fab_gotop.getVisibility()== View.GONE&&isShow){
            fab_gotop.setVisibility(View.VISIBLE);
        }else if(fab_gotop.getVisibility()==View.VISIBLE&&!isShow){
            fab_gotop.setVisibility(View.GONE);
        }
    }
    /*
     *列表加载图片
     */
    @Override
    public void onLoadImage(String url, ImageView iv_image) {
                    if (RecyclerView.SCROLL_STATE_SETTLING != rv_inventory.getScrollState()) {
                if (!"".equals(url)) {
                    //非飞翔状态
                    Glide.with(getActivity()).load(url).placeholder(R.drawable.goods_default).into(iv_image);
                } else {
                    //飞翔的时候
                    iv_image.setImageResource(R.drawable.goods_default);
                }
            }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_inventory_sort_available:
                if(isChecked){
                    params.put("order_by","available_stock_asc");
                }else {
                    params.put("order_by","available_stock_desc");
                }
                fab_gotop.setVisibility(View.GONE);
//                //显示数量
//                tv_count.setText(countTxt.replace("@","0"));
                //显示加载动画
                showBar();
                currentPage = 1;
                startRequest();
                break;
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
    @Bind(R.id.cb_inventory_sort_available)
    public CheckBox cb_sortavailable = null;
    @Bind(R.id.cb_inventory_sort_time)
    public CheckBox cb_sorttime;
    //获取屏幕宽度
//    private int width = 0;
    private String countTxt="";
    @Override
    public int getContentLayoutResId() {
        return R.layout.inventory_layout;
    }
    @Override
    protected void key(int keyCode, KeyEvent event) {

    }



    @Override
    public void initializeContentViews() {
        EventBus.getDefault().register(this);
        countTxt=getString(R.string.inventory_all_data);
        //初始化解析业务
        biz = new InventoryBiz();
        tv_allInventory.setOnClickListener(this);
        cb_sortavailable.setOnCheckedChangeListener(this);
        //获取屏幕宽度
//        width = getWindowManager().getDefaultDisplay().getWidth();
        //网络请求参数
        params = new RequestParams();
        //商品类型，20表示物料，默认10表示商品
        params.put("product_type", 10);
        //40表示发往仓库，1表示在库，10表示正常，20表示库存紧张，30表示断货
        //默认进入为“待入库”
        params.put("stock_status", 1);
//        rv_inventory = (XRecyclerView)getActivity().findViewById(R.id.rv_inventory);
        rv_inventory.setLoadingMoreEnabled(true);
        rv_inventory.setPullRefreshEnabled(false);
        rv_inventory.setLoadingListener(this);
        rv_inventory.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.list = new ArrayList<>();
        //初始化适配器
        adapter = new InventoryAdapter(getActivity(), this.list);
        adapter.setOnLoadingImgListener(this);
        adapter.setOnGoTopListener(this);
        willInAdapter = new InventoryWillInAdapter(getActivity(), this.list);
        willInAdapter.setOnLoadingImgListener(this);
        willInAdapter.setOnGoTopListener(this);
        rv_inventory.setAdapter(adapter);
        //tablayout
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
        //按照可用数量排序
        //点击扫码搜索
        iv_scan.setOnClickListener(this);
    }

    @Override
    protected void lazyLoad() {

    }

    /*
    *初始化状态
    */
    private void initStatus(){
        String [] status=getResources().getStringArray(R.array.inventory_all_status);
        LayoutInflater inflater=LayoutInflater.from(getActivity());
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
//                            countPage = jobj.getInt("page_num");
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
//                                JSONObject jroot = jobj.getJSONObject("products");
//                                list = biz.parseJson2List(jroot);
                                list = biz.gson2list(jobj.getString("products"));
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
                                    T.showShort(getActivity(), R.string.inventory_tip_3);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        T.showShort(getActivity(), R.string.inventory_tip_2);
                    }
                } else {
                    T.showShort(getActivity(), R.string.inventory_tip_1);
                }
                //结束动画
                stopHttpAnim();
            }
        };
        responeHandler.setTag(tag);
        BirdApi.getInventory(getActivity(), params, responeHandler);
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
    public void onDestroyView() {
        super.onDestroyView();
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
        final View popWindow = LayoutInflater.from(getActivity()).inflate(R.layout.common_recycleview_layout, null);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(getActivity()));
        wareAdapter = new OrderWareHouseAdapter(getActivity(), null);
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
                tv_count.setText(countTxt.replace("@","0"));
                showBar();
                currentPage = 1;
                startRequest();
            }
        });
        rcy.setAdapter(wareAdapter);
        mPopupWindow = new PopupWindow(popWindow, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
//        mPopupWindow.showAsDropDown(view, width / 2, 0);
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
            case R.id.iv_inventory_scan_code:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
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
                    params.put("keyword", v.getText().toString().trim());
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
                adapter = new InventoryAdapter(getActivity(), null);
                adapter.setOnLoadingImgListener(this);
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
                willInAdapter = new InventoryWillInAdapter(getActivity(), null);
                willInAdapter.setOnGoTopListener(this);
                willInAdapter.setOnLoadingImgListener(this);
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
                adapter = new InventoryAdapter(getActivity(), null);
                adapter.setOnLoadingImgListener(this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result");
                    if(result!=null){
                        if(!TextUtils.isEmpty(result.trim())){
                            et_search.setText(result);
                            params.put("keyword", result.trim());
                            reStartHttp();
                        }
                    }
                }
                break;
        }
    }
}
