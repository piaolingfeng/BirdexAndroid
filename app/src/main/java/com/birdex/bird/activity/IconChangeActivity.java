package com.birdex.bird.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.util.glide.GlideUtils;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    // 传过来的 logo
    private String logo;

    // 修改后的 bitmap
    private Bitmap resultBitmap;

    // 上传图片后，服务器返回的 path
    private String path;

    /**
     * 头像名称
     */
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

        // 传过来的 bitmap
        logo = (String) getIntent().getExtras().get("logo");
        GlideUtils.setImageToLocalPathForMyaccount(icon, logo);
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
//                //点击了 保存按钮
////                if (!TextUtils.isEmpty(filePath)) {
//                Intent resultIntent = new Intent();
//                Bundle bundle = new Bundle();
////                    bundle.putString("path", filePath);
//                if (resultBitmap != null) {
////                    Bitmap rebitmap = ImageUtils.drawableToBitmap(resultDrawable);
////                    bundle.putParcelable("bitmap", resultBitmap);
//                    bundle.putString("bitmap", uritempFile.toString());
//                } else {
////                    bundle.putParcelable("bitmap", bitmap);
//                    bundle.putString("bitmap", null);
//                }
//                resultIntent.putExtras(bundle);
//                this.setResult(RESULT_OK, resultIntent);
////                }
//                IconChangeActivity.this.finish();

                showLoading();
                // 点击保存按钮后，需要调用 修改公司信息
                RequestParams params = new RequestParams();
                params.put("company_code",MyApplication.user.getCompany_code());
                params.put("logo",path);
                BirdApi.companyEdit(MyApplication.getInstans(),params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        hideLoading();
                        try {
                            if("0".equals(response.getString("error"))){
                                IconChangeActivity.this.setResult(RESULT_OK);
                                IconChangeActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideLoading();
                        T.showShort(MyApplication.getInstans(),getString(R.string.save_fail));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideLoading();
                        T.showShort(MyApplication.getInstans(), getString(R.string.save_fail));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideLoading();
                        T.showShort(MyApplication.getInstans(), getString(R.string.save_fail));
                    }
                });

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

    Uri uritempFile;

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
//        intent.putExtra("return-data", true);

        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);

        //uritempFile为Uri类变量，实例化uritempFile
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }


    /**
     * 保存裁剪之后的图片数据
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

            case RESULT_REQUEST_CODE: // 图片缩放完成后

                try {
                    // 将图片上传到服务器
                    try {
                        File file = new File(new URI(uritempFile.toString()));
                        saveBitmapFile(comp(file.getAbsolutePath()),file.getAbsolutePath());
                        RequestParams params = new RequestParams();
                        params.put("logo", file);
                        showLoading();
                        BirdApi.upLoadLogo(MyApplication.getInstans(), params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                hideLoading();
                                T.showShort(MyApplication.getInstans(), "上传图片成功");
                                try {
                                    if("0".equals(response.getString("error"))) {
                                        try {
                                            String savepath = ((JSONObject)response.get("data")).getString("savepath");
                                            String thumb = ((JSONObject)response.get("data")).getString("thumb");
                                            path = savepath + thumb;
                                            resultBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                                            icon.setImageBitmap(resultBitmap);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                            hideLoading();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                hideLoading();
                                T.showShort(MyApplication.getInstans(),getString(R.string.upload_fail));
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                hideLoading();
                                T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                hideLoading();
                                T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                            }
                        });
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

//                    getImageToView(data);
//                }
                break;
        }
    }

    // 图片压缩
    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    // 将 bitmap 保存成图片
    public void saveBitmapFile(Bitmap bitmap, String imgpath) {
        File file = new File(imgpath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap comp(String path) {

        Bitmap image = BitmapFactory.decodeFile(path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }
}
