package com.xqxy.baseclass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BaseActivity extends Activity {
	private ArrayList<NetworkAction> requesType;// 记录当前页面所有的网络请求类型
	// private Dialog progressDialog; // 整个页面的进度条对话框
	// private User.LoginCallbackCH loginCallbackCH;
	private boolean getResualt = false;// 判断是否获取到了返回的结果
	// private NetStatus netStatus;// 网络监听对象
	private long exitTime = 0;// 记录点击退出的时间间隔

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if(Cst.EXITE)
		// {
		// finish();
		// System.exit(0);
		// }
		requesType = new ArrayList<NetworkAction>();
		// // 启动网络监听
		// if (getClass().getSimpleName().equals("HomePagerActivity")) {
		// netStatus = new NetStatus(this);
		// }
		// 每创建一个activity就加入list中
		MyApplication.list.add(this);
	}

	//
	@Override
	protected void onResume() {
		// if(Cst.EXITE)
		// {
		// finish();
		// System.exit(0);
		// }else{
		// MobclickAgent.onResume(this);
		// }
		super.onResume();
		JPushInterface.onResume(this);
	}

	 @Override
	 protected void onPause() {
//	 if(!Cst.EXITE)
//	 MobclickAgent.onResume(this);
	 super.onPause();
	 JPushInterface.onPause(this);
	 }
	// /**
	// * 显示一个加载中的整个页面的进度条
	// */
	// public void showProgressDialog() {
	// LayoutInflater inflater = LayoutInflater.from(this);
	// View v = inflater.inflate(com.ch.chtvshop.R.layout.progress_dialog,
	// null);// 得到加载view
	// LinearLayout layout = (LinearLayout) v
	// .findViewById(com.ch.chtvshop.R.id.dialog_view);// 加载布局
	// ImageView spaceshipImage = (ImageView) v
	// .findViewById(com.ch.chtvshop.R.id.img);
	// // 加载动画
	// Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
	// com.ch.chtvshop.R.anim.load_animation);
	// // 使用ImageView显示动画
	// spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	// progressDialog = new Dialog(this,
	// com.ch.chtvshop.R.style.loading_dialog);
	// progressDialog.setContentView(layout, new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.FILL_PARENT,
	// LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
	// progressDialog.setCancelable(true);
	//
	// // 取消进度条的时候取消网络请求
	// progressDialog.setOnCancelListener(new OnCancelListener() {
	//
	// @Override
	// public void onCancel(DialogInterface dialog) {
	// cancelNetRequest();
	// progressDialog.dismiss();
	// }
	// });
	// // 进度条关闭事件
	// progressDialog.setOnDismissListener(new OnDismissListener() {
	//
	// @Override
	// public void onDismiss(DialogInterface dialog) {
	// // 如果有网络请求并且还没有拿到结果的时候
	// if (!getResualt)
	// setOnDialogDismissListener();
	// }
	// });
	// // 让ProgressDialog显示
	// progressDialog.show();
	// }
	//
	// /**
	// * 进度条的关闭事件,前提是在数据未获取到之前用户点击了返回按钮而关闭进度条的时候响应的事件,默认为关闭当前页
	// *
	// * @param dismissListener
	// */
	// public void setOnDialogDismissListener() {
	// finish();
	// }
	//
	// /**
	// * 关闭整个页面的进度条
	// */
	// public void closeProgressDialog() {
	// progressDialog.dismiss();
	// }
	//
	/**
	 * 向服务器发起网络请求的方法，发送成功执行showResualt方法则可根据网络请求类型解析结果
	 * 发送失败，例如访问目标服务器出错等提示"访问服务器出错!"。
	 * 
	 * @param requestType
	 *            需要请求的接口类型
	 */
	public void sendData(RequestWrapper requestWrapper,
			final NetworkAction requestType) {
		String url = Cst.HOST;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		// if (requestType.equals(NetworkAction.user_login)) {
		// // paramMap.put("phone", requestWrapper.getUserName());
		// // paramMap.put("password", requestWrapper.getPassword());
		// url += "/user/";
		// }
		// Log.i("test", "identity-->"+MyApplication.identity);
		// if(MyApplication.identity!=null)
		// paramMap.put("identity", MyApplication.identity);
		paramMap = getMap(requestWrapper);
//		Log.i("test",
//				MyApplication.getUrl(paramMap,
//						Cst.HOST + requestType.toString()));
		MyApplication.client.postWithURL(url, paramMap, requestType,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						onResponseEvent(response, requestType);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i(Cst.TAG, "Volley error:" + error.toString());
						sendDataErrorResponse(requestType);
					}
				});

	}

	/**
	 * get请求方式
	 * 
	 * @param requestWrapper
	 * @param requestType
	 */
	public void sendDataByGet(RequestWrapper requestWrapper,
			final NetworkAction requestType) {
		String url = Cst.HOST;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		// if (requestType.equals(NetworkAction.car_brand)
		// || requestType.equals(NetworkAction.car_series)
		// || requestType.equals(NetworkAction.car_model)) {
		// url += "car/";
		// }
//		Log.i("test",
//				MyApplication.getUrl(paramMap,
//						Cst.HOST + requestType.toString()));
		MyApplication.client.getWithURL(url, getMap(requestWrapper),
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						onResponseEvent(response, requestType);
					}
				}, requestType, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i(Cst.TAG, "Volley error:" + error.toString());
						sendDataErrorResponse(requestType);
					}
				});
	}

	public void onResponseEvent(JSONObject response, NetworkAction requestType) {
		// 重置返回结果值
		getResualt = false;
		// 先分析返回code值，正确执行showResualt，错误直接输出结果
		boolean done = false;
		String msg = "";
		Log.i("response", response.toString());
		try {
			done = response.getBoolean("done");
			msg = response.getString("msg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i(Cst.TAG, response.toString());
		// 如果数据返回正确的时候正常执行showResualt
		if (done) {
			if (!requesType.contains(requestType))
				requesType.add(requestType);
			// Log.i(Cst.TAG,
			// "" + requestType + response.toString());
			// ResponseWrapper responseWrapper =
			// jsonToClass(response
			// .toString());
			ResponseWrapper responseWrappe = jsonToClass(response.toString());
			// Log.i(Cst.TAG,""+(responseWrappe.getBrand().get(0).getName()));
			showResualt(responseWrappe, requestType);

		}
		// 否则输出错误信息
		else {
			// 提示网络异常
			// DialogUtil.showToast(BaseActivity.this, msg);
			Log.i(Cst.TAG, msg);
			Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
			// if (progressDialog != null)
			// progressDialog.dismiss();
			// else if (progressDialog == null)
			// getErrorMsg(requestType);
		}

	}

	/**
	 * 解析和显示服务器返回的结果
	 * 
	 * @param responseWrapper
	 *            返回的ResponseWrapper格式的结果
	 * @param requestType
	 *            网络请求类型
	 */
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// 重置获取到数据状态的值
		getResualt = true;
		// if (progressDialog != null)
		// {
		// progressDialog.dismiss();
		// progressDialog=null;
		// }
		if (Cst.EXITE)
			return;
	}

	//
	public ResponseWrapper jsonToClass(String json) {
		return JsonUtil.fromJson(json, ResponseWrapper.class);
	}

	//
	// /**
	// * 当访问服务器返回code不为1000时候的处理方法，前提是在没有使用全局进度条的情况下
	// */
	// public void getErrorMsg(NetworkAction requestType) {
	//
	// }
	//
	/**
	 * 当Volley访问服务器出错时执行的方法
	 */
	public void sendDataErrorResponse(NetworkAction requestType) {
		// if (progressDialog != null)
		// progressDialog.dismiss();
		// DialogUtil.showToast(
		// BaseActivity.this,
		// getResources().getString(
		// com.ch.chtvshop.R.string.error_code_msg));
	}

	//
	// /**
	// * 手动取消该页面某个类型的网络请求，前提是该请求还未返回结果
	// *
	// * @param request
	// */
	// public void cancelRequest(NetworkAction request) {
	// CHApplication.client.getmRequestQueue().cancelAll(request);
	// if (requesType.contains(request))
	// requesType.remove(request);
	// }
	//
	/**
	 * 退出该页面之前查看是否有未返回的请求，如果有的话将所有未返回结果的请求取消
	 */
	@Override
	protected void onDestroy() {
		// if (netStatus != null)
		// netStatus.unregisteredBroad(this);// 注销网络监听广播
		cancelNetRequest();
		MyApplication.list.remove(this);
		// Log.i(Cst.TAG, getClass().getSimpleName() + "-->onDestroy");
		super.onDestroy();
	}

	/**
	 * 取消该页面的所有的网络请求
	 */
	private void cancelNetRequest() {
		for (int i = 0; i < requesType.size(); i++) {
			MyApplication.client.getmRequestQueue()
					.cancelAll(requesType.get(i));
		}
		requesType.clear();
	}

	//
	// /**
	// * 用户相关页面的跳转
	// *
	// * @param cls
	// * 需要跳转到的目标页class
	// */
	// public void goToPage(Class<?> cls) {
	// final Intent intent = new Intent();
	// intent.setClass(this, cls);
	// if (User.getInstance().getIsLogin()) {
	// startActivity(intent);
	// } else {
	// loginCallbackCH = new LoginCallbackCH() {
	//
	// @Override
	// public void onSuccess(Session session) {
	// goToPageSuccess();
	// Log.i(Cst.TAG, "LoginCallbackCH->onSuccess");
	// startActivity(intent);
	// }
	//
	// @Override
	// public void onFailure(int code, String msg) {
	// goToPageFailure();
	// // DialogUtil.showToast(BaseActivity.this, getResources()
	// // .getString(com.ch.chtvshop.R.string.login_failure));
	// }
	// };
	// User.getInstance().showLogin(this, loginCallbackCH);
	// }
	// }
	//
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// CallbackContext.onActivityResult(requestCode, resultCode, data);
	// }
	//
	// /**
	// * 跳转页面登录成功以后执行的方法
	// */
	// public void goToPageSuccess() {
	// Log.i(Cst.TAG, "goToPageSuccess");
	// }
	//
	// /**
	// * 跳转页面登录失败以后执行的方法
	// */
	// public void goToPageFailure() {
	// Log.i(Cst.TAG, "goToPageSuccess");
	// }
	//
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // 判断点击的返回键
	// if (keyCode == KeyEvent.KEYCODE_BACK
	// && event.getAction() == KeyEvent.ACTION_DOWN) {
	// // 需要点击返回键退出的页面
	// if (getClass().getSimpleName().equals("HomePagerActivity")
	// || getClass().getSimpleName().equals("NetActivity")
	// || getClass().getSimpleName().equals("LaunchActivity")) {
	// exit();
	// return true;
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	//
	// }
	//
	// /**
	// * 退出整个程序，先关闭所有的activity页面再杀掉主线程。
	// */
	// public void exit() {
	// Log.w(Cst.TAG, "activity->"+getClass().getSimpleName());
	// // 判断是否在两秒内连按两次返回键
	// if ((System.currentTimeMillis() - Cst.exitTime) > 2000) {
	// DialogUtil.showToast(getApplicationContext(),
	// getResources().getString(com.ch.chtvshop.R.string.exit));
	// Cst.exitTime = System.currentTimeMillis();
	// } else {
	// finish();
	// Cst.EXITE = true;
	// Intent intent = new Intent();
	// intent.setClass(this, HomePagerActivity.class);
	// startActivity(intent);
	//
	// }
	// }

	public static HashMap<String, String> getMap(Object thisObj) {
		HashMap<String, String> map = new HashMap<String, String>();
		Class c;
		try {
			c = Class.forName(thisObj.getClass().getName());
			Method[] m = c.getMethods();
			Field[] fileds = c.getFields();
			for (int i = 0; i < m.length; i++) {
				String method = m[i].getName();
				if (method.startsWith("get")) {
					try {
						Object value = m[i].invoke(thisObj);
						if (value != null) {
							String key = method.substring(3);
							key = key.substring(0, 1).toLowerCase()
									+ key.substring(1);
							if (key.equals("class"))
								continue;
							map.put(key, value.toString());
						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("error:" + method);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

}
