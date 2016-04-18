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

    public static final int ICON_CHANGE = 1001;
    public static final int[] name = {R.string.tool1, R.string.tool2, R.string.tool3, R.string.tool5, R.string.tool6};

    public static final String MSG_ORDER_STOCK_EXCEPTION = "ORDER_STOCK_EXCEPTION";//订单库存异常
    public static final String MSG_ORDER_VERIFY_FAIL = "ORDER_VERIFY_FAIL";//订单审核不通过
    public static final String MSG_ORDER_IDCARD_EXCEPTION = "ORDER_IDCARD_EXCEPTION";//订单身份证异常
    public static final String MSG_ACCOUNT_EXCEPTION = "ACCOUNT_EXCEPTION";//账号异常
    public static final String MSG_STOCK_WARNING = "STOCK_WARNING";//库存预警
}
