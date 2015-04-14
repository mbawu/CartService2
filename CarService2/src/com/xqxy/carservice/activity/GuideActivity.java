package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.xqxy.carservice.R;

public class GuideActivity extends Activity {

	private ViewPager viewPager;
	private List<View> views = new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_layout);
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

	public void onClickBtn(View v) {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

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
