package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.OrderListProductEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/4/15.
 * 消息-3种订单内部的产品list
 */
public class MsgOrderProductAdapter extends RecyclerView.Adapter<MsgOrderProductAdapter.MsgOrderProductHolder> {
    List<OrderListProductEntity> productList;
    Context mContext;

    public MsgOrderProductAdapter(Context mContext, List<OrderListProductEntity> productList) {
        this.productList = productList;
        this.mContext = mContext;
    }

    @Override
    public MsgOrderProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MsgOrderProductHolder(LayoutInflater.from(mContext).inflate(R.layout.item_msg_inven_item_layout, null));
    }

    @Override
    public void onBindViewHolder(MsgOrderProductHolder holder, int position) {
        holder.position = position;
        holder.tv_context.setText(productList.get(position).getName());
        holder.tv_product_code.setText(productList.get(position).getExternal_no());
        holder.tv_count.setText("x" + productList.get(position).getNums());
        holder.tv_inven_error.setText(productList.get(position).getError());
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (productList != null)
            size = productList.size();
        return size;
    }

    public class MsgOrderProductHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_context)
        TextView tv_context;
        @Bind(R.id.tv_product_code)
        TextView tv_product_code;
        @Bind(R.id.tv_count)
        TextView tv_count;
        @Bind(R.id.tv_inven_error)
        TextView tv_inven_error;
        int position = 0;

        public MsgOrderProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
