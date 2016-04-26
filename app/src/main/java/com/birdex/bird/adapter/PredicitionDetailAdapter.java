package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.activity.PredicitionDetailActivity;
import com.birdex.bird.entity.PredicitionDetailEntity;
import com.birdex.bird.interfaces.OnRecyclerViewInsClickListener;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/4/12.
 */
public class PredicitionDetailAdapter extends RecyclerView.Adapter<PredicitionDetailAdapter.PredicitionDetailHolder> {

    Context mContext;
    List<PredicitionDetailEntity.PredicitionData.PredicitionDetailProduct> productList;
    OnRecyclerViewInsClickListener onRecyclerViewInsClickListener;
    public int count = 0;//入库个数统计

    public OnRecyclerViewInsClickListener getOnRecyclerViewInsClickListener() {
        return onRecyclerViewInsClickListener;
    }

    public void setOnRecyclerViewInsClickListener(OnRecyclerViewInsClickListener onRecyclerViewInsClickListener) {
        this.onRecyclerViewInsClickListener = onRecyclerViewInsClickListener;
    }

    public PredicitionDetailAdapter(Context mContext, List<PredicitionDetailEntity.PredicitionData.PredicitionDetailProduct> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public PredicitionDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PredicitionDetailHolder(LayoutInflater.from(mContext).inflate(R.layout.item_predicition_detail_layout, null));
    }

    @Override
    public void onBindViewHolder(PredicitionDetailHolder holder, int position) {
        holder.position = position;
        if (position == 0) {
            count = 0;
        }
        holder.tv_product_code.setText(productList.get(position).getExternal_no());
        holder.tv_upc.setText(productList.get(position).getUpc());
        holder.tv_product_name.setText(productList.get(position).getName());
        holder.tv_pre_inno.setText(productList.get(position).getNums() + "/" + productList.get(position).getReal_nums());//预报/库存
        holder.tv_error_count.setText(productList.get(position).getDamaged_nums());
        holder.tv_remarks.setText(productList.get(position).getRemark().getMsg());
        holder.tv_status.setText(productList.get(position).getStatus_name());
        holder.tv_valid_date.setText(productList.get(position).getExpired_date());
        if (holder.tv_status.getText().toString().contains("待确认") || holder.tv_status.getText().toString().contains("待复核")
                || holder.tv_status.getText().toString().contains("已复核")) {
            if (holder.tv_status.getText().toString().contains("待复核")) {
                holder.btn_confirm.setVisibility(View.VISIBLE);
                holder.btn_re_confirm.setVisibility(View.INVISIBLE);
            } else {
                holder.btn_confirm.setVisibility(View.VISIBLE);
                holder.btn_re_confirm.setVisibility(View.VISIBLE);
            }
        } else {
            holder.btn_confirm.setVisibility(View.INVISIBLE);
            holder.btn_re_confirm.setVisibility(View.INVISIBLE);
        }
        if (productList.get(position).getStatus_name().equals("已入库")) {
            count++;
            if (count == productList.size()) {//统计已入库数量,来做fragment页面的UI刷新
                EventBus.getDefault().post(PredicitionDetailActivity.fragment_position, "confirm_fragment_adapter");//刷新fragment页面
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (productList != null)
            size = productList.size();
        return size;
    }

    public class PredicitionDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tv_product_code)
        TextView tv_product_code;
        @Bind(R.id.tv_upc)
        TextView tv_upc;
        @Bind(R.id.tv_product_name)
        TextView tv_product_name;
        @Bind(R.id.tv_pre_inno)
        TextView tv_pre_inno;
        @Bind(R.id.tv_error_count)
        TextView tv_error_count;
        @Bind(R.id.tv_valid_date)
        TextView tv_valid_date;
        @Bind(R.id.tv_remarks)
        TextView tv_remarks;
        @Bind(R.id.tv_status)
        TextView tv_status;

        @Bind(R.id.btn_confirm)
        Button btn_confirm;
        @Bind(R.id.btn_re_confirm)
        Button btn_re_confirm;

        int position = 0;

        public PredicitionDetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            EventBus.getDefault().register(this);
        }

        @OnClick({R.id.btn_re_confirm, R.id.btn_confirm})
        @Override
        public void onClick(View v) {
            if (onRecyclerViewInsClickListener != null) {
                switch (v.getId()) {
                    case R.id.btn_confirm:
                        onRecyclerViewInsClickListener.onItemConfirmClick(position);
                        break;
                    case R.id.btn_re_confirm:
                        onRecyclerViewInsClickListener.onItemReConfirmClick(position);
                        break;
                }
            }
        }

        @Subscriber(tag = "confirm")
        public void setBtn_confirm(int position) {
            if (this.position == position || position == -1) {//-1为刷新全部状态
                productList.get(this.position).setStatus_name("已入库");
                notifyDataSetChanged();
            }
        }

        @Subscriber(tag = "re_confirm")
        public void setBtn_re_confirm(int position) {
            if (this.position == position || position == -1) {//-1为刷新全部状态
                productList.get(position).setStatus_name("待复核");
                notifyDataSetChanged();
            }
        }


    }
}
