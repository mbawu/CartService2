package com.xqxy.baseclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import cn.jpush.android.api.JPushInterface;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.xqxy.model.AutoLogin;
import com.xqxy.model.Car;

public class MyApplication extends Application {

	public static MyHttpClient client;// 网络请求的终端
	public static ArrayList<BaseActivity> list;// 记录所有存在的activity
	public static SharedPreferences sp; // 本地存储SharedPreferences
	public static Editor ed; // 本地存储编辑器Editor

	public static String identity;
	public static boolean loginStat = false;
	public static boolean refresh = false; // 是否需要刷新

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		/*
		 * 初始化Volley框架的Http工具类
		 */

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

		// 初始化JPUSH
		JPushInterface.init(getApplicationContext());
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
		String carJson = sp.getString("car", null);
		if (carJson != null) {
			car = JsonUtil.fromJson(carJson, Car.class);
		}
		String autoLoginJson = sp.getString("autoLogin", null);
		if (autoLogin != null && !"".equals(autoLoginJson)) {
			autoLogin = JsonUtil.fromJson(autoLoginJson, AutoLogin.class);
		}
		
		guide = sp.getString("guide", null);
	};

	private Car car;

	public Car getCar() {

		return car;
	}

	public void setCar(Car car) {
		this.car = car;
		ed.putString("car", JsonUtil.toJson(car));
		ed.commit();
	}

	private AutoLogin autoLogin;

	public AutoLogin getAutoLogin() {
		return autoLogin;
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
	

}
