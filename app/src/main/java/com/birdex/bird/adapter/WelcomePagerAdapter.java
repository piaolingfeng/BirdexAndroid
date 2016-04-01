package com.birdex.bird.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by huwei on 16/3/29.
 */
public class WelcomePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list = null;

    public WelcomePagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        if (list == null) {
            this.list = new ArrayList<>();
        } else {
            this.list = list;
        }
    }
    @Override
    public int getCount() {
        return this.list.size();
    }


    @Override
    public Fragment getItem(int position) {
        return this.list.get(position);
    }
}
