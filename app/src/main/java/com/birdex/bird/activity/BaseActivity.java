package com.birdex.bird.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.util.HideSoftKeyboardUtil;
import com.birdex.bird.util.SafeProgressDialog;
import com.birdex.bird.widget.RotateLoading;
import com.birdex.bird.widget.SystemBarTintManager;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    private int mContentLayoutResId;
    ProgressDialog bar;
    Dialog loadingDialog;

    protected EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加到 activity 栈
        MyApplication.activityList.add(this);

        mContentLayoutResId = getContentLayoutResId();
        if (0 == mContentLayoutResId) {
            throw new IllegalArgumentException(
                    "mContentLayoutResId is 0, "
                            + "you must thought the method getContentLayoutResId() set the mContentLayoutResId's value"
                            + "when you used a fragment which implements the gta.dtp.fragment.BaseFragment.");
        }
        setContentView(mContentLayoutResId);
        ButterKnife.bind(this);
        HideSoftKeyboardUtil.setupAppCompatUI(getRootView(this), this);
        bar = new ProgressDialog(this);
        loadingDialog = new SafeProgressDialog(this, R.style.semester_dialog);// 创建自定义样式dialog
        bar.setCanceledOnTouchOutside(false);
        initializeContentViews();

    }

    /**
     * 获取根布局
     */
    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    //设置布局
    public abstract int getContentLayoutResId();

    public abstract void initializeContentViews();

    public void showBar() {
        bar.setMessage("加载中...");
        bar.show();
    }

    public void hideBar() {
        bar.dismiss();
    }

    public void showBarCommit() {
        bar.setMessage("正在提交...");
        bar.show();
    }

    public void showLoading() {
        if (loadingDialog == null)
            loadingDialog = new SafeProgressDialog(this, R.style.semester_dialog);// 创建自定义样式dialog
//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        loadingDialog.setContentView(view);// 设置布局
        final RotateLoading loading = (RotateLoading) view.findViewById(R.id.rotateloading);
        loading.start();
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loading.stop();
            }
        });
        loadingDialog.show();
    }

    public void hideLoading() {
        loadingDialog.dismiss();
    }

    //    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {// 获取横屏
//            return; // 此处添加用户自己的操作
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {// 获取竖屏
//            return;
//        }
//    }
    public void initSystemBar(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(this, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(colorId);
//        if (Build.VERSION.SDK_INT >= 19)
//        {
//            ViewGroup group=(ViewGroup)activity.getWindow().getDecorView();
//            View view=new View();
//            // 透明状态栏
////            activity.getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //长时间不运行，推送服务会在部分手机被停止（视厂商修改系统的而定）
        ((MyApplication)getApplication()).getUmengToken();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //长时间不运行，推送服务会在部分手机被停止（视厂商修改系统的而定）
        ((MyApplication)getApplication()).getUmengToken();
    }
}
