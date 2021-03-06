package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
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

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.CarImageView;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.carservice.adapter.CarouselAdapter;
import com.xqxy.model.AutoLogin;
import com.xqxy.model.Banner;
import com.xqxy.model.Product;
import com.xqxy.person.CartActivity;
import com.xqxy.person.LoginActivity;
import com.xqxy.person.NetworkAction;

/**
 * 主界面
 * @author Administrator
 *
 */
public class MainActivity extends BaseActivity {

	private RadioGroup radioGroup;//轮播图标
	private ViewPager viewPager;//轮播容器
	private ArrayList<View> carouseImageViews = new ArrayList<View>();//轮播数据源
	private Timer timer;//轮播定时器
	private ListView listView;//服务列表
	private ImageView imgBottomCar;//购物车图标
	private ProductAdapter productAdapter;//服务列表适配器
	private List<Banner> banners;//轮播数据源
	private List<Product> products;//服务数据源
	private Dialog myProgressDialog;

	private MyApplication app;

	/**
	 * 界面初始化
	 */
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
		AutoLogin a = app.getAutoLogin();
		if (app.getAutoLogin() != null && app.getAutoLogin().isLoginState()) {
			RequestWrapper wrapper = new RequestWrapper();
			wrapper.setPhone(app.getAutoLogin().getUsername());
			wrapper.setPassword(app.getAutoLogin().getPassword());
			sendData(wrapper, NetworkAction.userF_login);
		}

		sendRequest();
	}

	/**
	 * 发送轮播和服务列表请求
	 */
	public void sendRequest() {
		myProgressDialog = createDialog();
		myProgressDialog.show();
		sendData(new RequestWrapper(), NetworkAction.indexF_banner);
		sendData(new RequestWrapper(), NetworkAction.indexF_product);
	}

	/**
	 * 强制购物车获取焦点
	 */
	@Override
	protected void onResume() {

		super.onResume();
		imgBottomCar.postDelayed(new Runnable() {

			@Override
			public void run() {
				imgBottomCar.requestFocus();
			}
		}, 100);

	}

	/**
	 * 解析数据
	 */
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.userF_login) {
			MyApplication.loginStat = true;
			MyApplication.identity = responseWrapper.getIdentity().get(0)
					.getIdentity();
			if (MyApplication.getUserInfo() == null) {
				RequestWrapper request = new RequestWrapper();
				request.setIdentity(responseWrapper.getIdentity().get(0)
						.getIdentity());
				sendData(request, NetworkAction.centerF_user);
			}

		} else {
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
			if (respCount == 2) {//请求全部返回，关闭进度条
				myProgressDialog.dismiss();
			}
		}
		if (requestType == NetworkAction.centerF_user) {
			MyApplication.setUserInfo(responseWrapper.getUser());
		}

	}

	/**
	 * 类别按钮点击事件
	 * @param view 点击的控件
	 */
	public void btnOnClick(View view) {
		if (view.getId() == R.id.imageTopBack) {
			startActivity(new Intent(this, CategoryActivity.class));
		} else if (view.getId() == R.id.textTopRightBtn) {
			startActivity(new Intent(this, PersonCentreActivity.class));
		}
	}

	/**
	 * 初始化轮播组件
	 */
	private void initCarouselViewPager() {
		boolean repat = true;
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
					if (url == null || "".equals(url)) {
//						Toast.makeText(MainActivity.this, "暂无链接地址",
//								Toast.LENGTH_SHORT).show();
					} else {
						try {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri.parse(url));
							startActivity(intent);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			carouseImageViews.add(imageView);
			if (banners.size() == 2 && i == 1 && repat) {
				i = -1;
				repat = false;
			}
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

	/**
	 * 退出程序监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 服务列表适配器
	 * @author Administrator
	 *
	 */
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
			viewHolder.textServicePriceOld.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

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

	/**
	 * 服务列表item控件
	 * @author Administrator
	 *
	 */
	class ViewHolder {
		CarImageView imgServicePhoto;
		TextView textServiceName;
		TextView textServiceDis;
		TextView textServicePriceNew;
		TextView textServicePriceOld;
	}
}
