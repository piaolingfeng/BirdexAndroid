package com.birdex.bird.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.greendao.DaoMaster;
import com.birdex.bird.greendao.DaoSession;
import com.birdex.bird.greendao.NotifiMsg;
import com.birdex.bird.greendao.NotifiMsgDao;
import com.birdex.bird.util.T;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by hyj on 2016/4/13.
 */
public class MyMessageActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.prl_title)
    com.zhy.android.percent.support.PercentRelativeLayout title_rl;

    // 右上角设置
    @Bind(R.id.menu)
    ImageView menu;

    // 标题
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.warning_bv_ll)
    LinearLayout warning_bv_ll;
    @Bind(R.id.warning_bv)
    com.birdex.bird.widget.BadgeView warningBv;

    @Bind(R.id.idcard_exception_bv_ll)
    LinearLayout idcard_exception_bv_ll;
    @Bind(R.id.idcard_exception_bv)
    com.birdex.bird.widget.BadgeView idcard_exception_bv;

    @Bind(R.id.repertory_exception_bv_ll)
    LinearLayout repertory_exception_bv_ll;
    @Bind(R.id.repertory_exception_bv)
    com.birdex.bird.widget.BadgeView repertory_exception_bv;

    @Bind(R.id.check_exception_bv_ll)
    LinearLayout check_exception_bv_ll;
    @Bind(R.id.check_exception_bv)
    com.birdex.bird.widget.BadgeView check_exception_bv;

    @Bind(R.id.account_exception_bv_ll)
    LinearLayout account_exception_bv_ll;
    @Bind(R.id.account_exception_bv)
    com.birdex.bird.widget.BadgeView account_exception_bv;

    String tag = "MyMessageActivity";

    // 订单库存异常
    private static final String ORDER_STOCK_EXCEPTION = "ORDER_STOCK_EXCEPTION";
    private String orderStockCount = "0";
    // 订单审核不通过
    private static final String ORDER_VERIFY_FAIL = "ORDER_VERIFY_FAIL";
    private String orderVerifyCount = "0";
    // 订单身份证异常
    private static final String ORDER_IDCARD_EXCEPTION = "ORDER_IDCARD_EXCEPTION";
    private String orderIdcardCount = "0";
    // 账号异常
    private static final String ACCOUNT_EXCEPTION = "ACCOUNT_EXCEPTION";
    private String accountCount = "0";
    // 库存告警
    private static final String STOCK_WARNING = "STOCK_WARNING";
    private String stockCount = "0";

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_mymessage;
    }

    @Override
    public void initializeContentViews() {
        EventBus.getDefault().register(this);
        initData();
    }

    @Subscriber(tag = "msg_list_update")
    public void clearUnread(String title){
        if(getString(R.string.msg_warning).equals(title)){
            setWarning(0);
        }
        if(getString(R.string.msg_idcard_exception).equals(title)){
            setIdcard(0);
        }
        if(getString(R.string.msg_repertory_exception).equals(title)){
            setOrderStock(0);
        }
        if(getString(R.string.msg_check_exception).equals(title)){
            setOrderVerify(0);
        }
        if(getString(R.string.msg_account_exception).equals(title)){
            setAccountException(0);
        }
    }

    // 初始化数据
    private void initData() {
        EventBus.getDefault().register(this);
        title_rl.setBackgroundColor(Color.parseColor("#666666"));
        menu.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_setting));
        menu.setVisibility(View.VISIBLE);
        title.setText(getString(R.string.my_message));

        // 获取未读信息条数
        getUnreadData();

    }

    // 库存预警消息 未读条数设置
    private void setWarning(int count) {
        if (count > 0) {
            if (count <= 99) {
                warningBv.setText(count + "");
                warningBv.show();
            } else {
                warningBv.setText("99+");
                warningBv.show();
            }
            warning_bv_ll.setVisibility(View.VISIBLE);
        } else {
            warning_bv_ll.setVisibility(View.GONE);
        }
    }

    // 身份证异常 未读条数设置
    private void setIdcard(int count) {
        if (count > 0) {
            if (count <= 99) {
                idcard_exception_bv.setText(count + "");
                idcard_exception_bv.show();
            } else {
                idcard_exception_bv.setText("99+");
                idcard_exception_bv.show();
            }
            idcard_exception_bv_ll.setVisibility(View.VISIBLE);
        } else {
            idcard_exception_bv_ll.setVisibility(View.GONE);
        }
    }

    // 订单库存异常 未读条数设置
    private void setOrderStock(int count) {
        if (count > 0) {
            if (count <= 99) {
                repertory_exception_bv.setText(count + "");
                repertory_exception_bv.show();
            } else {
                repertory_exception_bv.setText("99+");
                repertory_exception_bv.show();
            }
            repertory_exception_bv_ll.setVisibility(View.VISIBLE);
        } else {
            repertory_exception_bv_ll.setVisibility(View.GONE);
        }
    }

    // 审核不通过 未读条数设置  ORDER_VERIFY_FAIL
    private void setOrderVerify(int count) {
        if (count > 0) {
            if (count <= 99) {
                check_exception_bv.setText(count + "");
                check_exception_bv.show();
            } else {
                check_exception_bv.setText("99+");
                check_exception_bv.show();
            }
            check_exception_bv_ll.setVisibility(View.VISIBLE);
        } else {
            check_exception_bv_ll.setVisibility(View.GONE);
        }
    }

    // 账户异常 未读条数设置
    private void setAccountException(int count) {
        if (count > 0) {
            if (count <= 99) {
                account_exception_bv.setText(count + "");
                account_exception_bv.show();
            } else {
                account_exception_bv.setText("99+");
                account_exception_bv.show();
            }
            account_exception_bv_ll.setVisibility(View.VISIBLE);
        } else {
            account_exception_bv_ll.setVisibility(View.GONE);
        }
    }

    private void getUnreadData() {
        showLoading();
        JsonHttpResponseHandler handler =  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if ("0".equals(response.getString("error"))) {
                        JSONArray array = (JSONArray) response.get("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jb = (JSONObject) array.get(i);
                            if(ORDER_STOCK_EXCEPTION.equals(jb.getString("msg_type"))){
                                // 订单库存异常
                                orderStockCount = jb.getString("count");
                            }
                            if(ORDER_VERIFY_FAIL.equals(jb.getString("msg_type"))){
                                // 订单审核不通过
                                orderVerifyCount = jb.getString("count");
                            }
                            if(ORDER_IDCARD_EXCEPTION.equals(jb.getString("msg_type"))){
                                // 订单身份证异常
                                orderIdcardCount = jb.getString("count");
                            }
                            if(ACCOUNT_EXCEPTION.equals(jb.getString("msg_type"))){
                                // 账号异常
                                accountCount = jb.getString("count");
                            }
                            if(STOCK_WARNING.equals(jb.getString("msg_type"))){
                                // 库存告警
                                stockCount = jb.getString("count");
                            }
                        }
                        setUnreadData();
                    }
                    hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideLoading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
            }
        };
        handler.setTag(tag);
        BirdApi.getMessageStat(MyApplication.getInstans(), null,handler);
    }

    private void setUnreadData(){
        try {
            int stockCountInt = Integer.parseInt(stockCount);
            setWarning(stockCountInt);
        } catch (Exception e){

        }
        try {
            int count = Integer.parseInt(orderIdcardCount);
            setIdcard(count);
        } catch (Exception e){

        }
        try {
            int count = Integer.parseInt(orderStockCount);
            setOrderStock(count);
        } catch (Exception e){

        }
        try {
            int count = Integer.parseInt(orderVerifyCount);
            setOrderVerify(count);
        } catch (Exception e){

        }
        try {
            int count = Integer.parseInt(accountCount);
            setAccountException(count);
        } catch (Exception e){

        }

    }


    @OnClick({R.id.back, R.id.menu, R.id.warning_ll, R.id.exception_ll, R.id.repertory_exception_ll, R.id.check_exception_ll, R.id.account_exception_ll})
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            // 返回
            case R.id.back:
                finish();
                break;

            // 右上角设置
            case R.id.menu:
                Intent intent = new Intent(this, MessageMenuActivity.class);
                startActivity(intent);
                break;
            // 点击 库存预警消息
            case R.id.warning_ll:
                openMsgActivity(getString(R.string.msg_1));
                break;
            // 点击 身份证异常消息
            case R.id.exception_ll:
                openMsgActivity(getString(R.string.msg_2));
                break;
            // 点击 库存异常消息
            case R.id.repertory_exception_ll:
                openMsgActivity(getString(R.string.msg_3));
                break;
            // 点击 审核不通过消息
            case R.id.check_exception_ll:
                openMsgActivity(getString(R.string.msg_4));
                break;
            // 点击 账户异常
            case R.id.account_exception_ll:
//                openMsgActivity(getString(R.string.msg_account_exception));
//                T.showLong(this, getString(R.string.please_wail));
                openMsgActivity(getString(R.string.msg_5));
//                T.showLong(this,getString(R.string.please_wail));
                break;

        }
    }

    public void openMsgActivity(String title) {
        Intent intent1 = new Intent(MyMessageActivity.this, MsgDetailActivity.class);
        intent1.putExtra("title", title);
        startActivity(intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
