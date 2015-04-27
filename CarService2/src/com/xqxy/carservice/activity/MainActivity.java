package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.xqxy.model.Banner;
import com.xqxy.model.Product;

public class MainActivity extends BaseActivity {

	private RadioGroup radioGroup;
	private ViewPager viewPager;
	private ArrayList<View> carouseImageViews = new ArrayList<View>();
	private Timer timer;
	private ListView listView;
	private ImageView imgBottomCar;
	private ProductAdapter productAdapter;
	private List<Banner> banners;
	private List<Product> products;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		radioGroup = (RadioGroup) findViewById(R.id.viewpager_radiogroup);
		listView = (ListView) findViewById(R.id.listview);
		imgBottomCar = (ImageView) findViewById(R.id.img_homepager_car);
		productAdapter = new ProductAdapter(this);
		listView.setAdapter(productAdapter);
		sendRequest();
	}

	public void sendRequest() {
		sendData(new RequestWrapper(), NetworkAction.indexF_banner);
		sendData(new RequestWrapper(), NetworkAction.indexF_product);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.indexF_banner) {
			banners = responseWrapper.getBanner();
			if (banners != null && banners.size() > 0) {
				initCarouselViewPager();
			}
		} else if (requestType == NetworkAction.indexF_product) {
			products = responseWrapper.getProduct();
			if (products != null && products.size() > 0) {
				productAdapter.setDataList(products);
				productAdapter.notifyDataSetChanged();
			}
		}
	}

	public void btnOnClick(View view) {
		if (view.getId() == R.id.imageTopBack) {
			startActivity(new Intent(this, CategoryActivity.class));
		} else if (view.getId() == R.id.textTopRightBtn) {
			startActivity(new Intent(this, PersonCentreActivity.class));
		}
	}

	private void initCarouselViewPager() {

		for (int i = 0; i < banners.size(); i++) {
			CarImageView imageView = new CarImageView(this);
			final String url = banners.get(i).getUrl();
			imageView.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.loadImage(banners.get(i).getPath());
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
			});
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

		for (int i = 0; i < banners.size(); i++) {
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

	class ProductAdapter extends CarBaseAdapter<Product> {

		public ProductAdapter(Activity activity) {
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

			final Product product = products.get(position);

			viewHolder.imgServicePhoto.loadImage(product.getPic());
			viewHolder.textServiceName.setText(product.getName());
			viewHolder.textServiceDis.setText(product.getContent()
					.replace("\r", "").replace("\n", "").replace("\t", ""));
			viewHolder.textServicePriceNew.setText(getString(
					R.string.product_price, product.getNew_price() + ""));
			viewHolder.textServicePriceOld.setText(getString(
					R.string.product_price, product.getOld_price() + ""));
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity,
							ServiceDetailActivity.class);
					intent.putExtra("pid", product.getPid());
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
}
