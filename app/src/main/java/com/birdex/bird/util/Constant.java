package com.birdex.bird.util;

import android.os.Environment;

import com.birdex.bird.R;

/**
 * Created by chuming.zhuang on 2016/3/18.
 */
public class Constant {
    public static final String NAME = "/Bird";
    public static final String BASEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + NAME;

    public static final int ICON_CHANGE =  1001;
    public static final int[] name = {R.string.tool1, R.string.tool2, R.string.tool3, R.string.tool5, R.string.tool6};
}
