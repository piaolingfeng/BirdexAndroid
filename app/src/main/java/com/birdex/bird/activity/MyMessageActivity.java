package com.birdex.bird.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/4/13.
 */
public class MyMessageActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.prl_title)
    com.zhy.android.percent.support.PercentRelativeLayout title_rl;

    // 右上角设置
    @Bind(R.id.menu)
    ImageView menu;

    // 标题
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.warning_bv)
    com.birdex.bird.widget.BadgeView warningBv;
    @Bind(R.id.idcard_exception_bv)
    com.birdex.bird.widget.BadgeView idcard_exception_bv;
    @Bind(R.id.repertory_exception_bv)
    com.birdex.bird.widget.BadgeView repertory_exception_bv;
    @Bind(R.id.check_exception_bv)
    com.birdex.bird.widget.BadgeView check_exception_bv;
    @Bind(R.id.account_exception_bv)
    com.birdex.bird.widget.BadgeView account_exception_bv;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_mymessage;
    }

    @Override
    public void initializeContentViews() {
        initData();
    }

    // 初始化数据
    private void initData() {
        title_rl.setBackgroundColor(Color.parseColor("#666666"));
        menu.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_setting));
        menu.setVisibility(View.VISIBLE);
        title.setText(getString(R.string.my_message));

        showLoading();
        // 调用今日数据接口，获取数量
        BirdApi.getTodayData(MyApplication.getInstans(),null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideLoading();
                T.showShort(MyApplication.getInstans(),getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong));
            }

        });

        warningBv.setText("1");
        warningBv.show();
        idcard_exception_bv.setText("2");
        idcard_exception_bv.show();
        repertory_exception_bv.setText("3");
        repertory_exception_bv.show();
        check_exception_bv.setText("4");
        check_exception_bv.show();
        account_exception_bv.setText("5");
        account_exception_bv.show();
    }

    @OnClick({R.id.back, R.id.menu, R.id.warning_ll, R.id.exception_ll, R.id.repertory_exception_ll, R.id.check_exception_ll, R.id.account_exception_ll})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.back:
                finish();
                break;

            // 右上角设置
            case R.id.menu:
                Intent intent = new Intent(this,MessageMenuActivity.class);
                startActivity(intent);
                break;

            // 点击 库存预警消息
            case R.id.warning_ll:
                T.showShort(MyApplication.getInstans(),"库存预警消息");
                break;
            // 点击 身份证异常消息
            case R.id.exception_ll:
                T.showShort(MyApplication.getInstans(),"身份证异常消息");
                break;

            // 点击 库存异常消息
            case R.id.repertory_exception_ll:
                T.showShort(MyApplication.getInstans(),"库存异常消息");
                break;

            // 点击 审核不通过消息
            case R.id.check_exception_ll:
                T.showShort(MyApplication.getInstans(),"审核不通过消息");
                break;

            // 点击 账户异常
            case R.id.account_exception_ll:
                T.showShort(MyApplication.getInstans(),"账户异常");
                break;
        }
    }
}
