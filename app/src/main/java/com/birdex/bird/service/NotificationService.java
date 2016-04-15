package com.birdex.bird.service;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.birdex.bird.R;
import com.birdex.bird.greendao.DaoMaster;
import com.birdex.bird.greendao.DaoSession;
import com.birdex.bird.greendao.NotifiMsg;
import com.birdex.bird.greendao.NotifiMsgDao;
import com.birdex.bird.util.GsonHelper;
import com.birdex.bird.util.HelperUtil;
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
    private SQLiteDatabase db=null;
    private DaoMaster daoMaster=null;
    private DaoSession daoSession=null;
    private NotifiMsgDao msgDao=null;
    private SimpleDateFormat format=null;
    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "Notimsg", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        msgDao=daoSession.getNotifiMsgDao();
        //设置时间的格式
        format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        builder = new NotificationCompat.Builder(this);
        myNotificationView = new RemoteViews(this.getPackageName(), R.layout.push_message_layout);
        notifiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notifi_small);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        // 需要调用父类的函数，否则无法统计到消息送达
        super.onMessage(context, intent);
        try {
            //可以通过MESSAGE_BODY取得消息体
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
//            Log.e("android", "message=" + message);    //消息体
//            Log.e("android", "custom=" + msg.custom);    //自定义消息的内容
//            Log.e("android", "title=" + msg.title);    //通知标题
//			Log.e(TAG, "text=" + msg.text);    //通知内容
            // code  to handle message here
            // ...

//			NotificationCompat.BigPictureStyle pictureStyle = new NotificationCompat.BigPictureStyle();
//			pictureStyle.setBigContentTitle("BigContentTitle")
//					.setSummaryText("SummaryText").bigPicture(bitmap);
//			builder.setSmallIcon(R.drawable.notifi_small)
//					.setLargeIcon(bitmap)
//					.setTicker(msg.ticker)
//					.setContentTitle(msg.title)
//					.setContentText(msg.text)
//					.setDefaults(Notification.DEFAULT_ALL)
//					.setStyle(pictureStyle)
//					.setAutoCancel(true);
//			notifiManager.notify(120, builder.build());
            if (msg.custom != null && !TextUtils.isEmpty(msg.custom)) {
				notimsg=getMsgEntity(msg.custom);
                notimsg.setMsgdate(format.format(new Date()));
                myNotificationView.setTextViewText(R.id.tv_message_title, notimsg.getTitle());
				myNotificationView.setTextViewText(R.id.tv_message_text, notimsg.getMsgtext());
                builder.setContent(myNotificationView)
                        .setSmallIcon(R.drawable.notifi_small)
                        .setTicker(msg.ticker)
                        .setAutoCancel(true);
                notifiManager.notify(120, builder.build());
                msgDao.insert(notimsg);
            } else {
                myNotificationView.setTextViewText(R.id.tv_message_title, msg.title);
                myNotificationView.setTextViewText(R.id.tv_message_text, msg.text);
                builder.setContent(myNotificationView)
                        .setSmallIcon(R.drawable.notifi_small)
                        .setTicker(msg.ticker)
                        .setAutoCancel(true);
                notifiManager.notify(120, builder.build());
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
}
