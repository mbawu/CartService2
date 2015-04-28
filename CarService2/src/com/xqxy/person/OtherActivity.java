package com.xqxy.person;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.carservice.R;

public class OtherActivity extends BaseActivity implements OnClickListener {

	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	
	private TextView personClear;
	private TextView personContranct;
	private TextView personAbout;
	private TextView personFeedBack;
	private TextView personComment;
	private TextView personLoginOut;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_other);
		init();
	}
	
	 private void init() {
		// TODO Auto-generated method stub
			backImageView = (ImageView) findViewById(R.id.imageTopBack);
			titleTextView = (TextView) findViewById(R.id.textTopTitle);
			rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
			
			personClear = (TextView) findViewById(R.id.personClear);
			personContranct = (TextView) findViewById(R.id.personContranct);
			personAbout = (TextView) findViewById(R.id.personAbout);
			personFeedBack = (TextView) findViewById(R.id.personFeedBack);
			personComment = (TextView) findViewById(R.id.personComment);
			personLoginOut = (TextView) findViewById(R.id.personLoginOut);
			
			
			backImageView.setOnClickListener(this);
			personClear.setOnClickListener(this);
			personContranct.setOnClickListener(this);
			personAbout.setOnClickListener(this);
			personFeedBack.setOnClickListener(this);
			personComment.setOnClickListener(this);
			personLoginOut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageTopBack:
			finish();
			break;
		case R.id.personClear:
			Thread thread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					handler.sendMessageDelayed(new Message(), 2000);
				}
			});
			thread.start();
			break;
		case R.id.personContranct:
			finish();
			break;
		case R.id.personAbout:
			finish();
			break;
		case R.id.personFeedBack:
			Intent intent=new Intent();
			intent.setClass(this, FeedBackActivity.class);
			startActivity(intent);
			break;
		case R.id.personComment:

			break;
		case R.id.personLoginOut:
			MyApplication.loginStat=false;
			MyApplication.identity="";
			Toast.makeText(OtherActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
			break;
		}
		
	}

	Handler handler=new Handler()
	{
		public void handleMessage(Message msg) {
			Toast.makeText(OtherActivity.this, "缓存清除完毕", Toast.LENGTH_SHORT).show();
		};
	};
}
