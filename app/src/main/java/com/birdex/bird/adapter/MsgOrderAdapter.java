package com.birdex.bird.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.activity.ChangeAdressActivity;
import com.birdex.bird.activity.OrderDetailActivity;
import com.birdex.bird.activity.UploadIDCardActivity;
import com.birdex.bird.entity.MsgListEntity;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.util.decoration.FullyLinearLayoutManager;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/4/14.
 * 3种类型订单消息
 */
public class MsgOrderAdapter extends RecyclerView.Adapter<MsgOrderAdapter.MsgOrderHolder> {

    Context mContext;
    List<MsgListEntity.MsgList.MsgListMessages> list;
    String currentName = "";

    public MsgOrderAdapter(Context mContext, List<MsgListEntity.MsgList.MsgListMessages> list, String currentName) {
        this.mContext = mContext;
        this.list = list;
        this.currentName = currentName;
    }

    public List<MsgListEntity.MsgList.MsgListMessages> getList() {
        return list;
    }

    public void setList(List<MsgListEntity.MsgList.MsgListMessages> list) {
        this.list = list;
    }

    @Override
    public MsgOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MsgOrderHolder(LayoutInflater.from(mContext).inflate(R.layout.item_msg_order_layout, null));
    }

    @Override
    public void onBindViewHolder(MsgOrderHolder holder, final int position) {
        holder.position = position;
        holder.tv_text_title.setText(currentName);
        holder.tv_create_time.setText(list.get(position).getCreated_date());
        holder.tv_order_num.setText(list.get(position).getMsg_content().getOrder_oms_no());
        holder.tv_recevice_addr.setText(list.get(position).getMsg_content().getReceiver_name() + " ," + list.get(position).getMsg_content().getReceiver_mobile()
                + " ," + list.get(position).getMsg_content().getReceiver_province() + list.get(position).getMsg_content().getReceiver_city()
                + list.get(position).getMsg_content().getReceiver_area() + list.get(position).getMsg_content().getReceiver_address());//缺了收件人地址
        holder.tv_error.setText(list.get(position).getMsg_content().getVerify_fail_detail());
        if (currentName.contains(mContext.getString(R.string.msg_idcard_exception))) {
            holder.tv_function.setText(mContext.getString(R.string.tv_function_id));
        } else {//审核不通过订单
            holder.tv_function.setText(mContext.getString(R.string.tv_change_addr));
        }
        holder.tv_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (currentName.contains(mContext.getString(R.string.msg_check_exception)))
                    startChangeAddrActivity(list.get(position).getMsg_content().getOrder_code(), position);
//                else
//                    upLoadIDCard(mContext, list.get(position).getMsg_content().getOrder_code(), "");//订单列表没有身份证id,只能穿order_code
            }
        });
        if (currentName.contains(mContext.getString(R.string.msg_repertory_exception))) {//库存异常显示
            holder.tv_error.setVisibility(View.GONE);
            holder.tv_function.setVisibility(View.GONE);
            holder.tv_recevice_addr_t.setVisibility(View.GONE);
            holder.tv_recevice_addr.setVisibility(View.GONE);
            holder.tv_discrip.setVisibility(View.VISIBLE);
            holder.rcy.setVisibility(View.VISIBLE);
            holder.rcy.setLayoutManager(new FullyLinearLayoutManager(mContext));
            MsgOrderProductAdapter adapter = new MsgOrderProductAdapter(mContext, list.get(position).getMsg_content().getProducts());
            holder.rcy.setAdapter(adapter);
        } else {
            holder.tv_error.setVisibility(View.VISIBLE);
            holder.tv_function.setVisibility(View.VISIBLE);
            holder.tv_recevice_addr_t.setVisibility(View.VISIBLE);
            holder.tv_recevice_addr.setVisibility(View.VISIBLE);
            holder.tv_discrip.setVisibility(View.GONE);
            holder.rcy.setVisibility(View.GONE);
        }
        if (list.get(position).getRead_status().equals("0"))//0表示未读，1表示已读
        {
            holder.img_read_status.setVisibility(View.VISIBLE);
        } else {
            holder.img_read_status.setVisibility(View.GONE);
        }
    }

    /**
     * 重新上传身份证
     */
    public void upLoadIDCard(Context mContext, String Order_code, String idcard) {
        Intent intent = new Intent(mContext, UploadIDCardActivity.class);
        intent.putExtra("order_code", Order_code);
        intent.putExtra("idcard", idcard);
        mContext.startActivity(intent);
    }

    /**
     * 修改地址
     */
    public void startChangeAddrActivity(String order_code, int position) {
//        Intent intent = new Intent(mContext, ChangeAdressActivity.class);
        Intent intent = new Intent(mContext, OrderDetailActivity.class);
        intent.putExtra("order_code", order_code);
//        intent.putExtra("MSG_position", position);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (list != null)
            size = list.size();
        return size;
    }

    public class MsgOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tv_text_title)
        TextView tv_text_title;
        @Bind(R.id.tv_create_time)
        TextView tv_create_time;
        @Bind(R.id.tv_order_num)
        TextView tv_order_num;
        @Bind(R.id.tv_recevice_addr_t)
        TextView tv_recevice_addr_t;
        @Bind(R.id.tv_recevice_addr)
        TextView tv_recevice_addr;
        @Bind(R.id.tv_error)
        TextView tv_error;
        @Bind(R.id.tv_function)
        TextView tv_function;
        @Bind(R.id.tv_discrip)
        TextView tv_discrip;

        @Bind(R.id.rcy)
        RecyclerView rcy;
        @Bind(R.id.img_read_status)
        ImageView img_read_status;
        int position = 0;

        public MsgOrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            EventBus.getDefault().register(this);
        }

        @Override
        public void onClick(View v) {

        }

//        @Subscriber(tag = "changeAddr")
//        public void changeAddr(HashMap map) {
//            if (map != null && position == map.get("MSG_position")) {
//                tv_recevice_addr.setText(map.get("changeAddr") + "");
//            }
//        }
    }
}
