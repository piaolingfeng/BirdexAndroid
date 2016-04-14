package com.birdex.bird.activity;

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
public class MessageMenuActivity extends BaseActivity implements View.OnClickListener{

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

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_messagemenu;
    }

    @Override
    public void initializeContentViews() {
        initData();
    }

    private void initData(){

        title_rl.setBackgroundColor(Color.parseColor("#666666"));
        title.setText(getString(R.string.message_setting));

        initRadioButton();
    }

    private void initRadioButton(){
        warning_tone_rb1.setChecked(true);
        warning_tone_rb2.setChecked(false);
        time_rb1.setChecked(true);
        time_rb2.setChecked(false);
    }

    @OnClick({R.id.back, R.id.warning_tone_rb1, R.id.warning_tone_rb2, R.id.time_rb1, R.id.time_rb2})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 左上角返回
            case R.id.back:
                finish();
                break;

            case R.id.warning_tone_rb1:
                if(warning_tone_rb2.isChecked()){
                    warning_tone_rb2.setChecked(false);
                }
                break;
            case R.id.warning_tone_rb2:
                if(warning_tone_rb1.isChecked()){
                    warning_tone_rb1.setChecked(false);
                }
                break;

            case R.id.time_rb1:
                if(time_rb2.isChecked()){
                    time_rb2.setChecked(false);
                }
                break;
            case R.id.time_rb2:
                if(time_rb1.isChecked()){
                    time_rb1.setChecked(false);
                }
                break;

        }
    }
}
