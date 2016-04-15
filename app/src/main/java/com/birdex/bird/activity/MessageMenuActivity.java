package com.birdex.bird.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.birdex.bird.R;

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

    public static final String SP_NAME = "MESSAGE_SETTING";
    public static final String TONE_SETTING = "tone";
    public static final String TIME_SETTING = "time";


    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_messagemenu;
    }

    @Override
    public void initializeContentViews() {
        initData();
    }

    private void initData() {

        title_rl.setBackgroundColor(Color.parseColor("#666666"));
        title.setText(getString(R.string.message_setting));

        initRadioButton();
    }

    private void initRadioButton() {

        sp = getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);
        editor = sp.edit();

        // 先通过 sp 进行设置， 如果sp 没有内容，则默认选择前面那项
        boolean tone = sp.getBoolean(TONE_SETTING, true);
        boolean time = sp.getBoolean(TIME_SETTING, true);

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
                editor.putBoolean(TONE_SETTING,true);
                editor.commit();
                break;
            // 不提示，只在消息中显示
            case R.id.warning_tone_rb2:
                if (warning_tone_rb1.isChecked()) {
                    warning_tone_rb1.setChecked(false);
                }
                editor.putBoolean(TONE_SETTING,false);
                editor.commit();
                break;
            // 全天接收
            case R.id.time_rb1:
                if (time_rb2.isChecked()) {
                    time_rb2.setChecked(false);
                }
                editor.putBoolean(TIME_SETTING,true);
                editor.commit();
                break;
            // 不提示，只在消息中显示
            case R.id.time_rb2:
                if (time_rb1.isChecked()) {
                    time_rb1.setChecked(false);
                }
                editor.putBoolean(TIME_SETTING,false);
                editor.commit();
                break;

        }
    }
}
