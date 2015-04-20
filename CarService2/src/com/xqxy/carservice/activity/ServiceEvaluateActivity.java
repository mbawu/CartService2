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
import com.xqxy.carservice.view.TopTitleView;

public class ServiceEvaluateActivity extends BaseActivity {
	private TopTitleView topTitleView;
	
	private ListView listView;
	private RadioGroup radioGroupType;
	private RadioButton radioBtnAll;
	private RadioButton radioBtnGood;
	private RadioButton radioBtnNormal;
	private RadioButton radioBtnBad;
	private EvaluateAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_evaluate_layout);
		topTitleView = new TopTitleView(this);
		topTitleView.setTitle("服务评价");
		listView = (ListView) findViewById(R.id.listview);
		radioBtnAll = (RadioButton) findViewById(R.id.eva_list_all_radiobtn);
		radioBtnGood = (RadioButton) findViewById(R.id.eva_list_good_radiobtn);
		radioBtnNormal = (RadioButton) findViewById(R.id.eva_list_normal_radiobtn);
		radioBtnBad = (RadioButton) findViewById(R.id.eva_list_bad_radiobtn);
		radioGroupType = (RadioGroup) findViewById(R.id.evaluate_list_type);
		radioGroupType
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.eva_list_all_radiobtn:

							break;
						case R.id.eva_list_good_radiobtn:

							break;
						case R.id.eva_list_normal_radiobtn:

							break;
						case R.id.eva_list_bad_radiobtn:

							break;
						}
					}
				});

		adapter = new EvaluateAdapter(this);
		List<EvaluateDetail> dataList = new ArrayList<EvaluateDetail>();
		for (int i = 0; i < 10; i++) {
			dataList.add(new EvaluateDetail());
		}
		adapter.setDataList(dataList);
		listView.setAdapter(adapter);
	}

	class EvaluateAdapter extends CarBaseAdapter<EvaluateDetail> {

		public EvaluateAdapter(Activity activity) {
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

	class EvaluateDetail {

	}

	class ViewHolder {
		TextView textEvaluateLevel;
		TextView textEvaluateUser;
		TextView textEvaluateContent;
		TextView textEvaluateDate;
	}
}
