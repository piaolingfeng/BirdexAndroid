package com.birdex.bird.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.birdex.bird.util.HelperUtil;
import com.umeng.common.message.Log;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

// 此服务是基于完全自定义消息来开启应用服务进程的示例
// 开发这可以仿照此服务来重写自己的服务，然后在服务中
//做相应的操作，比如打开应用，或者打开应用的主进程服务等
//这样可以唤醒应用的主进程服务，重启应用的服务
public class NotificationService extends UmengBaseIntentService {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// code to handle to create service
		// ......
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// code to handler to start service
		// ......
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		stopForeground(true);
		Intent intent = new Intent("com.dbjtech.waiqin.destroy");
		sendBroadcast(intent);
		super.onDestroy();
	}

	private static final String TAG = TestService.class.getName();

	@Override
	protected void onMessage(Context context, Intent intent) {
		// 需要调用父类的函数，否则无法统计到消息送达
		super.onMessage(context, intent);
		try {
			//可以通过MESSAGE_BODY取得消息体
			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
			UMessage msg = new UMessage(new JSONObject(message));
			Log.d(TAG, "message=" + message);    //消息体
			Log.d(TAG, "custom="+msg.custom);    //自定义消息的内容
			Log.d(TAG, "title="+msg.title);    //通知标题
			Log.d(TAG, "text="+msg.text);    //通知内容
			// code  to handle message here
			// ...

			// 对完全自定义消息的处理方式，点击或者忽略
			boolean isClickOrDismissed = true;
			if(isClickOrDismissed) {
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
			Log.d(TAG, "topic="+topic);
			if(topic != null && topic.equals("appName:startService")) {
				// 在友盟portal上新建自定义消息，自定义消息文本如下
				//{"topic":"appName:startService"}
				if(HelperUtil.isServiceRunning(context, NotificationService.class.getName()))
					return;
				Intent intent1 = new Intent();
				intent1.setClass(context, NotificationService.class);
				context.startService(intent1);
			} else if(topic != null && topic.equals("appName:stopService")) {
				// 在友盟portal上新建自定义消息，自定义消息文本如下
				//{"topic":"appName:stopService"}
				if(!HelperUtil.isServiceRunning(context, NotificationService.class.getName()))
					return;
				Intent intent1 = new Intent();
				intent1.setClass(context, NotificationService.class);
				context.stopService(intent1);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
}
