package com.xqxy.carservice.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xqxy.carservice.R;
/**
 * 操作确认对话框
 * @author Administrator
 *
 */
public class ConfirmDialog extends Dialog {
	public Button btu_on;
	public Button btu_off;
	public TextView msgTextView;
	public TextView titleTextView;

	/**
	 * 界面初始化
	 * @param context 上下文
	 */
	public ConfirmDialog(Context context) {
		super(context, R.style.confirmDialog);
		this.setContentView(R.layout.confirm_dialog);

		btu_on = (Button) findViewById(R.id.btu_on);
		btu_off = (Button) findViewById(R.id.btu_off);
		msgTextView = (TextView) findViewById(R.id.confirm_dialog_msg);
		titleTextView = (TextView) findViewById(R.id.confirm_dialog_title);
	}

	/**
	 * 设置取消按钮事件
	 * @param resId 显示的资源ID
	 * @param onClickListener 响应监听
	 */
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

	/**
	 * 设置取消按钮事件
	 * @param resId 显示的资源
	 * @param onClickListener 响应监听
	 */
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

	/**
	 * 设置确认按钮事件
	 * @param resId 显示的资源的ID
	 * @param onClickListener 响应监听
	 */
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
	/**
	 * 设置确认按钮事件
	 * @param resId 显示的资源
	 * @param onClickListener 响应监听
	 */
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

	/**
	 * 设置显示的内容
	 * @param msgResId 显示的资源ID
	 */
	public void setMessage(int msgResId) {
		msgTextView.setText(msgResId);
	}

	/**
	 * 设置显示的内容
	 * @param msgResId 显示的资源
	 */
	public void setMessage(String msg) {
		msgTextView.setText(msg);
	}
	
	/**
	 * 设置标题
	 */
	@Override
	public void setTitle(CharSequence title) {
		titleTextView.setText(title);
	}

	public interface OnClickListener {
		void onClick(Dialog dialog, View view);
	}
}
