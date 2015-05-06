package com.xqxy.carservice.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xqxy.carservice.R;

public class ConfirmDialog extends Dialog {
	public Button btu_on;
	public Button btu_off;
	public TextView msgTextView;
	public TextView titleTextView;

	public ConfirmDialog(Context context) {
		super(context, R.style.confirmDialog);
		this.setContentView(R.layout.confirm_dialog);

		btu_on = (Button) findViewById(R.id.btu_on);
		btu_off = (Button) findViewById(R.id.btu_off);
		msgTextView = (TextView) findViewById(R.id.confirm_dialog_msg);
		titleTextView = (TextView) findViewById(R.id.confirm_dialog_title);
	}

	public void setNegativeButton(int resId,
			final OnClickListener onClickListener) {
		btu_off.setText(resId);
		btu_off.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickListener.onClick(ConfirmDialog.this, btu_off);
				dismiss();
			}
		});
	}

	public void setNegativeButton(String res,
			final OnClickListener onClickListener) {
		btu_off.setText(res);
		btu_off.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickListener.onClick(ConfirmDialog.this, btu_off);
				dismiss();
			}
		});
	}

	public void setPositiveButton(int resId,
			final OnClickListener onClickListener) {
		btu_on.setText(resId);
		btu_on.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickListener.onClick(ConfirmDialog.this, btu_on);
				dismiss();
			}
		});
	}

	public void setPositiveButton(String res,
			final OnClickListener onClickListener) {
		btu_on.setText(res);
		btu_on.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickListener.onClick(ConfirmDialog.this, btu_on);
				dismiss();
			}
		});
	}

	public void setMessage(int msgResId) {
		msgTextView.setText(msgResId);
	}

	public void setMessage(String msg) {
		msgTextView.setText(msg);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		titleTextView.setText(title);
	}

	public interface OnClickListener {
		void onClick(Dialog dialog, View view);
	}
}
