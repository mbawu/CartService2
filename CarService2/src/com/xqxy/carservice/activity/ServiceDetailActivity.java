package com.xqxy.carservice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xqxy.carservice.R;

public class ServiceDetailActivity extends Activity implements OnClickListener {

	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	private ImageView imgServicePhoto;
	private TextView textServiceName;
	private TextView textServiceTime;
	private TextView textServicePriceNew;
	private TextView textServicePriceOld;
	private RadioButton radioBtnCarType;
	private TextView textEvaluateNum;
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_detail_layout);

		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		titleTextView.setText("服务详情");

		imgServicePhoto = (ImageView) findViewById(R.id.img_service_photo);
		textServiceName = (TextView) findViewById(R.id.text_service_name);
		textServiceTime = (TextView) findViewById(R.id.text_service_time);
		textServicePriceNew = (TextView) findViewById(R.id.text_service_price_new);
		textServicePriceOld = (TextView) findViewById(R.id.text_service_price_old);
		radioBtnCarType = (RadioButton) findViewById(R.id.radio_service_cartype_five);
		findViewById(R.id.btn_service_go_pay).setOnClickListener(this);
		findViewById(R.id.btn_service_go_order).setOnClickListener(this);
		findViewById(R.id.btn_service_add_car).setOnClickListener(this);
		findViewById(R.id.layout_service_evaluate).setOnClickListener(this);
		textEvaluateNum = (TextView) findViewById(R.id.text_service_evaluate_num);
		webview = (WebView) findViewById(R.id.webviw);

		webview.loadUrl("http://sina.cn/?vm=4007");

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
