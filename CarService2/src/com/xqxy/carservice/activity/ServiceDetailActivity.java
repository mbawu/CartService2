package com.xqxy.carservice.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.CarImageView;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.cn.hongwei.TopTitleView;
import com.xqxy.carservice.R;
import com.xqxy.model.Appraise;
import com.xqxy.model.Car;
import com.xqxy.model.Cart;
import com.xqxy.model.Product;
import com.xqxy.model.ProductAttr;
import com.xqxy.model.ProductDetails;
import com.xqxy.person.CallServiceActivity;
import com.xqxy.person.CartActivity;
import com.xqxy.person.LoginActivity;
import com.xqxy.person.NetworkAction;

public class ServiceDetailActivity extends BaseActivity implements
		OnClickListener {

	private TopTitleView topTitleView;

	private CarImageView imgServicePhoto;
	private TextView textServiceName;
	private TextView textServiceTime;
	private TextView textServicePriceNew;
	private TextView textServicePriceOld;
	private TextView textEvaluateNum;
	private TextView textCarType;
	private RadioGroup radioGroupAttr;
	private LinearLayout recommendLayout;

	private RelativeLayout layoutEva;
	private TextView textEvaLevel;
	private TextView textEvaUser;
	private TextView textEvaContent;
	private TextView textEvaData;
	private TextView textEvaTime;
	private View view1;
	private View view2;

	private WebView webview;
	private String pid;
	private String paid;
	private String flag;

	private ProductDetails product;
	private List<ProductAttr> attrs;
	private ProductAttr productAttr;
	private MyApplication app;
	private Car car;
	private Dialog myProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_detail_layout);
		app = (MyApplication) getApplication();
		pid = getIntent().getStringExtra("pid");
		flag = getIntent().getStringExtra("flag");
		topTitleView = new TopTitleView(this);
		topTitleView.setTitle("服务详情");
		topTitleView.setRightBtnText("分享", new OnClickListener() {

			@Override
			public void onClick(View v) {
				share();
			}
		});

		imgServicePhoto = (CarImageView) findViewById(R.id.img_service_photo);
		textServiceName = (TextView) findViewById(R.id.text_service_name);
		textServiceTime = (TextView) findViewById(R.id.text_service_time);
		textServicePriceNew = (TextView) findViewById(R.id.text_service_price_new);
		textServicePriceOld = (TextView) findViewById(R.id.text_service_price_old);
		radioGroupAttr = (RadioGroup) findViewById(R.id.radiogroup_service_attr);
		textCarType = (TextView) findViewById(R.id.text_service_car_type);

		layoutEva = (RelativeLayout) findViewById(R.id.layout_evaluate);
		textEvaLevel = (TextView) findViewById(R.id.text_evaluate_level);
		textEvaUser = (TextView) findViewById(R.id.text_evaluate_user);
		textEvaContent = (TextView) findViewById(R.id.text_evaluate_content);
		textEvaData = (TextView) findViewById(R.id.text_evaluate_date);
		textEvaTime = (TextView) findViewById(R.id.text_evaluate_time);
		recommendLayout = (LinearLayout) findViewById(R.id.service_recommend_linearlayout);
		view1 = findViewById(R.id.view_service_1);
		view2 = findViewById(R.id.view_service_2);

		findViewById(R.id.btn_service_go_pay).setOnClickListener(this);
		findViewById(R.id.btn_service_go_order).setOnClickListener(this);
		findViewById(R.id.btn_service_add_car).setOnClickListener(this);
		findViewById(R.id.layout_service_evaluate).setOnClickListener(this);
		textEvaluateNum = (TextView) findViewById(R.id.text_service_evaluate_num);
		webview = (WebView) findViewById(R.id.webviw);
		WebSettings settings = webview.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		if (pid != null && !"".equals(pid)) {
			RequestWrapper request = new RequestWrapper();
			request.setPid(pid);
			sendDataByGet(request, NetworkAction.indexF_product_details);
			sendDataByGet(request, NetworkAction.indexF_recommend);

			if ("2".equals(flag)) {
				textCarType.setVisibility(View.GONE);
			} else if ("1".equals(flag)) {
				radioGroupAttr.setVisibility(View.GONE);
				view1.setVisibility(View.VISIBLE);
				view2.setVisibility(View.VISIBLE);
				Car car = app.getCar();

				if (car != null) {
					String carType = car.getName() + "-" + car.getSname() + "-"
							+ car.getMname();
					request.setBid(car.getBid());
					request.setSid(car.getSid());
					request.setMid(car.getMid());
					textCarType.setText(carType);
				}
				textCarType.setVisibility(View.VISIBLE);
				textCarType.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!MyApplication.loginStat) {
							Toast.makeText(ServiceDetailActivity.this, "请先登录",
									Toast.LENGTH_SHORT).show();
							startActivity(new Intent(
									ServiceDetailActivity.this,
									LoginActivity.class));
						} else {
							Intent intent = new Intent(
									ServiceDetailActivity.this,
									CarListActivity.class);
							intent.putExtra("pid", pid);
							startActivityForResult(intent, 1);
						}
					}
				});
			}
			sendDataByGet(request, NetworkAction.indexF_product_attr);

			request = new RequestWrapper();
			request.setPid(pid);
			request.setLimit("1");
			sendDataByGet(request, NetworkAction.indexF_appraise);

			myProgressDialog = createDialog();
			myProgressDialog.show();
		} else {
			Toast.makeText(this, "服务项目不存在--", Toast.LENGTH_SHORT).show();
			finish();
		}

		radioGroupAttr
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton btn = (RadioButton) group
								.findViewById(checkedId);
						if (btn != null && btn.isChecked()) {
							setAttr((ProductAttr) btn.getTag());
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			Car car = app.getCar();

			if (car != null) {
				String carType = car.getName() + "-" + car.getSname() + "-"
						+ car.getMname();
				RequestWrapper request = new RequestWrapper();
				request.setShowDialog(true);
				request.setPid(pid);
				request.setBid(car.getBid());
				request.setSid(car.getSid());
				request.setMid(car.getMid());
				textCarType.setText(carType);
				sendDataByGet(request, NetworkAction.indexF_product_attr);
			}
		}
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (NetworkAction.cartF_add_cart == requestType) {
			Toast.makeText(this, "已成功加入购物车.", Toast.LENGTH_SHORT).show();
		} else {
			respCount++;
			if (requestType == NetworkAction.indexF_product_details) {
				product = responseWrapper.getProduct_details();
				if (product != null) {
					textServiceName.setText(product.getName());
					if (product.getAppnum() != null
							&& !"".equals(product.getAppnum())) {
						textEvaluateNum
								.setText("(" + product.getAppnum() + ")");
					}

					if (product.getPic() != null
							&& !"".equals(product.getPic())) {
						imgServicePhoto.loadImage(product.getPic());
					}
					if (product.getContent() != null
							&& !"".equals(product.getContent())) {
						webview.loadData(product.getContent(),
								"text/html; charset=UTF-8", null);
					}

				}
			} else if (requestType == NetworkAction.indexF_product_attr) {
				attrs = responseWrapper.getAttr();
				radioGroupAttr.removeAllViews();
				if (attrs != null && attrs.size() > 0) {
					for (int i = 0; i < attrs.size(); i++) {
						ProductAttr attr = attrs.get(i);
						RadioButton radioBtn = (RadioButton) getLayoutInflater()
								.inflate(R.layout.product_dretail_attr_item,
										radioGroupAttr, false);
						radioBtn.setTag(attr);
						radioBtn.setId(i);
						radioBtn.setText(attr.getName());
						if (i == 0) {
							radioBtn.setChecked(true);
							setAttr(attr);
						} else {
							radioBtn.setChecked(false);
						}
						radioGroupAttr.addView(radioBtn);
					}
				} else {
					Toast.makeText(this, "没有符合的服务项目,请重新选择", Toast.LENGTH_SHORT)
							.show();
					textServicePriceNew.setText("");
					textServicePriceOld.setText("");
					textServiceTime.setText("");
					paid = null;
				}
			} else if (requestType == NetworkAction.indexF_appraise) {
				if (responseWrapper.getAppraise() != null
						&& responseWrapper.getAppraise().size() > 0) {
					Appraise appraise = responseWrapper.getAppraise().get(0);
					if ("1".equals(appraise.getFlag())) {
						textEvaLevel
								.setText(getString(R.string.evaluate_level_good));
					} else {
						textEvaLevel
								.setText(getString(R.string.evaluate_level_bad));
					}

					if (appraise.getPhone() != null) {
						if (appraise.getPhone().length() == 11) {
							textEvaUser
									.setText(appraise.getPhone()
											.substring(0, 3)
											+ "****"
											+ appraise.getPhone().substring(7));
						} else {
							textEvaUser.setText(appraise.getPhone());
						}
					}

					if (appraise.getContent() != null) {
						textEvaContent.setText(appraise.getContent());
					}
					if (appraise.getCreate_time() != null) {
						textEvaData.setText(appraise.getCreate_time()
								.substring(0, 10));
						textEvaTime.setText(appraise.getCreate_time()
								.substring(10));
					}

					layoutEva.setVisibility(View.VISIBLE);
				} else {
					layoutEva.setVisibility(View.GONE);
				}
			} else if (requestType == NetworkAction.indexF_recommend) {
				List<Product> recs = responseWrapper.getRecommend();
				if (recs != null && recs.size() > 0) {
					for (final Product p : recs) {
						LinearLayout layout = (LinearLayout) getLayoutInflater()
								.inflate(R.layout.recommend_service_item,
										recommendLayout, false);
						CarImageView img = (CarImageView) layout
								.findViewById(R.id.img_service_photo);
						TextView textName = (TextView) layout
								.findViewById(R.id.text_service_name);
						img.loadImage(p.getPic());
						textName.setText(p.getName());
						recommendLayout.addView(layout);
						img.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent();
								intent.putExtra("pid", p.getPid());
								intent.putExtra("flag", p.getFlag());
								if ("2".equals(product.getFlag())) {// 其他类，直接打开
									intent.setClass(ServiceDetailActivity.this,
											ServiceDetailActivity.class);
									startActivity(intent);
								} else {
									if (app.getCar() == null) {
										intent.setClass(
												ServiceDetailActivity.this,
												CarActivity.class);
										startActivity(intent);
									} else {
										intent.setClass(
												ServiceDetailActivity.this,
												ServiceDetailActivity.class);
										startActivity(intent);
									}
								}
								if (pid.equals(p.getPid())) {
									finish();
								}

							}
						});
					}
				}

			}
			if (respCount == 4) {
				myProgressDialog.dismiss();
			}
		}
	}

	private void setAttr(ProductAttr attr) {
		if (attr != null) {
			productAttr = attr;
			paid = attr.getId();
			textServicePriceNew.setText(getString(R.string.product_price,
					attr.getReal_price()));
			textServicePriceOld.setText(getString(R.string.product_price,
					attr.getPrice()));
			textServicePriceOld.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
			textServiceTime.setText(getString(R.string.product_time,
					attr.getTime()));
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.layout_service_evaluate:
			Intent intent = new Intent(this, ServiceEvaluateActivity.class);
			intent.putExtra("pid", pid);
			startActivity(intent);
			break;
		case R.id.btn_service_go_pay:
			Intent intent1 = new Intent();
			if (!MyApplication.loginStat)
				intent1.setClass(this, LoginActivity.class);
			else
				intent1.setClass(this, CartActivity.class);
			startActivity(intent1);
			break;
		case R.id.btn_service_go_order:
			if (paid == null) {
				Toast.makeText(this, "没有符合的服务项目,请重新选择", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (!MyApplication.loginStat) {
					startActivity(new Intent(this, LoginActivity.class));
				} else {
					Cart cart = new Cart();
					cart.setName(product.getName());
					cart.setPid(pid);
					cart.setPaid(paid);
					cart.setTime(productAttr.getTime());
					cart.setReal_price(productAttr.getReal_price());
					cart.setPic(product.getPic());
					List<Cart> carts = new ArrayList<Cart>();
					carts.add(cart);
					Intent intent2 = new Intent(this, CallServiceActivity.class);
					intent2.putExtra("data", (Serializable) carts);
					startActivity(intent2);
				}
			}

			break;
		case R.id.btn_service_add_car:
			if (paid == null) {
				Toast.makeText(this, "没有符合的服务项目,请重新选择", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (!MyApplication.loginStat) {
					startActivity(new Intent(this, LoginActivity.class));
				} else {
					RequestWrapper request = new RequestWrapper();
					request.setShowDialog(true);
					request.setIdentity(MyApplication.identity);
					request.setPid(pid);
					request.setPaid(paid);
					sendData(request, NetworkAction.cartF_add_cart);
				}
			}

			break;

		}

	}

	private void share() {

		OnekeyShare oks = new OnekeyShare();
		// oks.disableSSOWhenAuthorize();
		/*
		 * // 关闭sso授权 oks.disableSSOWhenAuthorize();
		 * 
		 * // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法 //
		 * oks.setNotification(R.drawable.ic_launcher, //
		 * getString(R.string.app_name)); // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 * oks.setTitle(getString(R.string.share)); //
		 * titleUrl是标题的网络链接，仅在人人网和QQ空间使用 oks.setTitleUrl("http://sharesdk.cn");
		 * // text是分享文本，所有平台都需要这个字段 oks.setText("我是分享文本"); //
		 * imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 * oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片 //
		 * url仅在微信（包括好友和朋友圈）中使用 oks.setUrl("http://sharesdk.cn"); //
		 * comment是我对这条分享的评论，仅在人人网和QQ空间使用 oks.setComment("我是测试评论文本"); //
		 * site是分享此内容的网站名称，仅在QQ空间使用 oks.setSite(getString(R.string.app_name));
		 * // siteUrl是分享此内容的网站地址，仅在QQ空间使用 oks.setSiteUrl("http://sharesdk.cn");
		 */

		oks.setTitle(getString(R.string.app_name));
		oks.setTitleUrl("http://car.xinlingmingdeng.com/download/");
		oks.setText(product.getName() + "------来自"
				+ getString(R.string.app_name));
		oks.setImageUrl(product.getPic());
		oks.setUrl("http://car.xinlingmingdeng.com/download/");
		// oks.setTitle("分享标题--Title");

		// oks.setTitleUrl("http://mob.com");

		// oks.setText("分享测试文--Text");

		// oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");

		oks.show(this);

	}
}
