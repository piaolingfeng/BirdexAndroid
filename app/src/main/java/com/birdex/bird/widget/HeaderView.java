package com.birdex.bird.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.birdex.bird.R;

/**
 * Created by huwei on 16/3/31.
 */
public class HeaderView extends LinearLayout{
    private LayoutInflater inflater=null;
    private String title="";
    private int bgcolor=0;
    //主题
    private TextView tv_title=null;

    public HeaderView(Context context) {
        super(context);
        initView(context,null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }
    private void initView(Context context,AttributeSet attrs){
        inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.head_layout, null, false);
        tv_title=(TextView)view.findViewById(R.id.tv_head_title);
        if(attrs!=null){
            TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.HeaderView);
            title=array.getString(R.styleable.HeaderView_headtitle);
            bgcolor=array.getColor(R.styleable.HeaderView_bgcolor,0);
            if(title!=null){
                tv_title.setText(title);
            }
            if(bgcolor!=0){
                this.setBackgroundColor(bgcolor);
            }
        }
        addView(view, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
    }
}
