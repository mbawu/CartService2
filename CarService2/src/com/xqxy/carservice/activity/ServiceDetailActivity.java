package com.xqxy.carservice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.xqxy.carservice.R;

public class ServiceDetailActivity extends Activity {

	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_detail_layout);

		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		titleTextView.setText("服务详情");
	}
}
