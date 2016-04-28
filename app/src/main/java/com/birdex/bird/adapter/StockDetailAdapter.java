package com.birdex.bird.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.InventoryActivityEntity;
import com.birdex.bird.util.T;

import java.util.ArrayList;

/**
 * Created by huwei on 16/4/21.
 */
public class StockDetailAdapter extends RecyclerView.Adapter<StockDetailAdapter.ViewHolder>{
    private LayoutInflater inflater=null;
    private ArrayList<InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity.InventoryOrderEntity> list=null;
    private InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity.InventoryOrderEntity entity=null;
    private Context context=null;
    public StockDetailAdapter(Context context,ArrayList<InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity.InventoryOrderEntity> list){
        this.context=context;
        inflater=LayoutInflater.from(context);
        if(list==null){
            this.list=new ArrayList<>();
        }else{
            this.list=list;
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.stock_detail_item_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        entity=list.get(position);
        holder.itemView.setTag(position);
        if(TextUtils.isEmpty(entity.getTrackingNo())){
            holder.tv_batchcode.setText(R.string.stock_detail_item_none);
        }else{
            holder.tv_batchcode.setText(String.valueOf(entity.getTrackingNo()));
        }
        holder.tv_num.setText(String.valueOf(entity.getQuantity()));
        if(TextUtils.isEmpty(entity.getExpire())){
            holder.tv_expire.setText(R.string.stock_detail_item_none);
        }else{
            holder.tv_expire.setText(String.valueOf(entity.getExpire()));
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_batchcode;
        TextView tv_num;
        TextView tv_expire;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_batchcode=(TextView)itemView.findViewById(R.id.tv_stockdetail_item_batchcode);
            tv_num=(TextView)itemView.findViewById(R.id.tv_stockdetail_item_num);
            tv_expire=(TextView)itemView.findViewById(R.id.tv_stockdetail_item_expire);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=(Integer)v.getTag();
            StringBuilder builder=new StringBuilder();
            entity=list.get(position);
            if(entity!=null){
                if(entity.getTrackingNo()!=null){
                    builder.append(entity.getTrackingNo());
                }
                ClipboardManager clip = (ClipboardManager) StockDetailAdapter.this.context.getSystemService(Activity.CLIPBOARD_SERVICE);
                clip.setText(builder.toString());
                T.showShortByID(StockDetailAdapter.this.context,R.string.copy_to_clipboard);
            }
        }
    }
}
