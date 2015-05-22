package com.xqxy.carservice.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CarBaseAdapter<T> extends BaseAdapter {

	protected Activity activity;

	protected LayoutInflater inflater;

	private List<T> dataList;

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

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public void addDataList(List<T> dataList) {
		if (this.dataList == null) {
			this.dataList = new ArrayList<T>();
		}
		this.dataList.addAll(dataList);
	}

}
