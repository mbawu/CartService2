package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.carservice.adapter.CarouselAdapter;
import com.xqxy.carservice.view.CarImageView;
import com.xqxy.model.AutoLogin;
import com.xqxy.model.Banner;
import com.xqxy.model.Product;
import com.xqxy.person.CartActivity;
import com.xqxy.person.LoginActivity;

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
	private Dialog myProgressDialog;

	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		app = (MyApplication) getApplication();
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		radioGroup = (RadioGroup) findViewById(R.id.viewpager_radiogroup);
		listView = (ListView) findViewById(R.id.listview);
		imgBottomCar = (ImageView) findViewById(R.id.img_homepager_car);
		productAdapter = new ProductAdapter(this);
		listView.setAdapter(productAdapter);

		imgBottomCar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (!MyApplication.loginStat)
					intent.setClass(MainActivity.this, LoginActivity.class);
				else
					intent.setClass(MainActivity.this, CartActivity.class);
				startActivity(intent);
			}
		});
		AutoLogin a=app.getAutoLogin() ;
		if (app.getAutoLogin() != null && app.getAutoLogin().isLoginState()) {
				RequestWrapper wrapper = new RequestWrapper();
				wrapper.setPhone(app.getAutoLogin().getUsername());
				wrapper.setPassword(app.getAutoLogin().getPassword());
				sendData(wrapper, NetworkAction.userF_login);
		}

		sendRequest();
	}

	public void sendRequest() {
		myProgressDialog = createDialog();
		myProgressDialog.show();
		sendData(new RequestWrapper(), NetworkAction.indexF_banner);
		sendData(new RequestWrapper(), NetworkAction.indexF_product);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if(requestType == NetworkAction.userF_login){
			MyApplication.loginStat = true;
			MyApplication.identity = responseWrapper.getIdentity().get(0)
					.getIdentity();
			if(MyApplication.getUserInfo()==null)
			{
				RequestWrapper request = new RequestWrapper();
				request.setIdentity(responseWrapper.getIdentity().get(0)
						.getIdentity());
				sendData(request, NetworkAction.centerF_user);
			}
		
		}else{
			respCount++;
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
			if (respCount == 2) {
				myProgressDialog.dismiss();
			}
		}
		if(requestType == NetworkAction.centerF_user)
		{
			MyApplication.setUserInfo(responseWrapper.getUser());
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
	
	
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
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
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
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
					Intent intent = new Intent();
					intent.putExtra("pid", product.getPid());
					intent.putExtra("flag", product.getFlag());
					if ("2".equals(product.getFlag())) {// 其他类，直接打开
						intent.setClass(activity, ServiceDetailActivity.class);
						activity.startActivity(intent);
					} else {
						if (app.getCar() == null) {
							intent.setClass(activity, CarActivity.class);
							activity.startActivity(intent);
						} else {
							intent.setClass(activity,
									ServiceDetailActivity.class);
							activity.startActivity(intent);
						}
					}
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
