package com.birdex.bird.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.birdex.bird.R;
import com.birdex.bird.activity.InventoryActivity;
import com.birdex.bird.activity.InventoryInnerDetailActivity;
import com.birdex.bird.activity.StockDetailActivity;
import com.birdex.bird.entity.InventoryActivityEntity;
import com.birdex.bird.util.T;
import com.birdex.bird.util.recycleviewhelper.OnLoadingImgListener;
import com.birdex.bird.util.recycleviewhelper.OnShowGoTopListener;

import java.util.ArrayList;

/**
 * Created by huwei on 16/4/5.
 */
public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private LayoutInflater inflater = null;
    private Activity activity = null;
    private ArrayList<InventoryActivityEntity> list = null;
    private InventoryActivityEntity entity = null;
    //至顶
    private OnShowGoTopListener toplistener=null;
    //加载动画
    private OnLoadingImgListener imglistener=null;
    public InventoryAdapter(Activity activity, ArrayList<InventoryActivityEntity> list) {
        inflater = LayoutInflater.from(activity);
        this.activity = activity;
        if (list == null) {
            this.list = new ArrayList<>();
        } else {
            this.list = list;
        }
        if(toplistener!=null){
            toplistener.onShowFloatAtionButton(false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = inflater.inflate(R.layout.inventory_inner_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        entity = list.get(position);
        //设置点击事件的tag
        holder.itemView.setTag(position);
        if (entity != null) {
            holder.tv_upc.setText(entity.getUpc());
            holder.tv_name.setText(entity.getName());
//            int availableCount = 0;
//            int occupancyCount = 0;
            holder.ll_contain.removeAllViews();
            if(entity.getStock()!=null&&entity.getStock().size()>0){
                for (int j=0;j<entity.getStock().size();j++) {
                    InventoryActivityEntity.InventoryStockEntity entity1=entity.getStock().get(j);
//                for (InventoryActivityEntity.InventoryStockEntity entity1 : entity.getStock()) {
                    //每个仓库具体信息
                    int avail=0;
                    int occupancy=0;
                    int actual=0;
                    if(entity1.getDetail()!=null){
                        for (InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity entity2 : entity1.getDetail()) {
                            //可用=总入库-总出库-损耗-盘亏-占用-丢失-过期-破损+盘盈+允许超收数量
                            int in_stock =0,out_stock=0,spoilage_stock=0,shortage_stock=0,block_stock=0,lose_stock=0,expire_stock=0,damage_stock=0,overage_stock=0,overdraft_stock=0,stock=0;
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
                            if(entity2.getStock()!= null){
                                stock=Integer.parseInt(entity2.getStock());
                            }
//                            availableCount += (in_stock - out_stock - spoilage_stock - shortage_stock - block_stock - lose_stock - expire_stock - damage_stock - overage_stock + overdraft_stock);
//                            //占用=
//                            if(entity2.getBlock_stock()!=null){
//                                occupancyCount += Integer.parseInt(entity2.getBlock_stock());
//                            }else{
//                                occupancyCount += 0;
//                            }
                            avail+= (in_stock - out_stock - spoilage_stock - shortage_stock - block_stock - lose_stock - expire_stock - damage_stock - overage_stock + overdraft_stock);
                            occupancy+=block_stock;
                            actual+=stock;
                        }
                    }
                    View view=inflater.inflate(R.layout.inventory_item_contain_layout,null,false);
                    TextView tv_name=(TextView)view.findViewById(R.id.tv_inner_item_contain_name);
                    TextView tv_avail=(TextView)view.findViewById(R.id.tv_inner_item_contain_available);
                    TextView tv_occu=(TextView)view.findViewById(R.id.tv_inner_item_contain_occu);
                    TextView tv_actual=(TextView)view.findViewById(R.id.tv_inner_item_contain_actual);
                    View tv_line=view.findViewById(R.id.iv_stockdetail_line);
                    if(entity1.getWarehouse_name()!=null&&!TextUtils.isEmpty(entity1.getWarehouse_name().trim())){
                        tv_name.setText(entity1.getWarehouse_name());
                    }else{
                        tv_name.setText(R.string.inventory_noname);
                    }
                    tv_avail.setText(String.valueOf(avail));
                    tv_occu.setText(String.valueOf(occupancy));
                    tv_actual.setText(String.valueOf(actual));
//                    view.setPadding(0,10,0,10);
                    if(j!=entity.getStock().size()-1){
                        tv_line.setVisibility(View.VISIBLE);
                    }
                    if(entity1.getDetail()!=null){
                        tv_actual.setTag(entity1);
                        tv_actual.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InventoryActivityEntity.InventoryStockEntity stockEntity=(InventoryActivityEntity.InventoryStockEntity)v.getTag();
                                if(stockEntity==null){
                                    return;
                                }
                                Intent intent = new Intent(activity, StockDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("stocklist", stockEntity.getDetail());
                                intent.putExtras(bundle);
                                activity.startActivity(intent);
                            }
                        });
                    }else {
                        tv_actual.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                T.showShortByID(activity,R.string.stock_detail_item_tip1);
                            }
                        });
                    }
                    holder.ll_contain.addView(view);
                }
            }else{
                View view=inflater.inflate(R.layout.inventory_item_contain_layout,null,false);
                TextView tv_name=(TextView)view.findViewById(R.id.tv_inner_item_contain_name);
                TextView tv_avail=(TextView)view.findViewById(R.id.tv_inner_item_contain_available);
                TextView tv_occu=(TextView)view.findViewById(R.id.tv_inner_item_contain_occu);
                TextView tv_actual=(TextView)view.findViewById(R.id.tv_inner_item_contain_actual);
                tv_name.setText(R.string.inventory_item_none);
                tv_avail.setText("0");
                tv_occu.setText("0");
                tv_actual.setText("0");
                tv_actual.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.showShortByID(activity,R.string.stock_detail_item_tip1);
                    }
                });
//                view.setPadding(0, 10, 0, 10);
                holder.ll_contain.addView(view);
            }


//            holder.tv_available.setText(String.valueOf(availableCount));
//            holder.tv_occupancy.setText(String.valueOf(occupancyCount));
//            if(this.imglistener!=null){
//                if(entity.getPic()!=null&&!"".equals(entity.getPic())){
//                    this.imglistener.onLoadImage(entity.getPic(),holder.iv_pic);
//                }
//            }
            if(position<=8&&toplistener!=null){
                toplistener.onShowFloatAtionButton(false);
            }else if(position>8&&toplistener!=null){
                toplistener.onShowFloatAtionButton(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //商品编码
        TextView tv_upc = null;
//        //时间
//        TextView tv_time = null;
//        //商品图片
//        ImageView iv_pic = null;
        //商品名称
        TextView tv_name = null;
//        //可用
//        TextView tv_available = null;
//        //占用
//        TextView tv_occupancy = null;
        LinearLayout ll_contain=null;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_upc = (TextView) itemView.findViewById(R.id.tv_inventory_item_upc);
//            tv_time = (TextView) itemView.findViewById(R.id.tv_inventory_item_time);
//            iv_pic = (ImageView) itemView.findViewById(R.id.iv_inventory_item_pic);
            tv_name = (TextView) itemView.findViewById(R.id.tv_inventory_item_name);
            ll_contain = (LinearLayout) itemView.findViewById(R.id.ll_inventory_item_contain);
//            tv_available = (TextView) itemView.findViewById(R.id.tv_inventory_item_available);
//            tv_occupancy = (TextView) itemView.findViewById(R.id.tv_inventory_item_occupancy);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=(Integer)(v.getTag());
            entity=list.get(position);
            if(entity!=null){
                Intent intent=new Intent(activity, InventoryInnerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("inventory_inner", entity);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        }
    }

    //设置数据源
    public void setDataSource(ArrayList<InventoryActivityEntity> source) {
        if (source != null) {
            //刷新加载的数据
            this.list = source;
            notifyDataSetChanged();
        }
    }

    public void addDataSource(ArrayList<InventoryActivityEntity> source) {
        if (source != null) {
            if (source.size() > 0) {
                //记录上一次最后一条的记录
                int position = getItemCount() - 1;
                this.list.addAll(source);
                //只刷新最后的位置
                notifyItemChanged(position);
            }
        }
    }
    public void clearDataSource(){
        if(this.list.size()>0){
            this.list.clear();
            notifyDataSetChanged();
        }
    }
    //设置至顶
    public void setOnGoTopListener(OnShowGoTopListener listener){
        if(listener!=null){
            this.toplistener=listener;
        }
    }
    //设置加载动画
    public void setOnLoadingImgListener(OnLoadingImgListener listener){
        if(listener!=null){
            this.imglistener=listener;
        }
    }
}
