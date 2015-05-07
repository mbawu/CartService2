package com.xqxy.carservice.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.carservice.view.TopTitleView;
import com.xqxy.model.Appraise;

public class ServiceEvaluateActivity extends BaseActivity {
	private TopTitleView topTitleView;

	private ListView listView;
	private RadioGroup radioGroupType;
	private EvaluateAdapter adapter;
	private String pid;
	private String flag = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_evaluate_layout);
		pid = getIntent().getStringExtra("pid");
		topTitleView = new TopTitleView(this);
		topTitleView.setTitle("服务评价");
		listView = (ListView) findViewById(R.id.listview);

		radioGroupType = (RadioGroup) findViewById(R.id.evaluate_list_type);
		radioGroupType
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.eva_list_all_radiobtn:
							flag = "0";
							break;
						case R.id.eva_list_good_radiobtn:
							flag = "1";
							break;
						case R.id.eva_list_normal_radiobtn:

							break;
						case R.id.eva_list_bad_radiobtn:
							flag = "2";
							break;
						}
						getEvaDate();
					}
				});

		adapter = new EvaluateAdapter(this);
		listView.setAdapter(adapter);
		getEvaDate();
	}

	private void getEvaDate() {
		RequestWrapper request = new RequestWrapper();
		request.setShowDialog(true);
		request.setFlag(flag);
		request.setPid(pid);
		//request.setLimit("10000");
		sendDataByGet(request, NetworkAction.indexF_appraise);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.indexF_appraise) {
			List<Appraise> appraiseList = responseWrapper.getAppraise();
			adapter.setDataList(appraiseList);
			adapter.notifyDataSetChanged();
		}

	}

	class EvaluateAdapter extends CarBaseAdapter<Appraise> {

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
				viewHolder.textEvaluateTime = (TextView) convertView
						.findViewById(R.id.text_evaluate_time);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Appraise eva = getDataList().get(position);
			if ("1".equals(eva.getFlag())) {
				viewHolder.textEvaluateLevel
						.setText(getString(R.string.evaluate_level_good));
			} else {
				viewHolder.textEvaluateLevel
						.setText(getString(R.string.evaluate_level_bad));
			}

			if (eva.getPhone() != null) {
				if (eva.getPhone().length() == 11) {
					viewHolder.textEvaluateUser.setText(eva.getPhone()
							.substring(0, 3)
							+ "****"
							+ eva.getPhone().substring(7));
				} else {
					viewHolder.textEvaluateUser.setText(eva.getPhone());
				}

			}

			if (eva.getContent() != null) {
				viewHolder.textEvaluateContent.setText(eva.getContent());
			}
			if (eva.getCreate_time() != null) {
				viewHolder.textEvaluateDate.setText(eva.getCreate_time()
						.substring(0, 10));
				viewHolder.textEvaluateTime.setText(eva.getCreate_time()
						.substring(10));
			}
			return convertView;
		}
	}
	

	class ViewHolder {
		TextView textEvaluateLevel;
		TextView textEvaluateUser;
		TextView textEvaluateContent;
		TextView textEvaluateDate;
		TextView textEvaluateTime;
	}
}
