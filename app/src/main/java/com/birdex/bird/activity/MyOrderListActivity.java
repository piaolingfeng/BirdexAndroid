package com.birdex.bird.activity;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.CommonSimpleAdapter;
import com.birdex.bird.adapter.ToolPagerAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.OrderRequestEntity;
import com.birdex.bird.entity.OrderStatus;
import com.birdex.bird.entity.TimeSelectEntity;
import com.birdex.bird.entity.WarehouseEntity;
import com.birdex.bird.fragment.BaseFragment;
import com.birdex.bird.fragment.BillDetailFragment;
import com.birdex.bird.fragment.InventoryFragment;
import com.birdex.bird.fragment.OrderListManagerFragment;
import com.birdex.bird.fragment.PredictionManagerFragment;
import com.birdex.bird.fragment.RechargeFragment;
import com.birdex.bird.interfaces.BackHandledInterface;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.Constant;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
import com.birdex.bird.util.TimeUtil;
import com.birdex.bird.widget.TitleView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/3/30.
 */
public class MyOrderListActivity extends BaseActivity implements View.OnClickListener, BaseFragment.OnFragmentInteractionListener, BackHandledInterface {

    String tag = "MyOrderListActivity";

    @Bind(R.id.titleview)
    TitleView titleview;
    //    @Bind(R.id.tool_viewpager)
    ViewPager tool_viewpager;
    List<String> menuList;//menu菜单list
    public String currentName;

    //    public static OrderStatus orderStatus;//订单状态列表
//    public static OrderStatus predicitionStatus;//预报状态列表
//    public static WarehouseEntity warehouseEntity;//所有仓库列表
    public static List<TimeSelectEntity> timeList;

    OrderRequestEntity entity;//请求数据保存的实体
    EventBus bus;
    private FragmentTransaction transaction;
    List<BaseFragment> fragmentList;
    ToolPagerAdapter pageAdapter;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_my_orderlist_layout;
    }

    @Override
    public void initializeContentViews() {
//        warehouseEntity = new WarehouseEntity();
        if (orderListManagerFragment == null)
            orderListManagerFragment = new OrderListManagerFragment();
        if (predictionManagerFragment == null)
            predictionManagerFragment = new PredictionManagerFragment();
        if (inventoryFragment == null)
            inventoryFragment = new InventoryFragment();
        if (billfragment == null)
            billfragment = new BillDetailFragment();
        if (rechargefragment == null)
            rechargefragment = new RechargeFragment();
        bus = EventBus.getDefault();
        bus.register(this);
        titleview.setMenuVisble(true);

        menuList = new ArrayList<>();
        for (int i = 0; i < Constant.name.length; i++) {//初始化lmenu list
            menuList.add(getString(Constant.name[i]));
        }
        requestStateCount = 0;//
        initTimeStatus();
        setData();
    }


    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(orderListManagerFragment);
        fragmentList.add(predictionManagerFragment);
        fragmentList.add(inventoryFragment);
        fragmentList.add(billfragment);
        fragmentList.add(rechargefragment);
        pageAdapter = new ToolPagerAdapter(getSupportFragmentManager(), this, fragmentList);
        tool_viewpager.setAdapter(pageAdapter);
        tool_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleview.setTitle(getString(Constant.name[position]));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 重新初始化fragment
     */
    public void setData() {
        entity = new OrderRequestEntity();//请求实体初始化,切换时也要新建一个实体
        currentName = getIntent().getStringExtra("name");//首页的工具进来时
        if (StringUtils.isEmpty(currentName)) {
            String indexOrder = getIntent().getStringExtra("indexOrder");
//            if (indexOrder != null)
//                getIntent().removeExtra("indexOrder");
            if (!StringUtils.isEmpty(indexOrder)) {
//                getIntent().removeExtra("indexOrder");
                if (indexOrder.contains("预报")) {
                    currentName = getString(Constant.name[1]);
                } else {
                    if (indexOrder.contains("库存") && !indexOrder.contains("订单")) {
                        currentName = getString(Constant.name[2]);
                    } else {//订单
                        currentName = getString(Constant.name[0]);
                    }
                }
            }
        }
//        int position = 0;
//        for (int i = 0; i < Constant.name.length; i++) {//判断是哪个位置
//            if (currentName.equals(getString(Constant.name[i]))) {
//                position = i;
//                break;
//            }
//        }
//        tool_viewpager.setCurrentItem(position);
        addFragment(currentName);
        titleview.setTitle(currentName);
//        if (currentName.equals(getString(Constant.name[0]))) {
//            bus.post(entity, "requestOrderList");
//        } else {
//            if (currentName.equals(getString(Constant.name[1]))) {
//                bus.post(entity, "requestPredictList");
//            }
//        }
    }


    private void initTimeStatus() {
        timeList = new ArrayList<>();
        for (int i = 0; i < Constant.time.length; i++) {
            TimeSelectEntity entity = new TimeSelectEntity();
            entity.setName(getString(Constant.time[i]));
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


    public int requestStateCount = 0;

    /**
     * 获取全部的网络状态后
     */
    public synchronized void getNetStatusCount() {
        if (requestStateCount == 2) {
            requestStateCount = 0;
//            initViewPager();//获取完全部的全台才去初始化
            setData();
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
//                if ()
                getIntent().putExtra("name", list.get(position));
                setData();
            }
        });
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

    @OnClick(R.id.menu)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                showMenuWindow((View) titleview.getMenuView().getParent(), menuList, 3);
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
        if (string.equals(getString(Constant.name[0]))) {
            baseFragment = orderListManagerFragment;
        } else {
            if (string.equals(getString(Constant.name[1]))) {
                baseFragment = predictionManagerFragment;
            } else {
                if (string.equals(getString(Constant.name[2]))) {
                    baseFragment = inventoryFragment;
                } else {
                    if (string.equals(getString(Constant.name[3]))) {
                        baseFragment = billfragment;
                    } else {
                        baseFragment = rechargefragment;
                    }
                }
            }
        }
        Log.e("android", transaction.isEmpty() + "");
        if (!baseFragment.isAdded())
            transaction.add(R.id.tool_framelayout, baseFragment);
        transaction.show(baseFragment);
        Bundle b = new Bundle();
        b.putSerializable("entity", entity);
        baseFragment.setUIArguments(b);
        transaction.commit();
    }

    private PredictionManagerFragment predictionManagerFragment;
    private OrderListManagerFragment orderListManagerFragment;
    private RechargeFragment rechargefragment = null;
    private BillDetailFragment billfragment = null;
    private InventoryFragment inventoryFragment;

    //隐藏所有fragment
    private void hideFragment() {
        if (orderListManagerFragment != null)
            transaction.hide(orderListManagerFragment);
        if (predictionManagerFragment != null)
            transaction.hide(predictionManagerFragment);
        if (rechargefragment != null)
            transaction.hide(rechargefragment);
        if (billfragment != null)
            transaction.hide(billfragment);
        if (inventoryFragment != null)
            transaction.hide(inventoryFragment);
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }

    @Override
    protected void onDestroy() {
        BirdApi.cancelRequestWithTag(tag);
        super.onDestroy();
    }

}
