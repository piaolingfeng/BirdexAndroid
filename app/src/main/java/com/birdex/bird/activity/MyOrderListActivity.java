package com.birdex.bird.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.CommonSimpleAdapter;
import com.birdex.bird.adapter.OrderListAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.decoration.DividerItemDecoration;
import com.birdex.bird.decoration.FullyLinearLayoutManager;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.entity.OrderStatus;
import com.birdex.bird.entity.WarehouseEntity;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.SafeProgressDialog;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
import com.birdex.bird.widget.ClearEditText;
import com.birdex.bird.widget.RotateLoading;
import com.birdex.bird.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.apache.http.Header;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/3/30.
 */
public class MyOrderListActivity extends BaseActivity implements View.OnClickListener, XRecyclerView.LoadingListener {

    //    @Bind(R.id.viewPager)
//    ViewPager viewpager;
    @Bind(R.id.et_search)
    ClearEditText et_search;
//    @Bind(R.id.tablayout)
//    TabLayout tabLayout;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.back)
    PercentRelativeLayout back;
    @Bind(R.id.menu)
    ImageView menu;

    @Bind(R.id.state_all)
    TextView state_all;
    @Bind(R.id.state_time)
    TextView state_time;
    @Bind(R.id.state_Warehouse)
    TextView state_Warehouse;
    @Bind(R.id.img_scan_code)
    ImageView img_scan_code;
    @Bind(R.id.rcy_orderlist)
    XRecyclerView rcy_orderlist;
    public static final int[] name = {R.string.tool1, R.string.tool2, R.string.tool3, R.string.tool5, R.string.tool6};
    List<String> menuList = new ArrayList<>();
    public String currentName;
    public int position = 0;

    OrderStatus orderStatus;//状态列表
    OrderListEntity orderListEntities;//列表
    OrderListAdapter listAdapter;//列表adpter
    WarehouseEntity warehouseEntity;//所有仓库列表

    //当前选中的状态
    WarehouseEntity.WarehouseDetail nowSelectedWarehouse;//当前选中仓库
    OrderStatus.Status nowSelectedStatus;//当前选中状态

    EventBus bus;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_my_orderlist_layout;
    }

    @Override
    public void initializeContentViews() {
        bus = EventBus.getDefault();
        currentName = getIntent().getStringExtra("name");
        if (StringUtils.isEmpty(currentName)) {
            currentName = getString(name[0]);
        }
        title.setText(currentName);

        menu.setVisibility(View.VISIBLE);
        for (int i = 0; i < name.length; i++) {//初始化lmenu list
            menuList.add(getString(name[i]));
        }
        rcy_orderlist.setLoadingListener(this);
        rcy_orderlist.setPullRefreshEnabled(false);
        rcy_orderlist.setLoadingMoreEnabled(true);
        orderListEntities = new OrderListEntity();
        listAdapter = new OrderListAdapter(this, orderListEntities.getData().getOrders());
        rcy_orderlist.setLayoutManager(new FullyLinearLayoutManager(this));
        rcy_orderlist.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        rcy_orderlist.setAdapter(listAdapter);

        nowSelectedWarehouse = new WarehouseEntity().new WarehouseDetail();
        nowSelectedStatus = new OrderStatus().new Status();

        initSearch();
        getAllStatus();//获取所有状态
        getAllCompanyWarehouse();//获取所有仓库
        getOrderList();//获取订单
    }


    private void initTab() {
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_1));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_2));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_3));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_4));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_5));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_6));
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        IndexFragment.getTodayData();

    }

    /**
     * 初始化搜索
     */
    private void initSearch() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {//系统键盘搜索入口
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                }
                return false;
            }
        });

        et_search.setOnClearETChangeListener(new ClearEditText.OnClearETChangeListener() {
            @Override
            public void textChange(CharSequence text) {

            }
        });
    }

    /**
     * 获取订单列表
     */
    private void getOrderList() {
        RequestParams listParams = new RequestParams();
        listParams.add("page_no", "1");
        listParams.add("page_size", "20");
        listParams.add("keyword", "");
        listParams.add("warehouse_code", "");
        listParams.add("start_date", "");
        listParams.add("end_date", "");
        listParams.add("status", "");
        BirdApi.getOrderList(MyApplication.getInstans(), listParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                orderListEntities = GsonHelper.getPerson(response.toString(), OrderListEntity.class);
                listAdapter.setList(orderListEntities.getData().getOrders());
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                T.showLong(MyApplication.getInstans(), "error:" + responseString.toString());
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    /**
     * 获取订单所有状态
     */
    private void getAllStatus() {
        RequestParams stateParams = new RequestParams();
        stateParams.add("order_code", "");
        BirdApi.getOrderListState(MyApplication.getInstans(), stateParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    orderStatus = (OrderStatus) JsonHelper.parseLIst(response.getJSONArray("data"), OrderStatus.class);
                orderStatus = GsonHelper.getPerson(response.toString(), OrderStatus.class);
                OrderStatus.Status status = new OrderStatus().new Status();
                status.setStatus_name("全部状态");
                orderStatus.getData().add(0, status);
                bus.post(status, "changeState");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                T.showLong(MyApplication.getInstans(), "error:" + responseString.toString());
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

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
                bus.post(detail, "changeWarehouse");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                T.showLong(MyApplication.getInstans(), "error:" + responseString.toString());
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Subscriber(tag = "changeWarehouse")
    public void changeWarehouse(WarehouseEntity.WarehouseDetail detail) {
        nowSelectedWarehouse = detail;
        state_Warehouse.setText(detail.getName());
    }

    @Subscriber(tag = "changeState")
    public void changeState(OrderStatus.Status status) {
        nowSelectedStatus = status;
        state_all.setText(status.getStatus_name());
    }

    @Subscriber(tag = "changeTime")
    public void changeTime(String string) {
//        nowSelectedStatus = status;
        state_all.setText("");
    }


//    public List<OrderManagerEntity> indexOrderNetDatatList;//首页订单状态网络数据

//    /**
//     * 发送空字符串来来截取将要刷新的内容
//     */
//    @Subscriber(tag = "getLocalData")
//    public void getNetReflashData(String string) {
//        this.indexOrderNetDatatList = IndexFragment.indexOrderNetDatatList;//获取刷新过后的数据，地址取相同
//        for (int i = 0; i < indexOrderNetDatatList.size(); i++) {
////            if (indexOrderNetDatatList.get(i))
////            OrderTabItem tabItem = new OrderTabItem(this);
////            View view = tabItem.getTabView();
////            tabItem.setTabTitle(indexOrderNetDatatList.get(i).getName());
//////            if (indexOrderNetDatatList.get(i).getName().)
////            tabItem.setTabCount(indexOrderNetDatatList.get(i).getCount() + "");
////            TabLayout.Tab tab = tabLayout.newTab();
////            tab.setCustomView(R.layout.tab_order_layout);
////            tabLayout.addTab(tab);
//
//        }
//    }

    PopupWindow mPopupWindow;

    /**
     * 展示弹出框
     */
    public void showPopupWindow(View viewID, final List<String> list) {
//        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popWindow = LayoutInflater.from(this).inflate(R.layout.common_recycleview_layout, null);
//        popWindow.setBackgroundColor(Color.TRANSPARENT);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(this));
        CommonSimpleAdapter adapter = new CommonSimpleAdapter(this, list);
        rcy.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                T.showShort(MyApplication.getInstans(), list.get(position));
            }
        });
        int width = getWindowManager().getDefaultDisplay().getWidth();
        mPopupWindow = new PopupWindow(popWindow, width / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
        mPopupWindow.showAsDropDown(viewID, width / 2, 0);
    }

    @OnClick({R.id.back, R.id.menu, R.id.state_time, R.id.state_Warehouse, R.id.state_all})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                showPopupWindow((View) menu.getParent(), menuList);
                break;
            case R.id.state_all:
//                showPopupWindow((View) state_all.getParent(), menuList);
                break;
            case R.id.state_Warehouse:
                showPopupWindow((View) state_Warehouse.getParent(), menuList);
                break;
            case R.id.state_time:
//                showPopupWindow((View) state_time.getParent(), menuList);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    Dialog loadingDialog;

    public void showLoading() {
        loadingDialog = new SafeProgressDialog(this, R.style.semester_dialog);// 创建自定义样式dialog
//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
//        loadingDialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        loadingDialog.setContentView(view);// 设置布局
        final RotateLoading loading = (RotateLoading) view.findViewById(R.id.rotateloading);
        loading.start();
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loading.stop();
            }
        });
        loadingDialog.show();
    }

    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        T.showLong(MyApplication.getInstans(), "Load More");
    }
}
