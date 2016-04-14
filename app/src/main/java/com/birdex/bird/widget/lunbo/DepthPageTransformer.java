package com.birdex.bird.widget.lunbo;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 滑动动画
 * Created by zicong on 2015/7/1.
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) {
            // [-Infinity,-1)
            view.setAlpha(0);
        } else if (position <= 0) {
            // [-1,0]
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        } else if (position <= 1) {
            // (0,1]
            view.setAlpha(1);
            float i=(-pageWidth/2)+((1-position)*(pageWidth/2));
            System.out.println(position+","+i);
            view.setTranslationX(i);
        } else {
            // (1,+Infinity]
            view.setAlpha(0);
        }
    }
}
