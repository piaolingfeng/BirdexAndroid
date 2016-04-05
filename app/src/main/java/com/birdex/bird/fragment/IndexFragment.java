package com.birdex.bird.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.activity.InventoryActivity;
import com.birdex.bird.activity.MyAccountActivity;
import com.birdex.bird.activity.MyOrderListActivity;
import com.birdex.bird.activity.TodayDataActivity;
import com.birdex.bird.adapter.OrderManagerAdapter;
import com.birdex.bird.adapter.ToolManagerAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.decoration.FullyGridLayoutManager;
import com.birdex.bird.entity.OrderManagerEntity;
import com.birdex.bird.glide.GlideUtils;
import com.birdex.bird.helper.OnStartDragListener;
import com.birdex.bird.helper.SimpleItemTouchHelperCallback;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.interfaces.OnRecyclerViewItemLongClickListener;
import com.birdex.bird.lunbo.CycleViewPager;
import com.birdex.bird.lunbo.DepthPageTransformer;
import com.birdex.bird.util.PreferenceUtils;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/3/21.
 */
public class IndexFragment extends BaseFragment implements OnStartDragListener {
    CycleViewPager cycleViewPager;

    @Bind(R.id.rcv_order_manager)
    RecyclerView rcv_order_manager;

    @Bind(R.id.rcv_tool_manager)
    RecyclerView rcv_tool_manager;

    private ItemTouchHelper mItemTouchHelper;

    public static boolean firstCome = false;

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_index_layout;
    }

    @Override
    public void initializeContentViews() {
        if (PreferenceUtils.getPrefBoolean(getActivity(), "firstCome", true)) {//第一次进入应用时采用默认显示
            firstCome = true;
            PreferenceUtils.setPrefBoolean(getActivity(), "firstCome", false);
        }
        indexOrderLocalDataList = new ArrayList<>();//初始化
        indexOrderNetDatatList = new ArrayList<>();
        bus.register(this);
        cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.cycle_viewpage);
        if (cycleViewPager == null) {
            cycleViewPager = (CycleViewPager) getChildFragmentManager().findFragmentById(R.id.cycle_viewpage);
        }
        cycleViewPager.setCycle(true);
        cycleViewPager.setWheel(true);
        cycleViewPager.setTime(3 * 1000);
        cycleViewPager.setData(initCarouselData());
        cycleViewPager.getViewPager()
                .setPageTransformer(true,
                        new DepthPageTransformer());
        initToolManager();
        initOrderManager();
        getTodayData();
    }

    public static OrderManagerAdapter orderManagerAdapter;
    public static List<OrderManagerEntity> indexOrderLocalDataList;//首页订单状态首页显示数据
    public static List<OrderManagerEntity> indexOrderNetDatatList;//首页订单状态网络数据
    public static String localArrayString;

    /**
     * 数据看板初始化
     */
    private void initOrderManager() {

        rcv_order_manager.setLayoutManager(new FullyGridLayoutManager(getContext(), 3));
        if (indexOrderLocalDataList != null)
            indexOrderLocalDataList.add(null);//首页最后一个为进入数据更多
        orderManagerAdapter = new OrderManagerAdapter(getContext(), indexOrderLocalDataList, this);
        orderManagerAdapter.setOnRecyclerViewItemLongClickListener(new OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                bus.post(true, "LongClick");
                return false;
            }
        });
        orderManagerAdapter.setOnRecyclerViewAddItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), TodayDataActivity.class);
                intent.putExtra("TodayData", (Serializable) indexOrderNetDatatList);
                startActivity(intent);
            }
        });
        orderManagerAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                T.showShort(MyApplication.getInstans(), position + "");
            }
        });
        rcv_order_manager.setAdapter(orderManagerAdapter);

        //实现item拖动
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(orderManagerAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rcv_order_manager);
    }


    public static void getTodayData() {
        RequestParams params = new RequestParams();
        params.add("app_debug", 1 + "");
        params.add("user_code", MyApplication.user.getUser_code());
        params.add("company_code", MyApplication.user.getCompany_code());
        JsonHttpResponseHandler httpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                try {
                    parseNetData(response);
                    EventBus.getDefault().post("", "getLocalData");
//                    initOrderManager();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                T.showShort(MyApplication.getInstans(), errorResponse.toString());
            }
        };
        BirdApi.getTodayData(MyApplication.getInstans(), params, httpResponseHandler);
    }

    /**
     * 解析网络请求返回的今日数据
     */
    public static void parseNetData(final JSONObject response) throws JSONException {
        JSONObject object = response.getJSONObject("data");
        OrderManagerEntity entity;
        boolean state;

        for (int i = 0; i < name.length; i++) {
            entity = new OrderManagerEntity();
            entity.setCount((Integer) object.get(name[i]));
            state = PreferenceUtils.getPrefBoolean(MyApplication.getInstans(), nameText[i], false);
            entity.setChoose_state(state);
            entity.setName(nameText[i]);
            if (firstCome && i < name.length / 2) {
                entity.setChoose_state(firstCome);//第一次进入默认获取前5个显示在首页
                PreferenceUtils.setPrefBoolean(MyApplication.getInstans(), nameText[i], true);
//                indexOrderLocalDataList.add(indexOrderLocalDataList.size() - 1, entity);
            }
//            if (state && !firstCome) {
//                indexOrderLocalDataList.add(indexOrderLocalDataList.size() - 1, entity);
//            }
            indexOrderNetDatatList.add(entity);
        }
    }


    public static String[] name = {"today_confirm_storage_count", "today_checkout_order_count", "today_wait_checkout_order_count",
            "exception_order_count", "transport_order_count", "no_pass_order_count"
            , "today_sign_order_count", "no_pass_storage_count", "warning_stock_count", "wait_confirm_storage_count"};
    public static String[] nameText = {"今日已入库预报单", "今日已出库", "今日待出库",
            "异常订单", "今日运输中", "审核不通过的订单",
            "今日已签收", "审核不通过预报单", "库存紧张", "待确认预报单"};


    /**
     * 获取保存在数据库中的list顺序
     */
    @Subscriber(tag = "getLocalData")
    public void getDatabaseData(String string) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (firstCome) {
                    for (int i = 0; i < name.length / 2; i++) {
                        indexOrderLocalDataList.add(indexOrderLocalDataList.size() - 1, indexOrderNetDatatList.get(i));
                        bus.post("", "save");
                    }
                } else {
                    localArrayString = PreferenceUtils.getPrefString(MyApplication.getInstans(), "getSortData", "");
                    List<String> parseList = parseString(localArrayString);
                    for (String p : parseList) {
                        for (OrderManagerEntity entity : indexOrderNetDatatList) {
                            if (entity.getName().contains(p)) {
                                indexOrderLocalDataList.add(indexOrderLocalDataList.size() - 1, entity);//放在null之前
                                break;
                            }
                        }
                    }
                    bus.post("", "adapterReflash");
                }
            }
        }).start();
    }

    @Subscriber(tag = "adapterReflash")
    public void adapterReflash(String string) {
        orderManagerAdapter.notifyDataSetChanged();
    }

    /**
     * 保存今日数据的顺序
     */
    @Subscriber(tag = "save")
    public synchronized void saveDatabaseData(String string) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                localArrayString = "";
                List<String> arrayList = new ArrayList<String>();
                for (OrderManagerEntity entity : indexOrderLocalDataList) {
                    if (entity != null)
                        arrayList.add(entity.getName());
                }
                localArrayString = arrayList.toString();
                localArrayString = localArrayString.substring(1, localArrayString.length() - 1);
                PreferenceUtils.setPrefString(MyApplication.getInstans(), "getSortData", localArrayString);
                bus.post("", "adapterReflash");
            }
        }).start();
    }

    /**
     * 解析首页订单列表的字符串
     */
    public List<String> parseString(String sortString) {
        List<String> parseString = new ArrayList<>();
        if (sortString.contains(",") || sortString.contains(";")) {
            String sort[] = sortString.split(";");
            if (sortString.contains(",")) {
                sort = sortString.split(",");
            }
            for (String s : sort) {
                parseString.add(s.trim());
            }
        }
        return parseString;
    }


    /**
     * 网络数据状态
     */
    @Subscriber(tag = "entity")
    public void indexAmountListChange(OrderManagerEntity entity) {
        //遍历
        for (int i = 0; i < indexOrderNetDatatList.size(); i++) {
            if (indexOrderNetDatatList.get(i).getName().equals(entity.getName())) {
                indexOrderNetDatatList.get(i).setChoose_state(entity.isChoose_state());
                return;
            }
        }
    }

    /**
     * 首页显示状态
     */
    @Subscriber(tag = "entity")
    public synchronized void indexListChange(OrderManagerEntity entity) {
        //遍历
        for (int i = 0; i < indexOrderLocalDataList.size() - 1; i++) {//最后一个null为添加
            if (indexOrderLocalDataList.get(i).getName().equals(entity.getName())) {
                PreferenceUtils.setPrefBoolean(getContext(), indexOrderLocalDataList.get(i).getName(), entity.isChoose_state());
//                if (!entity.isChoose_state()) {
                indexOrderLocalDataList.remove(i);
//                }
                bus.post("", "save");
                return;
            }
            if (i == indexOrderLocalDataList.size() - 2) {
                PreferenceUtils.setPrefBoolean(getContext(), entity.getName(), entity.isChoose_state());
                indexOrderLocalDataList.add(indexOrderLocalDataList.size() - 1, entity);//在null之前添加
                bus.post("", "save");
                return;
            }
        }
        if (indexOrderLocalDataList.size() - 1 == 0) {
            PreferenceUtils.setPrefBoolean(getContext(), entity.getName(), entity.isChoose_state());
            indexOrderLocalDataList.add(indexOrderLocalDataList.size() - 1, entity);//在null之前添加
            bus.post("", "save");
        }
    }


    /**
     * 管理工具初始化
     */
    private void initToolManager() {
        rcv_tool_manager.setLayoutManager(new FullyGridLayoutManager(getContext(), 3));
        final List<OrderManagerEntity> list = new ArrayList<OrderManagerEntity>();
        int[] data = {R.drawable.tool1, R.drawable.tool2, R.drawable.tool3, R.drawable.tool5, R.drawable.tool6};
        final int[] name = {R.string.tool1, R.string.tool2, R.string.tool3, R.string.tool5, R.string.tool6};
        for (int i = 0; i < data.length; i++) {
            OrderManagerEntity entity = new OrderManagerEntity();
            entity.setCount(data[i]);
            entity.setDel_state(false);
            entity.setName(getString(name[i]));
            list.add(entity);
        }
        final ToolManagerAdapter toolManagerAdapter = new ToolManagerAdapter(getContext(), list);
        toolManagerAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                T.showShort(MyApplication.getInstans(), list.get(position).getName());
                // 点击了 我的账户
                if (list.get(position).getName() != null && list.get(position).getName().equals(getString(R.string.tool6))) {
                    // 跳转到我的账户
                    Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                    startActivity(intent);
                    return;
                }else if(list.get(position).getName() != null && list.get(position).getName().equals(getString(R.string.tool3))){
                    //我的库存
                    intent=new Intent(getActivity(), InventoryActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(getActivity(), MyOrderListActivity.class);
                intent.putExtra("name", getString(name[position]));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        rcv_tool_manager.setAdapter(toolManagerAdapter);
    }

    @Override
    protected void key(int keyCode, KeyEvent event) {
        if (orderManagerAdapter.isLongClickState()) {
            bus.post(false, "LongClick");
        }
    }

    @Subscriber(tag = "LongClick")
    public void changeLongClickState(boolean state) {
        orderManagerAdapter.setLongClickState(state);
        for (int i = 0; i < orderManagerAdapter.getOrderList().size(); i++) {
            if (orderManagerAdapter.getOrderList().get(i) != null) {
                orderManagerAdapter.getOrderList().get(i).setDel_state(state);
            }
        }
        orderManagerAdapter.notifyDataSetChanged();
        bus.post("", "save");
    }


    @Override
    protected void lazyLoad() {

    }

    /**
     * 初始化轮播数据
     */
    private List<View> initCarouselData() {

        String url[] = {"file:///android_asset/lunbo1.png"
                , "file:///android_asset/lunbo2.png"
                , "file:///android_asset/lunbo3.png"
                , "file:///android_asset/lunbo4.jpg"};
        List<String> localPathList = new ArrayList<>();
        for (int i = 0; i < url.length; i++) {
            localPathList.add(url[i]);
        }
        List<View> mImageViews = new ArrayList<View>();
        localPathList.add(localPathList.get(0));//把第一个view添加到最后
        localPathList.add(0, localPathList.get(localPathList.size() - 2));//把最后一张放到第一
        for (int i = 0; i < localPathList.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
//                imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            GlideUtils.Barnner(imageView, localPathList.get(i));
            mImageViews.add(imageView);
            if (i != 0 && i != localPathList.size() - 1) {
                final int position = i - 1;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //内部跳转
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra("position", position);
//                        startActivity(intent);
                        T.showLong(MyApplication.getInstans(), "跳转到产品引导" + position);
                    }
                });
            }
        }


//        }
        return mImageViews;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
