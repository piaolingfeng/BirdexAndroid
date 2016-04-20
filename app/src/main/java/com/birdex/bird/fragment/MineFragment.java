package com.birdex.bird.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.activity.AboutActivity;
import com.birdex.bird.activity.LoginActivity;
import com.birdex.bird.activity.MyAccountActivity;
import com.birdex.bird.activity.MyAccountInfoActivity;
import com.birdex.bird.activity.MyMessageActivity;
import com.birdex.bird.activity.TodayDataActivity;
import com.birdex.bird.adapter.MineIndexAdapter;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.MineEntity;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.util.T;
import com.birdex.bird.util.decoration.FullyLinearLayoutManager;
import com.birdex.bird.util.update.UpdateManager;
import com.birdex.bird.widget.TitleView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/3/25.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    //设置列表
    @Bind(R.id.rv_mine_list)
    public RecyclerView rv_list;
    @Bind(R.id.titleview)
    TitleView titleview;
    private MineIndexAdapter adapter = null;

    String tag = "MineFragment";
    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    public void initializeContentViews() {
        titleview.setTitle(getString(R.string.mine));
        titleview.setBackIvVisble(false);
        ArrayList<MineEntity> list = new ArrayList<>();
        String[] titles = getActivity().getResources().getStringArray(R.array.mine_item_title);
        for (int i = 0; i < titles.length; i++) {
            MineEntity entity = new MineEntity();
            entity.setTitle(titles[i]);
            if (i == 1 || i == 3 || i == 5) {
                entity.setShowType(MineEntity.Type.Bottom);
            } else if (i == 0 || i == 2 || i == 4) {
                entity.setShowType(MineEntity.Type.Top);
            } else {
                entity.setShowType(MineEntity.Type.Middle);
            }
            list.add(entity);
        }
        adapter = new MineIndexAdapter(getActivity(), list);
        rv_list.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
        rv_list.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        // 点击了 我的账户
                        intent = new Intent(getActivity(), MyAccountActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        //我的订阅消息
                        intent = new Intent(getActivity(), MyMessageActivity.class);
                        startActivity(intent);
                        break;
//                case 2:
//                    //我的设置
//                    T.showShort(activity,activity.getString(R.string.please_wail));
//                    break;
                    case 2:
                        //账户管理
                        intent = new Intent(getActivity(), MyAccountInfoActivity.class);
                        //显示第三个页面
                        intent.putExtra("enterindex", 2);
                        startActivity(intent);
                        break;
                    case 3:
                        //我的数据
                        intent = new Intent(getActivity(), TodayDataActivity.class);
                        startActivity(intent);
                        break;
//                case 5:
//                    //我的客户经理
//                    T.showShort(activity,activity.getString(R.string.please_wail));
//                    break;
                    case 4:
                        //关于
                        intent = new Intent(getActivity(), AboutActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        //检查更新
//                    intent=new Intent(MineIndexAdapter.this.activity, AboutActivity.class);
//                    MineIndexAdapter.this.activity.startActivity(intent);
                        checkUpdate();
                        break;
                    default:

                        break;
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick({R.id.out_account})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.out_account:
                // 清除掉登录的相关信息
                SharedPreferences sp = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("company_code", "");
                editor.putString("company_name", "");
                editor.putString("company_short_name", "");
                editor.putString("user_code", "");
                editor.putString("token", "");
                editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                MyApplication.getInstans().clearActivities();
                startActivity(intent);
                break;
        }
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
                    String description = (String) jsonObject.get("description");
                    String versionServer = (String) jsonObject.get("version");
                    // 检查更新
                    if (!MyApplication.app_version.equals(versionServer)) {
                        UpdateManager.getInstance().setDownLoadPath(updateUrl);
                        // 如果不相等，执行更新操作
                        UpdateManager.getInstance().set(getActivity(), description);
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
        };
        handler.setTag(tag);
        BirdApi.upDateMessage(MyApplication.getInstans(), null, handler);
    }

    @Override
    public void onDestroy() {
        BirdApi.cancelRequestWithTag(tag);
        super.onDestroy();
    }
}
