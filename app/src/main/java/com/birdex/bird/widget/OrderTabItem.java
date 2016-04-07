package com.birdex.bird.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.birdex.bird.R;

/**
 * Created by huangzx on 2015/8/27.
 */
public class OrderTabItem {


    public OrderTabItem(Context context) {
        this.context = context;
    }

    public View getTabView() {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_order_layout, null);
        tv_tab_item = (TextView) view.findViewById(R.id.tv_tab_item);
        tv_tab_count = (TextView) view.findViewById(R.id.tv_tab_count);
        return view;
    }


    public void setTabTitle(CharSequence title) {
        tv_tab_item.setText(title);
    }

    public void setTabCount(String count) {
        tv_tab_count.setVisibility(View.VISIBLE);
        tv_tab_count.setText(count);
    }

    public void setIndicVisibility(int visible) {
        if (tv_tab_count.getVisibility() == View.VISIBLE)
            tv_tab_count.setVisibility(View.INVISIBLE);
        else
            tv_tab_count.setVisibility(View.VISIBLE);
    }

    private Context context;

    private TextView tv_tab_item;
    private TextView tv_tab_count;
}
