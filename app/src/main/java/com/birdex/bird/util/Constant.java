package com.birdex.bird.util;

import android.os.Environment;

/**
 * Created by chuming.zhuang on 2016/3/18.
 */
public class Constant {
    public static final String NAME = "/Bird";
    public static final String BASEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + NAME;

    public static final int ICON_CHANGE =  1001;
}
