package com.xqxy.carservice.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.view.CarImageView;
import com.xqxy.carservice.view.TopTitleView;
import com.xqxy.model.Car;
import com.xqxy.model.ProductAttr;
import com.xqxy.model.ProductDetails;
import com.xqxy.person.LoginActivity;

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
	private WebView webview;
	private String pid;
	private String flag;

	private ProductDetails product;
	private List<ProductAttr> attrs;
	private MyApplication app;
	private Car car;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_detail_layout);
		app = (MyApplication) getApplication();
		pid = getIntent().getStringExtra("pid");
		flag = getIntent().getStringExtra("flag");
		topTitleView = new TopTitleView(this);
		topTitleView.setTitle("服务详情");
		topTitleView.setRightBtnText("分享", null);

		imgServicePhoto = (CarImageView) findViewById(R.id.img_service_photo);
		textServiceName = (TextView) findViewById(R.id.text_service_name);
		textServiceTime = (TextView) findViewById(R.id.text_service_time);
		textServicePriceNew = (TextView) findViewById(R.id.text_service_price_new);
		textServicePriceOld = (TextView) findViewById(R.id.text_service_price_old);
		radioGroupAttr = (RadioGroup) findViewById(R.id.radiogroup_service_attr);
		textCarType = (TextView) findViewById(R.id.text_service_car_type);
		findViewById(R.id.btn_service_go_pay).setOnClickListener(this);
		findViewById(R.id.btn_service_go_order).setOnClickListener(this);
		findViewById(R.id.btn_service_add_car).setOnClickListener(this);
		findViewById(R.id.layout_service_evaluate).setOnClickListener(this);
		textEvaluateNum = (TextView) findViewById(R.id.text_service_evaluate_num);
		webview = (WebView) findViewById(R.id.webviw);
		if (pid != null && !"".equals(pid)) {
			RequestWrapper request = new RequestWrapper();
			request.setPid(pid);
			sendDataByGet(request, NetworkAction.indexF_product_details);
			if ("2".equals(flag)) {

				textCarType.setVisibility(View.GONE);
			} else if ("1".equals(flag)) {
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
							startActivity(new Intent(
									ServiceDetailActivity.this,
									CarListActivity.class));
						}
					}
				});
			}
			sendDataByGet(request, NetworkAction.indexF_product_attr);
		} else {
			Toast.makeText(this, "服务项目不存在", Toast.LENGTH_SHORT).show();
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
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.indexF_product_details) {
			product = responseWrapper.getProduct_details();
			if (product != null) {
				textServiceName.setText(product.getName());
				if (product.getAppnum() != null
						&& !"".equals(product.getAppnum())) {
					textEvaluateNum.setText("(" + product.getAppnum() + ")");
				}

				if (product.getPic() != null && !"".equals(product.getPic())) {
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
							.inflate(R.layout.product_dretail_attr_item, null);
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
			}
		}
	}

	private void setAttr(ProductAttr attr) {
		if (attr != null) {
			textServicePriceNew.setText(getString(R.string.product_price,
					attr.getReal_price()));
			textServicePriceOld.setText(getString(R.string.product_price,
					attr.getPrice()));
			textServiceTime.setText(attr.getTime());
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.layout_service_evaluate:
			Intent intent = new Intent(this, ServiceEvaluateActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_service_go_pay:

			break;
		case R.id.btn_service_go_order:

			break;
		case R.id.btn_service_add_car:

			break;

		}

	}

}
