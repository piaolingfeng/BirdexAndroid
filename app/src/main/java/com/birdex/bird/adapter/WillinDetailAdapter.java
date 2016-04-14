package com.birdex.bird.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.entity.InventoryActivityEntity;
import com.birdex.bird.entity.ProductEntity;
import com.birdex.bird.entity.SimpleWillinEntity;

import java.util.ArrayList;

/**
 * Created by huwei on 16/4/13.
 */
public class WillinDetailAdapter extends BaseExpandableListAdapter{
    private LayoutInflater inflater=null;
    private ArrayList<SimpleWillinEntity> list=null;
    private SimpleWillinEntity entity=null;
    public  WillinDetailAdapter(Context context,ArrayList<SimpleWillinEntity> list){
        this.inflater=LayoutInflater.from(context);
        if(list!=null){
            this.list=list;
        }else{
            this.list=new ArrayList<>();
        }
    }
    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(list.get(groupPosition).getTranlist()!=null){
            return list.get(groupPosition).getTranlist().size();
        }
        return 0;
    }

    @Override
    public SimpleWillinEntity getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public SimpleWillinEntity.InventoryTransInfo getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getTranlist().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder gholder=null;
        if(convertView!=null){
            gholder=(GroupViewHolder)convertView.getTag();
        }else {
            convertView=inflater.inflate(R.layout.willin_detail_item_parent,null,false);
            gholder=new GroupViewHolder();
            gholder.tv_inventory=(TextView)convertView.findViewById(R.id.tv_willin_inventoryname);
            gholder.tv_num=(TextView)convertView.findViewById(R.id.tv_willin_inventorynum);
            convertView.setTag(gholder);
        }
        SimpleWillinEntity entity=list.get(groupPosition);
        gholder.tv_inventory.setText(entity.getWarehouse_name());
        gholder.tv_num.setText(""+entity.getCount());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder cholder=null;
        if(convertView!=null){
            cholder=(ChildViewHolder)convertView.getTag();
        }else {
            convertView=inflater.inflate(R.layout.willin_detail_item_layout,null,false);
            cholder=new ChildViewHolder();
            cholder.tv_createtime=(TextView)convertView.findViewById(R.id.tv_willin_detail_createtime);
            cholder.tv_ininventory=(TextView)convertView.findViewById(R.id.tv_willin_detail_incode);
            cholder.tv_transcode=(TextView)convertView.findViewById(R.id.tv_willin_detail_transcode);
            cholder.tv_transstyle=(TextView)convertView.findViewById(R.id.tv_willin_detail_transstayle);
            cholder.tv_num=(TextView)convertView.findViewById(R.id.tv_willin_detail_num);
            cholder.tv_other=(TextView)convertView.findViewById(R.id.detail_willin_viewother);
            convertView.setTag(cholder);
        }
        SimpleWillinEntity.InventoryTransInfo transInfo=list.get(groupPosition).getTranlist().get(childPosition);
        cholder.tv_createtime.setText(transInfo.getCreated_time());
        cholder.tv_ininventory.setText(transInfo.getStorage_no());
        cholder.tv_transcode.setText(transInfo.getTrack_no());
        cholder.tv_transstyle.setText(transInfo.getTrack_type_name());
        cholder.tv_num.setText(transInfo.getNums());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class GroupViewHolder{
        TextView tv_inventory;
        TextView tv_num;
    }
    class ChildViewHolder{
        TextView tv_createtime;
        TextView tv_ininventory;
        TextView tv_transcode;
        TextView tv_transstyle;
        TextView tv_num;
        TextView tv_other;
    }
}
