package com.xqxy.person;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.carservice.R;

public class CartActivity extends BaseActivity implements
OnClickListener{
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart);
		init();
		
	}

	private void init() {
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		backImageView.setOnClickListener(this);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageTopBack:
			finish();
			break;

		}
		
	}
}
