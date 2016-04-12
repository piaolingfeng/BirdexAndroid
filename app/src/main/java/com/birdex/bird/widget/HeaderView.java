package com.birdex.bird.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.birdex.bird.R;

import java.lang.reflect.Field;

/**
 * Created by huwei on 16/3/31.
 */
public class HeaderView extends LinearLayout implements View.OnClickListener{
    private LayoutInflater inflater = null;
    private String title = "";
    private int bgcolor = 0;
    //主题
    private TextView tv_title = null;
    private LinearLayout ll_left=null;
    private TextView tv_left=null;
    private ImageView iv_left=null;
    private LinearLayout ll_right=null;
    private TextView tv_right=null;
    private ImageView iv_right=null;
    //左边文字样式
    private int leftstyle = 0;
    //左边图片样式
    private int leftimg = 0;
    //左边文字
    private String lefttxt = "";

    //右边文字样式
    private int rightstyle = 0;
    //右边图片样式
    private int rightimg = 0;
    //右边文字
    private String righttxt = "";
    //是否显示据状态条
    private boolean showStatuBar=false;
    private OnHeadViewClickLister lister=null;
    public HeaderView(Context context) {
        super(context);
        initAttr(context, null);
        initView(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
        initView(context);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);
            title = array.getString(R.styleable.HeaderView_headtitle);
            bgcolor = array.getColor(R.styleable.HeaderView_bgcolor, 0);
            //左边
            leftstyle = array.getResourceId(R.styleable.HeaderView_leftstyle, 0);
            leftimg = array.getResourceId(R.styleable.HeaderView_leftimg, 0);
            lefttxt = array.getString(R.styleable.HeaderView_lefttxt);
            //右边
            rightstyle = array.getResourceId(R.styleable.HeaderView_rightstyle, 0);
            rightimg = array.getResourceId(R.styleable.HeaderView_rightimg, 0);
            righttxt = array.getString(R.styleable.HeaderView_righttxt);
            showStatuBar=array.getBoolean(R.styleable.HeaderView_hasstatubar, false);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initView(Context context) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.head_layout, null, false);
        tv_title = (TextView) view.findViewById(R.id.tv_head_title);
        ll_left = (LinearLayout) view.findViewById(R.id.ll_headview_left);
        ll_left.setOnClickListener(this);
        tv_left = (TextView) view.findViewById(R.id.tv_headview_left);
        iv_left = (ImageView) view.findViewById(R.id.iv_headview_left);
        ll_right = (LinearLayout) view.findViewById(R.id.ll_headview_right);
        ll_right.setOnClickListener(this);
        tv_right = (TextView) view.findViewById(R.id.tv_headview_right);
        iv_right = (ImageView) view.findViewById(R.id.iv_headview_right);
        if (title != null) {
            tv_title.setText(title);
        }
        if (bgcolor != 0) {
            this.setBackgroundColor(bgcolor);
        }
        if(leftstyle!=0){
            tv_left.setTextAppearance(leftstyle);
        }
        if(rightstyle!=0){
            tv_left.setTextAppearance(rightstyle);
        }
        if(leftimg!=0){
            iv_left.setImageResource(leftimg);
        }
        if(rightimg!=0){
            iv_right.setImageResource(rightimg);
        }
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if(showStatuBar){
            int statusBarHeight = getStatuHei();
//            view.setPadding(0,statusBarHeight,0,0);
            params.setMargins(0,statusBarHeight,0,0);
        }
        addView(view, params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_headview_left:
                if(lister!=null){
                    lister.onLeftClick(v);
                }
                break;
            case R.id.ll_headview_right:
                if(lister!=null){
                    lister.onRightClick(v);
                }
                break;
        }
    }

    public interface OnHeadViewClickLister{
        void onLeftClick(View view);
        void onRightClick(View view);
    }
    public void setOnHeadViewClickLister(OnHeadViewClickLister lister){
        if(lister!=null){
            this.lister=lister;
        }
    }
    private int getStatuHei(){

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
        } catch(Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
}
