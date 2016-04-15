package com.birdex.bird;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.birdex.bird.entity.User;
import com.birdex.bird.service.NotificationService;
import com.birdex.bird.util.Constant;
import com.birdex.bird.util.CrashHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuming.zhuang on 2016/3/18.
 */
public class MyApplication extends Application {
    private static MyApplication instants;
    //获取包名
    private static final String TAG = MyApplication.class.getName();
    //推送设置
    private PushAgent mPushAgent;

    public static AsyncHttpClient ahc;
    public static String appName = "bird";
    // 推送设备号
    public static String device_token;
    // app 版本号
    public static String app_version = "";
    // 设备信息
    public static String device_info;
    // 设备类型 这里写死为 android
    public static String device_type = "android";

    public static List<Activity> activityList = new ArrayList<>();

    // 登录的相关信息 user
    public static User user;


    // 清除 activity 栈
    public void clearActivities(){
        for(Activity activity: activityList){
            if(activity != null ){
                activity.finish();
            }
        }
    }

    // 获取当前版本号
    private String getVersionLocal(){
        String versionLocal = "";
        try {
            // 取得当前版本号
            PackageManager manager = MyApplication.getInstans().getPackageManager();
            PackageInfo info = null;
            info = manager.getPackageInfo(MyApplication.getInstans().getPackageName(), 0);
            versionLocal = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionLocal;
    }

    // 获取设备信息
    private String getDevice_info(){
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();
        sb.append("\nDeviceModel = " + android.os.Build.MODEL);
        sb.append("\nDeviceVERSION_RELEASE = " + android.os.Build.VERSION.RELEASE);
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSimState = " + tm.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
        Log.e("info", sb.toString());
        return sb.toString();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instants = this;

        initFile();
//        iniCrash();

        app_version = getVersionLocal();
        device_info = getDevice_info();

        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setPushIntentServiceClass(NotificationService.class);
        mPushAgent.setDebugMode(true);

//        UmengMessageHandler messageHandler = new UmengMessageHandler(){
//            /**
//             * 参考集成文档的1.6.3
//             * http://dev.umeng.com/push/android/integration#1_6_3
//             * */
//            @Override
//            public void dealWithCustomMessage(final Context context, final UMessage msg) {
//                new Handler().post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        // 对自定义消息的处理方式，点击或者忽略
//                        boolean isClickOrDismissed = true;
//                        if(isClickOrDismissed) {
//                            //自定义消息的点击统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//                        } else {
//                            //自定义消息的忽略统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
//                        }
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            /**
//             * 参考集成文档的1.6.4
//             * http://dev.umeng.com/push/android/integration#1_6_4
//             * */
//            @Override
//            public Notification getNotification(Context context,
//                                                UMessage msg) {
//                switch (msg.builder_id) {
//                    case 1:
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
//                        builder.setContent(myNotificationView)
//                                .setSmallIcon(getSmallIconId(context, msg))
//                                .setTicker(msg.ticker)
//                                .setAutoCancel(true);
//
//                        return builder.build();
//
//                    default:
//                        //默认为0，若填写的builder_id并不存在，也使用默认。
//                        return super.getNotification(context, msg);
//                }
//            }
//        };
//        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
//        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
//            @Override
//            public void dealWithCustomAction(Context context, UMessage msg) {
//                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//            }
//        };
//        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
//        //参考http://bbs.umeng.com/thread-11112-1-1.html
//        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        mPushAgent.onAppStart();
        //开启推送并设置注册的回调处理
        mPushAgent.enable(new IUmengRegisterCallback() {

            @Override
            public void onRegistered(final String registrationId) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //onRegistered方法的参数registrationId即是device_token
                        Log.e("device_token", registrationId);
                    }
                });
            }
        });
        device_token = UmengRegistrar.getRegistrationId(this);
//        String pkgName = getApplicationContext().getPackageName();
//        Log.e("pkgName", pkgName);
        Log.e("device_token", device_token);
//		if (MiPushRegistar.checkDevice(this)) {
//            MiPushRegistar.register(this, "2882303761517400865", "5501740053865");
//		}

        initAsyncHttpClient();
    }

    private void initFile(){
        File file = new File(Constant.BASEPATH);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 异常扑捉器
     */
    private void iniCrash() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

    }

    public static MyApplication getInstans() {
        if (instants == null) {
            instants = new MyApplication();
        }
        return instants;
    }

    //线程同步网络请求
    private synchronized void initAsyncHttpClient() {
        ahc = new AsyncHttpClient();//获取网络连接超时
        ahc.setTimeout(8 * 1000);//设置30秒超时
        ahc.setConnectTimeout(4 * 1000);//设置30秒超时
        ahc.setMaxConnections(5);
        ahc.addHeader("DEVICE-TOKEN", device_token);
        ahc.addHeader("APP-VERSION",app_version);
    }

}
