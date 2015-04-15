package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarouselAdapter;
import com.xqxy.carservice.view.CarImageView;

public class MainActivity extends Activity {

	private RadioGroup radioGroup;
	private ViewPager viewPager;
	private ArrayList<View> carouseImageViews = new ArrayList<View>();
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		radioGroup = (RadioGroup) findViewById(R.id.viewpager_radiogroup);
		initCarouselViewPager(imgUrls);

	}

	public void btnOnClick(View view) {
		if (view.getId() == R.id.imageTopBack) {

		} else if (view.getId() == R.id.textTopRightBtn) {
			startActivity(new Intent(this, PersonCentreActivity.class));
		}
	}

	private void initCarouselViewPager(String[] imgUrls) {

		for (int i = 0; i < imgUrls.length; i++) {
			CarImageView imageView = new CarImageView(this);
			imageView.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.loadImage(imgUrls[i]);
			carouseImageViews.add(imageView);
		}
		CarouselAdapter homeAdapter = new CarouselAdapter(this);
		homeAdapter.setArrayList(carouseImageViews);
		viewPager.setAdapter(homeAdapter);

		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							int index = viewPager.getCurrentItem();
							index++;
							viewPager.setCurrentItem(index);
						}
					});
				}
			}, 5000, 5000);
		}

		for (int i = 0; i < imgUrls.length; i++) {
			RadioButton rb = (RadioButton) getLayoutInflater().inflate(
					R.layout.homepage_radio_item, radioGroup, false);
			rb.setId(i);
			radioGroup.addView(rb);
			rb.setChecked(i == 0);
		}
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				((RadioButton) radioGroup.getChildAt(arg0
						% radioGroup.getChildCount())).setChecked(true);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private String[] imgUrls = new String[] {
			"http://imgstatic.baidu.com/img/image/shouye/fanbingbing.jpg",
			"http://imgstatic.baidu.com/img/image/shouye/wanglihong.jpg",
			"http://imgstatic.baidu.com/img/image/shouye/gaoyuanyuan.jpg",
			"http://imgstatic.baidu.com/img/image/shouye/yaodi.jpg" };
}
