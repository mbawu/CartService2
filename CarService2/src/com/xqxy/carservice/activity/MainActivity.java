package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.carservice.adapter.CarouselAdapter;
import com.xqxy.carservice.view.CarImageView;

public class MainActivity extends BaseActivity {

	private RadioGroup radioGroup;
	private ViewPager viewPager;
	private ArrayList<View> carouseImageViews = new ArrayList<View>();
	private Timer timer;
	private ListView listView;
	private ImageView imgBottomCar;
	private ServiceAdapter serviceAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		radioGroup = (RadioGroup) findViewById(R.id.viewpager_radiogroup);
		listView = (ListView) findViewById(R.id.listview);
		imgBottomCar = (ImageView) findViewById(R.id.img_homepager_car);
		serviceAdapter = new ServiceAdapter(this);
		listView.setAdapter(serviceAdapter);
		initCarouselViewPager(imgUrls);
		List<CarService> carServiceList = new ArrayList<CarService>();
		for (int i = 0; i < 10; i++) {
			carServiceList.add(new CarService());
		}
		serviceAdapter.setDataList(carServiceList);
		
		sendBannerRequest();
	}

	public void sendBannerRequest() {

		sendData(new RequestWrapper(), NetworkAction.index_banner);
		// 忘记密码按钮
	}
	
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
	}
	

	public void btnOnClick(View view) {
		if (view.getId() == R.id.imageTopBack) {
			startActivity(new Intent(this, CategoryActivity.class));
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

	class ServiceAdapter extends CarBaseAdapter<CarService> {

		public ServiceAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.homepager_service_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.imgServicePhoto = (CarImageView) convertView
						.findViewById(R.id.img_service_photo);
				viewHolder.textServiceName = (TextView) convertView
						.findViewById(R.id.text_service_name);
				viewHolder.textServiceDis = (TextView) convertView
						.findViewById(R.id.text_service_dis);
				viewHolder.textServicePriceNew = (TextView) convertView
						.findViewById(R.id.text_service_price_new);
				viewHolder.textServicePriceOld = (TextView) convertView
						.findViewById(R.id.text_service_price_old);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.imgServicePhoto
					.loadImage("http://imgstatic.baidu.com/img/image/shouye/fanbingbing.jpg");
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity,
							ServiceDetailActivity.class);
					activity.startActivity(intent);

				}
			});

			return convertView;
		}
	}

	class ViewHolder {
		CarImageView imgServicePhoto;
		TextView textServiceName;
		TextView textServiceDis;
		TextView textServicePriceNew;
		TextView textServicePriceOld;
	}

	class CarService {

	}

	private String[] imgUrls = new String[] {
			"http://imgstatic.baidu.com/img/image/shouye/fanbingbing.jpg",
			"http://imgstatic.baidu.com/img/image/shouye/wanglihong.jpg",
			"http://imgstatic.baidu.com/img/image/shouye/gaoyuanyuan.jpg",
			"http://imgstatic.baidu.com/img/image/shouye/yaodi.jpg" };
}
