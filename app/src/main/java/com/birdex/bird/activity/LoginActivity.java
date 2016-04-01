package com.birdex.bird.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.User;
import com.birdex.bird.update.UpdateManager;
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

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initializeContentViews() {
        initSystemBar(R.color.transparent);
        initData();
    }


    private void initData() {
        sp = getSharedPreferences("login", Activity.MODE_PRIVATE);
        editor = sp.edit();
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
        checkUpdate();
    }

    // 检查更新
    private void checkUpdate() {
        //如果是第一次打开，检查更新
        BirdApi.upDateMessage(MyApplication.getInstans(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//					super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject jsonObject = (JSONObject) response.get("android");
                    String updateUrl = (String) jsonObject.get("url");
                    description = (String) jsonObject.get("description");
                    String versionServer = (String) jsonObject.get("version");

                    // 取得当前版本号
                    PackageManager manager = MyApplication.getInstans().getPackageManager();
                    PackageInfo info = null;
                    info = manager.getPackageInfo(MyApplication.getInstans().getPackageName(), 0);
                    String versionLocal = info.versionName;

                    // 检查更新
                    if (!versionLocal.equals(versionServer)) {
                        UpdateManager.getInstance().setDownLoadPath(updateUrl);
                        // 如果不相等，执行更新操作
                        UpdateManager.getInstance().set(LoginActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//					super.onFailure(statusCode, headers, responseString, throwable);
                T.showShort(MyApplication.getInstans(), "获取更新信息失败");
            }
        });
    }


    // 执行登录操作
    private void login() {
        showLoading();
        RequestParams params = new RequestParams();
        params.put("account", username.getText().toString());
        params.put("password", password.getText().toString());
        BirdApi.login(MyApplication.getInstans(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String result = response.getString("error");
                    if ("0".equals(result)) {
                        hideLoading();
                        spEdit();
                        MyApplication.user = JsonHelper.parseObject((JSONObject) response.get("data"), User.class);
                        T.showShort(MyApplication.getInstans(), getString(R.string.loginsu));
                        Intent intent = new Intent(MyApplication.getInstans(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        T.showShort(MyApplication.getInstans(), response.getString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                T.showShort(MyApplication.getInstans(), getString(R.string.loginfa));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.loginfa));
            }
        });
    }


    private void spEdit(){
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