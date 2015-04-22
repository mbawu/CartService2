package com.xqxy.person;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.InputType;
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

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.Cst;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarInfoAdapter;
import com.xqxy.model.Brand;
import com.xqxy.model.Model;
import com.xqxy.model.Series;

public class RegisterActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener,OnItemSelectedListener {

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

	private int count = 0;
	private boolean agreeContract = true;
	private String bid="";
	private String sid="";
	private String mid="";

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
	}

	public void getCarInfo() {
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_brand);
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_series);
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_model);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType.equals(NetworkAction.carF_brand)) {
			brandAdapter = new CarInfoAdapter(this, NetworkAction.carF_brand,
					responseWrapper.getBrand());
			brandSpinner.setAdapter(brandAdapter);
			brandAdapter.notifyDataSetChanged();
			count++;
		} else if (requestType.equals(NetworkAction.carF_series)) {
			seriesAdapter = new CarInfoAdapter(this, NetworkAction.carF_series,
					responseWrapper.getSeries());
			seriesSpinner.setAdapter(seriesAdapter);
			seriesAdapter.notifyDataSetChanged();
			count++;
		} else if (requestType.equals(NetworkAction.carF_model)) {
			modelAdapter = new CarInfoAdapter(this, NetworkAction.carF_model,
					responseWrapper.getModel());
			modelSpinner.setAdapter(modelAdapter);
			modelAdapter.notifyDataSetChanged();
			count++;
		}else if(requestType.equals(NetworkAction.userF_send_phone))
		{
			Toast.makeText(this, "验证码已发送到手机", Toast.LENGTH_SHORT).show();
		}
	}

	
	
	@Override
	public void onClick(View v) {
		RequestWrapper wrapper=new RequestWrapper();
		String phone = phoneTxt.getText().toString();
		switch (v.getId()) {
		// 后退按钮
		case R.id.rst_finish:
			finish();
			break;
		// 获取验证码
		case R.id.rst_getcode:
			if(phone.equals(""))
			{
				Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			wrapper.setPhone(phone);
			sendData(wrapper, NetworkAction.userF_send_phone);
			break;
		// 注册
		case R.id.rst_register:
			String name = nameTxt.getText().toString();
			
			String code = codetTxt.getText().toString();
			String pwd = pwdTxt.getText().toString();
			if (count != 3) {
				error.setVisibility(View.VISIBLE);
				error.setText( "数据尚未加载结束，请稍候...");
				return;
			}
			if (!agreeContract) {
				error.setVisibility(View.VISIBLE);
				error.setText( "请勾选同意使用服务条款");
				return;
			}
			if (name.equals("") || phone.equals("") || code.equals("")
					|| pwd.equals("")) {
				error.setVisibility(View.VISIBLE);
				error.setText( "请填写完整的数据");
				return;
			}
			
			wrapper.setSurname(name);
			wrapper.setSex(sex);
			wrapper.setPhone(phone);
			wrapper.setBid(bid);
			wrapper.setSid(sid);
			wrapper.setMid(mid);
			wrapper.setCode(code);
			wrapper.setLat("1");
			wrapper.setLng("2");
			sendData(wrapper,NetworkAction.userF_register);
			break;
		// 显示密码
		case R.id.rst_openpwd:
			pwdTxt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
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
		Object object=parent.getItemAtPosition(position);
		if(object  instanceof Brand)
			bid=((Brand)object).getBid();
		else if(object  instanceof Series)
			sid=((Series)object).getSid();
		else if(object  instanceof Model)
			mid=((Model)object).getMid();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
