package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.birdex.bird.R;
import com.birdex.bird.entity.Wallet;
import com.rey.material.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by birdex on 16/4/27.
 */
public class RechargeAccountAdapter extends RecyclerView.Adapter<RechargeAccountAdapter.ViewHolder>{
    private List<Wallet> list=null;
    private LayoutInflater inflater=null;
    //获取选中的对象
    private Wallet selectWallet=null;
    public RechargeAccountAdapter(Context context,List<Wallet> list){
        this.inflater=LayoutInflater.from(context);
        if(list!=null){
            this.list=list;
        }else {
            this.list=new ArrayList<>();
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.recharge_item_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wallet wallet=this.list.get(position);
        holder.rb_account.setTag(position);
        if(wallet!=null){
            if(wallet.getName()==null){
                holder.rb_account.setText(R.string.recharge_account_unkonw);
            }else {
                holder.rb_account.setText(wallet.getName());
            }
            holder.rb_account.setChecked(wallet.isCheck());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RadioButton rb_account=null;
        public ViewHolder(View itemView) {
            super(itemView);
            rb_account=(RadioButton)itemView.findViewById(R.id.rb_recharge_account);
            rb_account.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position=(Integer)v.getTag();
            for(Wallet wallet:RechargeAccountAdapter.this.list){
                wallet.setCheck(false);
            }
            selectWallet=RechargeAccountAdapter.this.list.get(position);
            selectWallet.setCheck(true);
            notifyDataSetChanged();
        }
    }
    public void setDataSource(List<Wallet> list){
        if(list!=null){
            if(list.size()>0){
                this.list=list;
                notifyDataSetChanged();
            }
        }
    }
    public Wallet getSelectWallet(){
        return selectWallet;
    }
}
