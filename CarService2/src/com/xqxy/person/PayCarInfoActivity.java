package com.xqxy.person;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.DataFormatCheck;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarInfoAdapter;
import com.xqxy.model.Brand;
import com.xqxy.model.Model;
import com.xqxy.model.Series;
import com.xqxy.model.StoreCard;

public class PayCarInfoActivity extends BaseActivity implements
		OnClickListener, OnItemSelectedListener {

	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	private EditText carNumTxt;
	private TextView buyBtn;

	private Spinner brandSpinner;
	private CarInfoAdapter brandAdapter;
	private Spinner seriesSpinner;
	private CarInfoAdapter seriesAdapter;
	private Spinner modelSpinner;
	private CarInfoAdapter modelAdapter;
	private int countRequest = 0;
	private String bid = "";
	private String sid = "";
	private String mid = "";
	private StoreCard card;
	private RequestWrapper wrapper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_car_info);
		initView();
		getCarInfo();
	}

	private void initView() {
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		carNumTxt = (EditText) findViewById(R.id.car_num);
		buyBtn = (TextView) findViewById(R.id.buy_btn);
		buyBtn.setOnClickListener(this);

		brandSpinner = (Spinner) findViewById(R.id.rst_brand);
		seriesSpinner = (Spinner) findViewById(R.id.rst_series);
		modelSpinner = (Spinner) findViewById(R.id.rst_model);
		brandSpinner.setOnItemSelectedListener(this);
		seriesSpinner.setOnItemSelectedListener(this);
		modelSpinner.setOnItemSelectedListener(this);
		card = (StoreCard) getIntent().getSerializableExtra(StoreCardDetailActivity.DATA);
	}

	public void btnOnClick(View v)
	{
		finish();
	}
	public void getCarInfo() {
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_brand);
//		sendDataByGet(new RequestWrapper(), NetworkAction.carF_series);
//		sendDataByGet(new RequestWrapper(), NetworkAction.carF_model);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType==(NetworkAction.carF_brand)) {
			brandAdapter = new CarInfoAdapter(this, NetworkAction.carF_brand,
					responseWrapper.getBrand());
			brandSpinner.setAdapter(brandAdapter);
			brandAdapter.notifyDataSetChanged();
			countRequest++;
		} else if (requestType==(NetworkAction.carF_series)) {
			seriesAdapter = new CarInfoAdapter(this, NetworkAction.carF_series,
					responseWrapper.getSeries());
			seriesSpinner.setAdapter(seriesAdapter);
			seriesAdapter.notifyDataSetChanged();
			countRequest++;
		} else if (requestType==(NetworkAction.carF_model)) {
			modelAdapter = new CarInfoAdapter(this, NetworkAction.carF_model,
					responseWrapper.getModel());
			modelSpinner.setAdapter(modelAdapter);
			modelAdapter.notifyDataSetChanged();
			countRequest++;
		} else if (requestType==(NetworkAction.orderF_buy_card)) {
			card.setCid(responseWrapper.getCid());
			Intent intent=new Intent();
			intent.setClass(this, StoreCardPayActivity.class);
			Bundle mb=new Bundle();
			mb.putSerializable(StoreCardDetailActivity.DATA, card);
			intent.putExtras(mb);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buy_btn:
			String carNum = carNumTxt.getText().toString();
			if (countRequest < 2) {
				Toast.makeText(this, "数据尚未加载完", Toast.LENGTH_SHORT).show();
				return;
			} else {
				if (carNum.equals("")) {
					Toast.makeText(this, "请填写车牌号码", Toast.LENGTH_SHORT).show();
					return;
				} else {
					wrapper = new RequestWrapper();
					wrapper.setCid(card.getCid());
					wrapper.setBid(bid);
					wrapper.setSid(sid);
					wrapper.setMid(mid);
					wrapper.setCar_num(carNum);
					wrapper.setIdentity(MyApplication.identity);
					sendData(wrapper, NetworkAction.orderF_buy_card);
				}
			}
			break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
//		Object object = parent.getItemAtPosition(position);
//		if (object instanceof Brand)
//			bid = ((Brand) object).getBid();
//		else if (object instanceof Series)
//			sid = ((Series) object).getSid();
//		else if (object instanceof Model)
//			mid = ((Model) object).getMid();
		Object object = parent.getItemAtPosition(position);
		RequestWrapper request=new RequestWrapper();
		if (object instanceof Brand)
		{
			bid = ((Brand) object).getBid();
			request.setBid(bid);
			sendDataByGet(request, NetworkAction.carF_series);
		}
		
		else if (object instanceof Series)
		{
			sid = ((Series) object).getSid();
			request.setSid(sid);
			sendDataByGet(request, NetworkAction.carF_model);
		}
		
		else if (object instanceof Model)
		{
			mid = ((Model) object).getMid();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
