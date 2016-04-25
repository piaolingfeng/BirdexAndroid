package com.birdex.bird.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.util.glide.GlideUtils;
import com.birdex.bird.util.ImageUtils;
import com.birdex.bird.util.T;
import com.birdex.bird.widget.TitleView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/4/12.
 */
public class UploadIDCardActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "UploadIDCardActivity";

    @Bind(R.id.title_1l)
    TitleView title_1l;

    // 身份证号码
    @Bind(R.id.idcard_no)
    EditText idcardNo;

    // 左边身份证
    @Bind(R.id.left)
    ImageView left;

    // 右边身份证
    @Bind(R.id.right)
    ImageView right;


    /**
     * 头像名称
     */
    private static final String IMAGE_FILE_NAME = "image.jpg";
    private static final String IMAGE_FILE_NAME_BACK = "imageback.jpg";


    // 拍照 code
    private final static int PHOTO_GREQUEST_CODE = 1;
    private final static int REQUEST_CODE_PICK_IMAGE = 2;

    // 压缩完成
    private final static int COMPRESS_DOWN = 3;

    // 裁剪返回
    private final static int RESULT_REQUEST_CODE = 4;

    // 传过来的 order_code
    private String order_code;

    // 通过接口查询到的 身份证号码
    private String idcardNoStr;

    // 保存的左右边照片
    private File frontPic;
    private File backPic;

    // 图片上传后 返回的路径
    private String frontPicPath;
    private String backPicPath;


    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_uploadidcard;
    }

    @Override
    public void initializeContentViews() {
        initData();
    }

    // 初始化数据
    private void initData() {
        title_1l.setSaveText(getString(R.string.upload_IDCard));

        order_code = getIntent().getExtras().getString("order_code");

        // 获取身份证号码
        RequestParams params = new RequestParams();
        params.add("order_code", order_code);

        showLoading();

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if ("0".equals(response.getString("error"))) {
                        idcardNoStr = ((JSONObject) response.get("data")).getString("receiver_id_card");
                        idcardNo.setText(idcardNoStr);
                    } else {
                        T.showShort(MyApplication.getInstans(), getString(R.string.get_idcard_fail));
                    }
                    hideLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideLoading();
                    T.showShort(MyApplication.getInstans(), getString(R.string.get_idcard_fail));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.get_idcard_fail));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.get_idcard_fail));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.get_idcard_fail));
            }

        };
        handler.setTag(TAG);
        BirdApi.getOrderDetail(MyApplication.getInstans(), params, handler);

    }


    @OnClick({R.id.left, R.id.right, R.id.cancel, R.id.confirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击左边身份证
            case R.id.left:
                flag = true;
                choiceDialog();
                break;
            // 点击右边身份证
            case R.id.right:
                flag = false;
                choiceDialog();
                break;

            //点击确认
            case R.id.confirm:
                // 先要判断是否两张照片都有
                if (frontPic == null || backPic == null || TextUtils.isEmpty(idcardNo.getText())) {
                    T.showShort(MyApplication.getInstans(), getString(R.string.please_full));
                } else {
                    // 将照片上传
                    uploadPic();
                }
                break;

            // 点击取消 ，结束 activity
            case R.id.cancel:
                finish();
                break;
        }
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }


    // 需要前后两张照片都上传成功后才能调用保存
    private boolean leftUploadSu = false;
    private boolean rightUploadSu = false;

    // 上传照片
    private void uploadPic() {
        showLoading();
        new MyTask().execute();
    }

    // 标记位， true 左边， false 右边
    private boolean flag = true;

    public static final int CAMERA_REQUEST_CODE = 10;

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

                        if (ContextCompat.checkSelfPermission(UploadIDCardActivity.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(UploadIDCardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            try {

                                //申请拍照权限
                                ActivityCompat.requestPermissions(UploadIDCardActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        CAMERA_REQUEST_CODE);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            photo();
                        }

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


    private String filePath;


    // 拍照
    private void photo() {

        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        filePath = getFileName();

        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));

        startActivityForResult(photoIntent, PHOTO_GREQUEST_CODE);
    }


    // 6.0 权限控制
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 用户允许
                photo();
            } else {
                // Permission Denied
            }
        }
    }


    /**
     * 生成文件路径和文件名
     *
     * @return
     */
    private String getFileName() {
        String saveDir = Environment.getExternalStorageDirectory() + "/myPic";
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


    // 从相册获取
    private void getImageFromAlbum() {

        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery
                .setAction(Intent.ACTION_PICK);
        startActivityForResult(intentFromGallery,
                REQUEST_CODE_PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 拍照返回
            case PHOTO_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {

                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        T.showLong(this,"SDCard读取失败，请重试");
                        return;
                    }

//                    if(flag){
//                        frontPic = new File(filePath);
//                        GlideUtils.setImageToLocalPath(left, frontPic.getAbsolutePath());
//                    } else {
//                        backPic = new File(filePath);
//                        GlideUtils.setImageToLocalPath(right, backPic.getAbsolutePath());
//                    }

                    File file = new File(filePath);
                    startPhotoZoom(Uri.fromFile(file));
                }
                break;

            // 从相册获取的
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
//                    if (flag) {
//                        GlideUtils.setImageToLocalPath(left, picturePath);
//                        frontPic = new File(picturePath);
//                    } else {
//                        GlideUtils.setImageToLocalPath(right, picturePath);
//                        backPic = new File(picturePath);
//                    }
                    File file = new File(picturePath);
                    startPhotoZoom(Uri.fromFile(file));
                }
                break;

            // 剪切 返回
            case RESULT_REQUEST_CODE:
                try {
                    if (flag) {
                        frontPic = new File(new URI(uritempFileLeft.toString()));
//                        GlideUtils.setImageToLocalPath(left, frontPic.getAbsolutePath());
                        left.setImageBitmap(BitmapFactory.decodeFile(frontPic.getAbsolutePath()));
//                        System.out.print(1);
                    } else {
                        backPic = new File(new URI(uritempFileRight.toString()));
//                        GlideUtils.setImageToLocalPath(right, backPic.getAbsolutePath());
                        right.setImageBitmap(BitmapFactory.decodeFile(backPic.getAbsolutePath()));
//                        System.out.print(2);
                    }
                } catch (URISyntaxException e){
                    e.printStackTrace();
                }
                break;
        }
    }

    Uri uritempFileLeft;
    Uri uritempFileRight;

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
        uritempFileLeft = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "front.jpg");
        uritempFileRight = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "back.jpg");
        if(flag) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFileLeft);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFileRight);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }


    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case COMPRESS_DOWN:
                    // 执行上传操作
                    doUpload();
                    break;
            }
        }
    };

    // 压缩的左边图片
    private File leftFile;
    // 压缩的右边图片
    private File rightFile;


    private void doUpload(){

        if (frontPic != null) {
            // 上传左边
            RequestParams myparams = new RequestParams();
            try {

                myparams.put("front", leftFile);

                JsonHttpResponseHandler handler =  new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if ("0".equals(response.getString("error"))) {
                                // 上传成功
                                frontPicPath = ((JSONObject) response.get("data")).getString("savepath") + ((JSONObject) response.get("data")).getString("savename");
                                leftUploadSu = true;
                                // 只有当两张照片都成功上传才能调用
                                if (rightUploadSu) {
                                    // 调用修改订单，上传
                                    RequestParams params1 = new RequestParams();
                                    params1.add("order_code", order_code);
                                    params1.add("receiver_id_card", idcardNo.getText().toString());
                                    params1.add("receiver_id_card_img", frontPicPath);
                                    params1.add("receiver_id_card_img_back", backPicPath);

                                    JsonHttpResponseHandler handler1 = new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            super.onSuccess(statusCode, headers, response);
                                            try {
                                                if ("0".equals(response.getString("error"))) {
                                                    EventBus.getDefault().post(true,"checkIDCard");
                                                    T.showShort(MyApplication.getInstans(), getString(R.string.upload_suc));
                                                    finish();
                                                } else {
                                                    T.showShort(MyApplication.getInstans(), response.getString("data"));
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
                                            T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                                            hideLoading();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);
                                            T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                                            hideLoading();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);
                                            T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                                            hideLoading();
                                        }
                                    };
                                    handler1.setTag(TAG);

                                    BirdApi.uploadIDCard(MyApplication.getInstans(), params1, handler1);
                                }
                            } else {
                                T.showShort(MyApplication.getInstans(), response.getString("data"));
                                leftUploadSu = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideLoading();
                            leftUploadSu = false;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                        leftUploadSu = false;
                        hideLoading();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                        leftUploadSu = false;
                        hideLoading();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                        leftUploadSu = false;
                        hideLoading();
                    }
                };
                handler.setTag(TAG);
                BirdApi.uploadIDCardPic(MyApplication.getInstans(), myparams,handler);
            } catch (Exception e) {
                e.printStackTrace();
                T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                leftUploadSu = false;
                hideLoading();
            }

        }

        if (backPic != null) {
            // 上传右边
            RequestParams myparams = new RequestParams();
            try {

                myparams.put("back", rightFile);

                JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if ("0".equals(response.getString("error"))) {
                                // 上传成功
                                backPicPath = ((JSONObject) response.get("data")).getString("savepath") + ((JSONObject) response.get("data")).getString("savename");
                                rightUploadSu = true;
                                // 只有当两张照片都成功上传才能调用
                                if (leftUploadSu) {
                                    // 调用修改订单，上传
                                    // 调用修改订单，上传
                                    RequestParams params1 = new RequestParams();
                                    params1.add("order_code", order_code);
                                    params1.add("receiver_id_card", idcardNo.getText().toString());
                                    params1.add("receiver_id_card_img", frontPicPath);
                                    params1.add("receiver_id_card_img_back", backPicPath);

                                    JsonHttpResponseHandler handler1 = new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            super.onSuccess(statusCode, headers, response);
                                            try {
                                                if ("0".equals(response.getString("error"))) {
                                                    EventBus.getDefault().post(true,"checkIDCard");
                                                    T.showShort(MyApplication.getInstans(), getString(R.string.upload_suc));
                                                    finish();
                                                } else {
                                                    T.showShort(MyApplication.getInstans(), response.getString("data"));
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
                                            T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                                            hideLoading();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);
                                            T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                                            hideLoading();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);
                                            T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                                            hideLoading();
                                        }
                                    };
                                    handler1.setTag(TAG);
                                    BirdApi.uploadIDCard(MyApplication.getInstans(), params1, handler1);
                                }
                            } else {
                                T.showShort(MyApplication.getInstans(), response.getString("data"));
                                rightUploadSu = false;
                                hideLoading();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rightUploadSu = false;
                            hideLoading();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                        rightUploadSu = false;
                        hideLoading();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                        rightUploadSu = false;
                        hideLoading();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        T.showShort(MyApplication.getInstans(), getString(R.string.upload_fail));
                        rightUploadSu = false;
                        hideLoading();
                    }
                };
                handler.setTag(TAG);
                BirdApi.uploadIDCardPic(MyApplication.getInstans(), myparams, handler);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                T.showShort(MyApplication.getInstans(), getString(R.string.pic_notfound));
                rightUploadSu = false;
                hideLoading();
            }

        }

    }

    @Override
    protected void onDestroy() {
        BirdApi.cancelRequestWithTag(TAG);
        super.onDestroy();
    }

    class MyTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            // 先将照片进行压缩并保存
            String dirpath = getSDPath() + File.separator + "Bird";
            String filepath = getSDPath() + File.separator + "Bird" + File.separator + "frontPic.jpg";
            File dir = new File(dirpath);
            leftFile = new File(filepath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!leftFile.exists()) {
                try {
                    leftFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ImageUtils.saveBitmapFile(ImageUtils.comp(frontPic.getAbsolutePath(), 1900), leftFile.getAbsolutePath());

            // 压缩右边图片
            // 先将照片进行压缩并保存
            String filepath2 = getSDPath() + File.separator + "Bird" + File.separator + "backPic.jpg";
            File dir2 = new File(dirpath);
            rightFile = new File(filepath2);
            if (!dir2.exists()) {
                dir2.mkdirs();
            }
            if (!rightFile.exists()) {
                try {
                    rightFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ImageUtils.saveBitmapFile(ImageUtils.comp(backPic.getAbsolutePath(), 2000), rightFile.getAbsolutePath());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Message message = Message.obtain();
            message.what = COMPRESS_DOWN;
            myHandler.sendMessage(message);
        }
    }
}
