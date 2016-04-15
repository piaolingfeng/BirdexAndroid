package com.birdex.bird.activity;

import android.view.View;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.MsgInventoryAdapter;
import com.birdex.bird.adapter.MsgOrderAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.biz.InventoryBiz;
import com.birdex.bird.entity.InventorySimpleEntity;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.entity.OrderRequestEntity;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.T;
import com.birdex.bird.util.TimeUtil;
import com.birdex.bird.util.decoration.DividerItemDecoration;
import com.birdex.bird.util.decoration.FullyLinearLayoutManager;
import com.birdex.bird.widget.TitleView;
import com.birdex.bird.widget.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/4/14.
 */
public class MsgDetailActivity extends BaseActivity implements XRecyclerView.LoadingListener, View.OnClickListener {

    @Bind(R.id.title_view)
    TitleView title_view;
    @Bind(R.id.rcy)
    XRecyclerView rcy;
    @Bind(R.id.tv_clear)
    TextView tv_clear;
    String title = "";
    public String[] msg_list_name = null;

    MsgInventoryAdapter inventoryAdapter;
    MsgOrderAdapter orderAdapter;
    OrderListEntity orderListEntities;//订单列表
    private InventorySimpleEntity InvenEntity = null;

    OrderRequestEntity requestEntity = new OrderRequestEntity();

    EventBus bus;

    int inventoryPage_no = 1;

    String tag = "MsgDetailActivity";

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_msgdetail_layout;
    }

    @Override
    public void initializeContentViews() {
        title = getIntent().getStringExtra("title");
        bus = EventBus.getDefault();
        bus.register(this);
        InvenEntity = new InventorySimpleEntity();
        orderListEntities = new OrderListEntity();
        inventoryAdapter = new MsgInventoryAdapter(MsgDetailActivity.this, InvenEntity.getProducts());
        orderAdapter = new MsgOrderAdapter(this, orderListEntities.getData().getOrders(), title);

        title_view.setInventoryDetail(title, R.color.gray1);
        rcy.setLayoutManager(new FullyLinearLayoutManager(this));
        rcy.setLoadingListener(this);
//        rcy.setPullRefreshEnabled(false);
        rcy.setLoadingMoreEnabled(true);
        rcy.setPullRefreshEnabled(false);
        rcy.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        msg_list_name = getResources().getStringArray(R.array.msg_list_name);
        if (msg_list_name.length >= 5) {
            if (msg_list_name[0].equals(title)) {
                rcy.setAdapter(inventoryAdapter);
                getInvenList();
            } else if (msg_list_name[4].equals(title)) {
                T.showLong(this, getString(R.string.please_wail));
            } else {
                if (msg_list_name[1].equals(title)) {
                    requestEntity.setStatus("3");
                }
                if (msg_list_name[2].equals(title)) {
                    requestEntity.setStatus("4");
                }
                if (msg_list_name[3].equals(title)) {
                    requestEntity.setStatus("30");//审核不通过
                }
                getOrderList("");
                rcy.setAdapter(orderAdapter);
            }
        }
    }


    @Subscriber(tag = "MsgOrderLoadMore")
    public void getOrderList(String text) {
        showLoading();
        RequestParams params = new RequestParams();
        params.add("status", requestEntity.getStatus());
        params.add("start_date", TimeUtil.getMonthDelayData());//默认30天内的
        params.add("end_date", TimeUtil.getCurrentData());
        params.add("page_no", requestEntity.getPage_no() + "");
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                orderListEntities = GsonHelper.getPerson(response.toString(), OrderListEntity.class);
                if (orderListEntities != null) {
                    if (requestEntity.getPage_no() > 1)
                        if (orderListEntities.getData().getOrders().size() == 0) {
                            T.showShort(MyApplication.getInstans(), "已经是最后一页");
                        } else {
                            orderAdapter.getList().addAll(orderListEntities.getData().getOrders());
                            orderListEntities.getData().setOrders(orderAdapter.getList());
                        }
                    else {
                        orderAdapter.setList(orderListEntities.getData().getOrders());
                    }
                    orderAdapter.notifyDataSetChanged();
                } else {
                    try {
                        if (response.get("data") != null)
                            T.showLong(MyApplication.getInstans(), response.get("data").toString() + "请重新登录");
                        else
                            T.showLong(MyApplication.getInstans(),getString(R.string.parse_error));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }

            @Override
            public void onFinish() {
                stopHttpAnim();
                super.onFinish();
            }
        };
        handler.setTag(tag);
        BirdApi.getOrderList(this, params, handler);
    }

    public void getInvenList() {
        showLoading();
        RequestParams params = new RequestParams();
        params.add("stock_status", "20");
        params.add("page_no", inventoryPage_no + "");
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                InvenEntity = GsonHelper.getPerson(response.toString(), InventorySimpleEntity.class);
                if (InvenEntity != null) {
                    if (InvenEntity.getProducts().size() == 0) {
                        T.showShort(MyApplication.getInstans(), "已经是最后一页");
                    } else {
                        inventoryAdapter.getList().addAll(InvenEntity.getProducts());
                        rcy.setAdapter(inventoryAdapter);
                        inventoryAdapter.notifyDataSetChanged();
                    }
                } else {
                    try {
                        if (response.get("data") != null)
                            T.showLong(MyApplication.getInstans(), response.get("data").toString() + "请重新登录");
                        else
                            T.showLong(MyApplication.getInstans(),getString(R.string.parse_error));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFinish() {
                stopHttpAnim();
                super.onFinish();
            }
        };
        handler.setTag(tag);
        BirdApi.getInventory(this, params, handler);
    }

    @Override
    protected void onDestroy() {
        BirdApi.cancelRequestWithTag(tag);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
//        entity.setPage_no(entity.getPage_no() + 1);
//        bus.post(entity, "requestOrderList");
//        requestEntity.se
        if (msg_list_name[0].equals(title)) {
            inventoryPage_no++;
            getInvenList();
        } else {
            requestEntity.setPage_no(requestEntity.getPage_no() + 1);
            bus.post("", "MsgOrderLoadMore");
        }
    }

    /*
     *停止动画
     */
    private void stopHttpAnim() {
        hideLoading();
        if (rcy != null) {
            rcy.refreshComplete();
            rcy.loadMoreComplete();
        }
    }

    @OnClick(R.id.tv_clear)
    @Override
    public void onClick(View v) {
        orderAdapter.getList().clear();
        orderAdapter.notifyDataSetChanged();
        inventoryAdapter.getList().clear();
        inventoryAdapter.notifyDataSetChanged();
    }

}
