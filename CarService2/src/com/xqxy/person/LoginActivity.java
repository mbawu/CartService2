package com.xqxy.person;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
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


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 登录按钮
		case R.id.login_btn:

			break;
		// 忘记密码按钮
		case R.id.login_forgot:

			break;
		// 注册按钮
		case R.id.login_register:

			break;
		// 关闭按钮
		case R.id.login_finish:
			finish();
			break;
		}

	}
}
