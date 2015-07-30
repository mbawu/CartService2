package com.xqxy.carservice.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * 列表基础适配器
 * @author Administrator
 *
 * @param <T>
 */
public class CarBaseAdapter<T> extends BaseAdapter {

	protected Activity activity;

	protected LayoutInflater inflater;

	private List<T> dataList;

	/**
	 * 构造方法
	 * @param activity
	 */
	public CarBaseAdapter(Activity activity) {
		this.activity = activity;
		this.inflater = activity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return dataList == null ? 0 : dataList.size();
	}

	@Override
	public T getItem(int position) {

		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
	
	/**
	 * 获取数据列表
	 * @return 数据集合
	 */
	public List<T> getDataList() {
		return dataList;
	}

	/**
	 * 设置数据列表
	 * @param dataList 数据集合
	 */
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	/**
	 * 向列表增加数据
	 * @param dataList 数据集合
	 */
	public void addDataList(List<T> dataList) {
		if (this.dataList == null) {
			this.dataList = new ArrayList<T>();
		}
		this.dataList.addAll(dataList);
	}

}
