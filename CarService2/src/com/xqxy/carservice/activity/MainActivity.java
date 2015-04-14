package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarouselAdapter;
import com.xqxy.carservice.view.CarImageView;

public class MainActivity extends Activity {

	private ViewPager viewPager;
	private ArrayList<View> carouseImageViews = new ArrayList<View>();
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
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
	}
}
