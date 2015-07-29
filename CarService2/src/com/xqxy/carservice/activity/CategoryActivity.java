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
/**
 * 服务类型界面
 * @author Administrator
 *
 */
public class CategoryActivity extends BaseActivity implements
		AdapterView.OnItemClickListener {

	private ListView listView;//服务类型列表
	private ListView listViewProduct;//服务列表
	private TopTitleView topTitleView;//标题

	private List<Category> categorys;//类别数据源
	private CategoryAdapter categoryAdapter;//服务类型适配器
	private CategoryProductAdapter cpAdapter;//服务列表适配器
	private MyApplication app;

	/**
	 * 界面初始化
	 */
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

	/**
	 * 列表点击响应事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		if (parent == listView) {//类别列表
			categoryAdapter.setSelectedIndex(position);
			categoryAdapter.notifyDataSetChanged();
			String cid = categoryAdapter.getDataList().get(position).getCid()
					+ "";
			if (cid != null && !"".equals(cid)) {
				getCategoryProduct(cid);
			}

		} else if (parent == listViewProduct) {//服务列表
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

	/**
	 * 数据解析并显示
	 */
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

	/**
	 * 根据列表获取服务
	 * @param cid 类别ID
	 */
	private void getCategoryProduct(String cid) {
		RequestWrapper request = new RequestWrapper();
		request.setShowDialog(true);
		request.setCid(cid);
		sendDataByGet(request, NetworkAction.indexF_column_product);
	}

	//类别图标数组
	public int iconIds[] = new int[] { R.drawable.category_1,
			R.drawable.category_2, R.drawable.category_3,
			R.drawable.category_4, R.drawable.category_5,
			R.drawable.category_6, R.drawable.category_7, R.drawable.category_8 };

	/**
	 * 类别适配器
	 * @author Administrator
	 *
	 */
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

	/**
	 * 服务适配器
	 * @author Administrator
	 *
	 */
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
