package com.birdex.bird.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.birdex.bird.service.NotificationService;

/**
 * Created by chuming.zhuang on 2016/4/11.
 */
public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            System.out.println("手机开机了....");
            startUploadService(context);
        }
        if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            startUploadService(context);
        }
    }

    public void startUploadService(Context mContext){
        Intent intent = new Intent(mContext, NotificationService.class);
        mContext.startService(intent);
    }
}
