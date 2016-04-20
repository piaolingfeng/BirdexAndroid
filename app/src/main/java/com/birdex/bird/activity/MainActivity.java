package com.birdex.bird.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.birdex.bird.R;
import com.birdex.bird.adapter.OrderManagerAdapter;
import com.birdex.bird.fragment.BaseFragment;
import com.birdex.bird.fragment.CustomServiceFragment;
import com.birdex.bird.fragment.HelpFragment;
import com.birdex.bird.fragment.IndexFragment;
import com.birdex.bird.fragment.MineFragment;
import com.birdex.bird.interfaces.BackHandledInterface;
import com.birdex.bird.util.T;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener, BackHandledInterface, View.OnClickListener {

    private BaseFragment baseFragment;//加入栈顶的fragment

    private IndexFragment indexFragment;
    private HelpFragment helpFragment;
    private CustomServiceFragment customServiceFragment;
    private MineFragment mineFragment;
    private FragmentTransaction transaction;
    private long exitTime = 0;//退出事件累计
    private int tag = 1;
    @Bind(R.id.tv_index)
    TextView tv_index;
    @Bind(R.id.tv_help)
    TextView tv_help;
    @Bind(R.id.tv_custom_service)
    TextView tv_custom_service;
    @Bind(R.id.tv_mine)
    TextView tv_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initializeContentViews() {
        if (indexFragment == null)
            indexFragment = new IndexFragment();
        if (mineFragment == null)
            mineFragment = new MineFragment();
        if (helpFragment == null)
            helpFragment = new HelpFragment();
        if (customServiceFragment == null)
            customServiceFragment = new CustomServiceFragment();
        addFragment(indexFragment);
    }

    public void setSelectTV() {
        tv_index.setSelected(false);
        tv_custom_service.setSelected(false);
        tv_help.setSelected(false);
        tv_mine.setSelected(false);
        switch (tag) {
            case 1:
                tv_index.setSelected(true);
                break;
            case 2:
                tv_custom_service.setSelected(true);
                break;
            case 3:
                tv_help.setSelected(true);
                break;
            case 4:
                tv_mine.setSelected(true);
                break;
        }
    }

    //隐藏所有fragment
    private void hideFragment() {
        if (indexFragment != null)
            transaction.hide(indexFragment);
        if (mineFragment != null)
            transaction.hide(mineFragment);
        if (customServiceFragment != null)
            transaction.hide(customServiceFragment);
        if (helpFragment != null)
            transaction.hide(helpFragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.baseFragment = selectedFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (OrderManagerAdapter.longClickState)
                baseFragment.onKeyDown(keyCode, event);
            else {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    exit();
                }
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //两次点击退出
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @OnClick({R.id.tv_mine, R.id.tv_custom_service, R.id.tv_help, R.id.tv_index})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_index:
                tag = 1;
                addFragment(indexFragment);
                break;
            case R.id.tv_custom_service:
                tag = 2;
//                addFragment(customServiceFragment);
                T.showShort(MainActivity.this,getString(R.string.please_wail));
                break;
            case R.id.tv_help:
//                tag = 3;
//                addFragment(helpFragment);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MyMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_mine:
                tag = 4;
                addFragment(mineFragment);
                break;
        }
    }

    /**
     * 隐藏添加fragment
     * */
    private void addFragment(BaseFragment baseFragment) {
        setSelectTV();
        transaction = getSupportFragmentManager().beginTransaction();
        hideFragment();
        if (!baseFragment.isAdded())
            transaction.add(R.id.main_framelayout, baseFragment);
        transaction.show(baseFragment);
        transaction.commit();
    }
}
