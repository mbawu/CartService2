package com.xqxy.carservice.view;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xqxy.carservice.R;

public class TopTitleView {

	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	public TopTitleView(final Activity activity) {
		backImageView = (ImageView) activity.findViewById(R.id.imageTopBack);
		titleTextView = (TextView) activity.findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) activity
				.findViewById(R.id.textTopRightBtn);
		if (backImageView != null) {
			backImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.finish();
				}
			});
		}
	}

	public void setTitle(String title) {
		titleTextView.setText(title);
	}

	public void setRightBtnText(String text,
			View.OnClickListener onClickListener) {
		rightBtnTextView.setText(text);
		if (onClickListener != null) {
			rightBtnTextView.setOnClickListener(onClickListener);
		}
	}
}
