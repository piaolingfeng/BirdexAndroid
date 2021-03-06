package com.birdex.bird.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.PredicitionDetailEntity;
import com.birdex.bird.fragment.IndexFragment;
import com.birdex.bird.util.T;
import com.loopj.android.http.HttpPatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huwei on 16/4/18.
 */
public class SameOrderAdapter extends RecyclerView.Adapter<SameOrderAdapter.ViewHolder>{
    private LayoutInflater inflater=null;
    private ArrayList<PredicitionDetailEntity.PredicitionData.PredicitionDetailProduct> list =null;
    private PredicitionDetailEntity.PredicitionData.PredicitionDetailProduct product=null;
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private  View header;
    private Context context=null;
    public SameOrderAdapter(Context context,View header){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.header = header;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new ViewHolder(header,ITEM_VIEW_TYPE_HEADER);
        }
        View view=inflater.inflate(R.layout.sameorder_item_layout,parent,false);
        ViewHolder holder=new ViewHolder(view,ITEM_VIEW_TYPE_ITEM);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isHeader(position)) {
            return;
        }
        product=list.get(position-1);
        holder.itemView.setTag(position-1);
        if(product!=null){
            holder.tv_name.setText(product.getName()==null?"":product.getName());
            holder.tv_code.setText(product.getExternal_no()==null?"":product.getExternal_no());
            holder.tv_num.setText(product.getNums()==null?"":product.getNums());
        }
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size()+1;
        }
        return 0+1;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_name;
        TextView tv_code;
        TextView tv_num;
        public ViewHolder(View itemView,int type) {
            super(itemView);
            if(type==ITEM_VIEW_TYPE_HEADER){
                return;
            }
            tv_name=(TextView)itemView.findViewById(R.id.tv_sameorder_name);
            tv_code=(TextView)itemView.findViewById(R.id.tv_sameorder_code);
            tv_num=(TextView)itemView.findViewById(R.id.tv_sameorder_num);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int realposition=(Integer)v.getTag();
            StringBuilder builder=new StringBuilder();
            product=list.get(realposition);
            if(product!=null){
                if(product.getExternal_no()!=null){
                    builder.append(product.getExternal_no());
                }
                ClipboardManager clip = (ClipboardManager) SameOrderAdapter.this.context.getSystemService(Activity.CLIPBOARD_SERVICE);
                clip.setText(builder.toString());
                T.showShortByID(SameOrderAdapter.this.context,R.string.copy_to_clipboard);
            }
        }
    }
    public void setDataSource(ArrayList<PredicitionDetailEntity.PredicitionData.PredicitionDetailProduct> list){
        if(list!=null){
            if(list.size()>0){
                this.list=list;
                notifyDataSetChanged();
            }
        }
    }
    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }
    public boolean isHeader(int position) {
        return position == 0;
    }
}
