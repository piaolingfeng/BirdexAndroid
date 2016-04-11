package com.birdex.bird.fragment;

import android.content.Intent;
import android.view.KeyEvent;

import com.birdex.bird.R;
import com.birdex.bird.activity.NotiListActivity;

/**
 * Created by chuming.zhuang on 2016/3/25.
 */
public class HelpFragment extends BaseFragment {
    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_help_layout;
    }

    @Override
    public void initializeContentViews() {
        Intent intent=new Intent(getActivity(), NotiListActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    protected void lazyLoad() {

    }
}
