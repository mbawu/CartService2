package com.xqxy.carservice.activity;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.Cst;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.view.CarImageView;
import com.xqxy.model.Message;
import com.xqxy.model.UserInfo;
import com.xqxy.person.AdressActivity;
import com.xqxy.person.CouponActivity;
import com.xqxy.person.CreditActivity;
import com.xqxy.person.LoginActivity;
import com.xqxy.person.MessageActivity;
import com.xqxy.person.OtherActivity;
import com.xqxy.person.StoreCardActivity;

public class PersonCentreActivity extends BaseActivity implements
		OnClickListener {
	public static int REQUEST_CODE_PHOTO = 1;
	public static int REQUEST_CODE_MSG = 1001;
	private LinearLayout layoutUser;
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	private TextView order;
	private TextView coupon;
	private TextView storedcard;
	private TextView address;
	private FrameLayout message;
	private TextView other;
	private TextView creditText;
	private TextView msgNum;
	private CarImageView headImg;
	private TextView textSex;
	private TextView textScore;
	private ArrayList<Message> data;
	private Intent msgIntent;

	private UserInfo user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_centre_layout);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyApplication.loginStat) {
			layoutUser.setVisibility(View.VISIBLE);
			if (user == null) {
				RequestWrapper request = new RequestWrapper();
				request.setIdentity(MyApplication.identity);
				sendData(request, NetworkAction.centerF_user);
			}
			getMsg();
		} else {
			layoutUser.setVisibility(View.INVISIBLE);
			user = null;
			msgNum.setText("0");
			headImg.setImageResource(R.drawable.head_default);
		}
	}

	private void init() {
		// 登录
		/*
		 * RequestWrapper wrapper = new RequestWrapper();
		 * wrapper.setPhone("13466899985"); wrapper.setPassword("1");
		 * sendData(wrapper, NetworkAction.userF_login);
		 */

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
		creditText = (TextView) findViewById(R.id.textPersonCenterCredits);
		msgNum = (TextView) findViewById(R.id.msgNum);
		headImg = (CarImageView) findViewById(R.id.imagePersonCenterHeader);
		layoutUser = (LinearLayout) findViewById(R.id.layout_user);
		textSex = (TextView) findViewById(R.id.textPersonCenterSex);
		headImg.setOnClickListener(this);
		backImageView.setOnClickListener(this);
		order.setOnClickListener(this);
		coupon.setOnClickListener(this);
		storedcard.setOnClickListener(this);
		address.setOnClickListener(this);
		message.setOnClickListener(this);
		other.setOnClickListener(this);
		creditText.setOnClickListener(this);
		headImg.setRound(true);
		// 注册广播
		registerBoradcastReceiver();

		/*
		 * if(MyApplication.loginStat) getMsg();
		 */
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.userF_login) {
			MyApplication.identity = responseWrapper.getIdentity().get(0)
					.getIdentity();
			getMsg();
		} else if (requestType == NetworkAction.centerF_user_msg) {
			data = responseWrapper.getInfo();
			int count = 0;
			for (int i = 0; i < data.size(); i++) {
				Message msg = data.get(i);
				if (msg.getStatus().equals("1"))
					count++;
			}
			if(count>0)
			{
				msgNum.setVisibility(View.VISIBLE);
				msgNum.setText(count + "");
			}
		
			msgIntent = new Intent();
			msgIntent
					.setClass(PersonCentreActivity.this, MessageActivity.class);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("datas", data);
			// msgIntent.putExtras(bundle);
		} else if (requestType == NetworkAction.centerF_user) {
			user = responseWrapper.getUser();
			if (user != null) {
				MyApplication.setUserInfo(user);
				if (user.getHead() != null && !"".equals(user.getHead())) {
					headImg.loadImage(user.getHead());
				}
				else
				{
					headImg.setRound(true);
					headImg.loadImage(user.getHead());
				}
				
				
				if ("1".equals(user.getSex())) {
					textSex.setText(getString(R.string.user_sex_man,
							user.getSurname()));
				} else {
					textSex.setText(getString(R.string.user_sex_woman,
							user.getSurname()));
				}

				creditText.setText(getString(R.string.user_credit,
						user.getIntegral()));
			}

		}
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Cst.GET_RECEIVE)) {
				getMsg();
			}
		}

	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Cst.GET_RECEIVE);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}

	public void getMsg() {
		RequestWrapper requestWrapper = new RequestWrapper();
		requestWrapper.setIdentity(MyApplication.identity);
		sendData(requestWrapper, NetworkAction.centerF_user_msg);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		if(v.getId() ==  R.id.imageTopBack)
		{
			finish();
			return;
		}
		if (v.getId() != R.id.personCenterItemOther && !MyApplication.loginStat) {
			intent = new Intent().setClass(this, LoginActivity.class);
			startActivity(intent);
			return;
		}

		switch (v.getId()) {
		// 订单
		case R.id.personCenterItemOrder:
			intent = new Intent(this, OrderListActivity.class);
			break;
		// 优惠券
		case R.id.personCenterItemCoupon:
			intent = new Intent().setClass(this, CouponActivity.class);
			break;
		// 充值卡
		case R.id.personCenterItemStoredcard:
			intent = new Intent().setClass(this, StoreCardActivity.class);

			break;
		// 常用地址
		case R.id.personCenterItemAddress:
			intent = new Intent().setClass(this, AdressActivity.class);
			break;
		// 消息
		case R.id.personCenterItemMessage:
			if (msgIntent == null) {
				Toast.makeText(this, "还未获取到消息记录，请稍等。", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			startActivityForResult(msgIntent, REQUEST_CODE_MSG);
			break;
		// 其他
		case R.id.personCenterItemOther:
			intent = new Intent().setClass(this, OtherActivity.class);
			break;
		// 积分
		case R.id.textPersonCenterCredits:
			intent = new Intent().setClass(this, CreditActivity.class);
			break;
		case R.id.imagePersonCenterHeader:
			intent = new Intent().setClass(this, PersonInfoActivity.class);
			if (user != null && user.getHead() != null) {
				intent.putExtra("src", user.getHead());
			}
			startActivityForResult(intent, REQUEST_CODE_PHOTO);
			return;
		}

		if (intent != null)
			startActivity(intent);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PHOTO && MyApplication.loginStat
				&& data != null) {
			String src = data.getStringExtra("src");
			if (src != null) {
				headImg.loadImage(src);
				if (user != null) {
					user.setHead(src);
				}
			}
		}
		else if(requestCode == REQUEST_CODE_MSG )
		{
			msgNum.setVisibility(View.GONE);
		}
	}

}
