package com.xqxy.person;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.DataFormatCheck;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;

public class AddressAddActivity extends BaseActivity {

	private EditText nameTxt;
	private EditText phoneTxt;
	private EditText carNumTxt;
	private EditText locationTxt;
	private EditText locationDelTxt;
	private TextView saveBtn;
	private RequestWrapper wrapper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_address_add);
		init();
	}

	private void init() {
		//登录
		wrapper=new RequestWrapper();
		wrapper.setPhone("13466899985");
		wrapper.setPassword("1");
		sendData(wrapper, NetworkAction.userF_login);
		
		nameTxt=(EditText) findViewById(R.id.add_name);
		phoneTxt=(EditText) findViewById(R.id.add_phone);
		carNumTxt=(EditText) findViewById(R.id.add_car_num);
		locationTxt=(EditText) findViewById(R.id.add_location);
		locationDelTxt=(EditText) findViewById(R.id.add_location_detail);
		saveBtn=(TextView) findViewById(R.id.address_save);
		saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name=nameTxt.getText().toString();
				String phone=phoneTxt.getText().toString();
				String carNum=carNumTxt.getText().toString();
				String location=locationTxt.getText().toString();
				String locationDel=locationDelTxt.getText().toString();
				if(name.equals("") || phone.equals("")||carNum.equals("")||location.equals(""))
				{
					Toast.makeText(AddressAddActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
					return;
				}
					
				if(!DataFormatCheck.isMobile(phone))
				{
					Toast.makeText(AddressAddActivity.this, "请填写正确手机号码", Toast.LENGTH_SHORT).show();
					return;
				}
					
				wrapper=new RequestWrapper();
				wrapper.setName(name);
				wrapper.setPhone(phone);
				wrapper.setCar_num(carNum);
				wrapper.setAddress(location);
				wrapper.setDetailed(locationDel);
				wrapper.setLng("110");
				wrapper.setLat("110");
				wrapper.setIdentity(MyApplication.identity);
				sendData(wrapper, NetworkAction.centerF_add_address);
				
			}
		});
	}
	
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if(requestType.equals(NetworkAction.centerF_add_address)){
			Toast.makeText(this, responseWrapper.getMsg(), Toast.LENGTH_SHORT).show();
			finish();
		}
		else if(requestType.equals(NetworkAction.userF_login)){
			MyApplication.identity=responseWrapper.getIdentity().get(0).getIdentity();
		}
	}
	public void btnOnClick(View view) {
		finish();
	}
}
