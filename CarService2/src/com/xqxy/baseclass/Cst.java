package com.xqxy.baseclass;

public class Cst {
	public static String UPGRADE_NAME = "TVShop.apk";
	public static String TAG = "CarService";// 测试用的TAG
	public static long exitTime = 0;
	//服务器通讯参数相关
	public static final String appkey = "00001";
	public static final String secret = "abcdeabcdeabcdeabcdeabcde";
	public static final String url = "http://localhost:8080/services";
	public static final String testurl = "http://mixiang.wx.jaeapp.com/services";
//	public static final String testurl = "http://10.9.50.109:9393/services";
	public static boolean EXITE=false;
	public static boolean lunchIsTop=false;//启动页面是否完全关闭，当启动也面的焦点关闭后才能使首页的焦点移动
	public static final String taoKePid="mm_41847425_0_0";//淘客中该程序的ID
	

	public static final String HOST="http://car.xinlingmingdeng.com/api.php/";
	public static final String GET_RECEIVE="get_receive"; //消息推送
}
