package com.xqxy.person;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
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
 * 忘记密码
 * @author Administrator
 *
 */
public class ForgotActivity extends BaseActivity implements OnClickListener {

	private ImageView finishBtn;//返回按钮
	private EditText phoneTxt; //电话输入框
	private EditText codeTxt; //验证码文本框
	private TextView getCode; //获取验证码按钮
	private EditText pwdTxt; //密码输入框
	private FrameLayout openPwdBtn; //打开可视化模式
	private TextView commit; //确认按钮
	private MyApplication app;
	
	private int defaultCount = 60;// 默认多长时间(秒)重复获取验证码
	private int count = defaultCount;
	private RequestWrapper wrapper;
	private String phone;
	private boolean mbDisplayFlg = true;  
	private ImageView pwdImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_forgot);
		init();
	}
	private void init() {
		finishBtn=(ImageView) findViewById(R.id.fot_finish);
		phoneTxt=(EditText) findViewById(R.id.fot_phone);
		codeTxt=(EditText) findViewById(R.id.fot_code);
		getCode=(TextView) findViewById(R.id.fot_getcode);
		pwdTxt=(EditText) findViewById(R.id.fot_pwd);
		openPwdBtn=(FrameLayout) findViewById(R.id.fot_openpwd);
		commit=(TextView) findViewById(R.id.fot_commit);
		pwdImg=(ImageView) findViewById(R.id.pwd_bg);
		app=(MyApplication) getApplicationContext();
		finishBtn.setOnClickListener(this);
		getCode.setOnClickListener(this);
		openPwdBtn.setOnClickListener(this);
		commit.setOnClickListener(this);
		openPwdBtn.setOnClickListener(new OnClickListener() {  
			  
	        @Override  
	        public void onClick(View v) {  
	            // TODO Auto-generated method stub  
//	            Log.d("AndroidTest", "mbDisplayFlg = " + mbDisplayFlg);  
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
	        }  
	          
	       });  
	}
	
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		
		if (requestType==(NetworkAction.userF_send_phone)) {
			Toast.makeText(this, "验证码已发送到手机", Toast.LENGTH_SHORT).show();
			// 正确拿到验证码以后开始倒计时
			handler.sendEmptyMessage(count);
			getCode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(ForgotActivity.this,
							"请勿在" + count + "秒内重复获取验证码！",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		else if (requestType==(NetworkAction.userF_resetpwd)) {
			Toast.makeText(this, "操作成功", Toast.LENGTH_SHORT).show();
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
		case R.id.fot_finish:
			finish();
			break;
	case R.id.fot_getcode:
		getCode();
			break;
	case R.id.fot_openpwd:
			pwdTxt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		break;
	case R.id.fot_commit:
		String code = codeTxt.getText().toString();
		String pwd = pwdTxt.getText().toString();
		if(!DataFormatCheck.isMobile(phone))
		{
			Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
			return;
		}
		if ( phone.equals("") || code.equals("")
				|| pwd.equals("")) {
			Toast.makeText(ForgotActivity.this,
					"请填写完整的数据",
					Toast.LENGTH_SHORT).show();
			return;
		}
		wrapper.setPhone(phone);
		wrapper.setCode(code);
		wrapper.setPassword(pwd);
		sendData(wrapper, NetworkAction.userF_resetpwd);
		break;
		}
		
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
