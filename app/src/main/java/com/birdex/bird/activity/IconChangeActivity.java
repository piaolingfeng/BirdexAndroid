package com.birdex.bird.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.birdex.bird.R;
import com.birdex.bird.glide.GlideUtils;
import com.birdex.bird.util.ImageUtils;
import com.birdex.bird.util.T;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/3/28.
 */
public class IconChangeActivity extends BaseActivity implements View.OnClickListener {

    // 标题
    @Bind(R.id.title)
    TextView title;

    // 保存按钮
    @Bind(R.id.save)
    TextView save;

    // 头像图片
    @Bind(R.id.iconchange_icon)
    ImageView icon;

    // 拍照 code
    private final static int PHOTO_GREQUEST_CODE = 1;
    private final static int REQUEST_CODE_PICK_IMAGE = 2;
    private final static int RESULT_REQUEST_CODE = 3;

    // 图片 path
    private String filePath;

    // 最终的 图片
    private Drawable resultDrawable;

    // 传过来的 bitmap
    private Bitmap bitmap;

    /** 头像名称 */
    private static final String IMAGE_FILE_NAME = "image.jpg";

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_iconchange;
    }

    @Override
    public void initializeContentViews() {
        initData();
    }

    private void initData() {

        // 设置title 内容
        title.setText(getString(R.string.iconchange_title));
        save.setText(getString(R.string.iconchange_save));

        // 传过来的图片 path 或者 url
//        String path = getIntent().getExtras().getString("path");
//        String url = getIntent().getExtras().getString("url");
        // 将 bitmap 设置进去 如果 path 为空 就设置 url ，如果都为空则用 default
//        if (!TextUtils.isEmpty(path)) {
//            GlideUtils.setImageToLocalPath(icon, path);
//        } else if (!TextUtils.isEmpty(url)) {
//            GlideUtils.setImageToUrl(icon, url);
//        } else {
//            icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.contacts));
//        }

        // 传过来的 bitmap
        bitmap = (Bitmap) getIntent().getExtras().get("bitmap");
        icon.setImageBitmap(bitmap);
    }

    @OnClick({R.id.back, R.id.save, R.id.iconchange_icon})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                //点击了 返回按钮
                finish();
                break;
            case R.id.save:
                //点击了 保存按钮
//                if (!TextUtils.isEmpty(filePath)) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
//                    bundle.putString("path", filePath);
                if(resultDrawable != null) {
                    Bitmap rebitmap = ImageUtils.drawableToBitmap(resultDrawable);
                    bundle.putParcelable("bitmap", rebitmap);
                } else {
                    bundle.putParcelable("bitmap", bitmap);
                }
                    resultIntent.putExtras(bundle);
                    this.setResult(RESULT_OK, resultIntent);
//                }
                IconChangeActivity.this.finish();
                break;
            case R.id.iconchange_icon:
                //点击了 图片， 进行选择 拍照或者从图库选择图片
                choiceDialog();
                break;
        }
    }

    /**
     * 生成文件路径和文件名
     *
     * @return
     */
    private String getFileName() {
        String saveDir = Environment.getExternalStorageDirectory() + "/birdex/myPic";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建文件夹
        }
        //用日期作为文件名，确保唯一性
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String fileName = saveDir + "/" + formatter.format(date) + ".jpg";

        return fileName;
    }


    // 拍照
    private void photo() {
//        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        filePath = getFileName();
//        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
//
//        startActivityForResult(photoIntent, PHOTO_GREQUEST_CODE);

        Intent intentFromCapture = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        String state = Environment
                .getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(path, IMAGE_FILE_NAME);
            intentFromCapture.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(file));
        }

        startActivityForResult(intentFromCapture,
                PHOTO_GREQUEST_CODE);
    }

    // 从相册获取
    private void getImageFromAlbum() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");//相片类型
//
//        // 设置裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 340);
//        intent.putExtra("outputY", 340);
//        intent.putExtra("return-data", true);
//
//        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery,
                REQUEST_CODE_PICK_IMAGE);
    }


    private void choiceDialog() {
        final String items[] = {getString(R.string.photo), getString(R.string.from_loacl)};
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
//        设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    // 拍照
                    case 0:
                        photo();
                        dialog.dismiss();
                        break;
                    // 从本地获取
                    case 1:
                        getImageFromAlbum();
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.create().show();
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }


    /**
     * 保存裁剪之后的图片数据
     *
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
            resultDrawable = drawable;
            icon.setImageDrawable(drawable);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 拍照返回
            case PHOTO_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {

//                    String sdStatus = Environment.getExternalStorageState();
//                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
//                        Log.i("TestFile",
//                                "SD card is not avaiable/writeable right now.");
//                        T.showLong(this, "SDCard读取失败，请重试");
//                        return;
//                    }
//
//                    // 将拍摄的照片设置上去
//                    GlideUtils.setImageToLocalPath(icon, filePath);

                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }

                }
                break;

            // 从相册获取的
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {

//                    Uri selectedImage = data.getData();
//                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                    Cursor cursor = getContentResolver().query(selectedImage,
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    filePath = cursor.getString(columnIndex);
//                    cursor.close();

//                    Bundle extras = data.getExtras();
//                    if (extras != null) {
//                        Bitmap photo = extras.getParcelable("data");
//                        Drawable drawable = new BitmapDrawable(this.getResources(), photo);
//                        icon.setImageDrawable(drawable);
//                    }

                    startPhotoZoom(data.getData());

                    // 将拍摄的照片设置上去
//                    GlideUtils.setImageToLocalPath(icon, filePath);

                }
                break;

            case RESULT_REQUEST_CODE : // 图片缩放完成后
                if (data != null) {
                    getImageToView(data);
                }
                break;
        }
    }
}
