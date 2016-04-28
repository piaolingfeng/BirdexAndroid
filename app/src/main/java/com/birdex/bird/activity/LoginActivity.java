package com.birdex.bird.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.User;
import com.birdex.bird.service.CacheService;
import com.birdex.bird.util.Constant;
import com.birdex.bird.util.update.UpdateManager;
import com.birdex.bird.util.HideSoftKeyboardUtil;
import com.birdex.bird.util.JsonHelper;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/3/25.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    // 用户名
    @Bind(R.id.username_edit)
    EditText username;

    // 密码
    @Bind(R.id.password_edit)
    EditText password;

    // 记住密码
    @Bind(R.id.remember_cb)
    CheckBox remember;

    // 登录按钮
    @Bind(R.id.login_bt)
    Button login;


    private SharedPreferences sp;

    private SharedPreferences.Editor editor;

    public static String description;
    private MyApplication application=null;
    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initializeContentViews() {
        application=(MyApplication)getApplication();
        HideSoftKeyboardUtil.setupAppCompatUI(getRootView(this), this);
//        initSystemBar(R.color.transparent);
        // 开启缓存 service
//        Intent service = new Intent(this, CacheService.class);
//        startService(service);

        initData();
    }


    private void initData() {
        sp = getSharedPreferences("login", Activity.MODE_PRIVATE);
        editor = sp.edit();

        // 先检查是否保存了 token，如果不为空说明不再需要登录
        String oldToken = sp.getString("token", "");
        if (!TextUtils.isEmpty(oldToken)) {
            // 将 token 添加进去
            MyApplication.ahc.addHeader("USER-TOKEN", oldToken);

            Intent intent = new Intent(MyApplication.getInstans(), MainActivity.class);

            startActivity(intent);
            finish();
        }

        // 确认是否勾选了 记住密码
        boolean ischecked = sp.getBoolean("remember", false);
        if (ischecked) {
            // 选中了 记住密码
            String usernamestr = sp.getString("username", "");
            String passwordstr = sp.getString("password", "");
            // 设置进去
            username.setText(usernamestr);
            password.setText(passwordstr);
        }
        remember.setChecked(ischecked);

        // 检查更新
//        checkUpdate();
    }

    // 检查更新
    private void checkUpdate() {
        //如果是第一次打开，检查更新
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//					super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject jsonObject = (JSONObject) response.get("android");
                    String updateUrl = (String) jsonObject.get("url");
                    description = (String) jsonObject.get("description");
                    String versionServer = (String) jsonObject.get("version");
                    // 检查更新
                    if (!MyApplication.app_version.equals(versionServer)) {
                        UpdateManager.getInstance().setDownLoadPath(updateUrl);
                        // 如果不相等，执行更新操作
                        UpdateManager.getInstance().set(LoginActivity.this, description);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//					super.onFailure(statusCode, headers, responseString, throwable);
                T.showShort(MyApplication.getInstans(), "获取更新信息失败");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                T.showShort(MyApplication.getInstans(), "获取更新信息失败");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                T.showShort(MyApplication.getInstans(), "获取更新信息失败");
            }
        };
        BirdApi.upDateMessage(MyApplication.getInstans(), null, handler);
    }

    @Override
    protected void onDestroy() {
        BirdApi.cancelRequestWithTag(TAG);
        super.onDestroy();
    }

    // 存储返回的 user
    private void saveUser(User user) {
        editor.putString("company_code", user.getCompany_code());
        editor.putString("company_name", user.getCompany_name());
        editor.putString("company_short_name", user.getCompany_short_name());
        editor.putString("user_code", user.getUser_code());

        editor.commit();
    }


    // 执行登录操作
    private void login() {
        showLoading();
        MyApplication application = (MyApplication) getApplication();
        String utoken = application.getUmengToken();
        if (TextUtils.isEmpty(utoken)) {
            utoken = sp.getString(MyApplication.SP_Umeng, "");
            if (TextUtils.isEmpty(utoken)) {
                utoken = "none_device_token";
                editor.putString(MyApplication.SP_Umeng, utoken);
                editor.commit();
            }
        } else {
            editor.putString(MyApplication.SP_Umeng, utoken);
            editor.commit();
        }


        MyApplication.ahc.addHeader("DEVICE-TOKEN", utoken);

        RequestParams params = new RequestParams();
        params.put("device_info", MyApplication.device_info);
        params.put("device_type", MyApplication.device_type);
        params.put("account", username.getText().toString());
        params.put("password", password.getText().toString());

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String result = response.getString("error");
                    if ("0".equals(result)) {
                        hideLoading();
                        spEdit01();
                        User user = JsonHelper.parseObject((JSONObject) response.get("data"), User.class);
//                        editor.putString(Constant.BIND_USER_ID, ((JSONObject) response.get("data")).getString("bind_user_id") + "");
                        editor.putString(Constant.BIND_USER_ID,user.getBind_user_id());
                        editor.commit();
                        //将 user 信息存入到 sp
                        saveUser(user);
                        // user_token 登录后返回
                        String token = (String) ((JSONObject) response.get("data")).get("user_token");
                        // 将 token 添加进去
                        MyApplication.ahc.addHeader("USER-TOKEN", token);
                        saveToken(token);

                        T.showShort(MyApplication.getInstans(), getString(R.string.loginsu));
                        Intent intent = new Intent(MyApplication.getInstans(), MainActivity.class);

                        startActivity(intent);
                        finish();
                    } else {
                        T.showShort(MyApplication.getInstans(), response.getString("data"));
                        hideLoading();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.loginfa));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.login_fail));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.loginfa));
            }
        };
        handler.setTag(TAG);
        BirdApi.login(MyApplication.getInstans(), params, handler);
    }


    private void spEdit() {
        if (remember.isChecked()) {
            // 选中了， 执行保存操作
            editor.putString("username", username.getText().toString());
//            editor.putString("password", password.getText().toString());
            editor.putBoolean("remember", remember.isChecked());
        } else {
            // 如果是取消  就全部设置为空
            editor.putString("username", "");
            editor.putString("password", "");
            editor.putBoolean("remember", false);
        }
        editor.commit();
    }

    private void spEdit01() {
        if (remember.isChecked()) {
            // 选中了， 执行保存操作
            editor.putString("username", username.getText().toString());
            editor.putString("password", password.getText().toString());
            editor.putBoolean("remember", remember.isChecked());
        } else {
            // 如果是取消  就全部设置为空
            editor.putString("username", "");
            editor.putString("password", "");
            editor.putBoolean("remember", false);
        }
        editor.commit();
    }




    // 保存接口发过来的 token
    private void saveToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    @OnClick({R.id.remember_cb, R.id.login_bt})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击记住密码
            case R.id.remember_cb:
                spEdit();
                break;

            // 点击登录按钮
            case R.id.login_bt:
                // 先检查帐号密码不能为空
                if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
                    T.showShort(MyApplication.getInstans(), getString(R.string.notempty));
                } else {
                    login();
                }
                break;
        }
    }
}
