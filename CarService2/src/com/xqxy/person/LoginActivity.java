package com.xqxy.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.DataFormatCheck;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.model.AutoLogin;

/**
 * 登录页面
 * @author Administrator
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private ImageView finishBtn; //返回按钮
	private EditText userNameTxt; //用户名输入框
	private EditText pwdTxt; //密码输入框
	private TextView loginBtn; //登录按钮
	private TextView forgotBtn; //忘记密码
	private TextView registerBtn; //注册按钮
	private MyApplication app; //获取application实体类

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
//			pwdTxt.setText(info.getPassword());
		}
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType == (NetworkAction.userF_login)) {
			Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
			
			MyApplication.loginStat = true;
			MyApplication.identity = responseWrapper.getIdentity().get(0)
					.getIdentity();
			AutoLogin autoLogin = new AutoLogin(userNameTxt.getText()
					.toString(), pwdTxt.getText().toString());
			autoLogin.setLoginState(true);
			app.setAutoLogin(autoLogin);
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
