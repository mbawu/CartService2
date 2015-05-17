package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.view.TopTitleView;
import com.xqxy.model.Category;
import com.xqxy.model.CategoryProduct;

public class CategoryActivity extends BaseActivity implements
		AdapterView.OnItemClickListener {

	private ListView listView;
	private ListView listViewProduct;
	private TopTitleView topTitleView;

	private List<Category> categorys;
	private ArrayAdapter<CategoryProduct> cpAdapter;
	private MyApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_layout);
		app = (MyApplication) getApplication();
		topTitleView = new TopTitleView(this);
		topTitleView.setTitle("服务分类");
		listView = (ListView) findViewById(R.id.listview);
		listViewProduct = (ListView) findViewById(R.id.listview_product);

		cpAdapter = new ArrayAdapter<CategoryProduct>(this,
				R.layout.category_produc_item);
		listViewProduct.setAdapter(cpAdapter);

		listView.setOnItemClickListener(this);
		listViewProduct.setOnItemClickListener(this);

		RequestWrapper request = new RequestWrapper();
		request.setShowDialog(true);
		sendData(request, NetworkAction.indexF_column);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		if (parent == listView) {
			Map<String, Object> map = (HashMap<String, Object>) parent
					.getItemAtPosition(position);

			listView.requestFocusFromTouch();

			listView.setSelection(position);
			String cid = map.get("cid").toString();
			if (cid != null && !"".equals(cid)) {
				getCategoryProduct(cid);
			}
		} else if (parent == listViewProduct) {
			CategoryProduct cp = cpAdapter.getItem(position);

			Intent intent = new Intent();
			intent.putExtra("pid", cp.getPid());
			intent.putExtra("flag", cp.getFlag());
			if ("2".equals(cp.getFlag())) {// 其他类，直接打开
				intent.setClass(this, ServiceDetailActivity.class);
				startActivity(intent);
			} else {
				if (app.getCar() == null) {
					intent.setClass(this, CarActivity.class);
					startActivity(intent);
				} else {
					intent.setClass(this, ServiceDetailActivity.class);
					startActivity(intent);
				}
			}
		}

	}

	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		if (requestType == NetworkAction.indexF_column) {
			categorys = responseWrapper.getColumn();
			if (categorys != null && categorys.size() > 0) {
				List<Map<String, Object>> dataMaps = new ArrayList<Map<String, Object>>();
				for (int i = 0, j = 0; i < categorys.size(); i++, j++) {
					Map map = new HashMap<String, Object>();
					map.put("cid", categorys.get(i).getCid());
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

				getCategoryProduct(dataMaps.get(0).get("cid").toString());

				listView.postDelayed(new Runnable() {

					@Override
					public void run() {
						listView.requestFocusFromTouch();

						listView.setSelection(0);
					}
				}, 500);

			}
		} else if (requestType == NetworkAction.indexF_column_product) {
			List<CategoryProduct> cps = responseWrapper.getColumn_product();
			cpAdapter.clear();
			cpAdapter.addAll(cps);

		}
	}

	private void getCategoryProduct(String cid) {
		RequestWrapper request = new RequestWrapper();
		request.setShowDialog(true);
		request.setCid(cid);
		sendDataByGet(request, NetworkAction.indexF_column_product);
	}

	public int iconIds[] = new int[] { R.drawable.category_1,
			R.drawable.category_2, R.drawable.category_3,
			R.drawable.category_4, R.drawable.category_5,
			R.drawable.category_6, R.drawable.category_7, R.drawable.category_8 };

}
