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
import com.birdex.bird.activity.PredicitionDetailActivity;
import com.birdex.bird.adapter.OrderStatusAdapter;
import com.birdex.bird.adapter.OrderTimeAdapter;
import com.birdex.bird.adapter.OrderWareHouseAdapter;
import com.birdex.bird.adapter.PredicitionAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.OrderRequestEntity;
import com.birdex.bird.entity.OrderStatus;
import com.birdex.bird.entity.PredicitionEntity;
 import com.birdex.bird.entity.WarehouseEntity;
 import com.birdex.bird.greendao.warehouse;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
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
public class PredictionManagerFragment extends BaseFragment implements XRecyclerView.LoadingListener, View.OnClickListener {
    PredicitionEntity predicitionEntity;//返回数据的实体
    PredicitionAdapter predicitionAdapter;
    OrderRequestEntity entity;//请求数据保存的实体
    @Bind(R.id.rcy_orderlist)
    XRecyclerView rcy_orderlist;
    String tag = "PredictionManagerFragment";

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

    public OrderStatus predicitionStatus;//预报状态列表
    public WarehouseEntity warehouseEntity;//所有仓库列表

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }


    @Override
    public int getContentLayoutResId() {
        return R.layout.xrecycle_view_layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initializeContentViews() {
        initView();
        predicitionStatus = new OrderStatus();
        warehouseEntity = new WarehouseEntity();
        getAllCompanyWarehouse();
        reInitTime();
        getAllPredicitionStatus();
    }

    /**
     * 获取预报列表
     */
    private void getPredicitionList() {
        showLoading();
        RequestParams listParams = new RequestParams();
        listParams.add("page_no", entity.getPage_no() + "");
        listParams.add("page_size", entity.getPage_size());
        listParams.add("keyword", entity.getKeyword());
        listParams.add("warehouse_code", entity.getWarehouse_code());
        listParams.add("updated_start_date", entity.getStart_date());
        listParams.add("updated_end_date", entity.getEnd_date());
        listParams.add("status", entity.getStatus());
//        listParams.add("count", entity.getCount());
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if ("0".equals(response.getString("error"))) {
                        predicitionEntity = GsonHelper.getPerson(response.toString(), PredicitionEntity.class);
                        if (predicitionEntity != null) {
                            if (entity.getPage_no() > 1) {
                                if (predicitionEntity.getData().getStorages().size() == 0 && entity.getPage_no() > 1) {
                                    T.showShort(MyApplication.getInstans(), "已经是最后一页");
                                } else {
                                    predicitionAdapter.getPredicitionDetailList().addAll(predicitionEntity.getData().getStorages());
                                    predicitionEntity.getData().setStorages(predicitionAdapter.getPredicitionDetailList());
                                }
                            } else {
                                predicitionAdapter.setPredicitionDetailList(predicitionEntity.getData().getStorages());
                            }
                            bus.post(predicitionEntity.getData().getCount(), "Pre_frame_layout");//刷新个数及界面
                            predicitionAdapter.notifyDataSetChanged();
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
        BirdApi.getPredicitionList(MyApplication.getInstans(), listParams, handler);

    }

    /**
     * 简化预报列表状态
     */
    private void dealPredictionStatus() {
        OrderStatus status = new OrderStatus();
        String statusName[] = {"待审核", "待入库", "待确认", "已入库", "审核不通过"};
        if (predicitionStatus != null) {
            for (int size = 0; size < statusName.length; size++) {
                for (int i = 0; i < predicitionStatus.getData().size(); i++) {
                    String name = predicitionStatus.getData().get(i).getStatus_name();
                    if (statusName[size].equals(name)) {
                        status.getData().add(predicitionStatus.getData().get(i));
                        break;
                    }
                }
            }
        }
        predicitionStatus = status;
    }

    /**
     * 获取预报所有状态
     */
    private void getAllPredicitionStatus() {
        showLoading();
        RequestParams stateParams = new RequestParams();
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    orderStatus = (OrderStatus) JsonHelper.parseLIst(response.getJSONArray("data"), OrderStatus.class);
                try {
                    if (response != null) {
                        if ("0".equals(response.getString("error"))) {
                            predicitionStatus = GsonHelper.getPerson(response.toString(), OrderStatus.class);
                            dealPredictionStatus();
                            if (predicitionStatus != null) {
                                OrderStatus.Status status = new OrderStatus().new Status();
                                status.setStatus_name("全部状态");
                                predicitionStatus.getData().add(0, status);
                                bus.post("","Pre_reInitStatus");
                            } else {
                                T.showLong(MyApplication.getInstans(), getString(R.string.tip_myaccount_prasedatawrong));
                            }
                        } else {
                            T.showLong(MyApplication.getInstans(), response.get("data") + "");
                        }
                    } else {
                        T.showLong(MyApplication.getInstans(), getString(R.string.request_error));
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
                hideLoading();
                super.onFinish();
            }
        };
        handler.setTag(tag);
        BirdApi.getPredicitionStatus(MyApplication.getInstans(), stateParams, handler);
    }

    /**
     * 获取所有的仓库
     */
    private void getAllCompanyWarehouse() {
        showLoading();
        RequestParams wareParams = new RequestParams();
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response == null) {
                        T.showLong(MyApplication.getInstans(), getString(R.string.request_error));
                        return;
                    }
                    if ("0".equals(response.getString("error"))) {
                        warehouseEntity = GsonHelper.getPerson(response.toString(), WarehouseEntity.class);
                        if (warehouseEntity != null) {
                            if (warehouseEntity.getError().equals("0")) {
                                WarehouseEntity.WarehouseDetail detail = new WarehouseEntity().new WarehouseDetail();
                                detail.setName("全部仓库");
//                nowSelectedWarehouse = detail;//默认选中全部
                                warehouseEntity.getData().add(0, detail);
                                bus.post(detail, "changeWarehouse");
                            } else {
                                T.showLong(MyApplication.getInstans(), warehouseEntity.getError());
                            }
                        } else {
                            T.showLong(MyApplication.getInstans(), getString(R.string.parse_error));
                        }
                    } else {
                        T.showLong(MyApplication.getInstans(), response.get("data") + "");
                    }
                } catch (Exception e) {
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
                hideLoading();
                super.onFinish();
            }
        };
        handler.setTag(tag);
        BirdApi.getAllWarehouse(MyApplication.getInstans(), wareParams, handler);
    }

    /**
     * 请求刷新数据
     */
    @Subscriber(tag = "requestPredictList")
    public void SearchOrderList(OrderRequestEntity entity) {
        this.entity = entity;
        getPredicitionList();
    }

    @Subscriber(tag = "Pre_reInitStatus")
    private void reInitStatus(String text){
        String indexOrder = getActivity().getIntent().getStringExtra("indexOrder");
        if (!StringUtils.isEmpty(indexOrder)) {
            if (indexOrder.contains("预报")) {
                for (int position = 0; position < predicitionStatus.getData().size(); position++) {
                    if (indexOrder.contains(predicitionStatus.getData().get(position).getStatus_name().trim())) {//包含该模块的名字
                        entity.setStatus(predicitionStatus.getData().get(position).getStatus() + "");
                        bus.post(predicitionStatus.getData().get(position), "Pre_changeState");
                        state_all.setText(predicitionStatus.getData().get(position).getStatus_name());
                        break;
                    }
                }
            }
        }
        bus.post(entity, "requestPredictList");
    }

    private void reInitTime() {
        String indexOrder = getActivity().getIntent().getStringExtra("indexOrder");
        if (!StringUtils.isEmpty(indexOrder)) {
//            getActivity().getIntent().removeExtra("indexOrder");
            if (indexOrder.contains("今日")) {//初始化时间
                entity.setStart_date(MyOrderListActivity.timeList.get(1).getStart_date());
                entity.setEnd_date(MyOrderListActivity.timeList.get(1).getEnd_date());
                bus.post(MyOrderListActivity.timeList.get(1).getName(), "Pre_changeTime");//改变显示的时间
                state_time.setText(MyOrderListActivity.timeList.get(1).getName());
            } else {
                entity.setStart_date(MyOrderListActivity.timeList.get(0).getStart_date());
                entity.setEnd_date(MyOrderListActivity.timeList.get(0).getEnd_date());
                bus.post(MyOrderListActivity.timeList.get(0).getName(), "Pre_changeTime");//改变显示的时间
                state_time.setText(MyOrderListActivity.timeList.get(1).getName());
            }
        }
    }

    @Override
    protected void lazyLoad() {
//        if(!isPrepared || !isVisible) {
//            return;
//        }
//        initView();
        //填充各控件的数据
    }

    @Override
    public void onRefresh() {
//        entity.setPage_no(1);
//        bus.post(entity, "requestPredictList");
    }

    @Override
    public void onLoadMore() {
        entity.setPage_no(entity.getPage_no() + 1);
        bus.post(entity, "requestPredictList");
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
        predicitionEntity = new PredicitionEntity();
        if (bundle != null) {
            entity = (OrderRequestEntity) bundle.getSerializable("entity");
        }
        if (entity == null) {
            entity = new OrderRequestEntity();
        }
        initFloatAction();
        initSearch();
        initscan_code();
        bus.register(this);
        rcy_orderlist.setLoadingListener(this);
        rcy_orderlist.setPullRefreshEnabled(false);
        rcy_orderlist.setLoadingMoreEnabled(true);

        rcy_orderlist.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rcy_orderlist.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL_LIST));
        predicitionAdapter = new PredicitionAdapter(getActivity(), predicitionEntity.getData().getStorages());
        predicitionAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String storage_code = predicitionAdapter.getPredicitionDetailList().get(position).getStorage_code();
                Intent intent = new Intent(getActivity(), PredicitionDetailActivity.class);
                intent.putExtra("storage_code", storage_code);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        rcy_orderlist.setAdapter(predicitionAdapter);
    }

    @Subscriber(tag = "predicition_visible")
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
    public void initFloatAction() {

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
        bus.post(entity, "requestPredictList");
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

    @Subscriber(tag = "Pre_changeWarehouse")
    public void changeWarehouse(WarehouseEntity.WarehouseDetail detail) {
//        nowSelectedWarehouse = detail;
        if (state_Warehouse != null)
            state_Warehouse.setText(detail.getName());
    }

    @Subscriber(tag = "Pre_changeState")
    public void changeState(OrderStatus.Status status) {
//        nowSelectedStatus = status;
        if (state_all != null)
            state_all.setText(status.getStatus_name());
    }

    @Subscriber(tag = "Pre_changeTime")
    public void changeTime(String text) {
//        nowSelectedStatus = status;
        if (state_time != null)
            state_time.setText(text);
    }

    @Subscriber(tag = "Pre_frame_layout")
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
                        bus.post(list.get(position), "Pre_changeState");
                        entity.setPage_noReset();
                        bus.post(entity, "requestPredictList");
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
        OrderWareHouseAdapter adapter = new OrderWareHouseAdapter(getActivity(), warehouseEntity.getData());
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
        entity.setWarehouse_code(warehouseEntity.getData().get(position).getWarehouse_code());
        entity.setPage_noReset();
        bus.post(warehouseEntity.getData().get(position), "Pre_changeWarehouse");
        bus.post(entity, "requestPredictList");
    }


    private void setTime(int position) {
        entity.setStart_date(MyOrderListActivity.timeList.get(position).getStart_date());
        entity.setEnd_date(MyOrderListActivity.timeList.get(position).getEnd_date());
        entity.setPage_noReset();
        bus.post(MyOrderListActivity.timeList.get(position).getName(), "Pre_changeTime");//改变显示的时间
        bus.post(entity, "requestPredictList");
    }


    @OnClick({R.id.state_time, R.id.state_Warehouse, R.id.state_all})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.state_all:
                showStateWindow((View) state_all.getParent(), predicitionStatus.getData(), 1);
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
