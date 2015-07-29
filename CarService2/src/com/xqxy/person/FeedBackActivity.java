package com.xqxy.person;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;

/**
 * 反馈信息页面
 * @author Administrator
 *
 */
public class FeedBackActivity extends BaseActivity {
	private ImageView backImageView;//显示图片的控件
	private TextView titleTextView; //标题栏
	private TextView rightBtnTextView; //标题右边的内容
	private EditText personFeedBack; //反馈信息
	private TextView personCommit; //提交按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_feedback);
		init();
	}

	private void init() {
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		personFeedBack=(EditText) findViewById(R.id.personFeedBack);
		personCommit=(TextView) findViewById(R.id.personCommit);
		personCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String content=personFeedBack.getText().toString();
				RequestWrapper wrapper=new RequestWrapper();
				if(MyApplication.loginStat)
					wrapper.setIdentity(MyApplication.identity);
				wrapper.setContent(content);
				sendData(wrapper, NetworkAction.indexF_suggest);
			}
		});
	}
	
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		 if (requestType==(NetworkAction.indexF_suggest)) {
			 Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
			 finish();
		 }
		
	}
}
