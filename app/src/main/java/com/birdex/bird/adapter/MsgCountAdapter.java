package com.birdex.bird.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.MsgListEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/4/19.
 */
public class MsgCountAdapter extends RecyclerView.Adapter<MsgCountAdapter.MsgCountHolder> {

    private List<MsgListEntity.MsgList.MsgListMessages> list = new ArrayList<>();
    Context mContext;

    public MsgCountAdapter(Context mContext, List<MsgListEntity.MsgList.MsgListMessages> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public List<MsgListEntity.MsgList.MsgListMessages> getList() {
        return list;
    }

    public void setList(List<MsgListEntity.MsgList.MsgListMessages> list) {
        this.list = list;
    }

    @Override
    public MsgCountHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MsgCountHolder(LayoutInflater.from(mContext).inflate(R.layout.item_msg_count_layout, null));
    }

    @Override
    public void onBindViewHolder(MsgCountHolder holder, int position) {
        holder.position = position;
        holder.tv_create_time.setText(list.get(position).getUpdated_date());
//        SharedPreferences sp = mContext.getSharedPreferences("login", Activity.MODE_PRIVATE);
//        sp.getString("company_name", "");
        holder.tv_context.setText(mContext.getString(R.string.msg_count_1) + list.get(position).getMsg_content().getCompany_name() +
                mContext.getString(R.string.msg_count_2)+list.get(position).getMsg_content().getOrder_count() + mContext.getString(R.string.msg_count_3) +
                list.get(position).getMsg_content().getCost() + mContext.getString(R.string.msg_count_4) +
                list.get(position).getMsg_content().getWallet() + mContext.getString(R.string.msg_count_5));
        if (list.get(position).getRead_status().equals("0"))//0表示未读，1表示已读
        {
            holder.img_read_status.setVisibility(View.VISIBLE);
        } else {
            holder.img_read_status.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (list != null)
            size = list.size();
        return size;
    }

    public class MsgCountHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img_read_status)
        ImageView img_read_status;
        @Bind(R.id.tv_create_time)
        TextView tv_create_time;
        @Bind(R.id.tv_context)
        TextView tv_context;
        @Bind(R.id.btn_confirm)
        Button btn_confirm;
        int position = 0;

        public MsgCountHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
