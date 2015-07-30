package com.xqxy.carservice.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import cn.jpush.android.api.JPushInterface;

import com.cn.hongwei.MyApplication;
import com.xqxy.carservice.R;

/**
 * 启动页界面
 * @author Administrator
 *
 */
public class StartActivity extends Activity {

	private View view;
	private MyApplication app;

	/**
	 * 界面初始化
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.start_layout);
		app = (MyApplication) getApplication();
		view = findViewById(R.id.view);
	}

	/**
	 * 启动页的跳转逻辑,初始化推送组件
	 */
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		view.postDelayed(new Runnable() {

			@Override
			public void run() {

				if (app.getGuide() == null) {
					startActivity(new Intent(StartActivity.this,
							GuideActivity.class));
				} else {

					startActivity(new Intent(StartActivity.this,
							MainActivity.class));
				}
				finish();
			}
		}, 1200);

	}

	/**
	 * 暂停推送组件
	 */
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

}
