package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.view.TopTitleView;
import com.xqxy.carservice.widget.WheelView;
import com.xqxy.carservice.widget.adapters.ArrayWheelAdapter;
import com.xqxy.model.Category;

public class CategoryActivity extends BaseActivity {

	private ListView listView;
	private TopTitleView topTitleView;
	private WheelView mViewProvince;
	protected String[] mProvinceDatas = new String[] { "甘肃1", "甘肃2", "甘肃3",
			"甘肃4", "甘肃5", "甘肃6", "甘肃7", "甘肃8", "甘肃9", "甘肃10" };

	private List<Category> categorys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_layout);
		topTitleView = new TopTitleView(this);
		topTitleView.setTitle("服务分类");
		listView = (ListView) findViewById(R.id.listview);
		mViewProvince = (WheelView) findViewById(R.id.id_province);

		ArrayWheelAdapter ad = new ArrayWheelAdapter<String>(
				CategoryActivity.this, mProvinceDatas);
		mViewProvince.setViewAdapter(ad);
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);

		sendData(new RequestWrapper(), NetworkAction.indexF_column);
	}

	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		if (requestType == NetworkAction.indexF_column) {
			categorys = responseWrapper.getColumn();
			if (categorys != null && categorys.size() > 0) {
				List<Map<String, Object>> dataMaps = new ArrayList<Map<String, Object>>();
				for (int i = 1, j = 0; i < categorys.size(); i++, j++) {
					Map map = new HashMap<String, Object>();
					map.put("img", iconIds[j]);
					map.put("text", categorys.get(i).getName());
					dataMaps.add(map);
					if (j == iconIds.length - 1) {
						j = -1;
					}
				}

				SimpleAdapter ad1 = new SimpleAdapter(this, dataMaps,
						R.layout.category_item, new String[] { "img", "text" },
						new int[] { R.id.img_category_item_icon,
								R.id.text_category_item_title });
				listView.setAdapter(ad1);
			}
		}
	}

	public int iconIds[] = new int[] { R.drawable.category_1,
			R.drawable.category_2, R.drawable.category_3,
			R.drawable.category_4, R.drawable.category_5,
			R.drawable.category_6, R.drawable.category_7, R.drawable.category_8 };
}
