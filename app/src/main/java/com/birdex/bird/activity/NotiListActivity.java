package com.birdex.bird.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.birdex.bird.R;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

public class NotiListActivity extends BaseActivity {

    private PushAgent mPushAgent;
    public Handler handler = new Handler();
    @Override
    public int getContentLayoutResId() {
        return R.layout.notilist_layout;
    }

    @Override
    public void initializeContentViews() {
        mPushAgent = PushAgent.getInstance(this);
//		mPushAgent.setPushCheck(true);    //默认不检查集成配置文件
//		mPushAgent.setLocalNotificationIntervalLimit(false);  //默认本地通知间隔最少是10分钟

        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // sdk关闭通知声音
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        // 通知声音由服务端控制
//		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);
//		mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
//		mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);

        //应用程序启动统计
        //参考集成文档的1.5.1.2
        //http://dev.umeng.com/push/android/integration#1_5_1
        mPushAgent.onAppStart();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String deviceId = null;
                do {
                    deviceId = UmengRegistrar.getRegistrationId(NotiListActivity.this);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                    Log.e("android", "11"+deviceId+"");
                } while (TextUtils.isEmpty(deviceId));
            }
        }, 1000);
        //开启推送并设置注册的回调处理
        mPushAgent.enable(mRegisterCallback);
    }
    //此处是注册的回调处理
    //参考集成文档的1.7.10
    //http://dev.umeng.com/push/android/integration#1_7_10
    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            // TODO Auto-generated method stub
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    updateStatus();
                }
            });
        }
    };
    private void updateStatus() {
        String pkgName = getApplicationContext().getPackageName();
        String info = String.format("enabled:%s\nisRegistered:%s\nDeviceToken:%s\n" +
                        "SdkVersion:%s\nAppVersionCode:%s\nAppVersionName:%s",
                mPushAgent.isEnabled(), mPushAgent.isRegistered(),
                mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
                UmengMessageDeviceConfig.getAppVersionCode(this), UmengMessageDeviceConfig.getAppVersionName(this));
            Log.e("android",info);
//        tvStatus.setText("应用包名：" + pkgName + "\n" + info);
//
//        btnEnable.setImageResource(mPushAgent.isEnabled() ? R.drawable.open_button : R.drawable.close_button);
//        copyToClipBoard();
//
//        Log.i(TAG, "updateStatus:" + String.format("enabled:%s  isRegistered:%s",
//                mPushAgent.isEnabled(), mPushAgent.isRegistered()));
//        Log.i(TAG, "=============================");
//        btnEnable.setClickable(true);
        int i=0;
    }
}
