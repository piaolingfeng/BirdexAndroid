package com.birdex.bird.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.activity.ChangeAdressActivity;
import com.birdex.bird.activity.LogisticsActivity;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.ClipboardManagerUtil;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/4/5.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.DetailHolder> {

    Context mContext;
    List<OrderListEntity.OrderListNum.Orders> list = new ArrayList<>();
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public OrderListAdapter(Context mContext, List<OrderListEntity.OrderListNum.Orders> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setList(List<OrderListEntity.OrderListNum.Orders> list) {
        this.list = list;
    }

    public List<OrderListEntity.OrderListNum.Orders> getList() {
        return list;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailHolder(LayoutInflater.from(mContext).inflate(R.layout.item_orderlist_layout, null));
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, final int position) {
        holder.position = position;
        holder.tv_created_time.setText(list.get(position).getCreated_time());
        holder.tv_order_oms_no.setText(list.get(position).getOrder_oms_no());
        holder.tv_status_name.setText(list.get(position).getStatus_name());
//        holder.tv_receiver_name.setText(list.get(position).getReceiver_name());
//        holder.tv_receiver_mobile.setText(list.get(position).getReceiver_mobile());
//        holder.tv_price.setText("¥ " + list.get(position).getPrice());
//        holder.tv_weight.setText(list.get(position).getWeight() + "kg");
        holder.rcy_productlist.setLayoutManager(new LinearLayoutManager(mContext));
        OrderProductAdapter productAdapter = new OrderProductAdapter(mContext, list.get(position).getProducts());
        productAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int x) {
                if (onRecyclerViewItemClickListener != null)
                    onRecyclerViewItemClickListener.onItemClick(position);
            }
        });
        holder.rcy_productlist.setAdapter(productAdapter);
        String statuName = list.get(position).getStatus_name();
        if (statuName.equals("等待出库") || statuName.equals("准备出库") || statuName.equals("待下架")
                || statuName.equals("下架中") || statuName.equals("审核不通过")
                || statuName.equals("身份证异常")) {
            holder.tv_change_address.setVisibility(View.VISIBLE);
            holder.tv_right_line.setVisibility(View.VISIBLE);
        } else {
            holder.tv_change_address.setVisibility(View.GONE);
            holder.tv_right_line.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(list.get(position).getVerify_fail_detail())) {
            holder.tv_id_error.setVisibility(View.VISIBLE);
            holder.tv_id_error.setText(list.get(position).getVerify_fail_detail());
        } else {
            holder.tv_id_error.setVisibility(View.GONE);
        }
        if (position > 5) {
            EventBus.getDefault().post(true,"order_visible");
        }else{
            EventBus.getDefault().post(false,"order_visible");
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (list != null)
            size = list.size();
        return size;
    }

    public class DetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //订单
        @Bind(R.id.tv_order_oms_no)
        TextView tv_order_oms_no;//订单号
        @Bind(R.id.tv_created_time)
        TextView tv_created_time;//订单创建时间
        @Bind(R.id.tv_status_name)
        TextView tv_status_name;//状态名称

//        @Bind(R.id.tv_receiver_name)
//        TextView tv_receiver_name;//收件人
//        @Bind(R.id.tv_receiver_mobile)
//        TextView tv_receiver_mobile;//手机号码
//        @Bind(R.id.tv_price)
//        TextView tv_price;//费用
//        @Bind(R.id.tv_weight)
//        TextView tv_weight;//重量


        @Bind(R.id.tv_logistics_tracking)
        TextView tv_logistics_tracking;//物流跟踪
        @Bind(R.id.tv_service_type)
        TextView tv_service_type;//服务方式编码
        @Bind(R.id.tv_change_address)
        TextView tv_change_address;//改派
        @Bind(R.id.tv_right_line)
        TextView tv_right_line;

        @Bind(R.id.rcy_productlist)
        RecyclerView rcy_productlist;

        @Bind(R.id.tv_id_error)
        TextView tv_id_error;
        int position = 0;

        public DetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            tv_order_oms_no.setOnClickListener(this);
//            tv_receiver_mobile.setOnClickListener(this);
        }

        @OnClick({R.id.tv_logistics_tracking, R.id.tv_service_type, R.id.tv_change_address})
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_logistics_tracking:
                    startTrackingActivity(list.get(position).getOrder_code(), list.get(position).getStatus_name(), list.get(position).getReceiver_mobile());
                    break;
                case R.id.tv_service_type:
                    T.showLong(MyApplication.getInstans(), mContext.getString(R.string.please_wail));
                    break;
                case R.id.tv_change_address:
                    startChangeAddrActivity(list.get(position).getOrder_code());
                    break;
                case R.id.tv_order_oms_no://复制订单号
                    ClipboardManagerUtil.copy(tv_order_oms_no.getText().toString(), mContext);
                    T.showShort(MyApplication.getInstans(), "已复制");
                    break;
//                case R.id.tv_receiver_mobile:
//                    dialPhoneNumber(tv_receiver_mobile.getText().toString());
//                    break;
                default:
                    if (onRecyclerViewItemClickListener != null)
                        onRecyclerViewItemClickListener.onItemClick(position);
            }
        }

        /**
         * 修改地址
         */
        public void startChangeAddrActivity(String order_code) {
            Intent intent = new Intent(mContext, ChangeAdressActivity.class);
            intent.putExtra("order_code", order_code);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }

        /**
         * 物流跟踪
         */
        public void startTrackingActivity(String order_code, String Status_name, String Receiver_mobile) {
            Intent intent = new Intent(mContext, LogisticsActivity.class);
            intent.putExtra("order_code", order_code);
            intent.putExtra("Status_name", Status_name);
            intent.putExtra("Receiver_mobile", Receiver_mobile);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }


    }
}
