package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.OrderManagerEntity;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.interfaces.OnRecyclerViewItemLongClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/3/22.
 */
public class ToolManagerAdapter extends RecyclerView.Adapter<ToolManagerAdapter.ToolHolder> {

    Context mContext;
    List<OrderManagerEntity> toolList;

    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public ToolManagerAdapter(Context mContext, List<OrderManagerEntity> toolList) {
        this.mContext = mContext;
        this.toolList = toolList;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (toolList != null)
            size = toolList.size();
        return size;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public ToolHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ToolHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tool_manager, null));
    }

    @Override
    public void onBindViewHolder(ToolHolder holder, int position) {
        holder.img_tool_item.setImageResource(toolList.get(position).getCount());
        holder.tv_tool_name.setText(toolList.get(position).getName());
//        if (position % 2 != 2 && position != toolList.size() - 1) {//最后一个数据跟边沿数据不显示
//            holder.tv_right_line.setVisibility(View.VISIBLE);
//        }
        int count = toolList.size() % 3;
        if (count == 0)//为0表示最后要一行都不显示
        {
            count = 3;
        }
        if (position >= toolList.size() - count) {//判断最后一行的内容
            holder.tv_bottom_line.setVisibility(View.INVISIBLE);
        } else {
            holder.tv_bottom_line.setVisibility(View.VISIBLE);
        }
        holder.position = position;
    }


    class ToolHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.img_tool_item)
        ImageView img_tool_item;
        @Bind(R.id.tv_tool_name)
        TextView tv_tool_name;
        @Bind(R.id.tv_right_line)
        TextView tv_right_line;
        @Bind(R.id.tv_bottom_line)
        TextView tv_bottom_line;
        int position;

        public ToolHolder(View itemView) {
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
