package com.xqxy.baseclass;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.xqxy.model.Car;
import com.xqxy.model.UserInfo;
import com.xqxy.person.MessageActivity;

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
			// processCustomMessage(context, bundle);
//			int notifactionId = bundle
//					.getInt("cn.jpush.android.MSG_ID");
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Log.i("test", "extras-->"+extras);
//			JSONObject ob;
//			try {
//				ob = new JSONObject(extras);
//				// 推送给部分人
//				if (!ob.getBoolean("isAll")) {
//					// 用户在登录状态下
//					if (MyApplication.loginStat) {
//						MyApplication myApp = (MyApplication) context
//								.getApplicationContext();
//						UserInfo user = myApp.getUserInfo();
//						Car car = myApp.getCar();
//						// 根据性别和车型推送
//						if (!ob.isNull("sex") && !ob.isNull("bid")) {
//							if (!ob.getString("sex").equals(user.getSex())
//									|| !ob.getString("bid")
//											.equals(car.getBid())
//									|| !ob.getString("sid")
//											.equals(car.getSid())
//									|| !ob.getString("sid")
//											.equals(car.getSid())) {
//								JPushInterface.clearNotificationById(context,
//										notifactionId);
//								return;
//							}
//						}
//						else if(!ob.isNull("sex") && ob.isNull("bid") )
//						{
//							if(!user.getSex().equals(ob.getString("sex")))
//							{
//								JPushInterface.clearLocalNotifications(context);
////								JPushInterface.clearNotificationById(context,
////										notifactionId);
//								return;
//							}
//						}
//						else if(ob.isNull("sex") && !ob.isNull("bid") )
//						{
//							if ( !ob.getString("bid")
//											.equals(car.getBid())
//									|| !ob.getString("sid")
//											.equals(car.getSid())
//									|| !ob.getString("sid")
//											.equals(car.getSid())) {
//								JPushInterface.clearNotificationById(context,
//										notifactionId);
//								return;
//							}
//						}
//						else if(!ob.isNull("uid"))
//						{
//							if(!user.getUid().equals(ob.getString("uid")))
//							{
//								JPushInterface.clearNotificationById(context,
//										notifactionId);
//								return;
//							}
//						}
//					}
//					// 用户未登录不显示在通知栏
//					else {
//						JPushInterface.clearLocalNotifications(context);
//						JPushInterface.clearAllNotifications(context);
////						JPushInterface.clearNotificationById(context,
////								notifactionId);
//						return;
//					}
//
//				}
//
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				Toast.makeText(context, "解析推送消息出错", Toast.LENGTH_SHORT).show();
//			}
//
//			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//			// 接收到消息推送以后通知改变消息数量
//			Intent mIntent = new Intent(Cst.GET_RECEIVE);
//			// 发送广播
//			context.sendBroadcast(mIntent);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			if(notifactionId!=0)
			{
				JPushInterface.clearNotificationById(context,
						notifactionId);
				return;
			}
			
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Log.i("test", "extras-->"+extras);
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
							if (!ob.getString("sex").equals(user.getSex())
									|| !ob.getString("bid")
											.equals(car.getBid())
									|| !ob.getString("sid")
											.equals(car.getSid())
									|| !ob.getString("sid")
											.equals(car.getSid())) {
								JPushInterface.clearNotificationById(context,
										notifactionId);
								return;
							}
						}
						else if(!ob.isNull("sex") && ob.isNull("bid") )
						{
							if(!user.getSex().equals(ob.getString("sex")))
							{
//								JPushInterface.clearLocalNotifications(context);
								JPushInterface.clearNotificationById(context,
										notifactionId);
								return;
							}
						}
						else if(ob.isNull("sex") && !ob.isNull("bid") )
						{
							if ( !ob.getString("bid")
											.equals(car.getBid())
									|| !ob.getString("sid")
											.equals(car.getSid())
									|| !ob.getString("sid")
											.equals(car.getSid())) {
								JPushInterface.clearNotificationById(context,
										notifactionId);
								return;
							}
						}
						else if(!ob.isNull("uid"))
						{
							if(!user.getUid().equals(ob.getString("uid")))
							{
								JPushInterface.clearNotificationById(context,
										notifactionId);
								return;
							}
						}
					}
					// 用户未登录不显示在通知栏
					else {
//						JPushInterface.clearLocalNotifications(context);
//						JPushInterface.clearAllNotifications(context);
						JPushInterface.clearNotificationById(context,
								notifactionId);
						return;
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(context, "解析推送消息出错", Toast.LENGTH_SHORT).show();
				JPushInterface.clearNotificationById(context,
						notifactionId);
				return;
			}

			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
			// 接收到消息推送以后通知改变消息数量
			Intent mIntent = new Intent(Cst.GET_RECEIVE);
			// 发送广播
			context.sendBroadcast(mIntent);
//			int notifactionId = bundle
//					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//				JPushInterface.clearNotificationById(context,
//						notifactionId);
//			JPushInterface.clearAllNotifications(context);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			// //打开自定义的Activity
			Intent i = new Intent(context, MessageActivity.class);
			// i.putExtras(bundle);
			// //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
			// Intent.FLAG_ACTIVITY_CLEAR_TOP );
			// context.startActivity(i);
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

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		// if (MainActivity.isForeground) {
		// String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		// String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		// Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
		// msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
		// if (!ExampleUtil.isEmpty(extras)) {
		// try {
		// JSONObject extraJson = new JSONObject(extras);
		// if (null != extraJson && extraJson.length() > 0) {
		// msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
		// }
		// } catch (JSONException e) {
		//
		// }
		//
		// }
		// context.sendBroadcast(msgIntent);
		// }
	}
}
