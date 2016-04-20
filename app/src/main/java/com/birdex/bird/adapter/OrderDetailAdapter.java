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
 * Created by chuming.zhuang on 2016/4/12.
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailHolder> {

    List<OrderListProductEntity> productList;
    Context mContext;

    public OrderDetailAdapter(Context mContext, List<OrderListProductEntity> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }


    @Override
    public OrderDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderDetailHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order_detail_layout, null));
    }

    @Override
    public void onBindViewHolder(OrderDetailHolder holder, int position) {
        holder.tv_count.setText(productList.get(position).getNums());
        holder.tv_product_code.setText(productList.get(position).getExternal_no());
        holder.tv_product_name.setText(productList.get(position).getName());
        holder.tv_upc_code.setText(productList.get(position).getUpc());
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (productList != null)
            size = productList.size();
        return size;
    }

    public class OrderDetailHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_upc_code)
        TextView tv_upc_code;

        @Bind(R.id.tv_product_code)
        TextView tv_product_code;

        @Bind(R.id.tv_product_name)
        TextView tv_product_name;

        @Bind(R.id.tv_count)
        TextView tv_count;

        public OrderDetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
