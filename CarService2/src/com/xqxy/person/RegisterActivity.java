package com.xqxy.person;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.carservice.R;

public class RegisterActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {

	private ImageView finish;// 后退
	private EditText name;// 姓名
	private RadioButton rbMan;// 先生
	private RadioButton rbLady;// 女士
	private EditText phone;// 电话号码
	private String sex = "1";
	private EditText code;// 验证码
	private TextView getCode;// 获取验证码
	private EditText pwd;// 密码
	private TextView error;// 错误提示
	private TextView register;// 注册
	private RadioButton contract;// 同意条款
	private FrameLayout openPwdBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_register);
		initView();
	}

	private void initView() {
		finish = (ImageView) findViewById(R.id.rst_finish);
		name = (EditText) findViewById(R.id.rst_name);
		rbMan = (RadioButton) findViewById(R.id.rst_man);
		rbLady = (RadioButton) findViewById(R.id.rst_lady);
		phone = (EditText) findViewById(R.id.rst_phone);
		code = (EditText) findViewById(R.id.rst_code);
		getCode = (TextView) findViewById(R.id.rst_getcode);
		pwd = (EditText) findViewById(R.id.rst_pwd);
		error = (TextView) findViewById(R.id.rst_error);
		register = (TextView) findViewById(R.id.rst_register);
		contract = (RadioButton) findViewById(R.id.rst_contract);
		openPwdBtn = (FrameLayout) findViewById(R.id.rst_openpwd);
		finish.setOnClickListener(this);
		getCode.setOnClickListener(this);
		register.setOnClickListener(this);
		rbMan.setOnCheckedChangeListener(this);
		rbLady.setOnCheckedChangeListener(this);
		contract.setOnCheckedChangeListener(this);
		openPwdBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 后退按钮
		case R.id.rst_finish:

			break;
		// 获取验证码
		case R.id.rst_getcode:

			break;
		// 注册
		case R.id.rst_register:

			break;
		// 显示密码
		case R.id.rst_openpwd:
			pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		// 先生
		case R.id.rst_man:

			break;
		// 女士
		case R.id.rst_lady:

			break;
		// 条款
		case R.id.rst_contract:

			break;
		}

	}
}
