package com.birdex.bird.fragment;


import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.adapter.TransactionAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.biz.MyAccountBiz;
import com.birdex.bird.entity.TransactionEntity;
import com.birdex.bird.util.T;
import com.birdex.bird.util.TimeUtil;
import com.birdex.bird.widget.xrecyclerview.ProgressStyle;
import com.birdex.bird.widget.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;

public class BillDetailFragment extends BaseFragment implements XRecyclerView.LoadingListener, View.OnClickListener, DatePicker.OnDateChangedListener, TabLayout.OnTabSelectedListener {
    @Bind(R.id.xrv_personal_detail_list)
    public XRecyclerView recyclerView = null;
    @Bind(R.id.tl_personal_details_type)
    public TabLayout tabLayout = null;
    @Bind(R.id.btn_details_pick_date)
    public Button btn_datepick;
    @Bind(R.id.tv_person_timeshow)
    public TextView tv_timeshow;
    @Bind(R.id.tv_personal_detail_nodata)
    public TextView tv_nodata;
    //popwin的开始日期
    private DatePicker dp_start;
    //popwin的结束日期
    private DatePicker dp_end;
    //取消按钮
    private Button btn_pickdate_cancel = null;
    //清除按钮
    private Button btn_pickdate_clear = null;
    //确定按钮
    private Button btn_pickdate_sure = null;
    //设置参数
    private RequestParams params = null;
    private ArrayList<TransactionEntity> list = null;
    private TransactionAdapter adapter = null;
    //选择日期条件
    private PopupWindow popWindow = null;
    //解析业务
    private MyAccountBiz maBiz = null;
    //设置相应的请求的tag
    private final String http_Request_Refresh = "myaccount_refresh";
    private final String http_Request_Load = "myaccount_load";
    //标记是否为最后一页
    private boolean isLastPage = false;
    private Calendar today = Calendar.getInstance();
    //当前页面 从1开始
    private int currentPage = 1;

    //    private void init(){
//        //初始化解析的类
//        maBiz = new MyAccountBiz();
//        //初始化请求参数
//        params = new RequestParams();
//        //初始化日期条件
//        // 一个自定义的布局，作为显示的内容
//        View contentView = LayoutInflater.from(getActivity()).inflate(
//                R.layout.get_date_layout, null);
//        initPopWin(contentView);
//        popWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        popWindow.setOutsideTouchable(false);
//        popWindow.setTouchable(true);
//        //设置选择日期事件
//        btn_datepick.setOnClickListener(this);
//        tv_timeshow.setOnClickListener(this);
//        adapter = new TransactionAdapter(getActivity());
//        recyclerView.setLoadingMoreEnabled(true);
//        recyclerView.setPullRefreshEnabled(true);
//        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setLoadingListener(this);
//        recyclerView.setAdapter(adapter);
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_1));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_2));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_3));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_4));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_5));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_6));
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabLayout.setOnTabSelectedListener(this);
//        //请求动画
//        showBar();
//        //开始数据请求
//        startHttpRequest(http_Request_Refresh);
//    }
    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_personal_details_list;
    }

    @Override
    public void initializeContentViews() {
        //初始化解析的类
        maBiz = new MyAccountBiz();
        //初始化请求参数
        params = new RequestParams();
        //初始化日期条件
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.get_date_layout, null);
        initPopWin(contentView);
        popWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popWindow.setOutsideTouchable(false);
        popWindow.setTouchable(true);
        //设置选择日期事件
        btn_datepick.setOnClickListener(this);
        tv_timeshow.setOnClickListener(this);
        adapter = new TransactionAdapter(getActivity());
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLoadingListener(this);
        recyclerView.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_3));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_4));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_5));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.cost_type_6));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setOnTabSelectedListener(this);
        //请求动画
        showBar();
        //开始数据请求
        startHttpRequest(http_Request_Refresh);
    }

    /**
     * 初始化弹出窗的里面控件
     */
    private void initPopWin(View contentView) {
        //初始化开始日期
        dp_start = (DatePicker) contentView.findViewById(R.id.dp_date_startpicker);
        //初始化日期
        dp_start.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), this);
        //初始化结束日期
        dp_end = (DatePicker) contentView.findViewById(R.id.dp_date_endpicker);
        //初始化日期
        dp_end.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), this);
        //三个按钮事件
        //取消
        btn_pickdate_cancel = (Button) contentView.findViewById(R.id.btn_pick_date_cancel);
        btn_pickdate_cancel.setOnClickListener(this);
        //取消
        btn_pickdate_clear = (Button) contentView.findViewById(R.id.btn_pick_date_clear);
        btn_pickdate_clear.setOnClickListener(this);
        //取消
        btn_pickdate_sure = (Button) contentView.findViewById(R.id.btn_pick_date_sure);
        btn_pickdate_sure.setOnClickListener(this);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onRefresh() {
        isLastPage = false;
        //清空之前的请求，防止快速滑动产生过多的请求
        BirdApi.cancelRequestWithTag(http_Request_Load);
        BirdApi.cancelRequestWithTag(http_Request_Refresh);
        //开始数据请求
        startHttpRequest(http_Request_Refresh);
    }

    @Override
    public void onLoadMore() {
        //底部加载
        if (!isLastPage) {
            params.put("page_no", currentPage + 1);
            //清空之前的请求，防止快速滑动产生过多的请求
            BirdApi.cancelRequestWithTag(http_Request_Load);
            BirdApi.cancelRequestWithTag(http_Request_Refresh);
            //顶部刷新
            startHttpRequest(http_Request_Load);
        } else {
            T.showShortByID(getActivity(), R.string.tip_myaccount_nodata);
            stopHttpAnim();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_person_timeshow) {
            if (popWindow != null) {
                if (!popWindow.isShowing()) {
                    popWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                }
            }

        } else if (R.id.btn_pick_date_cancel == v.getId()) {
            //取消不需要时间逻辑判断

            //取消
            if (popWindow != null) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        } else if (R.id.btn_pick_date_clear == v.getId()) {
            //清除时间逻辑不需要判断

            //清除
            if (popWindow != null) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
            //清除时间限制条件
            params.put("start_date", "");
            params.put("end_date", "");
            tv_timeshow.setText("");
//            startHttpRequest(http_Request_Refresh);
        } else if (R.id.btn_pick_date_sure == v.getId()) {
            //时间逻辑判断
            //开始时间
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(dp_start.getYear(), dp_start.getMonth(), dp_start.getDayOfMonth());
            //结束时间
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(dp_end.getYear(), dp_end.getMonth(), dp_end.getDayOfMonth());
            //今天
            today = Calendar.getInstance();
            if (calendar1.after(calendar2)) {
                //开始时间晚于结束时间
                T.showShortByID(getActivity(), R.string.tip_pick_date1);
                return;
            }
            if (calendar1.after(today)) {
                //开始时间晚于现在时间
                T.showShortByID(getActivity(), R.string.tip_pick_date2);
                return;
            }
            if (calendar2.after(today)) {
                //结束时间晚于现在时间
                T.showShortByID(getActivity(), R.string.tip_pick_date3);
                return;
            }
            //确定
            if (popWindow != null) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
            //设置时间限制条件
            String start = TimeUtil.calendar2String(calendar1);
            String end = TimeUtil.calendar2String(calendar2);
            params.put("start_date", start);
            params.put("end_date", end);
            tv_timeshow.setText(TimeUtil.calendar2SimpleString(calendar1) + "至" + TimeUtil.calendar2SimpleString(calendar2));
//            startHttpRequest(http_Request_Refresh);
        } else if (v.getId() == R.id.btn_details_pick_date) {
            currentPage = 1;
            isLastPage = false;
            params.put("page_no", currentPage);
            //开始数据请求
            startHttpRequest(http_Request_Refresh);
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popWindow != null) {
            if (popWindow.isShowing()) {
                popWindow.dismiss();
            }
        }
    }

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //时间逻辑判断
        //开始时间
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(dp_start.getYear(), dp_start.getMonth(), dp_start.getDayOfMonth());
        //结束时间
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(dp_end.getYear(), dp_end.getMonth(), dp_end.getDayOfMonth());
        //今天
        today = Calendar.getInstance();
        if (calendar1.after(calendar2)) {
            //开始时间晚于结束时间
            T.showShortByID(getActivity(), R.string.tip_pick_date1);
            return;
        }
        if (calendar1.after(today)) {
            //开始时间晚于现在时间
            T.showShortByID(getActivity(), R.string.tip_pick_date2);
            return;
        }
        if (calendar2.after(today)) {
            //结束时间晚于现在时间
            T.showShortByID(getActivity(), R.string.tip_pick_date3);
            return;
        }
    }


    //停止请求的动画
    private void stopHttpAnim() {
        //停止加载动画
        hideBar();
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
    }

    /**
     * 开始特定的请求
     */
    private void startHttpRequest(String tag) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        int responecode = response.getInt("error");
                        if (responecode == 0) {
                            list = maBiz.praseJson2List(response.getJSONObject("data").getJSONArray("records"));
                            //总页数
                            int countpage = response.getJSONObject("data").getInt("page_num");
                            //当前页数
                            currentPage = response.getJSONObject("data").getInt("page_no");
                            //判断是否为最后一页
                            if (countpage == currentPage) {
                                isLastPage = true;
                            } else {
                                isLastPage = false;
                            }
                            if (null != list) {
                                if (http_Request_Refresh.equals(getTag())) {
                                    //刷新数据
                                    adapter.setDataSource(list);
                                } else if (http_Request_Load.equals(getTag())) {
                                    //新增数据
                                    adapter.addDataSource(list);
                                }
                            } else {
                                //解析失败
                                T.showShortByID(getActivity(), R.string.tip_myaccount_prasedatawrong);
                            }
                        } else if (responecode == 403) {
                            T.showShortByID(getActivity(), R.string.tip_myaccount_needlogin);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    T.showShortByID(getActivity(), R.string.tip_myaccount_getdatawrong);
                }
                showData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                T.showShort(getActivity(), "获取数据失败!");
                showData();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //停止所有请求动画
                stopHttpAnim();
            }
        };
        //标记取消请求的tag
        handler.setTag(tag);
        //调用接口
        BirdApi.getWalletRecord(getActivity(), params, handler);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        currentPage = 1;
        isLastPage = false;
        params.put("page_no", currentPage);
        //全部-0，运费支出-1，仓租-2，关税-3，在线充值-4，其它-5
        params.put("transaction_type", tab.getPosition());
        //显示加载动画
        showBar();
        startHttpRequest(http_Request_Refresh);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void showData() {
        if (adapter.getItemCount() > 0) {
            tv_nodata.setVisibility(View.GONE);
        } else {
            tv_nodata.setVisibility(View.VISIBLE);
        }

    }
}
