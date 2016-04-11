package com.birdex.bird.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.CommonSimpleAdapter;
import com.birdex.bird.adapter.InventoryAdapter;
import com.birdex.bird.adapter.InventoryWillInAdapter;
import com.birdex.bird.adapter.OrderListAdapter;
import com.birdex.bird.adapter.OrderStatusAdapter;
import com.birdex.bird.adapter.OrderTimeAdapter;
import com.birdex.bird.adapter.OrderWareHouseAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.entity.OrderRequestEntity;
import com.birdex.bird.entity.OrderStatus;
import com.birdex.bird.entity.TimeSelectEntity;
import com.birdex.bird.entity.WarehouseEntity;
import com.birdex.bird.fragment.BaseFragment;
import com.birdex.bird.fragment.OrderListManagerFragment;
import com.birdex.bird.fragment.PredictionManagerFragment;
import com.birdex.bird.interfaces.BackHandledInterface;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.HideSoftKeyboardUtil;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
import com.birdex.bird.util.TimeUtil;
import com.birdex.bird.widget.ClearEditText;
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
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/3/30.
 */
public class MyOrderListActivity extends BaseActivity implements View.OnClickListener, BaseFragment.OnFragmentInteractionListener, BackHandledInterface ,TabLayout.OnTabSelectedListener{

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
    @Bind(R.id.fab_inventory_gotop)
    FloatingActionButton fab_inventory_gotop;
    @Bind(R.id.frame_layout)
    FrameLayout frame_layout;
    @Bind(R.id.tv_no_text)
    TextView tv_no_text;
    @Bind(R.id.tv_list_count)
    TextView tv_list_count;
    //库存布局
//    @Bind(R.id.tv_inventory_all)
//    public TextView tv_allInventory = null;
//    @Bind(R.id.tv_inventory_sort_available)
//    public TextView tv_sortavailable = null;
//    @Bind(R.id.tv_inventory_sort_time)
//    public TextView tv_sorttime;
//    @Bind(R.id.rl_inventory)
//    public RelativeLayout rl_inventory;
//    @Bind(R.id.tl_inventory_change)
//    public TabLayout tl_items;

    public static final int[] name = {R.string.tool1, R.string.tool2, R.string.tool3};
    public static final int[] time = {R.string.time_all, R.string.time_today, R.string.time_week, R.string.time_month, R.string.time_three_month, R.string.time_year};
    List<String> menuList;//menu菜单list
    public String currentName;
    public int position = 0;

    OrderStatus orderStatus;//订单状态列表
    OrderStatus predicitionStatus;//预报状态列表
    WarehouseEntity warehouseEntity;//所有仓库列表
    List<TimeSelectEntity> timeList;

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
        bus.register(this);
        menu.setVisibility(View.VISIBLE);
        menuList = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {//初始化lmenu list
            menuList.add(getString(name[i]));
        }
        requestStateCount = 0;//
        initTimeStatus();
        initscan_code();
        initSearch();
//        initTabStatus();
        getAllOrderStatus();//获取订单所有状态
        getAllPredicitionStatus();//获取预报所有状态
        getAllCompanyWarehouse();//获取所有仓库
        initFloatAction();
//        setData();//库存，订单管理
//        nowSelectedWarehouse = new WarehouseEntity().new WarehouseDetail();
//        nowSelectedStatus = new OrderStatus().new Status();
    }

    /**
     * 滚动到顶部
     */
    public void initFloatAction() {

        fab_inventory_gotop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.post(currentName, "AdapterTop");
            }
        });
    }

    /*
    *初始化状态
    */
//    private void initTabStatus(){
//        String [] status=getResources().getStringArray(R.array.inventory_all_status);
//        LayoutInflater inflater=LayoutInflater.from(this);
//        String str="";
//        for(int i=0;i<status.length;i++){
//            str=status[i];
//            View view=inflater.inflate(R.layout.status_layout,null,false);
//            tl_items.addTab(tl_items.newTab().setCustomView(view));
//            TextView tv_name=(TextView)view.findViewById(R.id.tv_inventory_status_name);
//            TextView tv_num=(TextView)view.findViewById(R.id.tv_inventory_status_num);
//            tv_name.setText(str);
//            if(i==0||i==1){
//                tv_num.setVisibility(View.GONE);
//            }else{
//                tv_num.setText("0");
//                tv_num.setVisibility(View.GONE);
//            }
//        }
//        tl_items.setOnTabSelectedListener(this);
//    }

    /**
     * 重新初始化fragment
     */
    public void setData() {
//        rl_inventory.setVisibility(View.GONE);//非库存时，隐藏
//        tl_items.setVisibility(View.GONE);
        entity = new OrderRequestEntity();//请求实体初始化,切换时也要新建一个实体
        currentName = getIntent().getStringExtra("name");
        if (StringUtils.isEmpty(currentName)) {
            String indexOrder = getIntent().getStringExtra("indexOrder");
            if (!StringUtils.isEmpty(indexOrder)) {
                getIntent().removeExtra("indexOrder");
            if (indexOrder.contains("今日")) {//初始化时间
                entity.setStart_date(timeList.get(1).getStart_date());
                entity.setEnd_date(timeList.get(1).getEnd_date());
                bus.post(timeList.get(1).getName(), "changeTime");//改变显示的时间
            } else {
                entity.setStart_date(timeList.get(0).getStart_date());
                entity.setEnd_date(timeList.get(0).getEnd_date());
                bus.post(timeList.get(0).getName(), "changeTime");//改变显示的时间
            }


                if (indexOrder.contains("预报")) {
                    currentName = getString(name[1]);
                    for (int position = 0; position < predicitionStatus.getData().size(); position++) {
                        if (indexOrder.contains(predicitionStatus.getData().get(position).getStatus_name().trim())) {//包含该模块的名字
                            entity.setStatus(predicitionStatus.getData().get(position).getStatus() + "");
                            bus.post(predicitionStatus.getData().get(position), "changeState");
                            break;
                        }
                    }
                } else {
                    if (indexOrder.contains("库存")) {
                        currentName = getString(name[2]);
//                        rl_inventory.setVisibility(View.VISIBLE);
//                        tl_items.setVisibility(View.VISIBLE);
                    } else {//订单
                        currentName = getString(name[0]);
                        //初始化状态选择
                        for (int position = 0; position < orderStatus.getData().size(); position++) {
                            if (indexOrder.contains(orderStatus.getData().get(position).getStatus_name().trim())) {//包含该模块的名字
                                entity.setStatus(orderStatus.getData().get(position).getStatus() + "");
                                bus.post(orderStatus.getData().get(position), "changeState");
                                break;
                            }
                        }
                    }
                }
            }
        }
        addFragment(currentName);
        title.setText(currentName);
        if (currentName.equals(getString(name[0]))) {
            bus.post(entity, "requestOrderList");
        } else {
            if (currentName.equals(getString(name[1]))) {
                bus.post(entity, "requestPredictList");
            }
        }
    }

    private void initscan_code() {
        img_scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(MyApplication.getInstans(), getString(R.string.please_wail));
            }
        });
    }


    private void initTimeStatus() {
        timeList = new ArrayList<>();
        for (int i = 0; i < time.length; i++) {
            TimeSelectEntity entity = new TimeSelectEntity();
            entity.setName(getString(time[i]));
            entity.setEnd_date(TimeUtil.getCurrentData());
            switch (i) {
                case 0:
                    entity.setStart_date("");// 表示全部
                    entity.setEnd_date("");
                    break;
                case 1:
                    entity.setStart_date(TimeUtil.getCurrentData());
                    break;
                case 2://今天
                    entity.setStart_date(TimeUtil.getWeekdelayData());
                    break;
                case 3://近一个月
                    entity.setStart_date(TimeUtil.getMonthDelayData());
                    break;
                case 4://近三个月
                    entity.setStart_date(TimeUtil.getThreeMonthDelayData());
                    break;
                case 5://近一年
                    entity.setStart_date(TimeUtil.getYearDelayData());
                    break;
            }
            timeList.add(entity);
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
                    String string = v.getText().toString();
                    entity.setKeyword(string);
                    if (currentName.equals(getString(name[0]))) {
                        bus.post(entity, "requestOrderList");
                    } else {
                        if (currentName.equals(getString(name[1]))) {
                            bus.post(entity, "requestPredictList");
                        }else{
                            bus.post(entity, "inventorytList");
                        }
                    }
                }
                return false;
            }
        });

        et_search.setOnClearTextListener(new ClearEditText.OnClearTextListener() {
            @Override
            public void clearTextListenr() {
                entity.setKeyword("");
//                currentPage = 1;
//                reStartHttp();
            }
        });
    }


    /**
     * 获取订单所有状态
     */
    private void getAllOrderStatus() {
        showBar();
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
                requestStateCount++;
                getNetStatusCount();
//                bus.post(status, "changeState");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                T.showLong(MyApplication.getInstans(), "error:" + responseString.toString());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish() {
                hideBar();
                super.onFinish();
            }
        });
    }

    public int requestStateCount = 0;

    /**
     * 获取全部的网络状态后
     */
    public synchronized void getNetStatusCount() {
        if (requestStateCount == 2) {
            requestStateCount = 0;
            setData();
        }
    }

    /**
     * 获取预报所有状态
     */
    private void getAllPredicitionStatus() {
        showBar();
        RequestParams stateParams = new RequestParams();
        BirdApi.getPredicitionStatus(MyApplication.getInstans(), stateParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    orderStatus = (OrderStatus) JsonHelper.parseLIst(response.getJSONArray("data"), OrderStatus.class);
                predicitionStatus = GsonHelper.getPerson(response.toString(), OrderStatus.class);
                OrderStatus.Status status = new OrderStatus().new Status();
                status.setStatus_name("全部状态");
                predicitionStatus.getData().add(0, status);
                requestStateCount++;
                getNetStatusCount();
//                bus.post(status, "changeState");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                T.showLong(MyApplication.getInstans(), "error:" + responseString.toString());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish() {
                hideBar();
                super.onFinish();
            }
        });
    }

    /**
     * 获取所有的仓库
     */
    private void getAllCompanyWarehouse() {
        showBar();
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

            @Override
            public void onFinish() {
                hideBar();
                super.onFinish();
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
    public void changeTime(String text) {
//        nowSelectedStatus = status;
        state_time.setText(text);
    }

    @Subscriber(tag = "frame_layout")
    private void setFrameLayoutVisble(int value) {
        tv_list_count.setText("共" + value + "条数据");
        if (value > 0) {
            frame_layout.setVisibility(View.VISIBLE);
            tv_no_text.setVisibility(View.GONE);
        } else {
            frame_layout.setVisibility(View.GONE);
            tv_no_text.setVisibility(View.VISIBLE);
        }
    }


    PopupWindow mPopupWindow;

    /**
     * 展示弹出框
     * menu,时间
     * w,用来除以父控件的宽度
     * viewID 显示在其下方
     */
    public void showMenuWindow(View viewID, final List<String> list, final int w) {
        CommonSimpleAdapter adapter = new CommonSimpleAdapter(this, list);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
//              切换内容
                getIntent().putExtra("name", list.get(position));
                setData();
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
    public void showTimeWindow(View viewID, final int w) {
//
        OrderTimeAdapter adapter = new OrderTimeAdapter(this, timeList);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
//                T.showShort(MyApplication.getInstans(), list.get(position));
//                entity.setStatus(list.get(position) + "");
                setTime(position);
            }
        });
        showPopupWindow(viewID, w, adapter);
    }

    /**
     * 展示弹出框
     * Warehouse
     */
    public void showStateWindow(View viewID, final List<OrderStatus.Status> list, int w) {
        OrderStatusAdapter adapter = new OrderStatusAdapter(this, list);
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
    }


    /**
     * 展示弹出框
     * Warehouse
     */
    public void showWarehouseWindow(View viewID, int w) {
        OrderWareHouseAdapter adapter = new OrderWareHouseAdapter(this, warehouseEntity.getData());
        adapter.setOnRecyclerViewItemClickListener(
                new OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
                        setWarehouse(position);
                    }
                }
        );

        showPopupWindow(viewID, w, adapter);
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


    /**
     * 设置仓库条件
     */
    private void setWarehouse(int position) {
        entity.setWarehouse_code(warehouseEntity.getData().get(position).getWarehouse_code());
        bus.post(warehouseEntity.getData().get(position), "changeWarehouse");
        if (currentName.equals(getString(name[0])))
            bus.post(entity, "requestOrderList");
        else if (currentName.equals(getString(name[1]))) {
            bus.post(entity, "requestPredictList");
        }
    }

    private void setState(int position) {

    }

    private void setTime(int position) {
        entity.setStart_date(timeList.get(position).getStart_date());
        entity.setEnd_date(timeList.get(position).getEnd_date());
        bus.post(timeList.get(position).getName(), "changeTime");//改变显示的时间
        if (currentName.equals(getString(name[0])))
            bus.post(entity, "requestOrderList");
        else if (currentName.equals(getString(name[1]))) {
            bus.post(entity, "requestPredictList");
        }
    }

    @OnClick({R.id.back, R.id.menu, R.id.state_time, R.id.state_Warehouse, R.id.state_all,R.id.tv_inventory_all,R.id.tv_inventory_sort_available,R.id.tv_inventory_sort_time})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                showMenuWindow((View) menu.getParent(), menuList, 3);
                break;
            case R.id.state_all:
                if (currentName.equals(getString(name[0])))//订单管理
                    showStateWindow((View) state_all.getParent(), orderStatus.getData(), 1);
                else
                    showStateWindow((View) state_all.getParent(), predicitionStatus.getData(), 1);
                break;
            case R.id.tv_inventory_all:
            case R.id.state_Warehouse://所有仓库
                showWarehouseWindow((View) state_Warehouse.getParent(), 1);
                break;
            case R.id.state_time:
                showTimeWindow((View) state_time.getParent(), 1);
                break;
            case R.id.back:
                finish();
                break;
//            case R.id.tv_inventory_all:
//                if (warehouseEntity != null) {
//                    if (warehouseEntity.getData() != null) {
//                        if (mPopupWindow != null) {
//                            if (!mPopupWindow.isShowing()) {
//                                //显示弹框
//                                mPopupWindow.showAsDropDown(tv_allInventory, 0, 0);
//                            }
//                        } else {
//                            initPopWindow();
//                            //显示弹框
//                            mPopupWindow.showAsDropDown(tv_allInventory, 0, 0);
//                        }
//                        return;
//                    }
//                }
//                //请求所有仓库
//                getAllCompanyWarehouse();
//                break;
            case R.id.tv_inventory_sort_available:

                break;
            case R.id.tv_inventory_sort_time:

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
        transaction = getSupportFragmentManager().beginTransaction();
        hideFragment();
        if (string.equals(getString(name[1]))) {
            baseFragment = predictionManagerFragment;
        } else {
            baseFragment = orderListManagerFragment;
        }
        if (!baseFragment.isAdded())
            transaction.add(R.id.frame_layout, baseFragment);
        transaction.show(baseFragment);
        Bundle b = new Bundle();
        b.putSerializable("entity", entity);
        baseFragment.setUIArguments(b);
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

    @Override
    protected void onDestroy() {
        BirdApi.cancelAllRequest();
        super.onDestroy();
    }

//    @Override
//    public void onTabSelected(TabLayout.Tab tab) {
//        //40表示发往仓库，1表示在库，10表示正常，20表示库存紧张，30表示断货
//        switch (tab.getPosition()) {
//            case 0:
//                type = InventoryActivity.Type.Inner;
//                tv_allInventory.setText(R.string.inventory_all);
//                adapter = new InventoryAdapter(this, null);
//                .setAdapter(adapter);
//                entity.setTab_type(0);
//                entity.setProduct_type(10);
//                entity.setStock_status(1);
//                bus.post(entity, "inventorytList");
//                break;
//            case 1:
//                type = Type.Willin;
//                tv_allInventory.setText(R.string.inventory_all);
//                willInAdapter = new InventoryWillInAdapter(this, null);
//                willInAdapter.setOnGoTopListener(this);
////                willInAdapter.clearDataSource();
//                rv_inventory.setAdapter(willInAdapter);
//                clearParams();
//                //商品类型，20表示物料，默认10表示商品
//                params.put("product_type", 10);
//                params.put("stock_status", 40);
//                //显示加载动画
//                reStartHttp();
//                break;
//            case 2:
//                type = Type.OutofWarning;
//                tv_allInventory.setText(R.string.inventory_all);
//                adapter = new InventoryAdapter(this, null);
//                adapter.setOnGoTopListener(this);
////                adapter.clearDataSource();
////                adapter.notifyDataSetChanged();
//                rv_inventory.setAdapter(adapter);
//                clearParams();
//                //商品类型，20表示物料，默认10表示商品
//                params.put("product_type", 10);
//                params.put("stock_status", 20);
//                //显示加载动画
//                reStartHttp();
//                break;
//            default:
//
//                break;
//        }
//    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
