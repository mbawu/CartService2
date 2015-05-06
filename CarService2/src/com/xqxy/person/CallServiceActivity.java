package com.xqxy.person;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.JsonUtil;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.model.Address;

public class CallServiceActivity extends BaseActivity implements
		OnClickListener {

	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	private Calendar calendar;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private LinearLayout dateLayout;
	private TextView cancelBtn;
	private TextView confirmBtn;
	private TextView submitBtn;
	private TextView addBtn;
	private TextView dateTxt;
	private LinearLayout addressLayout;
	private TextView nameTxt;
	private TextView phoneTxt;
	private TextView car_numTxt;
	private TextView address_locationTxt;
	private TextView address_location_detailTxt;
	private TextView changeTxt;
	private EditText noteTxt;
	
	private Address address;
	private String pid;
	private String paid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_service);
		init();
	}

	private void init() {
		pid=getIntent().getStringExtra("pid");
		paid=getIntent().getStringExtra("paid");
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		backImageView.setOnClickListener(this);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		dateLayout = (LinearLayout) findViewById(R.id.call_date_layout);
		cancelBtn = (TextView) findViewById(R.id.call_cancel);
		confirmBtn = (TextView) findViewById(R.id.call_confirm);
		submitBtn = (TextView) findViewById(R.id.call_submit);
		addBtn = (TextView) findViewById(R.id.call_add_address);
		dateTxt = (TextView) findViewById(R.id.call_date);
		changeTxt = (TextView) findViewById(R.id.change_address);
		noteTxt= (EditText) findViewById(R.id.call_note);
		cancelBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		dateTxt.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
		changeTxt.setOnClickListener(this);

		// 地址
		addressLayout = (LinearLayout) findViewById(R.id.address_layout);
		nameTxt = (TextView) findViewById(R.id.address_name);
		phoneTxt = (TextView) findViewById(R.id.address_phone);
		car_numTxt = (TextView) findViewById(R.id.address_car_num);
		address_locationTxt = (TextView) findViewById(R.id.address_location);
		address_location_detailTxt = (TextView) findViewById(R.id.address_location_detail);

		datePicker = (DatePicker) findViewById(R.id.call_picker_date);
		timePicker = (TimePicker) findViewById(R.id.call_picker_time);
		timePicker.setIs24HourView(true);
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {

						if (isDateBefore(timePicker, view)) {
							view.init(calendar.get(Calendar.YEAR),
									calendar.get(Calendar.MONTH),
									calendar.get(Calendar.DAY_OF_MONTH), this);
							if ((calendar.get(Calendar.MINUTE) + 40) > 60) {
								timePicker.setCurrentHour(calendar
										.get(Calendar.HOUR_OF_DAY) + 1);
								timePicker.setCurrentMinute((calendar
										.get(Calendar.MINUTE) + 40) - 60);
							} else {
								timePicker.setCurrentHour(calendar
										.get(Calendar.HOUR_OF_DAY));
								timePicker.setCurrentMinute((calendar
										.get(Calendar.MINUTE) + 40));
							}
						}
					}

				});

		initTime();
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				if (isDateBefore(view, datePicker)) {
					initTime();
				}

			}
		});
	}

	private boolean isDateBefore(TimePicker tempView, DatePicker date) {
		Calendar mCalendar = Calendar.getInstance();
		Calendar tempCalendar = Calendar.getInstance();

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (minute + 40 > 60) {
			mCalendar.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH), hour + 1,
					(minute + 40 - 60));
		} else
			mCalendar.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH), hour, minute, 0);

		tempCalendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth(),
				tempView.getCurrentHour(), tempView.getCurrentMinute(), 0);
		if (tempCalendar.before(mCalendar))
			return true;
		else
			return false;
	}

	private void initTime() {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (minute + 40 > 60) {
			timePicker.setCurrentHour(hour + 1);
			timePicker.setCurrentMinute(minute + 40 - 60);
		} else {
			timePicker.setCurrentHour(hour);
			timePicker.setCurrentMinute(minute);
		}
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageTopBack:
			finish();
			break;
		// 支付按钮
		case R.id.call_submit:
			if(address==null)
			{
				Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
				return;
			}
			Log.i("test","dateTxt-->"+ dateTxt.getText().toString());
			if(dateTxt.getText().toString().equals(""))
			{
				Toast.makeText(this, "请选择服务时间", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!MyApplication.loginStat)
			{
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
				return;
			}
			submitOrder();
			break;
		// 取消按钮
		case R.id.call_cancel:
			dateLayout.setVisibility(View.GONE);
			break;
		// 确定按钮
		case R.id.call_confirm:
			dateLayout.setVisibility(View.GONE);
			String date = "";
			String time = "";
			if (isDateBefore(timePicker, datePicker)) {
				date += calendar.get(Calendar.YEAR) + "年"
						+ calendar.get(Calendar.MONTH) + "月"
						+ calendar.get(Calendar.DAY_OF_MONTH) + "日 ";

				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				if (minute + 40 > 60) {
					time += (hour + 1) + "时" + (minute + 40 - 60) + "分";
				} else
					time += hour + "时" + (minute + 40) + "分";
			} else {
				date += datePicker.getYear() + "年" + datePicker.getMonth()
						+ "月" + datePicker.getDayOfMonth() + "日 ";
				int hour = timePicker.getCurrentHour();
				int minute = timePicker.getCurrentMinute();
				if (minute + 40 > 60) {
					time += (hour + 1) + "时" + (minute + 40 - 60) + "分";
				} else
					time += hour + "时" + (minute + 40) + "分";
			}
			date = date + time;
			dateTxt.setText(date);
			break;
		// 日期输入框
		case R.id.call_date:
			dateLayout.setVisibility(View.VISIBLE);
			initTime();
			break;
		// 添加地址
		case R.id.call_add_address:
		case R.id.change_address:
			Intent intent = new Intent();
			if (MyApplication.loginStat) {
				intent.setClass(this, AdressActivity.class);
				intent.putExtra("select", "select");
			} else
				intent.setClass(this, LoginActivity.class);
			startActivity(intent);
			break;
		}

	}

	private void submitOrder() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date=new Date(datePicker.getYear()-1900, datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
		//Date类型转换成String类型
		String sdate=sdf.format(date);
		RequestWrapper wrapper=new RequestWrapper();
		wrapper.setIdentity(MyApplication.identity);
//		wrapper.setPid(pid);
//		wrapper.setPaid(paid);
		wrapper.setPid("1");
		wrapper.setPaid("13");
		wrapper.setAid(address.getAid());
		wrapper.setServer_time(sdate);
		wrapper.setNote(noteTxt.getText().toString());
		wrapper.setShowDialog(true);
		sendData(wrapper, NetworkAction.orderF_index);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if(requestType==NetworkAction.orderF_index)
		{
			Toast.makeText(this, "生成订单成功", Toast.LENGTH_SHORT).show();
			//跳转到支付页面
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		String addString=MyApplication.sp.getString("address", null);
		if(addString!=null)
		{
			address=JsonUtil.fromJson(addString, Address.class);
		}
		if (address != null) {
			addBtn.setVisibility(View.GONE);
			addressLayout.setVisibility(View.VISIBLE);
			nameTxt.setText(address.getName());
			phoneTxt.setText(address.getPhone());
			car_numTxt.setText(address.getCar_num());
			address_locationTxt.setText(address.getAddress());
			address_location_detailTxt.setText(address.getDetailed());
		} else {
			addBtn.setVisibility(View.VISIBLE);
			addressLayout.setVisibility(View.GONE);
		}
	}
}
