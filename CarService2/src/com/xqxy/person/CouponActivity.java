package com.xqxy.person;

import java.sql.Wrapper;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.Cst;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Coupon;
import com.xqxy.model.Credit;
import com.xqxy.model.Message;
import com.xqxy.person.CreditActivity.CreditAdapter;
import com.xqxy.person.CreditActivity.ViewHolder;

public class CouponActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	private ListView listView;
	private CouponAdapter adapter;
	private ArrayList<Coupon> datas;
	private TextView nodata;
	private RequestWrapper requestWrapper;
	private RadioButton noUse;
	private RadioButton used;
	private RadioButton expired;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_coupon);
		init();
	}

	private void init() {
//		datas=new ArrayList<Coupon>();
		
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CouponActivity.this.finish();

			}
		});
		nodata = (TextView) findViewById(R.id.nodataTxt);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new CouponAdapter(this);
		listView.setAdapter(adapter);
		
		noUse = (RadioButton) findViewById(R.id.coupon_noUse);
		used = (RadioButton) findViewById(R.id.coupon_used);
		expired = (RadioButton) findViewById(R.id.coupon_expired);
		noUse.setOnCheckedChangeListener(this);
		used.setOnCheckedChangeListener(this);
		expired.setOnCheckedChangeListener(this);
		
		noUse.setChecked(true);
	}

	
	
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if(requestType==NetworkAction.centerF_user_coupon)
		{
			datas = responseWrapper.getCoupon();
			Log.i(Cst.TAG," datas.size()-->"+ datas.size()+"");
			if (datas.size() > 0) {
				nodata.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				adapter.setDataList(datas);
				adapter.notifyDataSetChanged();
			} else {
				nodata.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
		}
	
	}

	class CouponAdapter extends CarBaseAdapter<Coupon> {

		public CouponAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.person_coupon_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.coupon_price = (TextView) convertView
						.findViewById(R.id.coupon_price);
				viewHolder.coupon_name = (TextView) convertView
						.findViewById(R.id.coupon_name);
				viewHolder.coupon_num = (TextView) convertView
						.findViewById(R.id.coupon_num);
				viewHolder.coupon_expired = (TextView) convertView
						.findViewById(R.id.coupon_expired);
				viewHolder.coupon_status = (TextView) convertView
						.findViewById(R.id.coupon_status);
				viewHolder.coupon_date = (TextView) convertView
						.findViewById(R.id.coupon_date);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			try {
				
			
			final Coupon coupon = getItem(position);
			DecimalFormat decimalFormat = new DecimalFormat("#");
			String price = decimalFormat.format(Double.valueOf(coupon
					.getPrice()));
			viewHolder.coupon_price.setText(price + "元");
			String name = coupon.getName();
			if (name == null)
				name = price + "元优惠券全场使用";
			else
				name = price + "元优惠券限" + name + "使用";
			viewHolder.coupon_name.setText(name);
			viewHolder.coupon_num.setText("号码：" + coupon.getCode());
			String status = coupon.getStatus();
			if (status.equals("1"))
				status = "状态：未使用";
			else if (status.equals("2"))
				status = "状态：已使用";

			viewHolder.coupon_expired.setVisibility(View.VISIBLE);
			viewHolder.coupon_status.setText(status);
			viewHolder.coupon_date.setText(coupon.getOver_time());
			} catch (Exception e) {
				// TODO: handle exception
			}
			return convertView;
		}
	}

	class ViewHolder {
		TextView coupon_price;
		TextView coupon_name;
		TextView coupon_num;
		TextView coupon_expired;
		TextView coupon_status;
		TextView coupon_date;

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	
//		datas.clear();
		requestWrapper = new RequestWrapper();
		requestWrapper.setIdentity(MyApplication.identity);
		requestWrapper.setShowDialog(true);
		switch (buttonView.getId()) {
		case R.id.coupon_noUse:
			if(isChecked)
			{
				requestWrapper.setStatus("1");
				sendDataByGet(requestWrapper, NetworkAction.centerF_user_coupon);
			}
				
			break;
		case R.id.coupon_used:
			if(isChecked)
			{
				requestWrapper.setStatus("2");
				sendDataByGet(requestWrapper, NetworkAction.centerF_user_coupon);
			}
				
			break;
		case R.id.coupon_expired:
			if(isChecked)
			{
				requestWrapper.setStatus("3");
				sendDataByGet(requestWrapper, NetworkAction.centerF_user_coupon);
			}
				
			break;
		}
		
	}
}
