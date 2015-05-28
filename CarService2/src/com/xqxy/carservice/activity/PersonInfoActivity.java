package com.xqxy.carservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.cn.hongwei.CarImageView;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.PhotoActivity;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.cn.hongwei.TopTitleView;
import com.xqxy.carservice.R;
import com.xqxy.model.AutoLogin;
import com.xqxy.person.Cst;
import com.xqxy.person.NetworkAction;
import com.xqxy.person.OtherActivity;

public class PersonInfoActivity extends PhotoActivity implements
		OnClickListener {

	private TopTitleView topTitleView;
	private CarImageView imgHeadPhoto;
	private String imgPath = Cst.UPLOAD_TEMP + "head.jpg";

	private String src;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_info_layout);
		topTitleView = new TopTitleView(this);
		src = getIntent().getStringExtra("src");
		topTitleView = new TopTitleView(this);
		imgHeadPhoto = (CarImageView) findViewById(R.id.img_person_info_head);
		findViewById(R.id.text_person_info_car).setOnClickListener(this);
		findViewById(R.id.layout_person_info_head).setOnClickListener(this);
		findViewById(R.id.btn_person_info_exit).setOnClickListener(this);
		imgHeadPhoto.setRound(true);

		setImgPath(imgPath);
	}

	boolean isInit = false;

	@Override
	protected void onResume() {
		super.onResume();
		if (!isInit) {
			if (src != null) {
				imgHeadPhoto.postDelayed(new Runnable() {

					@Override
					public void run() {
						imgHeadPhoto.loadImage(src);
					}
				}, 200);

			}
			isInit = true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_person_info_head:
			showSelectPhotoDialog();
			break;
		case R.id.text_person_info_car:
			startActivity(new Intent(this, CarListActivity.class));
			break;
		case R.id.btn_person_info_exit:
			MyApplication.loginStat = false;
			MyApplication.identity = "";
			Toast.makeText(PersonInfoActivity.this, "退出登录成功",
					Toast.LENGTH_SHORT).show();
			loginOut();
			finish();
			break;
		}
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.centerF_head) {
			String imgsrc = responseWrapper.getSrc();
			if (src != null && !"".equals(imgsrc)) {
				imgHeadPhoto.loadImage(imgsrc);
				this.src = imgsrc;
				Intent intent = new Intent();
				intent.putExtra("src", imgsrc);
				setResult(PersonCentreActivity.REQUEST_CODE_PHOTO, intent);
			}
		}
	}

	public void uploadImg(String imagePath) {

		try {
			String data = fileToString(imagePath);
			if (data != null) {
				RequestWrapper request = new RequestWrapper();
				request.setIdentity(MyApplication.identity);
				request.setFile(data);
				request.setShowDialog(true);
				sendData(request, NetworkAction.centerF_head);
			} else {
				Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
