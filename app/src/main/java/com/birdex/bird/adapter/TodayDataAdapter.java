package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.OrderManagerEntity;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.rey.material.widget.Switch;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/3/29.
 */
public class TodayDataAdapter extends RecyclerView.Adapter<TodayDataAdapter.TodayDataHolder> {

    List<OrderManagerEntity> list;
    private Context mContext;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public void setList(List<OrderManagerEntity> list) {
        this.list = list;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public TodayDataAdapter(Context mContext, List<OrderManagerEntity> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public TodayDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_todaydata_layout, null);
        return new TodayDataHolder(view);
    }

    @Override
    public void onBindViewHolder(TodayDataHolder holder, int position) {
        holder.position = position;//check的时候会用到holder的position，所以需要
        holder.tv_name.setText(list.get(position).getName());
        holder.com_switch.setChecked(list.get(position).isChoose_state());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TodayDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.com_switch)
        Switch com_switch;
        @Bind(R.id.tv_name)
        TextView tv_name;
        int position = 0 ;

        public TodayDataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            com_switch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(Switch view, boolean checked) {
                    list.get(position).setChoose_state(checked);
//                    notifyDataSetChanged();
                    EventBus.getDefault().post(list.get(position), "entity");
                }
            });
        }

        @Override
        public void onClick(View v) {
                com_switch.setChecked(!com_switch.isChecked());
//            list.get(position).setChoose_state(!com_switch.isChecked());
//                    notifyDataSetChanged();
//            EventBus.getDefault().post(list.get(position), "entity");
//                onRecyclerViewItemClickListener.onItemClick(position);
        }
    }
}
