package com.birdex.bird.activity;

import com.birdex.bird.R;
import com.birdex.bird.widget.TitleView;

import butterknife.Bind;

public class AboutActivity extends BaseActivity{

    @Bind(R.id.title_view)
    TitleView title_view;
    @Override
    public int getContentLayoutResId() {
        return R.layout.about_layout;
    }

    @Override
    public void initializeContentViews() {
        title_view.setTitle(getString(R.string.about_us_title));
    }
}
