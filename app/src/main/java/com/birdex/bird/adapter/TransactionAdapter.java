package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.TransactionEntity;

import java.util.ArrayList;

/**
 * Created by huwei on 16/3/25.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder>{
    private ArrayList<TransactionEntity> list=null;
    private LayoutInflater inflater=null;
    private  Context context=null;
    public TransactionAdapter(Context context){
        list=new ArrayList<>();
        this.context=context;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(this.inflater==null){
            this.inflater=LayoutInflater.from(context);
        }
        View view=this.inflater.inflate(R.layout.fragment_personal_details_item, null);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TransactionEntity entity=list.get(position);
        if(entity.getOrder_code()!=null){
            if("".equals(entity.getOrder_code().trim())){
                holder.tv_code.setText(R.string.cost_miss_code);
            }else{
                holder.tv_code.setText(entity.getOrder_code());
            }
        }else{
            holder.tv_code.setText(R.string.cost_miss_code);
        }
        if(entity.getTransaction_amountIn()==0){
            //充值为0 则为支出
            holder.tv_account.setText(String.valueOf(entity.getTransaction_amountOut()));
        }else{
            holder.tv_account.setText(String.valueOf(entity.getTransaction_amountIn()));
        }
        if(entity.getRemarks()!=null){
            holder.tv_remark.setText(entity.getRemarks());
        }else{
            holder.tv_remark.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_code;
        private TextView tv_account;
        private TextView tv_remark;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_code=(TextView)itemView.findViewById(R.id.tv_personal_detail_item_orderid);
            tv_account=(TextView)itemView.findViewById(R.id.tv_personal_detail_item_ordernum);
            tv_remark=(TextView)itemView.findViewById(R.id.tv_personal_detail_item_ordercontent);
        }
    }
    /*
    * 设置数据源
    *
    */
    public void setDataSource(ArrayList<TransactionEntity> data){
        if(data!=null){
            if(data.size()>0){
                this.list.clear();
                this.list.addAll(data);
                notifyDataSetChanged();
            }else{
                //当源列表有数据，新列表没数据，刷新recycleview清空显示记录
                if(this.list.size()>0){
                    this.list.clear();
                    notifyDataSetChanged();
                }
            }
        }
    }
    /*
    * 添加数据源
    *
    */
    public void addDataSource(ArrayList<TransactionEntity> data){
        if(data!=null){
            if(data.size()>0){
                int position=data.size()-1;
                this.list.addAll(data);
//                notifyItemChanged(position);
                notifyDataSetChanged();
            }
        }
    }
}
