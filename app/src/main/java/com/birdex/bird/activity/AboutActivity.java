package com.birdex.bird.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.birdex.bird.R;

import butterknife.Bind;

public class AboutActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.iv_about_us_back)
    public ImageView iv_back;

    @Override
    public int getContentLayoutResId() {
        return R.layout.about_layout;
    }

    @Override
    public void initializeContentViews() {
        initSystemBar(R.color.blue_head_1);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_about_us_back:
                finish();
                break;
        }
    }
}
