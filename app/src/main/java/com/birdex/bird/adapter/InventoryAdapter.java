package com.birdex.bird.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.activity.InventoryActivity;
import com.birdex.bird.entity.InventoryEntity;

import java.util.ArrayList;

/**
 * Created by huwei on 16/4/5.
 */
public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder>{
    private LayoutInflater inflater=null;
    private Activity activity=null;
    private ArrayList<InventoryEntity> list=null;
    //设置显示的类型
    public enum Type {
        //在库，待入库
        Inner,Willin
    }
    private Type type=Type.Inner;
    private InventoryEntity entity=null;
    public InventoryAdapter(Activity activity,ArrayList<InventoryEntity> list,Type type){
        inflater=LayoutInflater.from(activity);
        this.activity=activity;
        if(list==null){
            this.list=new ArrayList<>();
        }else{
            this.list=list;
        }
        if(type!=null){
            this.type=type;
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if(type==Type.Inner){
            view=inflater.inflate(R.layout.inventory_inner_item_layout,parent,false);
        }else if(type==Type.Willin){
            view=inflater.inflate(R.layout.inventory_inner_item_layout,parent,false);
        }else {
            return null;
        }
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        entity=list.get(position);
        if(entity!=null){
            holder.tv_upc.setText(entity.getUpc());
            holder.tv_time.setText("");
        }
        if(RecyclerView.SCROLL_STATE_SETTLING==((InventoryActivity)activity).rv_inventory.getScrollState()){
            Log.e("android","fling");
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

   public class ViewHolder extends RecyclerView.ViewHolder{
        //商品编码
        TextView tv_upc=null;
        //时间
        TextView tv_time=null;
        //商品图片
        ImageView iv_pic=null;
        //商品名称
        TextView tv_name=null;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_upc=(TextView)itemView.findViewById(R.id.tv_inventory_item_upc);
            tv_time=(TextView)itemView.findViewById(R.id.tv_inventory_item_time);
        }
    }
    //设置数据源
    public void setDataSource(ArrayList<InventoryEntity> source){
        if(source!=null){
            //刷新加载的数据
            this.list=source;
            notifyDataSetChanged();
        }
    }
    public void addDataSource(ArrayList<InventoryEntity> source) {
        if (source!=null) {
            if(source.size()>0){
                //记录上一次最后一条的记录
                int position=getItemCount()-1;
                this.list.addAll(source);
                //只刷新最后的位置
                notifyItemChanged(position);
            }
        }
    }
}
