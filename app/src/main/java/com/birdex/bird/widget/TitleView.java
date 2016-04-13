package com.birdex.bird.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.activity.InventoryActivity;
import com.birdex.bird.activity.MyAccountInfoActivity;
import com.birdex.bird.activity.MyOrderListActivity;
import com.birdex.bird.adapter.CommonSimpleAdapter;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.Constant;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/12.
 */
public class TitleView extends RelativeLayout implements View.OnClickListener {
    Context mContext;
    View view;
    TextView title;
    PercentRelativeLayout back;
    ImageView menu;
    TextView save;
    ImageView back_iv;
    List<String> menuList;//menu菜单list
    PercentRelativeLayout prl_title;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (isInEditMode()) {
            return;
        }
        initView();
    }


    public TitleView(Context context) {
        super(context);
        mContext = context;
        if (isInEditMode()) {
            return;
        }
        initView();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (isInEditMode()) {
            return;
        }
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (mContext != null)
                    ((Activity) mContext).finish();
                break;
            case R.id.menu:
                showMenuWindow((View) menu.getParent(), menuList, 3);
                break;
        }
    }

    PopupWindow mPopupWindow;

    public void showMenuWindow(View viewID, final List<String> list, final int w) {
        CommonSimpleAdapter adapter = new CommonSimpleAdapter(mContext, list);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                Intent intent = null;
                if (list.get(position) != null && list.get(position).equals(mContext.getString(R.string.tool6))) {
                    //我的充值
                    intent = new Intent(mContext, MyAccountInfoActivity.class);
                    //显示第一个页面
                    intent.putExtra("enterindex", 0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    mContext.startActivity(intent);
                    return;
                } else if (list.get(position) != null && list.get(position).equals(mContext.getString(R.string.tool5))) {
                    //我的支出记录
                    intent = new Intent(mContext, MyAccountInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    //显示第二个页面
                    intent.putExtra("enterindex", 1);
                    mContext.startActivity(intent);
                    return;
                } else if (list.get(position) != null && list.get(position).equals(mContext.getString(R.string.tool3))) {
                    //我的库存
                    intent = new Intent(mContext, InventoryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    mContext.startActivity(intent);
                    return;
                }
                intent = new Intent(mContext, MyOrderListActivity.class);
                intent.putExtra("name", mContext.getString(Constant.name[position]));
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                mContext.startActivity(intent);

            }
        });
        showPopupWindow(viewID, w, adapter);
    }

    private void showPopupWindow(View viewID, int w, RecyclerView.Adapter adapter) {
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View popWindow = LayoutInflater.from(mContext).inflate(R.layout.common_recycleview_layout, null);
//        popWindow.setBackgroundColor(Color.TRANSPARENT);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(mContext));
        rcy.setAdapter(adapter);
        //        int width = getWindowManager().getDefaultDisplay().getWidth();
        int width = viewID.getWidth();
        mPopupWindow = new PopupWindow(popWindow, width / w, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
        if (w > 1)
            mPopupWindow.showAsDropDown(viewID, (width / w) * (w - 1), 0);
        else
            mPopupWindow.showAsDropDown(viewID, 0, 0);
    }

    private void initView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.title, null, false);
        title = (TextView) view.findViewById(R.id.title);
        back = (PercentRelativeLayout) view.findViewById(R.id.back);
        menu = (ImageView) view.findViewById(R.id.menu);
        back_iv = (ImageView) view.findViewById(R.id.back_iv);
        save = (TextView) view.findViewById(R.id.save);
        prl_title = (PercentRelativeLayout) view.findViewById(R.id.prl_title);
        back.setOnClickListener(this);
        menu.setOnClickListener(this);
        menuList = new ArrayList<>();
        for (int i = 0; i < Constant.name.length; i++) {//初始化lmenu list
            menuList.add(mContext.getString(Constant.name[i]));
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        addView(view, params);
    }

    public void setMenuRecycleviewListener(OnClickListener listener) {
        menu.setOnClickListener(listener);
    }

    public View getMenuView() {
        return menu;
    }

    public void setMenuVisble(boolean flag) {
        if (flag)
            menu.setVisibility(View.VISIBLE);
        else {
            menu.setVisibility(View.INVISIBLE);
        }
    }

    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    public void setSaveText(String text) {
        save.setVisibility(View.VISIBLE);
        save.setText(text);
    }

    public void setBackground(int col){
        prl_title.setBackgroundColor(col);
    }

    public void setTitleTextcolor(int col){
        title.setTextColor(col);
    }

    public void setBackIv(Bitmap bitmap){
        back_iv.setImageBitmap(bitmap);
    }

    public void setMenu(Bitmap bitmap){
        menu.setImageBitmap(bitmap);
    }
    /*
     *库存titleview
     */
    public void setInventoryDetail(String titleStr,int colorID){
        setMenuVisble(false);
        title.setText("");
        prl_title.setBackgroundColor(colorID);
        save.setText(titleStr);
    }
}
