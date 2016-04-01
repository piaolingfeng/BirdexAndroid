package com.birdex.bird.fragment;

import android.view.KeyEvent;

import com.birdex.bird.R;

/**
 * Created by chuming.zhuang on 2016/3/25.
 */
public class CustomServiceFragment extends BaseFragment {
    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_custom_layout;
    }

    @Override
    public void initializeContentViews() {

    }

    @Override
    protected void lazyLoad() {

    }
}
