package com.xqxy.person;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.JsonUtil;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.model.Address;
import com.xqxy.model.Cart;

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
	private LinearLayout addBtn;
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
	private boolean cartModule=false;
	private String cartJson;
	
	private ArrayList<Cart> datas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_service);
		init();
	}

	private void init() {
		
		check();
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		backImageView.setOnClickListener(this);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		dateLayout = (LinearLayout) findViewById(R.id.call_date_layout);
		cancelBtn = (TextView) findViewById(R.id.call_cancel);
		confirmBtn = (TextView) findViewById(R.id.call_confirm);
		submitBtn = (TextView) findViewById(R.id.call_submit);
		addBtn = (LinearLayout) findViewById(R.id.call_add_address);
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

						if (isDateBefore(timePicker, view) || isDateAfter(timePicker, view)) {
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
							
							initTime();
						}
					}

				});

		initTime();
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				if (isDateBefore(view, datePicker) || isDateAfter(view, datePicker)) {
					initTime();
				}

			}
		});
		dateTxt.setText(getDate());
	}

	@SuppressWarnings("unchecked")
	private void check() {
		datas=(ArrayList<Cart>) getIntent().getSerializableExtra("data");
		cartJson=getIntent().getStringExtra("cart");
		if(cartJson!=null)
		{
			cartModule=true;
		}
		else
		{
			pid=datas.get(0).getPid();
			paid=datas.get(0).getPaid();
			Log.i("test", "pid-->"+pid);
			Log.i("test", "paid-->"+paid);
		}
		
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
					calendar.get(Calendar.DAY_OF_MONTH), hour, minute+40, 0);

		tempCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
				timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
		if (tempCalendar.before(mCalendar))
			return true;
		else
			return false;
	}

	private boolean isDateAfter(TimePicker tempView, DatePicker date) {
		Calendar mCalendar = Calendar.getInstance();
		Calendar tempCalendar = Calendar.getInstance();

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (minute + 40 > 60) {
			mCalendar.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)+2, hour + 1,
					(minute + 40 - 60));
		} else
			mCalendar.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)+2, hour, minute+40, 0);

		tempCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
				timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
		if (tempCalendar.after(mCalendar))
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
			timePicker.setCurrentMinute(minute+40);
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
			
			dateTxt.setText(getDate());
			break;
		// 日期输入框
		case R.id.call_date:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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

	private String getDate()
	{
		String date = "";
		String time = "";
		calendar.setTimeInMillis(System.currentTimeMillis());
		if (isDateBefore(timePicker, datePicker)) {
			date += calendar.get(Calendar.YEAR) + "年"
					+ (calendar.get(Calendar.MONTH)+1) + "月"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "日 ";

			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			if (minute + 40 > 60) {
				time += (hour + 1) + "时" + (minute + 40 - 60) + "分";
			} else
				time += hour + "时" + (minute + 40) + "分";
		} else {
			date += datePicker.getYear() + "年" + (datePicker.getMonth()+1)
					+ "月" + datePicker.getDayOfMonth() + "日 ";
			int hour = timePicker.getCurrentHour();
			int minute = timePicker.getCurrentMinute();
				time += hour + "时" + minute+ "分";
		}
		date = date + time;
		return date;
	}
	
	private void submitOrder() { 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date=new Date(datePicker.getYear()-1900, datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
		//Date类型转换成String类型
		String sdate=sdf.format(date);
		Log.i("test",sdate );
		RequestWrapper wrapper=new RequestWrapper();
		wrapper.setIdentity(MyApplication.identity);
		wrapper.setShowDialog(true);
		wrapper.setNote(noteTxt.getText().toString());
		wrapper.setServer_time(sdate);
		wrapper.setAid(address.getAid());
		if(cartModule)
		{
			wrapper.setCart(cartJson);
			sendData(wrapper, NetworkAction.cartF_cart_order);
		}
		else
		{
			wrapper.setPid(pid);
			wrapper.setPaid(paid);
//			wrapper.setPid("1");
//			wrapper.setPaid("13");
			sendData(wrapper, NetworkAction.orderF_index);
		}
	
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if(requestType==NetworkAction.orderF_index
				||requestType==NetworkAction.cartF_cart_order )
		{
			Toast.makeText(this, "生成订单成功", Toast.LENGTH_SHORT).show();
			//跳转到支付页面
			Intent intent=new Intent();
			intent.setClass(this, OrderPayActivity.class);
			intent.putExtra("data", (Serializable) datas);
			intent.putExtra("address", (Serializable) address);
			intent.putExtra("oid",responseWrapper.getOrder().get(0).getOid());
			startActivity(intent);
			finish();
		}
		
		if(requestType==NetworkAction.cartF_cart_order)
		{
			Intent intent=new Intent(Cst.CART_CAHNGE);
			sendBroadcast(intent);
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
