package com.xqxy.carservice.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.cn.hongwei.TopTitleView;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Category;
import com.xqxy.model.CategoryProduct;
import com.xqxy.person.NetworkAction;

public class CategoryActivity extends BaseActivity implements
		AdapterView.OnItemClickListener {

	private ListView listView;
	private ListView listViewProduct;
	private TopTitleView topTitleView;

	private List<Category> categorys;
	private CategoryAdapter categoryAdapter;
	private CategoryProductAdapter cpAdapter;
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
		categoryAdapter = new CategoryAdapter(this);
		cpAdapter = new CategoryProductAdapter(this);
		listViewProduct.setAdapter(cpAdapter);
		listView.setTag(0);
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
			categoryAdapter.setSelectedIndex(position);
			categoryAdapter.notifyDataSetChanged();
			String cid = categoryAdapter.getDataList().get(position).getCid()
					+ "";
			if (cid != null && !"".equals(cid)) {
				getCategoryProduct(cid);
			}

		} else if (parent == listViewProduct) {
			CategoryProduct cp = cpAdapter.getItem(position);
			cpAdapter.setSelectedIndex(position);
			cpAdapter.notifyDataSetChanged();
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
				categoryAdapter.setDataList(categorys);
				listView.setAdapter(categoryAdapter);
				getCategoryProduct(categorys.get(0).getCid() + "");
			}
		} else if (requestType == NetworkAction.indexF_column_product) {
			List<CategoryProduct> cps = responseWrapper.getColumn_product();
			if (cpAdapter.getDataList() != null) {
				cpAdapter.getDataList().clear();
			}
			cpAdapter.setSelectedIndex(0);
			cpAdapter.addDataList(cps);
			cpAdapter.notifyDataSetChanged();

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

	class CategoryAdapter extends CarBaseAdapter<Category> {

		private int selectedIndex = 0;

		public CategoryAdapter(Activity activity) {
			super(activity);
		}

		public int getSelectedIndex() {
			return selectedIndex;
		}

		public void setSelectedIndex(int selectedIndex) {
			this.selectedIndex = selectedIndex;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.category_item, null);
				viewHolder = new ViewHolder();
				viewHolder.nameText = (TextView) convertView
						.findViewById(R.id.text_category_item_title);
				viewHolder.imgView = (ImageView) convertView
						.findViewById(R.id.img_category_item_icon);

				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (selectedIndex == position) {
				convertView
						.setBackgroundResource(R.drawable.category_item_border_selected);
				viewHolder.nameText.setTextColor(getResources().getColor(
						R.color.wihte));
			} else {
				convertView
						.setBackgroundResource(R.drawable.category_item_normal);
				viewHolder.nameText.setTextColor(getResources().getColor(
						R.color.blank));
			}
			Category c = getDataList().get(position);
			viewHolder.nameText.setText(c.getName());
			viewHolder.imgView.setImageResource(iconIds[position
					% iconIds.length]);
			return convertView;

		}
	}

	class CategoryProductAdapter extends CarBaseAdapter<CategoryProduct> {

		private int selectedIndex = 0;

		public CategoryProductAdapter(Activity activity) {
			super(activity);
		}

		public int getSelectedIndex() {
			return selectedIndex;
		}

		public void setSelectedIndex(int selectedIndex) {
			this.selectedIndex = selectedIndex;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.category_produc_item,
						null);

			}
			textView = (TextView) convertView;
			if (selectedIndex == position) {
				textView.setBackgroundResource(R.drawable.category_item_border_selected);
				textView.setTextColor(getResources().getColor(R.color.wihte));
			} else {
				textView.setBackgroundColor(getResources().getColor(
						R.color.wihte));
				textView.setTextColor(getResources().getColor(R.color.blank));
			}
			CategoryProduct cp = getDataList().get(position);
			textView.setText(cp.getName());

			return convertView;

		}
	}

	class ViewHolder {
		ImageView imgView;
		TextView nameText;
	}
}
