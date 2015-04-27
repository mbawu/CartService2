package com.xqxy.carservice.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.xqxy.carservice.R;

public class PhotoSelectDialog extends Dialog implements
		android.view.View.OnClickListener {

	public Button btnCamera;
	public Button btnSelect;
	public Button btnCancel;

	public PhotoSelectDialog(Context context) {
		super(context, R.style.MyProgressDialog);
		setContentView(R.layout.photo_select_layout);
		btnCamera = (Button) findViewById(R.id.btn_person_camera);
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
		if (v.getId() == R.id.btn_person_camera) {

		} else if (v.getId() == R.id.btn_person_select) {

		} else if (v.getId() == R.id.btn_person_cancel) {
			this.dismiss();
		}

	}

}
