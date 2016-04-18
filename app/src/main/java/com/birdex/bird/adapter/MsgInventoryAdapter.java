package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.InventoryActivityEntity;
import com.birdex.bird.entity.InventorySimpleEntity;
import com.birdex.bird.entity.MsgListEntity;
import com.birdex.bird.util.ClipboardManagerUtil;
import com.birdex.bird.util.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/4/14.
 */
public class MsgInventoryAdapter extends RecyclerView.Adapter<MsgInventoryAdapter.MsgInventoryHolder> {
    private List<MsgListEntity.MsgList.MsgListMessages> list = new ArrayList<>();
    Context mContext;

    public MsgInventoryAdapter(Context mContext, List<MsgListEntity.MsgList.MsgListMessages> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public MsgInventoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MsgInventoryHolder(LayoutInflater.from(mContext).inflate(R.layout.item_msg_inve_tension_layout, null));
    }

    public List<MsgListEntity.MsgList.MsgListMessages> getList() {
        return list;
    }

    public void setList(List<MsgListEntity.MsgList.MsgListMessages> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(MsgInventoryHolder holder, int position) {
        holder.position = position;
        holder.tv_create_time.setText(list.get(position).getUpdated_date());
        holder.tv_product_code.setText(list.get(position).getMsg_content().getOrder_oms_no());

//        holder.tv_name.setText(list.get(position).getMsg_content().get);
//        holder.tv_error.setText("可用库存为:" + list.get(position).getAvailable_stock());
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (list != null)
            size = list.size();
        return size;
    }

    public class MsgInventoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tv_create_time)
        TextView tv_create_time;
        @Bind(R.id.tv_product_code)
        TextView tv_product_code;
        @Bind(R.id.tv_copy)
        ImageView tv_copy;
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_error)
        TextView tv_error;
        int position = 0;

        public MsgInventoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_copy.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ClipboardManagerUtil.copy(list.get(position).getMsg_content().getOrder_oms_no().toString(), mContext);
            T.showShort(mContext,mContext.getString(R.string.copy_tip));
        }
    }
}
