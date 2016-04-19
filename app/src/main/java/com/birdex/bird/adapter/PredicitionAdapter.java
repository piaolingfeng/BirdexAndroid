package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.PredicitionEntity;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/4/8.
 */
public class PredicitionAdapter extends RecyclerView.Adapter<PredicitionAdapter.PredicitionHolder> {

    Context mContext;
    List<PredicitionEntity.Predicition.PredicitionDetail> predicitionDetailList = new ArrayList<>();
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public PredicitionAdapter(Context mContext, List<PredicitionEntity.Predicition.PredicitionDetail> predicitionDetailList) {
        this.mContext = mContext;
        this.predicitionDetailList = predicitionDetailList;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public List<PredicitionEntity.Predicition.PredicitionDetail> getPredicitionDetailList() {
        return predicitionDetailList;
    }

    public void setPredicitionDetailList(List<PredicitionEntity.Predicition.PredicitionDetail> predicitionDetailList) {
        this.predicitionDetailList = predicitionDetailList;
    }

    @Override
    public PredicitionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PredicitionHolder(LayoutInflater.from(mContext).inflate(R.layout.item_predicition_layout, null));
    }

    @Override
    public void onBindViewHolder(PredicitionHolder holder, int position) {
        holder.position = position;
        holder.tv_status.setText(predicitionDetailList.get(position).getStatus_name());
        holder.tv_storage_no.setText(predicitionDetailList.get(position).getStorage_no());
        holder.tv_warehouse_name.setText(predicitionDetailList.get(position).getWarehouse_name());
        if (position>15)
            EventBus.getDefault().post(true,"predicition_visible");
        else
            EventBus.getDefault().post(false,"predicition_visible");
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (predicitionDetailList != null)
            size = predicitionDetailList.size();
        return size;
    }


    public class PredicitionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tv_storage_no)
        TextView tv_storage_no;
        @Bind(R.id.tv_status)
        TextView tv_status;
        @Bind(R.id.tv_warehouse_name)
        TextView tv_warehouse_name;
        int position = 0;

        public PredicitionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            EventBus.getDefault().register(this);
        }

        @Override
        public void onClick(View v) {
            if (onRecyclerViewItemClickListener != null)
                onRecyclerViewItemClickListener.onItemClick(position);
        }

        @Subscriber(tag = "confirm_fragment_adapter")
        public void confirmPredicition(int position) {
            if (this.position == position) {
//                tv_status.setText("已入库");
//                predicitionDetailList.get(position).setStatus_name("已入库");
                predicitionDetailList.remove(position);
                notifyDataSetChanged();
            }
        }
    }
}
