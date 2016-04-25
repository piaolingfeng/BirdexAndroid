package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.WarehouseEntity;
import com.birdex.bird.greendao.warehouse;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/4/7.
 */
public class OrderWareHouseAdapter extends RecyclerView.Adapter<OrderWareHouseAdapter.WareHouseViewHolder> {
    Context mContext;
    List<warehouse> WarehouseList;
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public void setList(List<warehouse> WarehouseList) {
        this.WarehouseList = WarehouseList;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public OrderWareHouseAdapter(Context mContext, List<warehouse> WarehouseList) {
        this.mContext = mContext;
        this.WarehouseList = WarehouseList;
    }

    @Override
    public WareHouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WareHouseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tablayout, null));
    }

    @Override
    public void onBindViewHolder(WareHouseViewHolder holder, int position) {
        holder.position = position;
        holder.tv_context.setText(WarehouseList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (WarehouseList != null) {
            size = WarehouseList.size();
        }
        return size;
    }

    public class WareHouseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int position = 0;
        @Bind(R.id.tv_context)
        TextView tv_context;

        public WareHouseViewHolder(View itemView) {
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

    /*
    *修改数据源
    */
    public void setDataSource(List<warehouse> WarehouseList) {
        if (WarehouseList != null) {
            this.WarehouseList = WarehouseList;
            notifyDataSetChanged();
        }
    }
}
