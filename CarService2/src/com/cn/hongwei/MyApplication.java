package com.cn.hongwei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.cn.hongwei.BaiduLoction.LocationCallback;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xqxy.model.AutoLogin;
import com.xqxy.model.Car;
import com.xqxy.model.UserInfo;
import com.xqxy.person.Cst;
import com.xqxy.person.NetworkAction;

public class MyApplication extends Application {

	public static MyHttpClient client;// 网络请求的终端
	public static ArrayList<BaseActivity> list;// 记录所有存在的activity
	public static SharedPreferences sp; // 本地存储SharedPreferences
	public static Editor ed; // 本地存储编辑器Editor

	public static String identity;
	public static boolean loginStat = false;
	public static boolean refresh = false; // 是否需要刷新
	public static NotificationManager mNotificationManager;
	public static String lng = "0";
	public static String lat = "0";
	public static String address = "";
	public static String detail = "";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		/*
		 * 初始化Volley框架的Http工具类
		 */
		ShareSDK.initSDK(this);
		client = MyHttpClient.getInstance(MyApplication.this
				.getApplicationContext());
		BaiduLoction.getInstance().init(this);
		// AppInfo.init(this);
		// RelayoutTool.initScreenScale(this);
		initImageLoader(this);
		// initTaeSDK();
		list = new ArrayList<BaseActivity>();
		// UpgradeManager.getInstence().init(this);
		// 初始化SharedPreferences
		sp = getSharedPreferences("CarService", MODE_PRIVATE);
		ed = sp.edit();

		initSharePreferenceData();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 初始化JPUSH
		JPushInterface.init(getApplicationContext());
		getLocation();
	}

	private void getLocation() {
		BaiduLoction.getInstance().startLocation();

		BaiduLoction.getInstance().setLocationCallback(new LocationCallback() {

			@Override
			public void locationResult(BDLocation location) {
			
					address = location.getProvince() + location.getCity()
							+ location.getDistrict();
					detail = location.getStreet() + location.getStreetNumber();
					lng = String.valueOf(location.getLongitude());
					lat = String.valueOf(location.getLatitude());
					if (loginStat) {
					HashMap<String, String> pramer = new HashMap<String, String>();
					pramer.put("identity", identity);
					pramer.put("lng", lng);
					pramer.put("lat", lat);
					client.postWithURL(Cst.HOST, pramer,
							NetworkAction.centerF_location,
							new Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject arg0) {
									// TODO Auto-generated method stub

								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError arg0) {
									// TODO Auto-generated method stub

								}
							});
				}

			}

		});

	}

	// 获取拼接出来的请求字符串
	public static String getUrl(HashMap<String, String> paramter, String url) {
		Iterator iter = paramter.entrySet().iterator();
		int count = 0;
		while (iter.hasNext()) {
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			if (count == 0)
				url = url + "?" + key + "=" + val;
			else
				url = url + "&" + key + "=" + val;
			count++;
		}
		return url;
	}

	/**
	 * 初始化图片缓存模块，根据实际需要设置必要的选项。
	 * 
	 * @param ctx
	 */
	private void initImageLoader(Context ctx) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				ctx).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheSize(32 * 1024 * 1024)
				.memoryCacheSize(4 * 1024 * 1024).enableLogging().build();
		ImageLoader.getInstance().init(config);
	}

	private void initSharePreferenceData() {
		String carJson = sp.getString("car", "");
		if (!carJson.equals("")) {
			car = JsonUtil.fromJson(carJson, Car.class);
		}
		String autoLoginJson = sp.getString("autoLogin", null);
		if (autoLogin != null && !"".equals(autoLoginJson)) {
			autoLogin = JsonUtil.fromJson(autoLoginJson, AutoLogin.class);
			MyApplication.loginStat = autoLogin.isLoginState();
		}

		guide = sp.getString("guide", null);
	};

	private Car car;

	public Car getCar() {

		return car;
	}

	public void setCar(Car car) {
		this.car = car;
		if (car != null) {
			ed.putString("car", JsonUtil.toJson(car));
		} else
			ed.putString("car", "");
		ed.commit();
	}

	private AutoLogin autoLogin;

	public AutoLogin getAutoLogin() {
		try {
			String info = sp.getString("autoLogin", "");
			return JsonUtil.fromJson(info, AutoLogin.class);
		} catch (Exception e) {
			return null;
		}

	}

	public void setAutoLogin(AutoLogin autoLogin) {
		this.autoLogin = autoLogin;
		if (autoLogin == null) {
			ed.putString("autoLogin", "");
			ed.commit();
		} else {
			ed.putString("autoLogin", JsonUtil.toJson(autoLogin));
			ed.commit();
		}

	}

	private String guide;

	public String getGuide() {
		return guide;
	}

	public void setGuide(String guide) {
		this.guide = guide;
		ed.putString("guide", guide);
		ed.commit();
	}

	public static UserInfo getUserInfo() {
		try {
			String info = sp.getString("userInfo", "");
			return JsonUtil.fromJson(info, UserInfo.class);
		} catch (Exception e) {
			return null;
		}

	}

	public static void setUserInfo(UserInfo userInfo) {
		if (userInfo == null) {
			ed.putString("userInfo", "");
			ed.commit();
		} else {
			ed.putString("userInfo", JsonUtil.toJson(userInfo));
			ed.commit();
		}

	}

	public NotificationManager getmNotificationManager() {
		return mNotificationManager;
	}

	public void setmNotificationManager(NotificationManager mNotificationManager) {
		this.mNotificationManager = mNotificationManager;
	}

}
