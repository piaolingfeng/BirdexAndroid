package com.birdex.bird.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.OrderDetailAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.util.decoration.DividerItemDecoration;
import com.birdex.bird.util.decoration.FullyLinearLayoutManager;
import com.birdex.bird.entity.OrderDetailEntity;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
import com.birdex.bird.widget.TitleView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/4/12.
 */
public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TitleView title_view;
    @Bind(R.id.tv_order_num)
    TextView tv_order_num;
    @Bind(R.id.tv_status)
    TextView tv_status;
    @Bind(R.id.tv_customer_num)
    TextView tv_customer_num;

    @Bind(R.id.tv_delivery_warehouse)
    TextView tv_delivery_warehouse;
    @Bind(R.id.tv_service_type)
    TextView tv_service_type;
    @Bind(R.id.tv_remarks)
    TextView tv_remarks;

    @Bind(R.id.tv_weight)
    TextView tv_weight;
    @Bind(R.id.tv_free)
    TextView tv_free;
    @Bind(R.id.rcy)
    RecyclerView rcy;
    @Bind(R.id.tv_receiver_name)
    TextView tv_receiver_name;
    @Bind(R.id.tv_receiver_phone)
    TextView tv_receiver_phone;
    @Bind(R.id.tv_addr)
    TextView tv_addr;
    @Bind(R.id.tv_change_addr)
    TextView tv_change_addr;
    @Bind(R.id.tv_id_name)
    TextView tv_id_name;
    @Bind(R.id.tv_id_check)
    TextView tv_id_check;
    EventBus bus;
    String order_code;
    OrderDetailEntity orderDetailEntity;
    OrderDetailAdapter adapter;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_order_detail_layout;
    }

    @Override
    public void initializeContentViews() {
        orderDetailEntity = new OrderDetailEntity();
        bus = EventBus.getDefault();
        bus.register(this);
        rcy.setLayoutManager(new FullyLinearLayoutManager(this));
        rcy.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        order_code = getIntent().getStringExtra("order_code");
        title_view.setInventoryDetail(getString(R.string.order_detail), R.color.gray1);
        if (!StringUtils.isEmpty(order_code)) {
            getOrderDetail();
        } else {
            T.showLong(this, getString(R.string.send_error));
        }
    }

    private void getOrderDetail() {
        showLoading();
        RequestParams params = new RequestParams();
        params.add("order_code", order_code);
        BirdApi.getOrder(this, params, new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                T.showLong(OrderDetailActivity.this, responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                orderDetailEntity = GsonHelper.getPerson(response.toString(), OrderDetailEntity.class);
                if (orderDetailEntity != null) {
                    bus.post(orderDetailEntity, "detail");
                } else {
                    T.showLong(MyApplication.getInstans(), getString(R.string.tip_myaccount_prasedatawrong));
                }
            }

            @Override
            public void onFinish() {
                hideLoading();
                super.onFinish();
            }
        });
    }

    @Subscriber(tag = "detail")
    public void setUI(final OrderDetailEntity orderDetailEntity) {
        tv_order_num.setText(orderDetailEntity.getData().getOrder_oms_no());
        tv_status.setText(orderDetailEntity.getData().getStatus_name());
        tv_customer_num.setText(orderDetailEntity.getData().getOrder_no());
        tv_delivery_warehouse.setText(orderDetailEntity.getData().getWarehouse_name());
        tv_service_type.setText(orderDetailEntity.getData().getService_type_name());
        tv_remarks.setText(orderDetailEntity.getData().getRemark());
        tv_weight.setText(orderDetailEntity.getData().getWeight() + "KG");
        tv_free.setText("¥" + orderDetailEntity.getData().getPrice());
        tv_receiver_name.setText(orderDetailEntity.getData().getReceiver_name());
        tv_receiver_phone.setText(orderDetailEntity.getData().getReceiver_mobile());
        tv_addr.setText(orderDetailEntity.getData().getReceiver_province() + orderDetailEntity.getData().getReceiver_city() + orderDetailEntity.getData().getReceiver_area() + orderDetailEntity.getData().getReceiver_address());
        tv_id_name.setText(orderDetailEntity.getData().getReceiver_id_card());
        if (orderDetailEntity.getData().getVerify_id_card_result().equals("30")) {//30表示验证不通过
            bus.post(false, "checkIDCard");
        } else {
            bus.post(true, "checkIDCard");
        }
        tv_id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                T.showShort(OrderDetailActivity.this, "验证身份证!");
//                Intent intent = new Intent(OrderDetailActivity.this, UploadIDCardActivity.class);
//                intent.putExtra("order_code", orderDetailEntity.getData().getOrder_code());
//                intent.putExtra("idcard", orderDetailEntity.getData().getReceiver_id_card());
//                startActivity(intent);
                upLoadIDCard(OrderDetailActivity.this, orderDetailEntity.getData().getOrder_code(), orderDetailEntity.getData().getReceiver_id_card());
            }
        });
        String statuName = orderDetailEntity.getData().getStatus_name();
        if (statuName.equals("等待出库") || statuName.equals("准备出库") || statuName.equals("待下架") ||
                statuName.equals("出库中") || statuName.equals("下架中") || statuName.equals("审核不通过")
                || statuName.equals("身份证异常")) {
            tv_change_addr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startChangeAddrActivity(orderDetailEntity.getData().getOrder_code());
                }
            });
        } else {
            T.showLong(this, getString(R.string.can_not_change_addr));
        }
        adapter = new OrderDetailAdapter(this, orderDetailEntity.getData().getProducts());
        rcy.setAdapter(adapter);
    }

    /**
     * 重新上传身份证
     */
    public void upLoadIDCard(Context mContext, String Order_code, String idcard) {
        Intent intent = new Intent(mContext, UploadIDCardActivity.class);
        intent.putExtra("order_code", Order_code);
        intent.putExtra("idcard", idcard);
        startActivity(intent);
    }

    /**
     * 修改地址
     */
    public void startChangeAddrActivity(String order_code) {
        Intent intent = new Intent(this, ChangeAdressActivity.class);
        intent.putExtra("order_code", order_code);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Subscriber(tag = "changeAddr")
    public void changeAddr(String string) {
        tv_addr.setText(string);
    }

    @Subscriber(tag = "checkIDCard")
    public void checkIDCard(boolean flag) {
        if (!flag) {
            tv_id_check.setText(getString(R.string.tv_id_check_e));
            tv_id_check.setTextColor(Color.RED);
            Drawable drawable = getResources()
                    .getDrawable(R.drawable.error);
/// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tv_id_check.setCompoundDrawables(drawable, null, null, null);
            tv_id_check.setClickable(true);
        } else {
            tv_id_check.setTextColor(getResources().getColor(R.color.blue));
            tv_id_check.setClickable(false);
            if (orderDetailEntity.getData().getVerify_id_card_result().equals("10")) {
                tv_id_check.setText(getString(R.string.tv_id_wait_check));
            } else {
                tv_id_check.setText(getString(R.string.tv_id_check));
                Drawable drawable = getResources()
                        .getDrawable(R.drawable.right);
/// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                tv_id_check.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        BirdApi.cancelAllRequest();
        super.onDestroy();
    }
}
