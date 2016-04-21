package com.birdex.bird.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.birdex.bird.R;
import com.birdex.bird.adapter.WelcomePagerAdapter;
import com.birdex.bird.fragment.BaseFragment;
import com.birdex.bird.fragment.ImageFragment;
import com.birdex.bird.interfaces.BackHandledInterface;
import com.birdex.bird.service.CacheService;
import com.birdex.bird.util.Constant;

import java.util.ArrayList;
import java.util.logging.LogRecord;

import butterknife.Bind;


public class SplashActivity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener, BackHandledInterface, ViewPager.OnPageChangeListener, View.OnClickListener {

    private long m_dwSplashTime = 2000;
    private boolean m_bPaused = false;
    private boolean m_bSplashActive = true;

    @Bind(R.id.vp_welcome_images)
    public ViewPager vp_images;
    @Bind(R.id.ll_welcome_go)
    public LinearLayout ll_welcome;
    //图片的列表
    private ArrayList<Fragment> fraglist = null;
    private WelcomePagerAdapter pagerAdapter = null;
    //设置线程锁
    private Object lock = new Object();
    //设置结束标志
    private boolean isFinish = false;
    //创建展现线程
    private Thread imgThread = null;
    private Runnable runnable = null;
    private int nextPage = 0;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_splash;
    }

    private void startMyService() {
        // 开启缓存 service
        Intent service = new Intent(this, CacheService.class);
        startService(service);
    }

    @Override
    public void initializeContentViews() {
        // 先获取公用配置当前版本  判断是否要开启缓存service
        startMyService();

        initSystemBar(R.color.transparent);
        ll_welcome.setOnClickListener(this);
        fraglist = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            ImageFragment fragment1 = new ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("imgid", R.drawable.welcome);
            fragment1.setArguments(bundle);
            fraglist.add(fragment1);
        }
        pagerAdapter = new WelcomePagerAdapter(getSupportFragmentManager(), fraglist);
        vp_images.setAdapter(pagerAdapter);

        //设置默认首页
        if (fraglist.size() > 0) {
            vp_images.setCurrentItem(nextPage);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                nextPage++;
                if (fraglist.size() <= nextPage) {
                    if (!isFinish) {
                        //停止欢迎页的切换
                        stopChangeWelCome();
                    }
                } else {
                    vp_images.setCurrentItem(nextPage);
                }
            }
        };
        //配置
        imgThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    synchronized (imgThread) {
                        if (isFinish) {
//                            Log.e("android","welcome thread finish!");
                            break;
                        }
                    }
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SplashActivity.this.runOnUiThread(runnable);
                }
            }
        };
        imgThread.start();
    }

    private void stopChangeWelCome() {
        if (imgThread != null) {
            synchronized (imgThread) {
                isFinish = true;
                imgThread.notify();
            }
        }
        // 先判断是否是第一次打开app， 如果是第一次 需要加载 功能介绍页， 否则直接跳转登录页
        SharedPreferences sp = getSharedPreferences(Constant.LOGIN, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (sp.getBoolean(Constant.FIRST_ENTRY_APP, true)) {
            startActivity(new Intent("com.bird.app.splashy.CLEARSPLASH"));
            editor.putBoolean(Constant.FIRST_ENTRY_APP, false);
            editor.commit();
        } else {
            // 直接跳转到登录页
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        m_bPaused=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        m_bPaused=false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
//                m_bSplashActive=false;
                break;
            case KeyEvent.KEYCODE_BACK:
            /*两种退出方法*/
            /* System.exit(0);*/
            /* android.os.Process.killProcess(android.os.Process.myPid());*/
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imgThread != null) {
            if (imgThread.isAlive()) {
                synchronized (imgThread) {
                    isFinish = true;
                    imgThread.notify();
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_welcome_go:
                stopChangeWelCome();
                break;
        }
    }
}
