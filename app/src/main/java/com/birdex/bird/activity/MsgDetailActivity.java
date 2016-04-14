package com.birdex.bird.activity;

import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.widget.TitleView;
import com.birdex.bird.widget.xrecyclerview.XRecyclerView;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/4/14.
 */
public class MsgDetailActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TitleView title_view;
    @Bind(R.id.rcy)
    XRecyclerView rcy;
    @Bind(R.id.tv_clear)
    TextView tv_clear;
    String title = "";

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_msgdetail_layout;
    }

    @Override
    public void initializeContentViews() {
        getIntent().getStringExtra("title");
        getIntent();
    }
}
