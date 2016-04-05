package com.birdex.bird.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.adapter.CommonSimpleAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.OrderManagerEntity;
import com.birdex.bird.fragment.IndexFragment;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/3/30.
 */
public class MyOrderListActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.viewPager)
    ViewPager viewpager;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.img_menu)
    ImageView img_menu;
    @Bind(R.id.et_search)
    EditText et_search;
    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    @Bind(R.id.state_all)
    TextView state_all;
    @Bind(R.id.state_time)
    TextView state_time;
    @Bind(R.id.state_Warehouse)
    TextView state_Warehouse;
    public static final int[] name = {R.string.tool1, R.string.tool2, R.string.tool3, R.string.tool5, R.string.tool6};
    public static List<String> menuList = new ArrayList<>();
    public String currentName;
    public int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutResId());
        ButterKnife.bind(this);
        initializeContentViews();

    }

    public int getContentLayoutResId() {
        return R.layout.activity_my_orderlist_layout;
    }

    public void initializeContentViews() {
        currentName = getIntent().getStringExtra("name");
        if (!StringUtils.isEmpty(currentName)) {

        } else {
            currentName = getString(name[0]);
        }
        tv_title.setText(currentName);
        for (int i = 0; i < name.length; i++) {//初始化lmenu list
            menuList.add(getString(name[i]));
        }

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                }
                return false;
            }
        });
//        tabLayout.addTab();
    }

    PopupWindow mPopupWindow;

    /**
     * 展示弹出框
     */
    public void showPopupWindow(View viewID, final List<String> list) {
//        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popWindow = LayoutInflater.from(this).inflate(R.layout.common_recycleview_layout, null);
//        popWindow.setBackgroundColor(Color.TRANSPARENT);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(this));
        CommonSimpleAdapter adapter = new CommonSimpleAdapter(this, list);
        rcy.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                T.showShort(MyApplication.getInstans(), list.get(position));
            }
        });
        int width = getWindowManager().getDefaultDisplay().getWidth();
        mPopupWindow = new PopupWindow(popWindow, width / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
        mPopupWindow.showAsDropDown(viewID, width / 4, 0);
    }

    @OnClick({R.id.img_menu, R.id.state_time, R.id.state_Warehouse, R.id.state_all})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_menu:
                showPopupWindow((View) img_menu.getParent(), menuList);
                break;
            case R.id.state_all:
                showPopupWindow((View) state_all.getParent(), menuList);
                break;
            case R.id.state_Warehouse:
                showPopupWindow((View) state_Warehouse.getParent(), menuList);
                break;
            case R.id.state_time:
                showPopupWindow((View) state_time.getParent(), menuList);
                break;
        }
    }


}
