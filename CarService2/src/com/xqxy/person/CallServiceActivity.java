package com.xqxy.person;

import java.util.Calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.carservice.R;

public class CallServiceActivity extends BaseActivity {

	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	private Calendar calendar;
	private DatePicker datePicker;
	private TimePicker timePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_service);
		init();
	}

	private void init() {
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		datePicker = (DatePicker) findViewById(R.id.call_picker_date);
		timePicker = (TimePicker) findViewById(R.id.call_picker_time);
		timePicker.setIs24HourView(true);
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {

						if (isDateBefore(timePicker, view)) {
							view.init(calendar.get(Calendar.YEAR),
									calendar.get(Calendar.MONTH),
									calendar.get(Calendar.DAY_OF_MONTH), this);
							if ((calendar.get(Calendar.MINUTE) + 40) > 60) {
								timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY) + 1);
								timePicker.setCurrentMinute((calendar.get(Calendar.MINUTE) + 40) - 60);
							} else {
								timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
								timePicker.setCurrentMinute((calendar.get(Calendar.MINUTE) + 40));
							}
						}
					}

				});
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (minute + 40 > 60) {
			timePicker.setCurrentHour(hour + 1);
			timePicker.setCurrentMinute(minute + 40 - 60);
		} else {
			timePicker.setCurrentHour(hour);
			timePicker.setCurrentMinute(minute);
		}

		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				if (isDateBefore(view, datePicker)) {
					// view.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
					// view.setCurrentMinute(calendar.get(Calendar.MINUTE));
					if ((calendar.get(Calendar.MINUTE) + 40) > 60) {
						view.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY) + 1);
						view.setCurrentMinute((calendar.get(Calendar.MINUTE) + 40) - 60);
					} else {
						view.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
						view.setCurrentMinute((calendar.get(Calendar.MINUTE) + 40));
					}
					// Log.i("test",
					// "minute-->"+(calendar.get(Calendar.MINUTE)+40));
					// Log.i("test",
					// "minute-->"+calendar.get(Calendar.MINUTE)+40);
				}

			}
		});
	}

	private boolean isDateBefore(TimePicker tempView, DatePicker date) {
		Calendar mCalendar = Calendar.getInstance();
		Calendar tempCalendar = Calendar.getInstance();

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if (minute + 40 > 60) {
			mCalendar.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH), hour + 1,
					(minute + 40 - 60));
		} else
			mCalendar.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH), hour, minute, 0);

		tempCalendar.set(date.getYear(), date.getMonth(), date.getDayOfMonth(),
				tempView.getCurrentHour(), tempView.getCurrentMinute(), 0);
		if (tempCalendar.before(mCalendar))
			return true;
		else
			return false;
	}

	public void btnOnClick(View v) {
		finish();
	}

}
