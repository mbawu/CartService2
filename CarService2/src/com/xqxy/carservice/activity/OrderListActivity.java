package com.xqxy.carservice.activity;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;

public class OrderListActivity extends BaseActivity{
	
	private ListView listView;
	private RadioGroup radioGroupType;
	private RadioButton radioBtnAll;
	private RadioButton radioBtnUnderway;
	private RadioButton radioBtnCancel;
	private OrderListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list_layout);
		listView = (ListView) findViewById(R.id.listview);
		radioBtnAll = (RadioButton) findViewById(R.id.order_list_all_radiobtn);
		radioBtnUnderway = (RadioButton) findViewById(R.id.order_list_underway_radiobtn);
		radioBtnCancel = (RadioButton) findViewById(R.id.order_list_cancel_radiobtn);
		radioGroupType = (RadioGroup) findViewById(R.id.order_list_type);
		radioGroupType
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.order_list_all_radiobtn:

							break;
						case R.id.order_list_underway_radiobtn:

							break;
						case R.id.order_list_cancel_radiobtn:

							break;
						}
					}
				});

		adapter = new OrderListAdapter(this);
		List<OrderDetail> dataList = new ArrayList<OrderDetail>();
		for (int i = 0; i < 10; i++) {
			dataList.add(new OrderDetail());
		}
		adapter.setDataList(dataList);
		listView.setAdapter(adapter);
	}

	class OrderListAdapter extends CarBaseAdapter<OrderDetail> {

		public OrderListAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.evaluate_item, null);
				viewHolder = new ViewHolder();
				viewHolder.textEvaluateLevel = (TextView) convertView
						.findViewById(R.id.text_evaluate_level);
				viewHolder.textEvaluateUser = (TextView) convertView
						.findViewById(R.id.text_evaluate_user);
				viewHolder.textEvaluateContent = (TextView) convertView
						.findViewById(R.id.text_evaluate_content);
				viewHolder.textEvaluateDate = (TextView) convertView
						.findViewById(R.id.text_evaluate_date);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}
	}

	class OrderDetail {

	}

	class ViewHolder {
		TextView textEvaluateLevel;
		TextView textEvaluateUser;
		TextView textEvaluateContent;
		TextView textEvaluateDate;
	}
}
