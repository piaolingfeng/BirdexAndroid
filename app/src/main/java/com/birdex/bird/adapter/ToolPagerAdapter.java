package com.birdex.bird.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.birdex.bird.fragment.BaseFragment;

import java.util.List;

/**
 * Created by chuming.zhuang on 2016/4/19.
 */
public class ToolPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    List<BaseFragment> baseFragmentList;

    public ToolPagerAdapter(FragmentManager fm, Context mContext, List<BaseFragment> baseFragmentList) {
        super(fm);
        this.mContext = mContext;
        this.baseFragmentList = baseFragmentList;
    }

    @Override
    public int getCount() {
        int size = 0;
        if (baseFragmentList != null)
            size = baseFragmentList.size();
        return size;
    }

    @Override
    public Fragment getItem(int position) {
        return baseFragmentList.get(position);
    }

}
