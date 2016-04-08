package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.TimeSelectEntity;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/4/8.
 */
public class OrderTimeAdapter extends RecyclerView.Adapter<OrderTimeAdapter.TimeHolder>{

    Context mContext;
    List<TimeSelectEntity> timeList;
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

   public  OrderTimeAdapter( Context mContext, List<TimeSelectEntity> timeList){
        this.mContext = mContext;
        this.timeList = timeList;
    }

    public List<TimeSelectEntity> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<TimeSelectEntity> timeList) {
        this.timeList = timeList;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public TimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TimeHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tablayout,null));
    }

    @Override
    public void onBindViewHolder(TimeHolder holder, int position) {
        holder.position = position;
        holder.tv_context.setText(timeList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (timeList !=null)
            size = timeList.size();
        return size;
    }

    public class TimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        int position = 0;
        @Bind(R.id.tv_context)
        TextView tv_context;

        public TimeHolder(View itemView) {
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
