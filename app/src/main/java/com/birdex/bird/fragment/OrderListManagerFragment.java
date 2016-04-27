package com.birdex.bird.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.birdex.bird.activity.MipcaActivityCapture;
import com.birdex.bird.activity.MyOrderListActivity;
import com.birdex.bird.activity.OrderDetailActivity;
import com.birdex.bird.adapter.OrderListAdapter;
import com.birdex.bird.adapter.OrderStatusAdapter;
import com.birdex.bird.adapter.OrderTimeAdapter;
import com.birdex.bird.adapter.OrderWareHouseAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.entity.OrderRequestEntity;
import com.birdex.bird.entity.OrderStatus;
import com.birdex.bird.greendao.warehouse;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
import com.birdex.bird.util.decoration.DividerItemDecoration;
import com.birdex.bird.util.decoration.FullyLinearLayoutManager;
import com.birdex.bird.widget.ClearEditText;
import com.birdex.bird.widget.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/4/7.
 */
public class OrderListManagerFragment extends BaseFragment implements XRecyclerView.LoadingListener, View.OnClickListener {
    OrderListEntity orderListEntities;//列表
    OrderListAdapter OrderAdapter;//列表adpter
    OrderRequestEntity entity;//请求数据保存的实体
    @Bind(R.id.rcy_orderlist)
    XRecyclerView rcy_orderlist;
    String tag = "OrderListManagerFragment";

    @Bind(R.id.et_search)
    ClearEditText et_search;
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
    @Bind(R.id.tv_no_text)
    TextView tv_no_text;
    @Bind(R.id.tv_list_count)
    TextView tv_list_count;

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }


    @Override
    public int getContentLayoutResId() {
        return R.layout.xrecycle_view_layout;
    }

    @Override
    public void initializeContentViews() {

        initView();

    }

    /**
     * 请求刷新数据
     */
    @Subscriber(tag = "requestOrderList")
    private void SearchOrderList(OrderRequestEntity entity) {
        this.entity = entity;
        getOrderList();
    }


    /**
     * 获取订单列表
     */
    private void getOrderList() {
        showLoading();
        RequestParams listParams = new RequestParams();
        listParams.add("page_no", entity.getPage_no() + "");
        listParams.add("page_size", entity.getPage_size());
        listParams.add("keyword", entity.getKeyword());
        listParams.add("warehouse_code", entity.getWarehouse_code());
        listParams.add("start_date", entity.getStart_date());
        listParams.add("end_date", entity.getEnd_date());
        listParams.add("status", entity.getStatus());
//        listParams.add("count", entity.getCount());
        listParams.add("service_type", entity.getService_type());
        listParams.add("receiver_moblie", entity.getReceiver_moblie());
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (0 == response.get("error")) {
                        orderListEntities = GsonHelper.getPerson(response.toString(), OrderListEntity.class);
                        if (orderListEntities != null) {
                            if (entity.getPage_no() > 1)
                                if (orderListEntities.getData().getOrders().size() == 0 && entity.getPage_no() > 1) {
                                    T.showShort(MyApplication.getInstans(), "已经是最后一页");
                                } else {
                                    OrderAdapter.getList().addAll(orderListEntities.getData().getOrders());
                                    orderListEntities.getData().setOrders(OrderAdapter.getList());
                                }
                            else {
                                OrderAdapter.setList(orderListEntities.getData().getOrders());
                            }
                            bus.post(orderListEntities.getData().getCount(), "Order_frame_layout");//刷新个数及界面
                            OrderAdapter.notifyDataSetChanged();
                        }else {
                            T.showLong(MyApplication.getInstans(), getString(R.string.tip_myaccount_prasedatawrong));
                        }
                    } else {
                        T.showLong(MyApplication.getInstans(), response.get("data") + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                T.showLong(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong) + responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                T.showLong(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong) + errorResponse);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                T.showLong(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong) + errorResponse);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }


            @Override
            public void onFinish() {
                stopHttpAnim();
                super.onFinish();
            }
        };
        handler.setTag(tag);
        BirdApi.getOrderList(MyApplication.getInstans(), listParams, handler);
    }

    @Override
    protected void lazyLoad() {
//        if(!isPrepared || !isVisible) {
//            return;
//        }
//        initView();
    }

    @Override
    public void onRefresh() {
//        entity.setPage_no(1);
//        bus.post(entity, "requestOrderList");
    }

    @Override
    public void onLoadMore() {
        entity.setPage_no(entity.getPage_no() + 1);
        bus.post(entity, "requestOrderList");
    }

    /*
      *停止动画
      */
    private void stopHttpAnim() {
        hideLoading();
        if (rcy_orderlist != null) {
            rcy_orderlist.refreshComplete();
            rcy_orderlist.loadMoreComplete();
        }
    }

    @Override
    public void onDestroy() {
        BirdApi.cancelRequestWithTag(tag);
        super.onDestroy();
    }

    public void initView() {
        orderListEntities = new OrderListEntity();
        if (bundle != null) {
            entity = (OrderRequestEntity) bundle.getSerializable("entity");
        }
        if (entity == null) {
            entity = new OrderRequestEntity();
        }
        bus.register(this);
        initFloatAction();
        initSearch();
        initscan_code();
        String indexOrder = getActivity().getIntent().getStringExtra("indexOrder");
        if (!StringUtils.isEmpty(indexOrder)) {
//            getActivity().getIntent().removeExtra("indexOrder");
            if (indexOrder.contains("今日")) {//初始化时间
                entity.setStart_date(MyOrderListActivity.timeList.get(1).getStart_date());
                entity.setEnd_date(MyOrderListActivity.timeList.get(1).getEnd_date());
                bus.post(MyOrderListActivity.timeList.get(1).getName(), "Order_changeTime");//改变显示的时间
            } else {
                entity.setStart_date(MyOrderListActivity.timeList.get(0).getStart_date());
                entity.setEnd_date(MyOrderListActivity.timeList.get(0).getEnd_date());
                bus.post(MyOrderListActivity.timeList.get(0).getName(), "Order_changeTime");//改变显示的时间
            }
            for (int position = 0; position < MyOrderListActivity.orderStatus.getData().size(); position++) {
                if (indexOrder.contains(MyOrderListActivity.orderStatus.getData().get(position).getStatus_name().trim())) {//包含该模块的名字
                    entity.setStatus(MyOrderListActivity.orderStatus.getData().get(position).getStatus() + "");
                    bus.post(MyOrderListActivity.orderStatus.getData().get(position), "Order_changeState");
                    break;
                }
            }
        }
        rcy_orderlist.setLoadingListener(this);
//        rcy_orderlist.setPullRefreshEnabled(false);
        rcy_orderlist.setLoadingMoreEnabled(true);
        rcy_orderlist.setPullRefreshEnabled(false);

        rcy_orderlist.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
        rcy_orderlist.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        OrderAdapter = new OrderListAdapter(getActivity(), orderListEntities.getData().getOrders());
        OrderAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                T.showShort(getActivity(), position + "");
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("order_code", OrderAdapter.getList().get(position).getOrder_code());
                startActivity(intent);
            }
        });
        rcy_orderlist.setAdapter(OrderAdapter);
//        SearchOrderList(entity);
        getOrderList();
    }

    @Subscriber(tag = "order_visible")
    public void FloatActionVisble(boolean flag) {
        if (flag) {
            fab_inventory_gotop.setVisibility(View.VISIBLE);
        } else {
            fab_inventory_gotop.setVisibility(View.GONE);
        }
    }

    /**
     * 滚动到顶部
     */
    private void initFloatAction() {

        fab_inventory_gotop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcy_orderlist.smoothScrollToPosition(1);
            }
        });
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
                    search(string);
                }
                return false;
            }
        });

        et_search.setOnClearTextListener(new ClearEditText.OnClearTextListener() {
            @Override
            public void clearTextListenr() {
                entity.setKeyword("");
            }
        });
    }

    // 搜索
    private void search(String search) {
        entity.setKeyword(search);
        bus.post(entity, "requestOrderList");
    }

    private final static int SCANNIN_GREQUEST_CODE = 1;

    private void initscan_code() {
        img_scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                T.showShort(MyApplication.getInstans(), getString(R.string.please_wail));
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result");
                    et_search.setText(result);
                    search(result);
                }
                break;
        }
    }

    @Subscriber(tag = "Order_changeWarehouse")
    public void changeWarehouse(warehouse house) {
//        nowSelectedWarehouse = detail;
        state_Warehouse.setText(house.getName());
    }

    @Subscriber(tag = "Order_changeState")
    public void changeState(OrderStatus.Status status) {
//        nowSelectedStatus = status;
        state_all.setText(status.getStatus_name());
    }

    @Subscriber(tag = "Order_changeTime")
    public void changeTime(String text) {
//        nowSelectedStatus = status;
        state_time.setText(text);
    }

    @Subscriber(tag = "Order_frame_layout")
    private void setFrameLayoutVisble(int value) {
        tv_list_count.setText("共" + value + "条数据");
        if (value > 0) {
            rcy_orderlist.setVisibility(View.VISIBLE);
            tv_no_text.setVisibility(View.GONE);
        } else {
            rcy_orderlist.setVisibility(View.GONE);
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
    public void showTimeWindow(View viewID, final int w) {
//
        OrderTimeAdapter adapter = new OrderTimeAdapter(getActivity(), MyOrderListActivity.timeList);
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
        OrderStatusAdapter adapter = new OrderStatusAdapter(getActivity(), list);
        adapter.setOnRecyclerViewItemClickListener(
                new OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (mPopupWindow.isShowing()) {
                            mPopupWindow.dismiss();
                        }
//                        T.showShort(MyApplication.getInstans(), list.get(position).getStatus_name());
                        entity.setStatus(list.get(position).getStatus() + "");
                        bus.post(list.get(position), "Order_changeState");
                        entity.setPage_noReset();
                        bus.post(entity, "requestOrderList");
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
        OrderWareHouseAdapter adapter = new OrderWareHouseAdapter(getActivity(), MyOrderListActivity.warehouseEntity.getData());
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
        LayoutInflater mLayoutInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View popWindow = LayoutInflater.from(getActivity()).inflate(R.layout.common_recycleview_layout, null);
//        popWindow.setBackgroundColor(Color.TRANSPARENT);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        entity.setWarehouse_code(MyOrderListActivity.warehouseEntity.getData().get(position).getWarehouse_code());
        entity.setPage_noReset();
        bus.post(MyOrderListActivity.warehouseEntity.getData().get(position), "Order_changeWarehouse");
        bus.post(entity, "requestOrderList");
    }


    private void setTime(int position) {
        entity.setStart_date(MyOrderListActivity.timeList.get(position).getStart_date());
        entity.setEnd_date(MyOrderListActivity.timeList.get(position).getEnd_date());
        entity.setPage_noReset();
        bus.post(MyOrderListActivity.timeList.get(position).getName(), "Order_changeTime");//改变显示的时间
        bus.post(entity, "requestOrderList");
    }


    @OnClick({R.id.state_time, R.id.state_Warehouse, R.id.state_all})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.state_all:
                showStateWindow((View) state_all.getParent(), MyOrderListActivity.orderStatus.getData(), 1);
                break;
            case R.id.state_Warehouse://所有仓库
                showWarehouseWindow((View) state_Warehouse.getParent(), 1);
                break;
            case R.id.state_time:
                showTimeWindow((View) state_time.getParent(), 1);
                break;
        }
    }
}
