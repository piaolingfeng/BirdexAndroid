package com.birdex.bird.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.InventoryActivityEntity;

/**
 * Created by huwei on 16/4/12.
 */
public class InventoryInnerDetailAdapter extends RecyclerView.Adapter<InventoryInnerDetailAdapter.ViewHolder>{
    private InventoryActivityEntity entity = null;
    private LayoutInflater inflater=null;
    public InventoryInnerDetailAdapter(Activity activity,InventoryActivityEntity entity){
        inflater=LayoutInflater.from(activity);
        this.entity=entity;
        Log.e("android","-------0");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.detail_inner_item_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InventoryActivityEntity.InventoryStockEntity stockEntity =entity.getStock().get(position);
        int availableCount = 0;
        int occupancyCount = 0;
        int incount=0;
        int outcount=0;
        int overdraftcount=0;
        int diffcount=0;
        int damagecount=0;
        int actualcount=0;
        //可用=总入库-总出库-损耗-盘亏-占用-丢失-过期-破损+盘盈+允许超收数量
        int in_stock =0,out_stock=0,spoilage_stock=0,shortage_stock=0,block_stock=0,lose_stock=0,expire_stock=0,damage_stock=0,overage_stock=0,overdraft_stock=0;
        if(stockEntity!=null){
            if(stockEntity.getDetail()!=null){
                for (InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity entity2 : stockEntity.getDetail()) {
                    //可用=总入库-总出库-损耗-盘亏-占用-丢失-过期-破损+盘盈+允许超收数量
                    if(entity2.getIn_stock()!=null){
                        in_stock = Integer.parseInt(entity2.getIn_stock());
                    }
                    if(entity2.getOut_stock()!=null){
                        out_stock = Integer.parseInt(entity2.getOut_stock());
                    }
                    if(entity2.getSpoilage_stock()!=null){
                        spoilage_stock = Integer.parseInt(entity2.getSpoilage_stock());
                    }
                    if(entity2.getShortage_stock()!=null){
                        shortage_stock = Integer.parseInt(entity2.getShortage_stock());
                    }
                    if(entity2.getBlock_stock()!=null){
                        block_stock = Integer.parseInt(entity2.getBlock_stock());
                    }
                    if(entity2.getLose_stock()!=null){
                        lose_stock = Integer.parseInt(entity2.getLose_stock());
                    }
                    if(entity2.getExpire_stock()!=null){
                        expire_stock = Integer.parseInt(entity2.getExpire_stock());
                    }
                    if(entity2.getDamage_stock()!=null){
                        damage_stock = Integer.parseInt(entity2.getDamage_stock());
                    }
                    if(entity2.getOverage_stock()!=null){
                        overage_stock = Integer.parseInt(entity2.getOverage_stock());
                    }
                    if(entity2.getOverdraft_stock()!=null){
                        overdraft_stock = Integer.parseInt(entity2.getOverdraft_stock());
                    }
                    availableCount += (in_stock - out_stock - spoilage_stock - shortage_stock - block_stock - lose_stock - expire_stock - damage_stock - overage_stock + overdraft_stock);
                    //占用=
                    if(entity2.getBlock_stock()!=null){
                        occupancyCount += Integer.parseInt(entity2.getBlock_stock());
                    }else{
                        occupancyCount += 0;
                    }
                    incount+=in_stock;
                    outcount+=out_stock;
                    overdraftcount+=overdraft_stock;
                    //差异=盘亏-盘盈
                    diffcount+=(shortage_stock-overage_stock);
                    damagecount+=damage_stock;
                    //实际=入库-出库
                    actualcount+=(in_stock-out_stock);
                }
                //仓库名
                holder.tv_name.setText(stockEntity.getWarehouse_name());
                //可用
                holder.tv_available.setText(String.valueOf("" + availableCount));
                //占用
                holder.tv_occupancy.setText(String.valueOf(""+occupancyCount));
                //总入库量
                holder.tv_in.setText(String.valueOf(""+incount));
                //总出库量
                holder.tv_out.setText(String.valueOf(""+outcount));
                //允许超售量
                holder.tv_allowout.setText(""+overdraftcount);
                //差异
                holder.tv_diff.setText(String.valueOf(""+diffcount));
                //损耗
                holder.tv_broken.setText(String.valueOf(""+damagecount));
                //实际数量
                holder.tv_actual.setText(String.valueOf(""+actualcount));
            }
        }

    }

    @Override
    public int getItemCount() {
        if(entity!=null){
            if(entity.getStock()!=null){
                return entity.getStock().size();
            }
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_available;
        TextView tv_occupancy;
        TextView tv_in;
        TextView tv_out;
        TextView tv_allowout;
        TextView tv_diff;
        TextView tv_broken;
        TextView tv_actual;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name=(TextView)itemView.findViewById(R.id.tv_detail_inner_name);
            tv_available=(TextView)itemView.findViewById(R.id.tv_detail_inner_available);
            tv_occupancy=(TextView)itemView.findViewById(R.id.tv_detail_inner_occupancy);
            tv_in=(TextView)itemView.findViewById(R.id.tv_detail_inner_in);
            tv_out=(TextView)itemView.findViewById(R.id.tv_detail_inner_out);
            tv_allowout=(TextView)itemView.findViewById(R.id.tv_detail_inner_allowout);
            tv_diff=(TextView)itemView.findViewById(R.id.tv_detail_inner_diff);
            tv_broken=(TextView)itemView.findViewById(R.id.tv_detail_inner_broken);
            tv_actual=(TextView)itemView.findViewById(R.id.tv_detail_inner_actual);
        }
    }
}
