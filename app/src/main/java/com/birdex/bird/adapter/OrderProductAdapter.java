package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.OrderListProductEntity;
import com.birdex.bird.glide.GlideUtils;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/4/6.
 */
public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ProductHold> {


    List<OrderListProductEntity> productList;
    Context mContext;


    public OrderProductAdapter(Context mContext, List<OrderListProductEntity> productList) {
        this.productList = productList;
        this.mContext = mContext;
    }

    @Override
    public ProductHold onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductHold(LayoutInflater.from(mContext).inflate(R.layout.item_orderlist_product_layout, null));
    }

    @Override
    public void onBindViewHolder(ProductHold holder, int position) {
        holder.position = position;
        GlideUtils.setImageToLocalPath(holder.img_pic, productList.get(position).getPic());
        holder.tv_nums.setText("x" + productList.get(position).getNums());
        holder.tv_name.setText(productList.get(position).getName());
        holder.tv_external_no.setText(productList.get(position).getExternal_no());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductHold extends RecyclerView.ViewHolder implements View.OnClickListener {
        //产品
        @Bind(R.id.img_pic)
        ImageView img_pic;//商品图片
        @Bind(R.id.tv_nums)
        TextView tv_nums;//数量
        @Bind(R.id.tv_name)
        TextView tv_name;//商品名称
        @Bind(R.id.tv_external_no)
        TextView tv_external_no;
        int position;

        public ProductHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
