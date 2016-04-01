package com.birdex.bird.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.activity.AboutActivity;
import com.birdex.bird.activity.MyAccountInfoActivity;
import com.birdex.bird.entity.MineEntity;

import java.util.ArrayList;

/**
 * Created by huwei on 16/3/31.
 */
public class MineIndexAdapter extends RecyclerView.Adapter<MineIndexAdapter.MineViewHolder>{
    private ArrayList<MineEntity> list=null;
    private LayoutInflater inflater=null;
    private MineEntity entity=null;
    private Activity activity=null;
    public MineIndexAdapter(Activity activity,ArrayList<MineEntity> list){
        if(list==null){
            this.list=new ArrayList<>();
        }else{
            this.list=list;
        }
        this.activity=activity;
        inflater=LayoutInflater.from(activity);
    }
    @Override
    public MineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View containerView=inflater.inflate(R.layout.fragment_mine_item,parent,false);
        MineViewHolder holder=new MineViewHolder(containerView);
        return holder;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public void onBindViewHolder(MineViewHolder holder, int position) {
        entity=list.get(position);
        if(entity!=null){
            if(entity.getTitle()!=null){
                holder.tv_title.setText(entity.getTitle());
            }else{
                holder.tv_title.setText("");
            }
            if(entity.getShowType()== MineEntity.Type.Top){
                holder.v_line.setVisibility(View.VISIBLE);
                holder.ll_bg.setBackgroundResource(R.drawable.mine_item_top_selector);
                holder.ll_space.setVisibility(View.VISIBLE);
            }else if(entity.getShowType()== MineEntity.Type.Middle){
                holder.v_line.setVisibility(View.VISIBLE);
                holder.ll_bg.setBackgroundResource(R.drawable.mine_item_midd_selector);
                holder.ll_space.setVisibility(View.GONE);
            }else {
                holder.v_line.setVisibility(View.GONE);
                holder.ll_bg.setBackgroundResource(R.drawable.mine_item_bot_selector);
                holder.ll_space.setVisibility(View.GONE);
            }
            holder.ll_bg.setTag(position);
        }
        switch (position){
            case 0:
                holder.iv_icon.setImageResource(R.drawable.myaccount);
                break;
            case 1:
                holder.iv_icon.setImageResource(R.drawable.message);
                break;
            case 2:
                holder.iv_icon.setImageResource(R.drawable.mysetting);
                break;
            case 3:
                holder.iv_icon.setImageResource(R.drawable.managaccount);
                break;
            case 4:
                holder.iv_icon.setImageResource(R.drawable.mydata);
                break;
            case 5:
                holder.iv_icon.setImageResource(R.drawable.mycustomer);
                break;
            case 6:
                holder.iv_icon.setImageResource(R.drawable.about);
                break;
            default:

                break;
        }
    }

    public class MineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_title=null;
        LinearLayout ll_space=null;
        View v_line=null;
        LinearLayout ll_bg=null;
        ImageView iv_icon=null;
        public MineViewHolder(View itemView) {
            super(itemView);
            tv_title=(TextView)itemView.findViewById(R.id.tv_mine_item_title);
            ll_space=(LinearLayout)itemView.findViewById(R.id.ll_mine_item_space);
            v_line=itemView.findViewById(R.id.v_mine_item_line);
            ll_bg=(LinearLayout)itemView.findViewById(R.id.ll_mine_item_bg);
            ll_bg.setOnClickListener(this);
            iv_icon=(ImageView)itemView.findViewById(R.id.iv_mine_item_icon);
        }

        @Override
        public void onClick(View v) {
            int position=(int)v.getTag();
            Intent intent=null;
            switch (position){
                case 0:
                    //我的账号
                    intent=new Intent(MineIndexAdapter.this.activity, MyAccountInfoActivity.class);
                    MineIndexAdapter.this.activity.startActivity(intent);
                    break;
                case 1:
                    //我的订阅消息
                    break;
                case 2:
                    //我的设置
                    break;
                case 3:
                    //账户管理
                    break;
                case 4:
                    //我的数据
                    break;
                case 5:
                    //我的客户经理
                    break;
                case 6:
                    //关于
                    intent=new Intent(MineIndexAdapter.this.activity, AboutActivity.class);
                    MineIndexAdapter.this.activity.startActivity(intent);
                    break;
                default:

                    break;
            }
        }
    }
}
