package com.xqxy.baseclass;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * 
 * 集成百度定位模块
 * 
 */
public class BaiduLoction {

	public LocationClient mLocationClient;
	public ShopLocationListener mLocationListener;
	private LocationMode locationMode = LocationMode.Hight_Accuracy;
	private LocationCallback locationCallback;
	private String coorType = "gcj02";

	private static BaiduLoction instance;

	private BaiduLoction() {
	}

	public static BaiduLoction getInstance() {
		if (instance == null) {
			instance = new BaiduLoction();
		}
		return instance;
	}

	public void init(Context ctx) {
		mLocationClient = new LocationClient(ctx);
		mLocationListener = new ShopLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);

		LocationClientOption option = new LocationClientOption();

		// 设置定位模式
		option.setLocationMode(locationMode);
		// 返回的定位结果是百度经纬度，默认值gcj02
		option.setCoorType(coorType);
		// 设置发起定位请求的间隔时间为3000ms
		option.setScanSpan(5000);

		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	public void startLocation() {
		mLocationClient.start();
	}

	public void stopLocation() {
		mLocationClient.stop();
	}

	public void setLocationCallback(LocationCallback locationCallback) {
		this.locationCallback = locationCallback;
	}

	/**
	 * 实现定位回调监听
	 */
	private class ShopLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			if (locationCallback != null
					&& (location.getLocType() == BDLocation.TypeNetWorkLocation
							|| location.getLocType() == BDLocation.TypeGpsLocation || location
							.getLocType() == BDLocation.TypeCacheLocation)) {
				locationCallback.locationResult(location);
			}
		}

	}

	public interface LocationCallback {
		void locationResult(BDLocation location);
	}
	// error code说明
	// 61 ： GPS定位结果
	// 62 ： 扫描整合定位依据失败。此时定位结果无效。
	// 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
	// 65 ： 定位缓存的结果。
	// 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
	// 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
	// 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
	// 161： 表示网络定位结果
	// 162~167： 服务端定位失败
	// 502：key参数错误
	// 505：key不存在或者非法
	// 601：key服务被开发者自己禁用
	// 602：key mcode不匹配
	// 501～700：key验证失败
}
