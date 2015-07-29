package com.xqxy.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.cn.hongwei.BaiduLoction;
import com.cn.hongwei.BaiduLoction.LocationCallback;
import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.DataFormatCheck;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.model.Address;

/**
 * 添加地址类
 * @author Administrator
 *
 */
public class AddressAddActivity extends BaseActivity {

	public static String ADDRESS_ID = "addressid";//地址ID

	private EditText nameTxt; //姓名文本框
	private EditText phoneTxt; //电话输入框
	private EditText carNumTxt; //车牌号输入框
	private EditText locationTxt; //地址输入框
	private EditText locationDelTxt; //详细地址输入框
	private TextView saveBtn; //保存按钮
	private RequestWrapper wrapper; //发请求实体类
	private String aid = ""; //地址ID
	private Address address; //地址实体类
	private String lng = "0"; //坐标经纬度
	private String lat = "0"; //坐标经纬度
	private String temp; //临时参数

	/**
	 * activity的初始化方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_address_add);
		init();
	}

	/**
	 * 初始化控件以及数据
	 */
	private void init() {

		nameTxt = (EditText) findViewById(R.id.add_name);
		phoneTxt = (EditText) findViewById(R.id.add_phone);
		carNumTxt = (EditText) findViewById(R.id.add_car_num);
		locationTxt = (EditText) findViewById(R.id.add_location);
		locationDelTxt = (EditText) findViewById(R.id.add_location_detail);

		checkModel();
		saveBtn = (TextView) findViewById(R.id.address_save);
		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = nameTxt.getText().toString();
				String phone = phoneTxt.getText().toString();
				String carNum = carNumTxt.getText().toString();
				String location = locationTxt.getText().toString();
				String locationDel = locationDelTxt.getText().toString();
				if (name.equals("") || phone.equals("") || carNum.equals("")
						|| location.equals("")) {
					Toast.makeText(AddressAddActivity.this, "请填写完整信息",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (!DataFormatCheck.isMobile(phone)) {
					Toast.makeText(AddressAddActivity.this, "请填写正确手机号码",
							Toast.LENGTH_SHORT).show();
					return;
				}

				wrapper = new RequestWrapper();
				if (aid != null)
					wrapper.setAid(aid);
				wrapper.setName(name);
				wrapper.setPhone(phone);
				wrapper.setCar_num(carNum);
				wrapper.setAddress(location);
				wrapper.setDetailed(locationDel);
				wrapper.setLng(lng);
				wrapper.setLat(lat);
				wrapper.setIdentity(MyApplication.identity);
				sendData(wrapper, NetworkAction.centerF_add_address);

			}
		});
	}

	/**
	 * 检查模式
	 */
	private void checkModel() {
		Intent intent = getIntent();
		aid = intent.getStringExtra(ADDRESS_ID);
		if (aid != null) {
			address = (Address) intent.getSerializableExtra("address");
			nameTxt.setText(address.getName());
			phoneTxt.setText(address.getPhone());
			carNumTxt.setText(address.getCar_num());
			locationTxt.setText(address.getAddress());
			locationDelTxt.setText(address.getDetailed());
		} else {
			getLocation();
		}
	}

	/**
	 * 获取定位地址
	 */
	private void getLocation() {
		temp = MyApplication.address;
		locationTxt.setText(MyApplication.address);
		locationTxt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					locationTxt.setText("");
				else {
					if (locationTxt.getText().toString().equals("")
							|| locationTxt.getText().toString().equals(temp))
						locationTxt.setText(temp);
				}

			}
		});
		locationDelTxt.setText(MyApplication.detail);
		lng = String.valueOf(MyApplication.lng);
		lat = String.valueOf(MyApplication.lat);

	}

	/**
	 * 解析数据类
	 */
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType == (NetworkAction.centerF_add_address)) {
			MyApplication.refresh = true;
			String msg = "";
			if (aid == null)
				msg = "添加成功";
			else
				msg = "修改成功";
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			finish();
		}
		// else if (requestType.equals(NetworkAction.userF_login)) {
		// MyApplication.identity = responseWrapper.getIdentity().get(0)
		// .getIdentity();
		// }
	}

	/**
	 * 按钮点击事件
	 * @param view
	 */
	public void btnOnClick(View view) {
		finish();
	}
}
