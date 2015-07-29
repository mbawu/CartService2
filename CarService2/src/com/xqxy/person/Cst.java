package com.xqxy.person;

import android.os.Environment;

public class Cst {
	public static String UPGRADE_NAME = "TVShop.apk";//升级的名称
	public static String TAG = "CarService";// 测试用的TAG
	public static long exitTime = 0; //退出的时间
	// 服务器通讯参数相关
	public static final String appkey = "00001";
	public static final String secret = "abcdeabcdeabcdeabcdeabcde";
	public static final String url = "http://localhost:8080/services";
	public static final String testurl = "http://mixiang.wx.jaeapp.com/services";
	// public static final String testurl = "http://10.9.50.109:9393/services";
	public static boolean EXITE = false;//是否退出
	public static boolean lunchIsTop = false;// 启动页面是否完全关闭，当启动也面的焦点关闭后才能使首页的焦点移动
	public static final String taoKePid = "mm_41847425_0_0";// 淘客中该程序的ID

	public static final String HOST = "http://car.xinlingmingdeng.com/api.php/";//服务器地址
	public static final String GET_RECEIVE = "get_receive"; // 消息推送
	public static final String CART_CAHNGE = "cart_change"; // 编辑后更改购物车

	/** 本地缓存目录 */
	public static final String CACHE_DIR;///** 本地缓存目录 */
	public static final String UPLOAD_TEMP;///** 本地缓存目录 */
	static {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			CACHE_DIR = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/CarService";
		} else {
			CACHE_DIR = Environment.getRootDirectory().getAbsolutePath()
					+ "/CarService";
		}
		UPLOAD_TEMP = CACHE_DIR + "/upload/";
	}
}
