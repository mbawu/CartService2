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
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.carservice.R;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private ImageView finishBtn;
	private EditText userNameTxt;
	private EditText pwdTxt;
	private TextView loginBtn;
	private TextView forgotBtn;
	private TextView registerBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_login);
		initView();
	}

	private void initView() {
		finishBtn=(ImageView) findViewById(R.id.login_finish);
		userNameTxt=(EditText) findViewById(R.id.login_username);
		pwdTxt=(EditText) findViewById(R.id.login_pwd);
		loginBtn=(TextView) findViewById(R.id.login_btn);
		forgotBtn=(TextView) findViewById(R.id.login_forgot);
		registerBtn=(TextView) findViewById(R.id.login_register);
		finishBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		forgotBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
	}


//	@Override
//	public void showResualt(String msg, NetworkAction requestType) {
//		// TODO Auto-generated method stub
//		super.showResualt(msg, requestType);
//	}
	@Override
	public void onClick(View v) {
		Intent intent=null;
		RequestWrapper wrapper=new RequestWrapper();
		switch (v.getId()) {
		// 登录按钮
		case R.id.login_btn:
			String userName=userNameTxt.getText().toString();
			String password=pwdTxt.getText().toString();
			if(userName.equals("") || password.equals(""))
			{
				Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			wrapper.setUserName(userName);
			wrapper.setPassword(password);
			sendData(wrapper,NetworkAction.user_login);
			break;
		// 忘记密码按钮
		case R.id.login_forgot:
			intent=new Intent().setClass(this, ForgotActivity.class);
			break;
		// 注册按钮
		case R.id.login_register:
			intent=new Intent().setClass(this, RegisterActivity.class);
			break;
		// 关闭按钮
		case R.id.login_finish:
			finish();
			break;
		}
		if(intent!=null)
			startActivity(intent);

	}
}
