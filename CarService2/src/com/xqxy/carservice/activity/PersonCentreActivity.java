package com.xqxy.carservice.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.model.Message;
import com.xqxy.person.AddressAddActivity;
import com.xqxy.person.AdressActivity;
import com.xqxy.person.CreditActivity;
import com.xqxy.person.LoginActivity;
import com.xqxy.person.MessageActivity;

public class PersonCentreActivity extends BaseActivity implements
		OnClickListener {

	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	private TextView order;
	private TextView coupon;
	private TextView storedcard;
	private TextView address;
	private FrameLayout message;
	private TextView other;
	private TextView credit;
	private TextView msgNum;
	private ArrayList<Message> data;
	private Intent msgIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_centre_layout);
		init();
	}

	private void init() {
		// 登录
		RequestWrapper wrapper = new RequestWrapper();
		wrapper.setPhone("13466899985");
		wrapper.setPassword("1");
		sendData(wrapper, NetworkAction.userF_login);

		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		titleTextView.setText("个人中心");
		order = (TextView) findViewById(R.id.personCenterItemOrder);
		coupon = (TextView) findViewById(R.id.personCenterItemCoupon);
		storedcard = (TextView) findViewById(R.id.personCenterItemStoredcard);
		address = (TextView) findViewById(R.id.personCenterItemAddress);
		message = (FrameLayout) findViewById(R.id.personCenterItemMessage);
		other = (TextView) findViewById(R.id.personCenterItemOther);
		credit = (TextView) findViewById(R.id.textPersonCenterCredits);
		msgNum = (TextView) findViewById(R.id.msgNum);
		backImageView.setOnClickListener(this);
		order.setOnClickListener(this);
		coupon.setOnClickListener(this);
		storedcard.setOnClickListener(this);
		address.setOnClickListener(this);
		message.setOnClickListener(this);
		other.setOnClickListener(this);
		credit.setOnClickListener(this);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType.equals(NetworkAction.userF_login)) {

			MyApplication.identity = responseWrapper.getIdentity().get(0)
					.getIdentity();
			RequestWrapper requestWrapper = new RequestWrapper();
			requestWrapper.setIdentity(responseWrapper.getIdentity().get(0)
					.getIdentity());

			sendData(requestWrapper, NetworkAction.centerF_user_msg);
		} else if (requestType.equals(NetworkAction.centerF_user_msg)) {
			data = responseWrapper.getInfo();
			int count=0;
			for (int i = 0; i < data.size(); i++) {
				Message msg=data.get(i);
				if(msg.getStatus().equals("1"))
					count++;
			}
			msgNum.setText(count+ "");
			msgIntent = new Intent();
			msgIntent.setClass(PersonCentreActivity.this, MessageActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("datas", data);
			msgIntent.putExtras(bundle);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		// 后退按钮
		case R.id.imageTopBack:
			finish();
			break;
		// 订单
		case R.id.personCenterItemOrder:

			break;
		// 优惠券
		case R.id.personCenterItemCoupon:

			break;
		// 充值卡
		case R.id.personCenterItemStoredcard:

			break;
		// 常用地址
		case R.id.personCenterItemAddress:
			intent = new Intent().setClass(this, AdressActivity.class);
			break;
		// 消息
		case R.id.personCenterItemMessage:
			if (MyApplication.loginStat)
			{
				if(msgIntent==null)
				{
					Toast.makeText(this, "还未获取到消息记录，请稍等。", Toast.LENGTH_SHORT).show();
					return;
				}
					
				startActivity(msgIntent);
			}
			else
			{
				intent = new Intent().setClass(this, LoginActivity.class);
			}
				break;
			// 其他
		case R.id.personCenterItemOther:

			break;
		// 积分
		case R.id.textPersonCenterCredits:
			intent = new Intent().setClass(this, CreditActivity.class);
			break;
		}

		if (intent != null)
			startActivity(intent);

	}
	

}
