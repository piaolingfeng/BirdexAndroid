package com.birdex.bird.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.adapter.TodayDataAdapter;
import com.birdex.bird.fragment.IndexFragment;
import com.birdex.bird.util.decoration.FullyLinearLayoutManager;
import com.birdex.bird.entity.OrderManagerEntity;
import com.birdex.bird.widget.TitleView;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/3/29.
 */
public class TodayDataActivity extends BaseActivity{
    @Bind(R.id.title_view)
    TitleView title_view;
    @Bind(R.id.rcy)
    RecyclerView rcy;

    private TodayDataAdapter adapter;
    private List<OrderManagerEntity> orderList;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_today_layout;
    }

    @Override
    public void initializeContentViews() {
        title_view.setTitle(getString(R.string.data));
        title_view.setBackIvVisble(true);
        orderList = (List<OrderManagerEntity>) getIntent().getSerializableExtra("TodayData");
        if (orderList == null || orderList.size() == 0) {
            orderList = IndexFragment.indexOrderNetDatatList;
        }
        rcy.setLayoutManager(new FullyLinearLayoutManager(this));
        adapter = new TodayDataAdapter(this, orderList);
        rcy.setAdapter(adapter);
    }

}
