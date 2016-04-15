package com.birdex.bird.fragment;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.activity.MyOrderListActivity;
import com.birdex.bird.activity.PredicitionDetailActivity;
import com.birdex.bird.adapter.PredicitionAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.OrderRequestEntity;
import com.birdex.bird.entity.PredicitionEntity;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.T;
import com.birdex.bird.widget.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/4/7.
 */
public class PredictionManagerFragment extends BaseFragment implements XRecyclerView.LoadingListener {
    PredicitionEntity predicitionEntity;//返回数据的实体
    PredicitionAdapter predicitionAdapter;
    OrderRequestEntity entity;//请求数据保存的实体
    @Bind(R.id.rcy_orderlist)
    XRecyclerView rcy_orderlist;

    String tag = "PredictionManagerFragment";

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }


    @Override
    public int getContentLayoutResId() {
        return R.layout.xrecycle_view_layout;
    }


    @Override
    public void initializeContentViews() {
//        orderListEntities = new OrderListEntity();
        predicitionEntity = new PredicitionEntity();
//        if (getArguments() != null) {
        entity = (OrderRequestEntity) bundle.getSerializable("entity");
        if (entity == null) {
            entity = new OrderRequestEntity();
        }
//        }
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
                String storage_code = predicitionEntity.getData().getStorages().get(position).getStorage_code();
                Intent intent = new Intent(getActivity(), PredicitionDetailActivity.class);
                intent.putExtra("storage_code", storage_code);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        rcy_orderlist.setAdapter(predicitionAdapter);
        getPredicitionList();
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
        listParams.add("start_date", entity.getStart_date());
        listParams.add("end_date", entity.getEnd_date());
        listParams.add("status", entity.getStatus());
//        listParams.add("count", entity.getCount());
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                predicitionEntity = GsonHelper.getPerson(response.toString(), PredicitionEntity.class);
//                orderListEntities = GsonHelper.getPerson(response.toString(), OrderListEntity.class);
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
                    bus.post(predicitionEntity.getData().getCount(), "frame_layout");//刷新个数及界面
                    predicitionAdapter.notifyDataSetChanged();
                } else {
                    try {
                        if (response.get("data") != null)
                            T.showLong(MyApplication.getInstans(), response.get("data").toString() + "请重新登录");
                        else
                            T.showLong(MyApplication.getInstans(), getString(R.string.parse_error));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse != null)
                    T.showLong(MyApplication.getInstans(), "error:" + errorResponse.toString());
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
     * 请求刷新数据
     */
    @Subscriber(tag = "requestPredictList")
    public void SearchOrderList(OrderRequestEntity entity) {
        this.entity = entity;
        getPredicitionList();
    }

    @Subscriber(tag = "AdapterTop")
    private void reflashAdapter(String string) {
        if (string.equals(getString(R.string.tool2))) {
            rcy_orderlist.smoothScrollToPosition(1);
        }
    }

    @Override
    protected void lazyLoad() {

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

    public static void hideSoft() {
        //1.得到InputMethodManager对象
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//2.调用hideSoftInputFromWindow方法隐藏软键盘
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
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
}
