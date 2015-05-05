package com.xqxy.person;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.pay.Pay;
import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.model.PayModel;
import com.xqxy.model.StoreCard;

public class StoreCardPayActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	private TextView totalTxt;
	private RadioButton alipay_client;
	private RadioButton alipay_web;
	private RadioButton bank_pay;
	private TextView payBtn;
	private StoreCard card;

	private int payMethod = 1; // 1.支付宝客户端 2.支付宝网页 3. 网银

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_storecard);
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
		alipay_client.setOnCheckedChangeListener(this);
		alipay_web.setOnCheckedChangeListener(this);
		bank_pay.setOnCheckedChangeListener(this);
		payBtn = (TextView) findViewById(R.id.pay_btn);
		final Pay pay = new Pay(this);
		payBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (payMethod) {
				case 1:
					RequestWrapper wrapper=new RequestWrapper();
					wrapper.setShowDialog(true);
					sendDataByGet(wrapper, NetworkAction.indexF_pay_base);
					break;
				case 2:

					break;
				case 3:

					break;
				}

			}
		});
		card = (StoreCard) getIntent().getSerializableExtra(
				StoreCardDetailActivity.DATA);
		totalTxt.setText("￥" + card.getPrice());
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if(requestType==NetworkAction.indexF_pay_base)
		{
			Pay pay=new Pay(this);
			if(payMethod==1)
			{
				PayModel payModel=responseWrapper.getPay();
				pay.setPARTNER(payModel.getPay_pid());
				pay.setSELLER(payModel.getPay_name());
				pay.setRSA_PRIVATE(payModel.getPay_key());
				try {
					pay.alipay(card.getName(),
							"{\"type\":\"1\",\"identity\":\""
									+ MyApplication.identity + "\",\"cid\":\""
									+ card.getCid() + "\"}", card.getPrice());
				} catch (Exception e) {
					Toast.makeText(this, "商家支付信息有误，请重试", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		}
		else if(requestType==null)
		{
			Toast.makeText(this, "获取支付信息失败，请重试", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void btnOnClick(View v) {
		finish();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.alipay_client:
				payMethod = 1;
				break;
			case R.id.alipay_web:
				payMethod = 2;
				break;
			case R.id.bank_pay:
				payMethod = 3;
				break;
			}
		}

	}
}