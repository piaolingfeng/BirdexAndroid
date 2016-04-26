package com.birdex.bird.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.birdex.bird.R;
import com.birdex.bird.activity.MsgDetailActivity;
import com.birdex.bird.greendao.DaoMaster;
import com.birdex.bird.greendao.DaoSession;
import com.birdex.bird.greendao.NotifiMsg;
import com.birdex.bird.greendao.NotifiMsgDao;
import com.birdex.bird.util.Constant;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.HelperUtil;
import com.birdex.bird.util.StringUtils;
import com.ta.utdid2.android.utils.SystemUtils;
import com.umeng.common.message.Log;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// 此服务是基于完全自定义消息来开启应用服务进程的示例
// 开发这可以仿照此服务来重写自己的服务，然后在服务中
//做相应的操作，比如打开应用，或者打开应用的主进程服务等
//这样可以唤醒应用的主进程服务，重启应用的服务
public class NotificationService extends UmengBaseIntentService {

    private static final String TAG = TestService.class.getName();
    private NotificationCompat.Builder builder = null;
    private RemoteViews myNotificationView = null;
    private NotificationManager notifiManager = null;
    private Bitmap bitmap = null;

    //设置
    private NotifiMsg notimsg = null;
    //设置数据库的操作
    private SQLiteDatabase db = null;
    private DaoMaster daoMaster = null;
    private DaoSession daoSession = null;
    private NotifiMsgDao msgDao = null;
    private SimpleDateFormat format = null;

    //设置声音的
//    private Uri sounduri=null;
    //点击notification打开activity
    private SharedPreferences sharedPreferences = null;
    //
    private PushAidlImpl pushAidl = null;
    //记录上次的获取时间
    private static long time1=0;
    private DaoMaster.DevOpenHelper helper =null;
    @Override
    public void onCreate() {
        super.onCreate();
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "Bird", null);
        android.util.Log.e("android","---------------start service");
        helper = new DaoMaster.DevOpenHelper(this, Constant.DBName, null);

        //设置时间的格式
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        builder = new NotificationCompat.Builder(this);
        myNotificationView = new RemoteViews(this.getPackageName(), R.layout.push_message_layout);
        notifiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notifi_small);
//        sounduri=Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.fv);
        sharedPreferences = getSharedPreferences(Constant.SP_NAME, Activity.MODE_PRIVATE);
        pushAidl = new PushAidlImpl(this);
//        time1=(new Date()).getTime();
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        // 需要调用父类的函数，否则无法统计到消息送达
        super.onMessage(context, intent);
        try {
            android.util.Log.e("android", "1-------------------------");
            //可以通过MESSAGE_BODY取得消息体
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
            msgDao = daoSession.getNotifiMsgDao();
            Log.e("android", "1----------message=" + message);    //消息体
            Log.e("android", "1----------custom=" + msg.custom);    //自定义消息的内容
            Log.e("android", "1----------title=" + msg.title);    //通知标题
            Log.e("android", "1----------text=" + msg.text);    //通知内容
            if (msg.custom != null && !TextUtils.isEmpty(msg.custom)) {
                notimsg = getMsgEntity(msg.custom);
                notimsg.setIsread(false);
                notimsg.setMsgdate(format.format(new Date()));
                myNotificationView.setTextViewText(R.id.tv_message_title, StringUtils.ToDBC(notimsg.getTitle()));
                myNotificationView.setTextViewText(R.id.tv_message_text,StringUtils.ToDBC(notimsg.getTitle()));
                //判断是否app进程是否在运行
//                if(com.birdex.bird.util.SystemUtils.isAppAlive(context,Constant.APPPackageName)){
//
//                }else{
//
//                }
                //设置点击打开activity事件
                Intent intent1 = new Intent(this, MsgDetailActivity.class);
                intent1.putExtra("title", notimsg.getTypeid());
                intent.setAction(Constant.NotiAction1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 12, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContent(myNotificationView)
                        .setSmallIcon(R.drawable.notifi_small)
                        .setTicker(msg.ticker)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setAutoCancel(true);
                boolean sound = sharedPreferences.getBoolean(Constant.TONE_SETTING, true);
                boolean soundtime = sharedPreferences.getBoolean(Constant.TIME_SETTING, true);

                long time2=new Date().getTime();
                if(((time2-time1)/1000/60)>3){
                    if (sound) {
                        if (soundtime) {
                            builder.setDefaults(Notification.DEFAULT_SOUND);
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            if (hour >= 9 && hour <= 21) {
                                builder.setDefaults(Notification.DEFAULT_SOUND);
                            }
                        }
                    }
                }
                time1=time2;
//                if (sound) {
//                    if (soundtime) {
//                        builder.setDefaults(Notification.DEFAULT_SOUND);
//                    } else {
//                        Calendar calendar = Calendar.getInstance();
//                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                        if (hour >= 9 && hour <= 21) {
//                            builder.setDefaults(Notification.DEFAULT_SOUND);
//                        }
//                    }
//                }
                notifiManager.notify(0, builder.build());
                msgDao.insert(notimsg);
                db.close();
            } else {
                myNotificationView.setTextViewText(R.id.tv_message_title, StringUtils.ToDBC(msg.title));
                myNotificationView.setTextViewText(R.id.tv_message_text, StringUtils.ToDBC(msg.text));

                builder.setContent(myNotificationView)
                        .setSmallIcon(R.drawable.notifi_small)
                        .setTicker(msg.ticker)
                        .setAutoCancel(true);

                boolean sound = sharedPreferences.getBoolean(Constant.TONE_SETTING, true);
                boolean soundtime = sharedPreferences.getBoolean(Constant.TIME_SETTING, true);

                long time2=new Date().getTime();
                if(((time2-time1)/1000/60)>3){
                    if (sound) {
                        if (soundtime) {
                            builder.setDefaults(Notification.DEFAULT_SOUND);
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            if (hour >= 9 && hour <= 21) {
                                builder.setDefaults(Notification.DEFAULT_SOUND);
                            }
                        }
                    }
                }
                time1=time2;
                notifiManager.notify(0, builder.build());
                notimsg = new NotifiMsg();
                notimsg.setIsread(false);
                notimsg.setMsgtext(msg.text);
                notimsg.setTitle(msg.title);
                notimsg.setTypeid("self");
                notimsg.setMsgdate(format.format(new Date()));
                msgDao.insert(notimsg);
                db.close();
            }
//			myNotificationView.setTextViewText(R.id.tv_message_title, msg.title);
//			myNotificationView.setTextViewText(R.id.tv_message_text, msg.custom);
//			builder.setContent(myNotificationView)
//					.setSmallIcon(R.drawable.notifi_small)
//					.setTicker(msg.ticker)
//					.setAutoCancel(true);
//			notifiManager.notify(120, builder.build());
            // 对完全自定义消息的处理方式，点击或者忽略
            boolean isClickOrDismissed = true;
            if (isClickOrDismissed) {
                //完全自定义消息的点击统计
                UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
            } else {
                //完全自定义消息的忽略统计
                UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
            }

            // 使用完全自定义消息来开启应用服务进程的示例代码
            // 首先需要设置完全自定义消息处理方式
            // mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
            // code to handle to start/stop service for app process
            JSONObject json = new JSONObject(msg.custom);
            String topic = json.getString("topic");
            if (topic != null && topic.equals("appName:startService")) {
                // 在友盟portal上新建自定义消息，自定义消息文本如下
                //{"topic":"appName:startService"}
                if (HelperUtil.isServiceRunning(context, NotificationService.class.getName()))
                    return;
                Intent intent1 = new Intent();
                intent1.setClass(context, NotificationService.class);
                context.startService(intent1);
            } else if (topic != null && topic.equals("appName:stopService")) {
                // 在友盟portal上新建自定义消息，自定义消息文本如下
                //{"topic":"appName:stopService"}
                if (!HelperUtil.isServiceRunning(context, NotificationService.class.getName()))
                    return;
                Intent intent1 = new Intent();
                intent1.setClass(context, NotificationService.class);
                context.stopService(intent1);
            }
        } catch (Exception e) {
            Log.e("android", e.getMessage());
        }
    }

    //将获取的消息进行解析
    public NotifiMsg getMsgEntity(String json) {
        NotifiMsg msg = GsonHelper.getPerson(json, NotifiMsg.class);
        return msg;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
* START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
* 随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
* START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务。
* START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
* START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
* */
        flags = START_REDELIVER_INTENT;//如果服务被异常kill掉，系统会自动重启该服务
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return pushAidl;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.util.Log.e("android","---------------stop service");
    }
}
