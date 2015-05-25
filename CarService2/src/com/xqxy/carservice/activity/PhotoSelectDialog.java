package com.xqxy.carservice.activity;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.cn.hongwei.PhotoActivity;
import com.xqxy.carservice.R;

public class PhotoSelectDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Activity activity;
	public ImageButton btnCamera;
	public Button btnSelect;
	public Button btnCancel;
	private String imgPath;

	public PhotoSelectDialog(Context context, String imgPath) {
		super(context, R.style.MyProgressDialog);
		setContentView(R.layout.photo_select_layout);
		this.activity = (Activity) context;
		this.imgPath = imgPath;
		btnCamera = (ImageButton) findViewById(R.id.btn_person_camera);
		btnSelect = (Button) findViewById(R.id.btn_person_select);
		btnCancel = (Button) findViewById(R.id.btn_person_cancel);
		btnCamera.setOnClickListener(this);
		btnSelect.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		Window win = getWindow();
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		win.setAttributes(lp);
		win.setGravity(Gravity.BOTTOM);

	}

	@Override
	public void onClick(View v) {
		File vFile = new File(imgPath);
		if (v.getId() != R.id.btn_person_cancel && !vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		}
		if (v.getId() == R.id.btn_person_camera) {
			Uri uri = Uri.fromFile(vFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//
			activity.startActivityForResult(intent,
					PhotoActivity.REQUEST_CODE_CAMERA);
		} else if (v.getId() == R.id.btn_person_select) {
			Intent photoIntent = new Intent();
			photoIntent.setType("image/*");
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
				photoIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
			} else {
				photoIntent.setAction(Intent.ACTION_GET_CONTENT);
			}
			activity.startActivityForResult(photoIntent,
					PhotoActivity.REQUEST_CODE_SELECT_PHOTO);

		}
		this.dismiss();
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

}
