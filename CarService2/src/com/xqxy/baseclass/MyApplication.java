package com.xqxy.baseclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;



public class MyApplication extends Application {

	public static MyHttpClient client;// 网络请求的终端
	public static ArrayList<BaseActivity> list;// 记录所有存在的activity
	public static SharedPreferences sp; // 本地存储SharedPreferences
	public static Editor ed; // 本地存储编辑器Editor

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		/*
		 * 初始化Volley框架的Http工具类
		 */
		
		client = MyHttpClient.getInstance(MyApplication.this
				.getApplicationContext());
//		AppInfo.init(this);
//		RelayoutTool.initScreenScale(this);
		initImageLoader(this);
//		initTaeSDK();
		list = new ArrayList<BaseActivity>();
//		UpgradeManager.getInstence().init(this);
		//初始化SharedPreferences
		sp = getSharedPreferences("CarService", MODE_PRIVATE);
		ed = sp.edit();
	}

//	// 初始化TaeSDK
//	public void initTaeSDK() {
//		// TaeSDK.setEnvIndex(3); // 1或者不设置代表生产环境，3代表沙箱环境，在TaeSDK.asyncInit前调用
//		TaeSDK.asyncInit(this, new InitResultCallback() {
//			@Override
//			public void onSuccess() {
//				Log.i(Cst.TAG, "TaeSDK初始化成功！");
//				String internalSessionJson = SecurityGuardManager
//						.getInstance(getApplicationContext())
//						.getDynamicDataStoreComp()
//						.getString("internal_session");
//				System.out.println("获取SID：" + internalSessionJson);
//			}
//
//			@Override
//			public void onFailure(int code, String msg) {
//				Log.i(Cst.TAG, "TaeSDK初始化失败！" + msg);
//			}
//		});
//	}

	// 获取拼接出来的请求字符串
	public static String getUrl(Hashtable<String, String> paramter, String url) {
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
}
