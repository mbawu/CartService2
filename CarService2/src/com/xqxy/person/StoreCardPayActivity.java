package com.xqxy.person;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.carservice.R;

public class StoreCardPayActivity extends BaseActivity implements OnCheckedChangeListener{
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	
	private TextView totalTxt;
	private RadioButton alipay_client;
	private RadioButton alipay_web;
	private RadioButton bank_pay;
	private TextView payBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_car_info);
		initView();
	}

	private void initView() {
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		
		totalTxt = (TextView) findViewById(R.id.totalTxt);
		alipay_client = (RadioButton) findViewById(R.id.alipay_client);
		alipay_web = (RadioButton) findViewById(R.id.alipay_web);
		bank_pay = (RadioButton) findViewById(R.id.bank_pay);
		payBtn = (TextView) findViewById(R.id.pay_btn);
	}
	public void btnOnClick(View v)
	{
		finish();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}
}
