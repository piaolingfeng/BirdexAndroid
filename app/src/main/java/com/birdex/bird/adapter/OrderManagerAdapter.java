package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.OrderManagerEntity;
import com.birdex.bird.util.recycleviewhelper.ItemTouchHelperAdapter;
import com.birdex.bird.util.recycleviewhelper.ItemTouchHelperViewHolder;
import com.birdex.bird.util.recycleviewhelper.OnStartDragListener;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.interfaces.OnRecyclerViewItemLongClickListener;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/3/22.
 */
public class OrderManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    Context mContext;


    List<OrderManagerEntity> orderList;
    OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener;
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    OnRecyclerViewItemClickListener onRecyclerViewAddItemClickListener;

    private final OnStartDragListener mDragStartListener;

    public void setOnRecyclerViewAddItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewAddItemClickListener) {
        this.onRecyclerViewAddItemClickListener = onRecyclerViewAddItemClickListener;
    }

    public static boolean longClickState = false;//长按状态

    public OrderManagerAdapter(Context mContext, List<OrderManagerEntity> orderList, OnStartDragListener dragStartListener) {
        this.mContext = mContext;
        this.orderList = orderList;
        mDragStartListener = dragStartListener;
    }

    public boolean isLongClickState() {
        return longClickState;
    }

    public void setLongClickState(boolean longClickState) {
        this.longClickState = longClickState;
    }

    public void setOrderList(List<OrderManagerEntity> orderList) {
        this.orderList = orderList;
    }

    public List<OrderManagerEntity> getOrderList() {
        return orderList;
    }

    public void setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener) {
        this.onRecyclerViewItemLongClickListener = onRecyclerViewItemLongClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (orderList.get(position) != null)
            return 1;
        else
            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_order_manager, null);
            return new OrderManagerHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.add_layout, null);
            return new AddHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (orderList.get(position) != null) {
            OrderManagerHolder orderManagerHolder = (OrderManagerHolder) holder;
            orderManagerHolder.order_item_name.setText(orderList.get(position).getName());
            orderManagerHolder.order_item_count.setText(orderList.get(position).getCount() + "");
            if (orderList.get(position).isDel_state()) {//长按删除状态
                orderManagerHolder.order_item_del.setVisibility(View.VISIBLE);
            } else {
                orderManagerHolder.order_item_del.setVisibility(View.INVISIBLE);
            }
//            if (position % 3 != 2) {//边沿数据不显示
//                orderManagerHolder.tv_right_line.setVisibility(View.VISIBLE);
//            }
//            int count = orderList.size() % 3;
//            if (count == 0)//为0表示最后要一行都不显示
//                count = 3;
//            if (position >= orderList.size() - count) {//判断最后一行的内容
//                orderManagerHolder.tv_bottom_line.setVisibility(View.INVISIBLE);
//            } else {
//                orderManagerHolder.tv_bottom_line.setVisibility(View.VISIBLE);
//            }
            orderManagerHolder.position = position;
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (longClickState) {
                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                            mDragStartListener.onStartDrag(holder);
                        }
                    }
                    return false;
                }
            });
        } else {
            AddHolder addHolder = (AddHolder) holder;
            addHolder.position = position;
        }
    }


    @Override
    public int getItemCount() {
        int size=0;
        if (orderList!=null)
            size = orderList.size();
        return size;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
//        Collections.swap(orderList, fromPosition, toPosition);
        OrderManagerEntity entityFrom = orderList.get(fromPosition);
        orderList.remove(fromPosition);
        orderList.add(toPosition, entityFrom);
//        EventBus.getDefault().post("", "save");//保存移动状态
//        JSONObject object = new JSONObject();
//        try {
//            for (int i=0;i<orderList.size()-1;i++){
//                orderList.get(i).getName();
//            }
//            object.put("orderlist", orderList.toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        OrderManagerEntity prev = orderList.remove(fromPosition);
//        orderList.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
//        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
//        orderList.remove(position);
//        notifyItemRemoved(position);
    }

    class OrderManagerHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, ItemTouchHelperViewHolder {

        @Bind(R.id.order_item_count)
        TextView order_item_count;
        @Bind(R.id.order_item_name)
        TextView order_item_name;
        @Bind(R.id.order_item_del)
        ImageView order_item_del;
        @Bind(R.id.tv_right_line)
        TextView tv_right_line;
        @Bind(R.id.tv_bottom_line)
        TextView tv_bottom_line;
        int position = 0;

        public OrderManagerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            order_item_del.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.order_item_del:
                    orderList.get(position).setChoose_state(false);
                    EventBus.getDefault().post(orderList.get(position), "entity");
                    break;
                default:
                    if (null != onRecyclerViewItemClickListener) {
                        onRecyclerViewItemClickListener.onItemClick(position);
                    }
                    break;
            }

        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewItemLongClickListener) {
                onRecyclerViewItemLongClickListener.onItemLongClick(position);
            }
            return false;
        }

        @Override
        public void onItemSelected() {
//            itemView.setBackgroundColor(R.color.background);
//            PercentRelativeLayout.LayoutParams params = new PercentRelativeLayout.LayoutParams(this);
//            itemView.setLayoutParams(new PercentRelativeLayout.LayoutParams());
        }

        @Override
        public void onItemClear() {
//            itemView.setBackgroundColor(0);
        }
    }

    class AddHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int position = 0;

        public AddHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onRecyclerViewAddItemClickListener != null)
                onRecyclerViewAddItemClickListener.onItemClick(position);
        }
    }
}
