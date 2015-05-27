package com.cn.hongwei;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.xqxy.carservice.R;
import com.xqxy.model.Car;
import com.xqxy.model.UserInfo;
import com.xqxy.person.Cst;
import com.xqxy.person.MessageActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	public void notifyMsg(Context context, String msg) {
		NotificationManager mNotificationManager = MyApplication.mNotificationManager;
		//定义通知栏展现的内容信息
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = msg;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
         
        //定义下拉通知栏时要展现的内容信息
//        Context context = getApplicationContext();
        notification.flags = Notification.FLAG_AUTO_CANCEL; // 设置默认声音
        notification.defaults |= Notification.DEFAULT_SOUND; // 设定震动(需加VIBRATE权限)
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        CharSequence contentTitle = msg;
        CharSequence contentText = msg;
        Intent notificationIntent = new Intent(context, MessageActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentIntent);
         
        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(1, notification);
        mNotificationManager.cancel(-5);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			notifyMsg(context,message);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Log.i("test", "extras-->" + extras);
			JSONObject ob;
			try {
				ob = new JSONObject(extras);
				// 推送给部分人
				if (!ob.getBoolean("isAll")) {
					// 用户在登录状态下
					if (MyApplication.loginStat) {
						MyApplication myApp = (MyApplication) context
								.getApplicationContext();
						UserInfo user = myApp.getUserInfo();
						Car car = myApp.getCar();
						// 根据性别和车型推送
						if (!ob.isNull("sex") && !ob.isNull("bid")) {
							if(car==null)
								return;
							if (!ob.getString("sex").equals(user.getSex())
									|| !ob.getString("bid")
											.equals(car.getBid())
									|| !ob.getString("sid")
											.equals(car.getSid())
									|| !ob.getString("sid")
											.equals(car.getSid())) {
								return;
							}
						} else if (!ob.isNull("sex") && ob.isNull("bid")) {
							if (!user.getSex().equals(ob.getString("sex"))) {
								return;
							}
						} else if (ob.isNull("sex") && !ob.isNull("bid")) {
							if(car==null)
								return;
							if (!ob.getString("bid").equals(car.getBid())
									|| !ob.getString("sid")
											.equals(car.getSid())
									|| !ob.getString("sid")
											.equals(car.getSid())) {
								return;
							}
						} else if (!ob.isNull("uid")) {
							JSONArray uidArray=ob.getJSONArray("uid");
							int count=0;
							for (int i = 0; i < uidArray.length(); i++) {
//								uidArray.get(i).toString();
								if (user.getUid().equals(uidArray.get(i).toString())) {
									count++;
								}
							}
							if(count==0)
								return;
						}
					}
					// 用户未登录不显示在通知栏
					else {
						return;
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "推送消息格式出错，请后台人员修正", Toast.LENGTH_SHORT).show();
			}

			notifyMsg(context,message);
			// 接收到消息推送以后通知改变消息数量
			Intent mIntent = new Intent(Cst.GET_RECEIVE);
			// 发送广播
			context.sendBroadcast(mIntent);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			//打开自定义的Activity
			Intent i = new Intent(context, MessageActivity.class);
			MyApplication.list.get(0).startActivity(i);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}


}
