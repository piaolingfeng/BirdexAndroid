package com.birdex.bird.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.birdex.bird.adapter.OrderStatusAdapter;
import com.birdex.bird.adapter.OrderWareHouseAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.decoration.DividerItemDecoration;
import com.birdex.bird.decoration.FullyLinearLayoutManager;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.entity.OrderRequestEntity;
import com.birdex.bird.entity.OrderStatus;
import com.birdex.bird.entity.WarehouseEntity;
import com.birdex.bird.fragment.BaseFragment;
import com.birdex.bird.fragment.OrderListManagerFragment;
import com.birdex.bird.fragment.PredictionManagerFragment;
import com.birdex.bird.interfaces.BackHandledInterface;
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
public class MyOrderListActivity extends BaseActivity implements View.OnClickListener, BaseFragment.OnFragmentInteractionListener, BackHandledInterface {

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
    public static final int[] name = {R.string.tool1, R.string.tool2, R.string.tool3};
    public static final int[] time = {R.string.time_all, R.string.time_today, R.string.time_week, R.string.time_month, R.string.time_year};
    List<String> menuList;//menu菜单list
    List<String> timeList = new ArrayList<>();
    public String currentName;
    public int position = 0;

    OrderStatus orderStatus;//状态列表
    OrderListEntity orderListEntities;//列表
    OrderListAdapter listAdapter;//列表adpter
    WarehouseEntity warehouseEntity;//所有仓库列表
    //当前选中的状态保存实体
//    WarehouseEntity.WarehouseDetail nowSelectedWarehouse;//当前选中仓库
//    OrderStatus.Status nowSelectedStatus;//当前选中状态

    OrderRequestEntity entity;//请求数据保存的实体

    EventBus bus;

    private FragmentTransaction transaction;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_my_orderlist_layout;
    }

    @Override
    public void initializeContentViews() {
        if (predictionManagerFragment == null)
            predictionManagerFragment = new PredictionManagerFragment();
        if (orderListManagerFragment == null) {
            orderListManagerFragment = new OrderListManagerFragment();
        }

        bus = EventBus.getDefault();
        menu.setVisibility(View.VISIBLE);
        menuList = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {//初始化lmenu list
            menuList.add(getString(name[i]));
        }
        timeList = new ArrayList<>();
        for (int i = 0; i < time.length; i++) {
            timeList.add(getString(time[i]));
        }

        initSearch();
        getAllStatus();//获取所有状态
        getAllCompanyWarehouse();//获取所有仓库

        setData();//库存，订单管理
//        nowSelectedWarehouse = new WarehouseEntity().new WarehouseDetail();
//        nowSelectedStatus = new OrderStatus().new Status();
    }

    public void setData() {
        currentName = getIntent().getStringExtra("name");
        if (StringUtils.isEmpty(currentName)) {
            currentName = getString(name[0]);
        }
        addFragment(currentName);
        title.setText(currentName);
        entity = new OrderRequestEntity();//请求实体初始化,切换时也要新建一个实体
        if (currentName.equals(getString(name[0]))) {
            bus.post(entity, "requestOrderList");
        } else {
            if (currentName.equals(getString(name[1]))) {

            }
        }
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
                    if (currentName.equals(getString(name[0]))) {
                        String string = v.getText().toString();
                        entity.setKeyword(string);
                        bus.post("", "requestOrderList");
                    } else {

                    }
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
//        nowSelectedWarehouse = detail;
        state_Warehouse.setText(detail.getName());
    }

    @Subscriber(tag = "changeState")
    public void changeState(OrderStatus.Status status) {
//        nowSelectedStatus = status;
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
     * menu,时间
     * w,用来除以父控件的宽度
     * viewID 显示在其下方
     */
    public void showMenuWindow(View viewID, final List<String> list, final int w) {
//
        CommonSimpleAdapter adapter = new CommonSimpleAdapter(this, list);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
//                T.showShort(MyApplication.getInstans(), list.get(position));
                entity.setStatus(list.get(position) + "");
                bus.post(list.get(position), "changeTime");//改变显示的时间
                if (currentName.equals(getString(name[0])))
                    bus.post(entity, "requestOrderList");
                else if (currentName.equals(getString(name[1]))) {
                    bus.post(entity, "requestPredictList");
                }
            }
        });
        showPopupWindow(viewID, w, adapter);
    }

    /**
     * 展示弹出框
     * menu,时间
     * w,用来除以父控件的宽度
     * viewID 显示在其下方
     */
    public void showTimeWindow(View viewID, final List<String> list, final int w) {
//
        CommonSimpleAdapter adapter = new CommonSimpleAdapter(this, list);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
//                T.showShort(MyApplication.getInstans(), list.get(position));
                if (w <= 1) {
                    entity.setStatus(list.get(position) + "");
                    bus.post(list.get(position), "changeTime");//改变显示的时间
                    if (currentName.equals(getString(name[0])))
                        bus.post(entity, "requestOrderList");
                    else if (currentName.equals(getString(name[1]))) {
                        bus.post(entity, "requestPredictList");
                    }
                } else {//切换内容
                    getIntent().putExtra("name", list.get(position));
                    setData();
                }
            }
        });
        showPopupWindow(viewID, w, adapter);
    }

    /**
     * 展示弹出框
     * Warehouse
     */
    public void showStateWindow(View viewID, final List<OrderStatus.Status> list, int w) {
        OrderStatusAdapter adapter = new OrderStatusAdapter(this, orderStatus.getData());
        adapter.setOnRecyclerViewItemClickListener(
                new OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
//                        T.showShort(MyApplication.getInstans(), list.get(position).getStatus_name());
                        entity.setStatus(list.get(position).getStatus() + "");
                        bus.post(list.get(position), "changeState");
                        if (currentName.equals(getString(name[0])))
                            bus.post(entity, "requestOrderList");
                        else if (currentName.equals(getString(name[1]))) {
                            bus.post(entity, "requestPredictList");
                        }
                    }
                }
        );
        showPopupWindow(viewID, w, adapter);
//        CommonSimpleAdapter adapter = new CommonSimpleAdapter(this, list);
//        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                if (mPopupWindow.isShowing()) {
//                    mPopupWindow.dismiss();
//                }
//                T.showShort(MyApplication.getInstans(), list.get(position));
//            }
//        });

//        showPopupWindow(viewID, w, adapter);
    }


    /**
     * 展示弹出框
     * Warehouse
     */
    public void showWarehouseWindow(View viewID, final List<WarehouseEntity.WarehouseDetail> list, int w) {
        OrderWareHouseAdapter adapter = new OrderWareHouseAdapter(this, warehouseEntity.getData());
        adapter.setOnRecyclerViewItemClickListener(
                new OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
                        entity.setWarehouse_cod(list.get(position).getWarehouse_code());
                        bus.post(list.get(position), "changeWarehouse");
                        if (currentName.equals(getString(name[0])))
                            bus.post(entity, "requestOrderList");
                        else if (currentName.equals(getString(name[1]))) {
                            bus.post(entity, "requestPredictList");
                        }
                    }
                }
        );

        showPopupWindow(viewID, w, adapter);
//        CommonSimpleAdapter adapter = new CommonSimpleAdapter(this, list);
//        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                if (mPopupWindow.isShowing()) {
//                    mPopupWindow.dismiss();
//                }
//                T.showShort(MyApplication.getInstans(), list.get(position));
//            }
//        });
//
//        showPopupWindow(viewID, w, adapter);
    }

    /**
     * 弹出框
     */

    private void showPopupWindow(View viewID, int w, RecyclerView.Adapter adapter) {
        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popWindow = LayoutInflater.from(this).inflate(R.layout.common_recycleview_layout, null);
//        popWindow.setBackgroundColor(Color.TRANSPARENT);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(this));
        rcy.setAdapter(adapter);
        //        int width = getWindowManager().getDefaultDisplay().getWidth();
        int width = viewID.getWidth();
        mPopupWindow = new PopupWindow(popWindow, width / w, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
        if (w > 1)
            mPopupWindow.showAsDropDown(viewID, (width / w) * (w - 1), 0);
        else
            mPopupWindow.showAsDropDown(viewID, 0, 0);
    }


    @OnClick({R.id.back, R.id.menu, R.id.state_time, R.id.state_Warehouse, R.id.state_all})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                showTimeWindow((View) menu.getParent(), menuList, 3);
                break;
            case R.id.state_all:
                showStateWindow((View) state_all.getParent(), orderStatus.getData(), 1);
                break;
            case R.id.state_Warehouse:
                showWarehouseWindow((View) state_Warehouse.getParent(), warehouseEntity.getData(), 1);
                break;
            case R.id.state_time:
                showTimeWindow((View) state_time.getParent(), timeList, 1);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * 隐藏添加fragment
     */
    private void addFragment(String string) {
        BaseFragment baseFragment;
        if (string.equals(getString(name[1]))) {
            baseFragment = predictionManagerFragment;
        } else {
            baseFragment = orderListManagerFragment;
        }
        transaction = getSupportFragmentManager().beginTransaction();
        hideFragment();
        if (!baseFragment.isAdded())
            transaction.add(R.id.frame_layout, baseFragment);
        transaction.show(baseFragment);
        transaction.commit();
    }

    PredictionManagerFragment predictionManagerFragment;
    OrderListManagerFragment orderListManagerFragment;

    //隐藏所有fragment
    private void hideFragment() {
        if (orderListManagerFragment != null)
            transaction.hide(orderListManagerFragment);
        if (predictionManagerFragment != null)
            transaction.hide(predictionManagerFragment);
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }
}
