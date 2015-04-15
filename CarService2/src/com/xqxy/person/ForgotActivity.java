package com.xqxy.person;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.carservice.R;

/**
 * 忘记密码
 * @author Administrator
 *
 */
public class ForgotActivity extends BaseActivity implements OnClickListener {

	private ImageView finishBtn;
	private EditText phone;
	private EditText code;
	private TextView getCode;
	private EditText pwd;
	private FrameLayout openPwdBtn;
	private TextView commit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_forgot);
		init();
	}
	private void init() {
		finishBtn=(ImageView) findViewById(R.id.fot_finish);
		phone=(EditText) findViewById(R.id.fot_phone);
		code=(EditText) findViewById(R.id.fot_code);
		getCode=(TextView) findViewById(R.id.fot_getcode);
		pwd=(EditText) findViewById(R.id.fot_pwd);
		openPwdBtn=(FrameLayout) findViewById(R.id.fot_openpwd);
		commit=(TextView) findViewById(R.id.fot_commit);
		finishBtn.setOnClickListener(this);
		getCode.setOnClickListener(this);
		openPwdBtn.setOnClickListener(this);
		commit.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fot_finish:
			
			break;
	case R.id.fot_getcode:
			
			break;
	case R.id.fot_openpwd:
			pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		break;
	case R.id.fot_commit:
		
		break;
		}
		
	}
}
