package com.birdex.bird.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.activity.WillinDetailActivity;
import com.birdex.bird.entity.InventoryActivityEntity;
import com.birdex.bird.util.recycleviewhelper.OnLoadingImgListener;
import com.birdex.bird.util.recycleviewhelper.OnShowGoTopListener;

import java.util.ArrayList;

/**
 * Created by huwei on 16/4/7.
 */
public class InventoryWillInAdapter extends RecyclerView.Adapter<InventoryWillInAdapter.ViewHolder> {
    private LayoutInflater inflater = null;
    private Activity activity = null;
    private ArrayList<InventoryActivityEntity> list = null;
    private InventoryActivityEntity entity = null;
    //至顶
    private OnShowGoTopListener toplistener=null;
    //加载动画
    private OnLoadingImgListener imglistener=null;
    private LinearLayout.LayoutParams params=null;
    public InventoryWillInAdapter(Activity activity, ArrayList<InventoryActivityEntity> list) {
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
        params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,0,10,0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = inflater.inflate(R.layout.inventory_willin_item_layout, parent, false);
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
            holder.tv_time.setText("");
            holder.tv_name.setText(entity.getName());
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
            int count=0;
            holder.arl_inventory.removeAllViews();
            int start=0,end=0;
            SpannableStringBuilder ss=new SpannableStringBuilder();
            for (InventoryActivityEntity.InventoryStockEntity entity1 : entity.getStock()) {
                if(entity1.getDetail()!=null){
                    for (InventoryActivityEntity.InventoryStockEntity.InventoryDetailEntity entity2 : entity1.getDetail()) {
                        count+=Integer.parseInt(entity2.getStock());
                        String name= TextUtils.isEmpty(entity1.getWarehouse_name())?this.activity.getString(R.string.inventory_noname):entity1.getWarehouse_name() +":";
                        String value=entity2.getStock()+" ";
                        ss.append(name);
                        ss.append(value);
                        //-------------
                        end+=(name).length();
                        ss.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.inventory_item_gray)),start,end,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        start+=name.length();
                        end+=value.length();
                        ss.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.inventory_item_blue)),start,end,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        start+=value.length();
                    }
                }
            }
//            View view=inflater.inflate(R.layout.inventory_item_willinnum_layout,null,false);
//            TextView tv_willin_name=(TextView)view.findViewById(R.id.tv_inventory_willin_name);
//            TextView tv_willin_num=(TextView)view.findViewById(R.id.tv_inventory_willin_num);
//            tv_willin_name.setText(ss);
//            holder.arl_inventory.addView(view,params);
            holder.tv_inventoryinfo.setText(ss);
            holder.tv_willin_count.setText(String.valueOf(count));
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //商品编码
        TextView tv_upc = null;
        //时间
        TextView tv_time = null;
        //商品图片
//        ImageView iv_pic = null;
        //商品名称
        TextView tv_name = null;
        //待入库数
        TextView tv_willin_count = null;
        //
        LinearLayout arl_inventory=null;
        //
        TextView tv_inventoryinfo=null;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_upc = (TextView) itemView.findViewById(R.id.tv_inventory_item_upc);
            tv_time = (TextView) itemView.findViewById(R.id.tv_inventory_item_time);
//            iv_pic = (ImageView) itemView.findViewById(R.id.iv_inventory_item_pic);
            tv_name = (TextView) itemView.findViewById(R.id.tv_inventory_item_name);
            tv_willin_count = (TextView) itemView.findViewById(R.id.tv_inventory_willin_count);
            arl_inventory=(LinearLayout)itemView.findViewById(R.id.arl_inventory_number);
            tv_inventoryinfo=(TextView)itemView.findViewById(R.id.tv_inventory_willin_inventoryinfo);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position=(Integer)(v.getTag());
            entity=list.get(position);
            if(entity!=null){
                Intent intent=new Intent(activity, WillinDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("inventory_willin", entity);
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