package com.xqxy.carservice.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.CarImageView;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.cn.hongwei.TopTitleView;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Appraise;
import com.xqxy.person.NetworkAction;

/**
 * 服务评价列表界面
 * @author Administrator
 *
 */
public class ServiceEvaluateActivity extends BaseActivity {
	private TopTitleView topTitleView;

	private ListView listView;
	private RadioGroup radioGroupType;
	private EvaluateAdapter adapter;
	private String pid;
	private String flag = "0";

	/**
	 * 界面初始化
	 */
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

	/**
	 * 向服务端发送获取评价列表的请求
	 */
	private void getEvaDate() {
		RequestWrapper request = new RequestWrapper();
		request.setShowDialog(true);
		request.setFlag(flag);
		request.setPid(pid);
		 request.setLimit("10000");
		sendDataByGet(request, NetworkAction.indexF_appraise);
	}

	/**
	 * 解析服务端返回的评价数据
	 */
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

	/**
	 * 评价列表适配器
	 * @author Administrator
	 *
	 */
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
				viewHolder.textEvaluateAnswer = (TextView) convertView
						.findViewById(R.id.text_evaluate_answer);
				viewHolder.layoutEaluateAnswer = (LinearLayout) convertView
						.findViewById(R.id.layout_evaluate_answer);
				viewHolder.imgLinearLayout = (LinearLayout) convertView
						.findViewById(R.id.goods_evaluate_image_linearlayout);
				viewHolder.hScrollView = (HorizontalScrollView) convertView
						.findViewById(R.id.goods_evaluate_image_scrollview);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.hScrollView.scrollTo(0, 0);
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

			if (eva.getReply() != null && !"".equals(eva.getReply())) {
				viewHolder.textEvaluateAnswer.setText(eva.getReply());
				viewHolder.layoutEaluateAnswer.setVisibility(View.VISIBLE);
			} else {
				viewHolder.layoutEaluateAnswer.setVisibility(View.GONE);
			}
			viewHolder.imgLinearLayout.removeAllViews();
			setEvaluateImage(viewHolder.imgLinearLayout, eva.getPic());
			return convertView;
		}
	}

	/**
	 * 动态组装评价的图片
	 * @param imgLinearLayout 布局容器
	 * @param imgUrls 图片数组
	 */
	private void setEvaluateImage(LinearLayout imgLinearLayout,
			final ArrayList<String> imgUrls) {
		// imgUrl = itemUrls;
		if (imgUrls != null && imgUrls.size() > 0) {
			CarImageView imgView;
			for (int i = 0; i < imgUrls.size(); i++) {
				final int index = i;
				imgView = (CarImageView) getLayoutInflater().inflate(
						R.layout.goods_evaluate_img, imgLinearLayout, false);
				imgLinearLayout.addView(imgView);
				imgView.loadImage(imgUrls.get(i));
				imgView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								ServiceEvaluateActivity.this,
								ImageDisplayActivity.class);
						intent.putStringArrayListExtra("imgUrls", imgUrls);
						intent.putExtra("imgIndex", index);
						ServiceEvaluateActivity.this.startActivity(intent);
					}
				});
			}
		}

	}

	class ViewHolder {
		TextView textEvaluateLevel;
		TextView textEvaluateUser;
		TextView textEvaluateContent;
		TextView textEvaluateDate;
		TextView textEvaluateTime;
		TextView textEvaluateAnswer;
		LinearLayout layoutEaluateAnswer;
		LinearLayout imgLinearLayout;
		HorizontalScrollView hScrollView;
	}
}
