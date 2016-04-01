package com.birdex.bird.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.adapter.TodayDataAdapter;
import com.birdex.bird.decoration.FullyLinearLayoutManager;
import com.birdex.bird.entity.OrderManagerEntity;

import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/3/29.
 */
public class TodayDataActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.rcy)
    RecyclerView rcy;
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.save)
    TextView save;
    private TodayDataAdapter adapter;
    private List<OrderManagerEntity> orderList;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_today_layout;
    }

    @Override
    public void initializeContentViews() {
        title.setVisibility(View.VISIBLE);
        title.setText(getString(R.string.data));
        back.setOnClickListener(this);
        orderList = (List<OrderManagerEntity>) getIntent().getSerializableExtra("TodayData");
        rcy.setLayoutManager(new FullyLinearLayoutManager(this));
        adapter = new TodayDataAdapter(this, orderList);
        rcy.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();

        }
    }
}