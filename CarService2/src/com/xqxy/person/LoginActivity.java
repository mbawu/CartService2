package com.xqxy.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.Cst;
import com.xqxy.baseclass.DataFormatCheck;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.model.AutoLogin;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private ImageView finishBtn;
	private EditText userNameTxt;
	private EditText pwdTxt;
	private TextView loginBtn;
	private TextView forgotBtn;
	private TextView registerBtn;
	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_login);
		app = (MyApplication) getApplication();
		initView();
	}

	private void initView() {
		finishBtn = (ImageView) findViewById(R.id.login_finish);
		userNameTxt = (EditText) findViewById(R.id.login_username);
		pwdTxt = (EditText) findViewById(R.id.login_pwd);
		loginBtn = (TextView) findViewById(R.id.login_btn);
		forgotBtn = (TextView) findViewById(R.id.login_forgot);
		registerBtn = (TextView) findViewById(R.id.login_register);
		finishBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		forgotBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		app=(MyApplication) getApplicationContext();
		getLogin();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(MyApplication.loginStat)
		{
			finish();
		}
	}
	
	private void getLogin() {
		AutoLogin info=app.getAutoLogin();
		if(info!=null)
		{
			userNameTxt.setText(info.getUsername());
			pwdTxt.setText(info.getPassword());
		}
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType == (NetworkAction.userF_login)) {
			Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
			AutoLogin autoLogin = new AutoLogin(userNameTxt.getText()
					.toString(), pwdTxt.getText().toString());
			app.setAutoLogin(autoLogin);
			MyApplication.loginStat = true;
			MyApplication.identity = responseWrapper.getIdentity().get(0)
					.getIdentity();
			
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		RequestWrapper wrapper = new RequestWrapper();
		switch (v.getId()) {
		// 登录按钮
		case R.id.login_btn:
			String userName = userNameTxt.getText().toString();
			String password = pwdTxt.getText().toString();
			if (!DataFormatCheck.isMobile(userName)) {
				Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (userName.equals("") || password.equals("")) {
				Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			wrapper.setPhone(userName);
			wrapper.setPassword(password);
			wrapper.setShowDialog(true);
			sendData(wrapper, NetworkAction.userF_login);
			break;
		// 忘记密码按钮
		case R.id.login_forgot:
			intent = new Intent().setClass(this, ForgotActivity.class);
			break;
		// 注册按钮
		case R.id.login_register:
			intent = new Intent().setClass(this, RegisterActivity.class);
			break;
		// 关闭按钮
		case R.id.login_finish:
			finish();
			break;
		}
		if (intent != null)
			startActivity(intent);

	}
}
