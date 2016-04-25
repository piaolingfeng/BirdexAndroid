package com.birdex.bird.activity;

import android.view.View;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.MsgCountAdapter;
import com.birdex.bird.adapter.MsgInventoryAdapter;
import com.birdex.bird.adapter.MsgOrderAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.InventorySimpleEntity;
import com.birdex.bird.entity.MsgListEntity;
import com.birdex.bird.util.Constant;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.HashMap;

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
    public String[] msg_list_name_id = null;
    public String[] msg_list_name = null;
    MsgInventoryAdapter inventoryAdapter;
    MsgOrderAdapter orderAdapter;
    MsgCountAdapter countAdapter;
    //    OrderListEntity orderListEntities;//订单列表
    private InventorySimpleEntity InvenEntity = null;

//    OrderRequestEntity requestEntity = new OrderRequestEntity();

    EventBus bus;

    int inventoryPage_no = 1;

    String tag = "MsgDetailActivity";

    //new 接口
    MsgListEntity listEntity;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_msgdetail_layout;
    }

    @Override
    public void initializeContentViews() {
        title = getIntent().getStringExtra("title");
        bus = EventBus.getDefault();
        bus.register(this);
//        InvenEntity = new InventorySimpleEntity();
//        orderListEntities = new OrderListEntity();
        listEntity = new MsgListEntity();
        rcy.setLayoutManager(new FullyLinearLayoutManager(this));
        rcy.setLoadingListener(this);
//        rcy.setPullRefreshEnabled(false);
        rcy.setLoadingMoreEnabled(true);
        rcy.setPullRefreshEnabled(false);
        rcy.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        msg_list_name_id = getResources().getStringArray(R.array.msg_list_name_id);
        msg_list_name = getResources().getStringArray(R.array.msg_list_name);
        for (int i = 0; i < msg_list_name_id.length; i++) {//解析msg_id
            if (title.equals(msg_list_name_id[i])) {
                title = msg_list_name[i];
                break;
            }
        }
        title_view.setInventoryDetail(title, R.color.gray1);
        inventoryAdapter = new MsgInventoryAdapter(MsgDetailActivity.this, listEntity.getData().getMessages());
        orderAdapter = new MsgOrderAdapter(this, listEntity.getData().getMessages(), title);
        countAdapter = new MsgCountAdapter(this, listEntity.getData().getMessages());
        if (msg_list_name[0].equals(title)) {
            rcy.setAdapter(inventoryAdapter);
        } else {
            if (msg_list_name[4].equals(title)) {
                rcy.setAdapter(countAdapter);
            } else {
                rcy.setAdapter(orderAdapter);
            }
        }

        getMsgList("");
    }



    @Subscriber(tag = "MsgOrderLoadMore")
    public void getMsgList(String text) {
        showLoading();
        RequestParams params = new RequestParams();
        params.add("page_no", inventoryPage_no + "");
        params.add("start_date", TimeUtil.getMonthDelayData());//默认30天内的
        params.add("end_date", TimeUtil.getCurrentData());
        if (msg_list_name[0].equals(title))
            params.add("msg_type", Constant.MSG_STOCK_WARNING);
        if (msg_list_name[1].equals(title))
            params.add("msg_type", Constant.MSG_ORDER_IDCARD_EXCEPTION);
        if (msg_list_name[2].equals(title))
            params.add("msg_type", Constant.MSG_ORDER_STOCK_EXCEPTION);
        if (msg_list_name[3].equals(title))
            params.add("msg_type", Constant.MSG_ORDER_VERIFY_FAIL);
        if (msg_list_name[4].equals(title))
            params.add("msg_type", Constant.MSG_ACCOUNT_EXCEPTION);
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response!=null) {
                        if (0 == response.get("error")) {
                            listEntity = GsonHelper.getPerson(response.toString(), MsgListEntity.class);
                            if (listEntity != null) {
                                if (listEntity.getData().getMessages().size() != 20 && inventoryPage_no > 1) {
                                    T.showShort(MyApplication.getInstans(), "已经是最后一页");
                                } else {
                                    HashMap map = new HashMap();
                                    map.put("msg_list_update",title);
                                    int size = 0;
                                    for (int i =0; i <listEntity.getData().getMessages().size();i++){//0表示未读，1表示已读
                                        if (listEntity.getData().getMessages().get(i).getRead_status().equals("0")){
                                            size++;
                                        }
                                    }
                                    map.put("size",size);
                                    bus.post(map, "msg_list_update");
                                }
                            } else {
                                T.showLong(MyApplication.getInstans(), getString(R.string.tip_myaccount_prasedatawrong));
                            }
                        } else {
                            T.showLong(MyApplication.getInstans(), response.get("data") + "");
                        }
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
        BirdApi.getMsgList(this, params, handler);
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
        inventoryPage_no++;
        bus.post("", "MsgOrderLoadMore");
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

    @Subscriber(tag = "msg_list_update")
    public void listUpdate(HashMap text) {
        if (msg_list_name_id[0].equals(title)) {
            inventoryAdapter.getList().addAll(listEntity.getData().getMessages());
//            rcy.setAdapter(inventoryAdapter);
            inventoryAdapter.notifyDataSetChanged();
        } else {
            if (msg_list_name_id[4].equals(title)) {
                countAdapter.getList().addAll(listEntity.getData().getMessages());
//                rcy.setAdapter(countAdapter);
                countAdapter.notifyDataSetChanged();
            } else {
                orderAdapter.getList().addAll(listEntity.getData().getMessages());
//                rcy.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
            }
        }
    }

}
