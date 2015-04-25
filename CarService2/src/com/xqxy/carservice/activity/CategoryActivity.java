package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xqxy.carservice.R;
import com.xqxy.carservice.view.TopTitleView;
import com.xqxy.carservice.widget.WheelView;
import com.xqxy.carservice.widget.adapters.ArrayWheelAdapter;

public class CategoryActivity extends Activity {

	private ListView listView;
	private TopTitleView topTitleView;
	private WheelView mViewProvince;
	protected String[] mProvinceDatas = new String[] { "甘肃1", "甘肃2", "甘肃3",
			"甘肃4", "甘肃5", "甘肃6", "甘肃7", "甘肃8", "甘肃9", "甘肃10" };

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

		for (int i = 0; i < iconIds.length; i++) {
			Map map = new HashMap<String, Object>();
			map.put("img", iconIds[i]);
			map.put("text", "text" + i);
			maps.add(map);
		}

		SimpleAdapter ad1 = new SimpleAdapter(this, maps,
				R.layout.category_item, new String[] { "img", "text" },
				new int[] { R.id.img_category_item_icon,
						R.id.text_category_item_title });
		listView.setAdapter(ad1);
	}

	public List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();

	public int iconIds[] = new int[] { R.drawable.address_car_num,
			R.drawable.address_name };
}
