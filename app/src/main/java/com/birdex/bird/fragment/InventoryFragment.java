package com.birdex.bird.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.activity.InventoryActivity;
import com.birdex.bird.adapter.InventoryAdapter;
import com.birdex.bird.adapter.InventoryWillInAdapter;
import com.birdex.bird.adapter.PredicitionAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.entity.OrderRequestEntity;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.T;
import com.birdex.bird.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/4/11.
 */
public class InventoryFragment extends BaseFragment implements XRecyclerView.LoadingListener {
    @Bind(R.id.rcy_orderlist)
    XRecyclerView rcy_orderlist;
    OrderRequestEntity entity;//请求数据保存的实体
    //适配器
    private InventoryAdapter adapter = null;
    private InventoryWillInAdapter willInAdapter = null;
    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.xrecycle_view_layout;
    }

    @Override
    public void initializeContentViews() {
        bus.register(this);
        rcy_orderlist.setLoadingListener(this);
        rcy_orderlist.setPullRefreshEnabled(false);
        rcy_orderlist.setLoadingMoreEnabled(true);

        rcy_orderlist.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rcy_orderlist.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL_LIST));
//        predicitionAdapter = new PredicitionAdapter(getActivity(), predicitionEntity.getData().getStorages());
//        rcy_orderlist.setAdapter(predicitionAdapter);
    }

    /**
     * 获取库存列表
     */
//    private void getInventoryList() {
//        showBar();
//        RequestParams listParams = new RequestParams();
//        listParams.add("product_type", entity.getProduct_type()+"");
//        listParams.add("warehouse_code", entity.getWarehouse_code());
//        listParams.add("keyword", entity.getKeyword());
//        listParams.add("stock_status", entity.getStock_status() + "");
//        listParams.add("order_by", entity.getOrder_by() + "");
//        listParams.add("page_num", entity.getPage_size());
//        listParams.add("page_no", entity.getPage_no() + "");
//        BirdApi.getOrderList(MyApplication.getInstans(), listParams, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                if (statusCode == 200) {
//                    int code = -1;
//                    try {
//                        code = response.getInt("error");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if (code == 0) {
//                        JSONObject jobj = null;
//                        try {
//                            jobj = response.getJSONObject("data");
//                            countNum = jobj.getInt("count");
//                            countPage = jobj.getInt("page_num");
//                            Object proObj=jobj.get("products");
//                            if((proObj instanceof JSONArray)&&currentPage==1){
//                                JSONArray jroot = jobj.getJSONArray("products");
//                                //搜索或者限制条件导致没有数据
//                                if (type == Type.Inner) {
//                                    adapter.clearDataSource();
//                                } else if (type == Type.Willin) {
//                                    willInAdapter.clearDataSource();
//                                }else if(type==Type.OutofWarning){
//                                    adapter.clearDataSource();
//                                }
//                            }else if(proObj instanceof JSONObject){
//                                JSONObject jroot = jobj.getJSONObject("products");
//                                list = biz.parseJson2List(jroot);
//                                if (list != null && list.size() > 0) {
//                                    if (currentPage == 1) {
//                                        //显示数量
//                                        tv_count.setText(countTxt.replace("@",countNum+""));
//                                        //首页为重新加载
//                                        if (type == Type.Inner) {
//                                            adapter.setDataSource(list);
//                                        } else if (type == Type.Willin) {
//                                            willInAdapter.setDataSource(list);
//                                        }else if(type==Type.OutofWarning){
//                                            adapter.setDataSource(list);
//                                        }
//                                    } else {
//                                        //其他页面则为添加
//                                        if (type == Type.Inner) {
//                                            adapter.addDataSource(list);
//                                        } else if (type == Type.Willin) {
//                                            willInAdapter.addDataSource(list);
//                                        }else if(type==Type.OutofWarning){
//                                            adapter.addDataSource(list);
//                                        }
//
//                                    }
//                                    //下次页数加1
//                                    currentPage++;
//                                }else{
//                                    T.showShort(InventoryActivity.this, R.string.inventory_tip_3);
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        T.showShort(InventoryActivity.this, R.string.inventory_tip_2);
//                    }
//                } else {
//                    T.showShort(InventoryActivity.this, R.string.inventory_tip_1);
//                }
//                //结束动画
//                stopHttpAnim();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                stopHttpAnim();
//                T.showLong(MyApplication.getInstans(), "error:" + responseString.toString());
//                super.onFailure(statusCode, headers, responseString, throwable);
//            }
//
//            @Override
//            public void onFinish() {
//                stopHttpAnim();
//                super.onFinish();
//            }
//        });
//    }

    /**
     * 请求刷新数据
     */
    @Subscriber(tag = "inventorytList")
    public void SearchOrderList(OrderRequestEntity entity) {
        this.entity = entity;
//        getInventoryList();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Subscriber(tag = "AdapterTop")
    private void reflashAdapter(String string) {
        if (string.equals(getString(R.string.tool2))) {
            rcy_orderlist.smoothScrollToPosition(1);
        }
    }
}
