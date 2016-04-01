package com.birdex.bird.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.glide.GlideUtils;
import com.birdex.bird.util.Constant;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/3/28.
 */
public class MyAccountActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.head_icon)
    ImageView head;

    @Bind(R.id.account_manager)
    TextView accountManager;

    @Bind(R.id.account_framelayout)
    FrameLayout frameLayout;

//    private String path;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_myaccount;
    }

    @Override
    public void initializeContentViews() {

    }

    @OnClick({R.id.head_icon, R.id.account_manager})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_icon:
                // 点击头像，进入修改头像界面
                Intent intent = new Intent(this,IconChangeActivity.class);
                Bundle b = new Bundle();
                Drawable drawable = head.getDrawable();
                Bitmap bitmap = drawableToBitmap(drawable);
                b.putParcelable("bitmap",bitmap);
//                b.putString("path",path);
                // 还需要把服务器加载头像的 url 传过去, 先为空测试。。。
//                b.putString("url","");
                intent.putExtras(b);
                startActivityForResult(intent, Constant.ICON_CHANGE);
                break;

            // 点击账户管理
            case R.id.account_manager:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            // 修改头像
            case Constant.ICON_CHANGE:
                if(resultCode == RESULT_OK){
                    // 返回的图片路径
//                    path = data.getExtras().getString("path");
//                    GlideUtils.setImageToLocalPath(head,path);
                    if(data.getExtras()!= null) {
                        Bitmap resultBitmap = (Bitmap) data.getExtras().get("bitmap");
                        if(resultBitmap!=null){
                            head.setImageBitmap(resultBitmap);
                        }
                    }
                }
                break;

        }
    }

    // 将 drawable 转换成 bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
