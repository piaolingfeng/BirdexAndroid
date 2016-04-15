package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.OrderListEntity;
import com.birdex.bird.entity.OrderStatus;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/4/7.
 */
public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.StatusViewHolder> {
    Context mContext;
    List<OrderStatus.Status> statusList = new ArrayList<>();
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public void setList(List<OrderStatus.Status> statusList) {
        this.statusList = statusList;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public OrderStatusAdapter(Context mContext, List<OrderStatus.Status> statusList) {
        this.mContext = mContext;
        this.statusList = statusList;
    }


    @Override
    public StatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StatusViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tablayout, null));
    }

    @Override
    public void onBindViewHolder(StatusViewHolder holder, int position) {
        holder.position = position;
        holder.tv_context.setText(statusList.get(position).getStatus_name());
    }


    @Override
    public int getItemCount() {
        int size=0;
        if (statusList!=null)
            size = statusList.size();
        return size;
    }


    public class StatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int position = 0;
        @Bind(R.id.tv_context)
        TextView tv_context;

        public StatusViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (onRecyclerViewItemClickListener != null) {
                onRecyclerViewItemClickListener.onItemClick(position);
            }
        }
    }
}
