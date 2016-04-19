package com.birdex.bird.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.service.IPushAidlInterface;
import com.birdex.bird.service.NotificationService;
import com.birdex.bird.util.Constant;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/4/13.
 */
public class MessageMenuActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.prl_title)
    com.zhy.android.percent.support.PercentRelativeLayout title_rl;

    // 标题
    @Bind(R.id.title)
    TextView title;

    // rb
    @Bind(R.id.warning_tone_rb1)
    com.rey.material.widget.RadioButton warning_tone_rb1;
    @Bind(R.id.warning_tone_rb2)
    com.rey.material.widget.RadioButton warning_tone_rb2;

    @Bind(R.id.time_rb1)
    com.rey.material.widget.RadioButton time_rb1;
    @Bind(R.id.time_rb2)
    com.rey.material.widget.RadioButton time_rb2;


    private SharedPreferences sp;

    private SharedPreferences.Editor editor;

//    public static final String SP_NAME = "MESSAGE_SETTING";
//    public static final String TONE_SETTING = "tone";
//    public static final String TIME_SETTING = "time";
    private IPushAidlInterface pushAIDL=null;
    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_messagemenu;
    }
    private ServiceConnection connection=null;
    @Override
    public void initializeContentViews() {
        initData();
    }

    private void initData() {

        title_rl.setBackgroundColor(Color.parseColor("#666666"));
        title.setText(getString(R.string.message_setting));

//        initRadioButton();
        //绑定服务获取数据
        connection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                pushAIDL=IPushAidlInterface.Stub.asInterface(service);
                initRadioButton();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        //开启服务
        Intent intent=new Intent(this, NotificationService.class);
        intent.setAction("com.dbjtech.myservice");
        startService(intent);
        //绑定服务
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    private void initRadioButton() {

//        sp = getSharedPreferences(Constant.SP_NAME, Activity.MODE_WORLD_READABLE);
//        editor = sp.edit();

        // 先通过 sp 进行设置， 如果sp 没有内容，则默认选择前面那项
//        boolean tone = sp.getBoolean(Constant.TONE_SETTING, true);
//        boolean time = sp.getBoolean(Constant.TIME_SETTING, true);
        boolean tone =true ;
        try {
            tone =pushAIDL.getVoiceStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        boolean time =true ;
        try {
            time =pushAIDL.getTimeVoiceStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(tone){
            warning_tone_rb1.setChecked(true);
            warning_tone_rb2.setChecked(false);
        } else {
            warning_tone_rb1.setChecked(false);
            warning_tone_rb2.setChecked(true);
        }

        if(time) {
            time_rb1.setChecked(true);
            time_rb2.setChecked(false);
        } else {
            time_rb1.setChecked(false);
            time_rb2.setChecked(true);
        }
    }

    @OnClick({R.id.back, R.id.warning_tone_rb1, R.id.warning_tone_rb2, R.id.time_rb1, R.id.time_rb2})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 左上角返回
            case R.id.back:
                finish();
                break;
            // 系统提示音
            case R.id.warning_tone_rb1:
                if (warning_tone_rb2.isChecked()) {
                    warning_tone_rb2.setChecked(false);
                }
//                editor.putBoolean(Constant.TONE_SETTING,true);
//                editor.commit();
                if(pushAIDL!=null){
                    try {
                        pushAIDL.setVoiceStatus(true);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // 不提示，只在消息中显示
            case R.id.warning_tone_rb2:
                if (warning_tone_rb1.isChecked()) {
                    warning_tone_rb1.setChecked(false);
                }
//                editor.putBoolean(Constant.TONE_SETTING,false);
//                editor.commit();
                if(pushAIDL!=null){
                    try {
                        pushAIDL.setVoiceStatus(false);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // 全天接收
            case R.id.time_rb1:
                if (time_rb2.isChecked()) {
                    time_rb2.setChecked(false);
                }
//                editor.putBoolean(Constant.TIME_SETTING,true);
//                editor.commit();
                if(pushAIDL!=null){
                    try {
                        pushAIDL.setTimeVoiceStatus(true);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // 不提示，只在消息中显示
            case R.id.time_rb2:
                if (time_rb1.isChecked()) {
                    time_rb1.setChecked(false);
                }
//                editor.putBoolean(Constant.TIME_SETTING,false);
//                editor.commit();
                if(pushAIDL!=null){
                    try {
                        pushAIDL.setTimeVoiceStatus(false);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connection!=null){
            unbindService(connection);
        }
    }
}
