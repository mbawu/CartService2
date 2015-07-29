package com.xqxy.person;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.xqxy.carservice.activity.WebActivity;
import com.xqxy.carservice.adapter.CarInfoAdapter;
import com.xqxy.model.AutoLogin;
import com.xqxy.model.Brand;
import com.xqxy.model.Model;
import com.xqxy.model.Series;

/**
 * 注册页面
 * @author Administrator
 *
 */
public class RegisterActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener, OnItemSelectedListener {

	private ImageView finish;// 后退
	private EditText nameTxt;// 姓名
	private RadioButton rbMan;// 先生
	private RadioButton rbLady;// 女士
	private EditText phoneTxt;// 电话号码
	private String sex = "1";
	private EditText codetTxt;// 验证码
	private TextView getCode;// 获取验证码
	private EditText pwdTxt;// 密码
	private TextView error;// 错误提示
	private TextView register;// 注册
	private CheckBox contract;// 同意条款
	private FrameLayout openPwdBtn;

	private Spinner brandSpinner;
	private CarInfoAdapter brandAdapter;
	private Spinner seriesSpinner;
	private CarInfoAdapter seriesAdapter;
	private Spinner modelSpinner;
	private CarInfoAdapter modelAdapter;

	private int countRequest = 0;
	private boolean agreeContract = true;
	private String bid = "";
	private String sid = "";
	private String mid = "";

	private int defaultCount = 60;// 默认多长时间(秒)重复获取验证码
	private int count = defaultCount;
	private RequestWrapper wrapper;
	private String phone;
	private MyApplication app;
	private ImageView pwdImg;
	private boolean mbDisplayFlg = true;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_register);
		initView();
		getCarInfo();
	}

	private void initView() {
		finish = (ImageView) findViewById(R.id.rst_finish);
		nameTxt = (EditText) findViewById(R.id.rst_name);
		rbMan = (RadioButton) findViewById(R.id.rst_man);
		rbLady = (RadioButton) findViewById(R.id.rst_lady);
		phoneTxt = (EditText) findViewById(R.id.rst_phone);
		codetTxt = (EditText) findViewById(R.id.rst_code);
		getCode = (TextView) findViewById(R.id.rst_getcode);
		pwdTxt = (EditText) findViewById(R.id.rst_pwd);
		error = (TextView) findViewById(R.id.rst_error);
		register = (TextView) findViewById(R.id.rst_register);
		contract = (CheckBox) findViewById(R.id.rst_contract);
		openPwdBtn = (FrameLayout) findViewById(R.id.rst_openpwd);
		brandSpinner = (Spinner) findViewById(R.id.rst_brand);
		seriesSpinner = (Spinner) findViewById(R.id.rst_series);
		modelSpinner = (Spinner) findViewById(R.id.rst_model);
		pwdImg=(ImageView) findViewById(R.id.pwd_bg);
		brandSpinner.setOnItemSelectedListener(this);
		seriesSpinner.setOnItemSelectedListener(this);
		modelSpinner.setOnItemSelectedListener(this);
		finish.setOnClickListener(this);
		getCode.setOnClickListener(this);
		register.setOnClickListener(this);
		rbMan.setOnCheckedChangeListener(this);
		rbLady.setOnCheckedChangeListener(this);
		contract.setOnCheckedChangeListener(this);
		openPwdBtn.setOnClickListener(this);
		findViewById(R.id.rst_txt).setOnClickListener(this);
		app=(MyApplication) getApplicationContext();
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
		} else if (requestType==(NetworkAction.userF_send_phone)) {
			Toast.makeText(this, "验证码已发送到手机", Toast.LENGTH_SHORT).show();
			// 正确拿到验证码以后开始倒计时
			handler.sendEmptyMessage(count);
			getCode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(RegisterActivity.this,
							"请勿在" + count + "秒内重复获取验证码！",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		else if(requestType==(NetworkAction.userF_register))
		{
			Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
			MyApplication.loginStat = true;
			MyApplication.identity=responseWrapper.getIdentity().get(0).getIdentity();
			AutoLogin autoLogin = new AutoLogin(phoneTxt.getText()
					.toString(), pwdTxt.getText().toString());
			autoLogin.setLoginState(true);
			app.setAutoLogin(autoLogin);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		wrapper = new RequestWrapper();
		phone = phoneTxt.getText().toString();
		switch (v.getId()) {
		// 后退按钮
		case R.id.rst_finish:
			finish();
			break;
		// 获取验证码
		case R.id.rst_getcode:
			getCode();
			break;
		// 注册
		case R.id.rst_register:
			String name = nameTxt.getText().toString();

			String code = codetTxt.getText().toString();
			String pwd = pwdTxt.getText().toString();
//			if (countRequest != 3) {
//				error.setVisibility(View.VISIBLE);
//				error.setText("数据尚未加载结束，请稍候...");
//				return;
//			}
			if (!agreeContract) {
				error.setVisibility(View.VISIBLE);
				error.setText("请勾选同意使用服务条款");
				return;
			}
			if (name.equals("") || phone.equals("") || code.equals("")
					|| pwd.equals("")) {
				error.setVisibility(View.VISIBLE);
				error.setText("请填写完整的数据");
				return;
			}

			wrapper.setSurname(name);
			wrapper.setSex(sex);
			wrapper.setPhone(phone);
			wrapper.setBid(bid);
			wrapper.setSid(sid);
			wrapper.setMid(mid);
			wrapper.setCode(code);
			wrapper.setLat("39.0100");
			wrapper.setLng("116.0400");
			wrapper.setPassword(pwd);
			sendData(wrapper, NetworkAction.userF_register);          
			break;
		// 显示密码
		case R.id.rst_openpwd:
			   if (!mbDisplayFlg) {  
	                // display password text, for example "123456"  
	            	pwdTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); 
	            	pwdImg.setBackgroundResource(R.drawable.open_pwd);
	            } else {  
	                // hide password, display "."  
	            	pwdTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());  
	            	pwdImg.setBackgroundResource(R.drawable.close_pwd);
	            }  
	            mbDisplayFlg = !mbDisplayFlg;  
	            pwdTxt.postInvalidate();  
	        
			break;
		case R.id.rst_txt:
			Intent intent1 = new Intent();
			intent1.setClass(this, WebActivity.class);
			intent1.putExtra("name", "agreement");
			startActivity(intent1);
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		// 先生
		case R.id.rst_man:
			if (isChecked)
				sex = "1";
			break;
		// 女士
		case R.id.rst_lady:
			if (isChecked)
				sex = "2";
			break;
		// 条款
		case R.id.rst_contract:
			agreeContract = isChecked;
			break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
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

	Handler handler = new Handler() {
		@SuppressLint("ResourceAsColor")
		public void handleMessage(android.os.Message msg) {
			if (count > 0) {
				getCode.setText("" + count);
				getCode.setBackgroundColor(R.color.more_gray);
				getCode.setPadding(20, 20, 20, 20);
				count--;
				handler.sendEmptyMessageDelayed(count, 1000);
			} else {
				changeBtnStyle();
				getCode.setText("获取验证码");
				count = defaultCount;
				getCode.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						getCode();
					}

				});
			}
		}

	};

	private void changeBtnStyle() {
		getCode.setText("获取验证码");
		getCode.setBackgroundResource(R.drawable.ok_btn_bg);
		getCode.setPadding(20, 20, 20, 20);

	}

	private void getCode() {
		if(!DataFormatCheck.isMobile(phone))
		{
			Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (phone.equals("")) {
			Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		wrapper.setPhone(phone);
		sendData(wrapper, NetworkAction.userF_send_phone);
	}
}
