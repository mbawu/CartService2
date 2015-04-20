package com.xqxy.carservice.activity;

import android.app.Activity;
import android.os.Bundle;

import com.xqxy.carservice.R;
import com.xqxy.carservice.view.TopTitleView;
import com.xqxy.carservice.widget.WheelView;
import com.xqxy.carservice.widget.adapters.ArrayWheelAdapter;

public class CategoryActivity extends Activity {
	
	private TopTitleView topTitleView;
	private WheelView mViewProvince;
	protected String[] mProvinceDatas=new String[]{"甘肃1","甘肃2","甘肃3","甘肃4","甘肃5","甘肃6","甘肃7","甘肃8","甘肃9","甘肃10"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_layout);
		topTitleView = new TopTitleView(this);
		topTitleView.setTitle("服务分类");
		
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		
		ArrayWheelAdapter ad =new ArrayWheelAdapter<String>(CategoryActivity.this, mProvinceDatas);
		mViewProvince.setViewAdapter(ad);
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
	
	}
}
