package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import cn.jpush.android.api.JPushInterface;

import com.cn.hongwei.MyApplication;
import com.xqxy.carservice.R;
/**
 * 引导页界面
 * @author Administrator
 *
 */
public class GuideActivity extends Activity {
	private MyApplication app;
	private ViewPager viewPager; //图片容器
	private List<View> views = new ArrayList<View>(); //数据源

	/**
	 * 界面初始化
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.guide_layout);
		app = (MyApplication) getApplication();
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		ImageView imgView = (ImageView) getLayoutInflater().inflate(
				R.layout.image_view_fitxy, viewPager, false);
		imgView.setImageResource(R.drawable.index_1);
		views.add(imgView);

		imgView = (ImageView) getLayoutInflater().inflate(
				R.layout.image_view_fitxy, viewPager, false);
		imgView.setImageResource(R.drawable.index_2);
		views.add(imgView);

		views.add(getLayoutInflater().inflate(R.layout.index_last_layout,
				viewPager, false));

		viewPager.setAdapter(mPagerAdapter);

	}

	/**
	 * 初始化推送组件
	 */
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	/**
	 * 暂停推送
	 */
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	public void onClickBtn(View v) {
		app.setGuide("1");
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	/**
	 * 引导页适配器
	 */
	PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position));
			return views.get(position);
		}
	};
}
