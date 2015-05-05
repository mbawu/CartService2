package com.xqxy.person;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;

public class FeedBackActivity extends BaseActivity {
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	private EditText personFeedBack;
	private TextView personCommit;
	
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
		 }
		
	}
}
