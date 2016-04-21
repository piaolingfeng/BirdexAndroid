package com.birdex.bird.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.PredicitionDetailAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.util.decoration.DividerItemDecoration;
import com.birdex.bird.util.decoration.FullyLinearLayoutManager;
import com.birdex.bird.entity.PredicitionDetailEntity;
import com.birdex.bird.interfaces.OnRecyclerViewInsClickListener;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
import com.birdex.bird.widget.TitleView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/4/12.
 */
public class PredicitionDetailActivity extends BaseActivity {
    @Bind(R.id.title_view)
    TitleView title_view;
    @Bind(R.id.tv_now_status)
    TextView tv_now_status;
    @Bind(R.id.tv_confirm_predicition)
    TextView tv_confirm_predicition;
    @Bind(R.id.tv_Destination_warehousee)
    TextView tv_Destination_warehousee;
    @Bind(R.id.tv_Logistics_tracking)
    TextView tv_Logistics_tracking;
    @Bind(R.id.tv_remarks)
    TextView tv_remarks;
    @Bind(R.id.tv_update_time)
    TextView tv_update_time;
    @Bind(R.id.tv_create_time)
    TextView tv_create_time;
    @Bind(R.id.tv_error)
    TextView tv_error;
    @Bind(R.id.rcy)
    RecyclerView rcy;

    PredicitionDetailEntity entity;
    String storage_code;
    EventBus bus;
    PredicitionDetailAdapter adapter;
    int fragment_position = 0;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_predicition_layout;
    }

    @Override
    public void initializeContentViews() {
        entity = new PredicitionDetailEntity();
        storage_code = getIntent().getStringExtra("storage_code");
        fragment_position = getIntent().getIntExtra("position", 0);
        bus = EventBus.getDefault();
        bus.register(this);
        title_view.setInventoryDetail(getString(R.string.predicition_detail), R.color.gray1);
        rcy.setLayoutManager(new FullyLinearLayoutManager(this));
        rcy.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        if (storage_code != null) {
            getStorageDetail(storage_code);
        } else {
            T.showLong(this, getString(R.string.send_error));
        }
    }

    /**
     * @param storage_code 商品入库单唯一编号,详情
     */
    private void getStorageDetail(String storage_code) {
        showLoading();
        RequestParams params = new RequestParams();
        params.add("storage_code", storage_code);
        BirdApi.getStorageDetail(this, params, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                hideLoading();
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                entity = GsonHelper.getPerson(response.toString(), PredicitionDetailEntity.class);
                if (entity != null)
                    bus.post("", "predicitiondetail");
                else {
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
        });
    }

    @Subscriber(tag = "predicitiondetail")
    public void setUI(String text) {
        tv_now_status.setText(entity.getData().getStatus_name());
        tv_Destination_warehousee.setText(entity.getData().getWarehouse_name());
        tv_confirm_predicition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                T.showShort(PredicitionDetailActivity.this, "confirm_predicition");
                setConfirmStorage(entity.getData().getStorage_code(), "", -1);//批量复核//-1为刷新全部状态
            }
        });
        tv_Logistics_tracking.setText(entity.getData().getTrack_no());
        tv_remarks.setText(entity.getData().getRemark());
        tv_update_time.setText(entity.getData().getUpdated_time());
        tv_create_time.setText(entity.getData().getCreated_time());
        if (!StringUtils.isEmpty(entity.getData().getVerify_fail_detail())) {
            tv_error.setText(entity.getData().getVerify_fail_detail());
            tv_error.setVisibility(View.VISIBLE);
        }
        adapter = new PredicitionDetailAdapter(this, entity.getData().getProducts());
        rcy.setAdapter(adapter);
        adapter.setOnRecyclerViewInsClickListener(new OnRecyclerViewInsClickListener() {

            @Override
            public void onItemConfirmClick(int position) {
                setConfirmStorage(entity.getData().getStorage_code(), entity.getData().getProducts().get(position).getProduct_code(), position);
            }

            @Override
            public void onItemReConfirmClick(int position) {
                showAlertDialog(position);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showAlertDialog(final int position) {
        final Dialog myDialog = new Dialog(this, R.style.semester_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_predicition_layout, null);
//        myDialog.setView(view);
//        try {
//            myDialog.show();
//            Window window = myDialog.getWindow();
//            window.setContentView(view);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        myDialog.show();
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setContentView(view);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        final EditText remark = (EditText) view.findViewById(R.id.tv_review_Storage_reason);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        TextView confirm = (TextView) view.findViewById(R.id.tv_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remarkText = remark.getText().toString();
                if (!StringUtils.isEmpty(remarkText)) {
                    setReviewStorage(entity.getData().getStorage_code(), entity.getData().getProducts().get(position).getProduct_code(), remarkText);
                    myDialog.dismiss();
                } else {
                    T.showLong(PredicitionDetailActivity.this, getString(R.string.tv_review_Storage_reason));
                }
            }
        });
    }

    /**
     * @param storage_code
     * @param product_code 确认入库的商品，不传表示确认所有的待确认入库的商品
     */
    public void setConfirmStorage(String storage_code, String product_code, final int position) {
        showLoading();
        RequestParams params = new RequestParams();
        params.add("storage_code", storage_code);
        if (!StringUtils.isEmpty(product_code))
            params.add("product_code", product_code);
        BirdApi.setConfirmStorage(this, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    try {
                        String success = (String) response.get("data");
                        int error = (int) response.get("error");
                        if (error == 0) {
                            if (success != null && success.equals("success")) {
                                bus.post(position, "confirm");//刷新当前页面
                                bus.post("", "getTodayData");//刷新今日看板数据
                                if (position == -1) {//-1为刷新全部状态
                                    bus.post(fragment_position, "confirm_fragment_adapter");//刷新fragment页面
                                }
                                T.showLong(PredicitionDetailActivity.this, getString(R.string.confirm_success));
                            }
                        } else {
                            T.showLong(PredicitionDetailActivity.this, success);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFinish() {
                hideLoading();
                super.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                T.showLong(PredicitionDetailActivity.this, errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    /**
     * 复核
     *
     * @param storage_code 入库单唯一编号
     * @param product_code 发起复核的商品code
     * @param remark       发起复核的原因
     */
    public void setReviewStorage(String storage_code, String product_code, String remark) {
        showLoading();
        RequestParams params = new RequestParams();
        params.add("storage_code", storage_code);
        params.add("product_code", product_code);
        params.add("remark", remark);
        BirdApi.setReviewStorage(this, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    try {
                        String success = (String) response.get("data");
                        int error = (int) response.get("error");
                        T.showLong(PredicitionDetailActivity.this, success);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFinish() {
                hideLoading();
                super.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                T.showLong(PredicitionDetailActivity.this, errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
