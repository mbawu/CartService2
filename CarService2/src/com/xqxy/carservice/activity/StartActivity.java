package com.xqxy.carservice.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import cn.jpush.android.api.JPushInterface;

import com.cn.hongwei.MyApplication;
import com.xqxy.carservice.R;

public class StartActivity extends Activity {

	private View view;
	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.start_layout);
		app = (MyApplication) getApplication();
		view = findViewById(R.id.view);
	}

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

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

}
