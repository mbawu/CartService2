package com.xqxy.carservice.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.xqxy.baseclass.PhotoActivity;
import com.xqxy.baseclass.UploadUtil;
import com.xqxy.carservice.R;
import com.xqxy.carservice.view.PhotoSelectDialog;
import com.xqxy.carservice.view.TopTitleView;

public class PersonInfoActivity extends PhotoActivity implements
		OnClickListener {
	Dialog dialog;
	private UploadUtil uploadUtil;
	private TopTitleView topTitleView;
	private ImageView imgHeadPhoto;
	private String imgPath = Environment.getExternalStorageDirectory()
			.getPath() + "/CarTemp/head.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_info_layout);
		topTitleView = new TopTitleView(this);
		imgHeadPhoto = (ImageView) findViewById(R.id.img_person_info_head);
		findViewById(R.id.text_person_info_car).setOnClickListener(this);
		findViewById(R.id.layout_person_info_head).setOnClickListener(this);
		findViewById(R.id.btn_person_info_exit).setOnClickListener(this);
		dialog = new PhotoSelectDialog(this, imgPath);
		setImgPath(imgPath);
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
			break;
		}
	}

	public void showSelectPhotoDialog() {

		dialog.show();
	}

	public void uploadImg(String imagePath) {
		Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
		if (uploadUtil == null) {
			uploadUtil = new UploadUtil();
			uploadUtil.setOnUploadProcessListener(onUploadProcessListener);
		}
		String url = "http://car.xinlingmingdeng.com/api.php/center/head";
		uploadUtil.uploadFile(imagePath, url);
	}

	UploadUtil.OnUploadProcessListener onUploadProcessListener = new UploadUtil.OnUploadProcessListener() {

		@Override
		public void onUploadProcess(int uploadSize) {
			Log.i("UploadUtil", "process---" + uploadSize);

		}

		@Override
		public void onUploadDone(int responseCode, final String message) {

			Log.i("UploadUtil", "responseCode---" + responseCode + "-----msg--"
					+ message);
			if (responseCode == UploadUtil.UPLOAD_SUCCESS_CODE) {
				// userEdit.head_photo = message;

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(PersonInfoActivity.this, message,
								Toast.LENGTH_SHORT).show();
						Bitmap bm = BitmapFactory.decodeFile(imgPath);
						// headImageView.setImageBitmap(bm);

					}
				});

			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// showTipMessage(message);
					}
				});
			}
			Log.i("UploadUtil", "opus---" + message);
		}

		@Override
		public void initUpload(int fileSize) {
			Log.i("UploadUtil", "fileSize---" + fileSize);
		}
	};
}
