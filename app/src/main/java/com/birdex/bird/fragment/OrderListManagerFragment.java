package com.birdex.bird.fragment;

import android.view.KeyEvent;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.activity.BaseActivity;
import com.birdex.bird.adapter.OrderListAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.decoration.DividerItemDecoration;
import com.birdex.bird.decoration.FullyLinearLayoutManager;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.entity.OrderRequestEntity;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.HideSoftKeyboardUtil;
import com.birdex.bird.util.T;
import com.birdex.bird.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/4/7.
 */
public class OrderListManagerFragment extends BaseFragment implements XRecyclerView.LoadingListener {
    OrderListEntity orderListEntities;//列表
    OrderListAdapter OrderAdapter;//列表adpter
    OrderRequestEntity entity;//请求数据保存的实体
    @Bind(R.id.rcy_orderlist)
    XRecyclerView rcy_orderlist;

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.xrecycle_view_layout;
    }

    @Override
    public void initializeContentViews() {
        HideSoftKeyboardUtil.setupAppCompatUI(contentView, (BaseActivity) getActivity());
        orderListEntities = new OrderListEntity();
        entity = new OrderRequestEntity();
        bus.register(this);
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
                T.showShort(getActivity(), position + "");
            }
        });
        rcy_orderlist.setAdapter(OrderAdapter);
//        SearchOrderList(entity);
        getOrderList();
    }

    /**
     * 请求刷新数据
     */
    @Subscriber(tag = "requestOrderList")
    public void SearchOrderList(OrderRequestEntity entity) {
        this.entity = entity;
        getOrderList();
    }

    @Subscriber(tag = "AdapterTop")
    private void reflashAdapter(String string) {
        if (string.equals(getString(R.string.tool1))) {
            rcy_orderlist.smoothScrollToPosition(1);
        }
    }

    /**
     * 获取订单列表
     */
    private void getOrderList() {
        showBar();
        RequestParams listParams = new RequestParams();
        listParams.add("page_no", entity.getPage_no() + "");
        listParams.add("page_size", entity.getPage_size());
        listParams.add("keyword", entity.getKeyword());
        listParams.add("warehouse_code", entity.getWarehouse_cod());
        listParams.add("start_date", entity.getStart_date());
        listParams.add("end_date", entity.getEnd_date());
        listParams.add("status", entity.getStatus());
//        listParams.add("count", entity.getCount());
        listParams.add("service_type", entity.getService_type());
        listParams.add("receiver_moblie", entity.getReceiver_moblie());
        BirdApi.getOrderList(MyApplication.getInstans(), listParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                orderListEntities = GsonHelper.getPerson(response.toString(), OrderListEntity.class);
                if (entity.getPage_no() > 1) {
                    if (orderListEntities.getData().getOrders().size() == 0) {
                        T.showShort(MyApplication.getInstans(), "已经是最后一页");
                    } else
                        OrderAdapter.getList().addAll(orderListEntities.getData().getOrders());
                } else {
                    OrderAdapter.setList(orderListEntities.getData().getOrders());
                }
                bus.post(orderListEntities.getData().getCount(), "frame_layout");//刷新个数及界面
                OrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                stopHttpAnim();
                T.showLong(MyApplication.getInstans(), "error:" + responseString.toString());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish() {
                stopHttpAnim();
                super.onFinish();
            }
        });
    }

    @Override
    protected void lazyLoad() {
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
        hideBar();
        if (rcy_orderlist != null) {
            rcy_orderlist.refreshComplete();
            rcy_orderlist.loadMoreComplete();
        }
    }
}
