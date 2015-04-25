package com.xqxy.carservice.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.carservice.R;
import com.xqxy.carservice.view.PhotoSelectDialog;
import com.xqxy.carservice.view.TopTitleView;

public class PersonInfoActivity extends BaseActivity implements OnClickListener {
	Dialog dialog;
	private TopTitleView topTitleView;
	private ImageView imgHeadPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_info_layout);
		topTitleView = new TopTitleView(this);
		imgHeadPhoto = (ImageView) findViewById(R.id.img_person_info_head);
		findViewById(R.id.text_person_info_car).setOnClickListener(this);
		findViewById(R.id.layout_person_info_head).setOnClickListener(this);
		findViewById(R.id.btn_person_info_exit).setOnClickListener(this);
		
		dialog = new PhotoSelectDialog(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_person_info_head:
			showSelectPhotoDialog();
			break;
		case R.id.text_person_info_car:
			break;
		case R.id.btn_person_info_exit:
			break;
		}
	}

	public void showSelectPhotoDialog() {

		dialog.show();
	}
}
