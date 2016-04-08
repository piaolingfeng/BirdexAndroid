package com.birdex.bird.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.activity.LogisticsActivity;
import com.birdex.bird.decoration.FullyLinearLayoutManager;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.ClipboardManagerUtil;
import com.birdex.bird.util.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/4/5.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.DetailHolder> {

    Context mContext;
    List<OrderListEntity.OrderListNum.Orders> list;
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
    public void onBindViewHolder(DetailHolder holder, int position) {
        holder.tv_created_time.setText(list.get(position).getCreated_time());
        holder.tv_order_oms_no.setText(list.get(position).getOrder_oms_no());
        holder.tv_status_name.setText(list.get(position).getStatus_name());
        holder.tv_receiver_name.setText(list.get(position).getReceiver_name());
        holder.tv_receiver_mobile.setText(list.get(position).getReceiver_mobile());
        holder.tv_price.setText("¥ " + list.get(position).getPrice());
        holder.tv_weight.setText(list.get(position).getWeight() + "kg");
        holder.rcy_productlist.setLayoutManager(new LinearLayoutManager(mContext));
        OrderProductAdapter productAdapter = new OrderProductAdapter(mContext, list.get(position).getProducts());
        holder.rcy_productlist.setAdapter(productAdapter);
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else return 0;
    }

    public class DetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //订单
        @Bind(R.id.tv_order_oms_no)
        TextView tv_order_oms_no;//订单号
        @Bind(R.id.tv_created_time)
        TextView tv_created_time;//订单创建时间
        @Bind(R.id.tv_status_name)
        TextView tv_status_name;//状态名称

        @Bind(R.id.tv_receiver_name)
        TextView tv_receiver_name;//收件人
        @Bind(R.id.tv_receiver_mobile)
        TextView tv_receiver_mobile;//手机号码
        @Bind(R.id.tv_price)
        TextView tv_price;//费用
        @Bind(R.id.tv_weight)
        TextView tv_weight;//重量


        @Bind(R.id.tv_logistics_tracking)
        TextView tv_logistics_tracking;//物流跟踪
        @Bind(R.id.tv_service_type)
        TextView tv_service_type;//服务方式编码
        @Bind(R.id.tv_change_address)
        TextView tv_change_address;//改派

        @Bind(R.id.rcy_productlist)
        RecyclerView rcy_productlist;
        int position = 0;

        public DetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            tv_order_oms_no.setOnClickListener(this);
            tv_receiver_mobile.setOnClickListener(this);
        }

        @OnClick({R.id.tv_logistics_tracking, R.id.tv_service_type, R.id.tv_change_address})
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_logistics_tracking:
                    startTrackingActivity(list.get(position).getOrder_code(),list.get(position).getStatus_name(),list.get(position).getReceiver_mobile());
                    break;
                case R.id.tv_service_type:
                    T.showLong(MyApplication.getInstans(),mContext.getString(R.string.please_wail));
                    break;
                case R.id.tv_change_address:
                    break;
                case R.id.tv_order_oms_no://复制订单号
                    ClipboardManagerUtil.copy(tv_order_oms_no.getText().toString(), mContext);
                    T.showShort(MyApplication.getInstans(), "已复制");
                    break;
                case R.id.tv_receiver_mobile:
                    dialPhoneNumber(tv_receiver_mobile.getText().toString());
                    break;
                default:
                    if (onRecyclerViewItemClickListener != null)
                        onRecyclerViewItemClickListener.onItemClick(position);
            }
        }

        public void startTrackingActivity(String order_oms_no,String Status_name,String Receiver_mobile){
            Intent intent = new Intent(mContext, LogisticsActivity.class);
            intent.putExtra("order_oms_no",order_oms_no);
            intent.putExtra("Status_name",Status_name);
            intent.putExtra("Receiver_mobile",Receiver_mobile);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }

        /**
         * 拨打电话
         * */
        public void dialPhoneNumber(String phoneNumber) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(intent);
            }
        }
    }
}
