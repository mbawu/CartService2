/**
 * ProjectName:AndroidShopNC2014Moblie
 * PackageName:net.shopnc.android.adapter
 * FileNmae:AddressListViewAdapter.java
 */
package com.xqxy.carservice.adapter;

import java.util.ArrayList;



import com.xqxy.baseclass.NetworkAction;
import com.xqxy.carservice.R;
import com.xqxy.model.Brand;
import com.xqxy.model.Model;
import com.xqxy.model.Series;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CarInfoAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Brand> brands;
	private ArrayList<Series> series;
	private ArrayList<Model> model;
	private NetworkAction type;
	private LayoutInflater inflater;
	private ViewHolder holder;

	@SuppressWarnings("unchecked")
	public <T> CarInfoAdapter(Context context,NetworkAction type,ArrayList<T> data) {
		this.context = context;
		this.type=type;
		if(type.equals(NetworkAction.carF_brand))
			brands=(ArrayList<Brand>) data;
		else if(type.equals(NetworkAction.carF_series))
			series=(ArrayList<Series>) data;
		else if(type.equals(NetworkAction.carF_model))
			model=(ArrayList<Model>) data;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(type.equals(NetworkAction.carF_brand))
			return brands.size();
		else if(type.equals(NetworkAction.carF_series))
			return series.size();
		else if(type.equals(NetworkAction.carF_model))
			return model.size();
		return 0;
	}





	

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public Object getItem(int position) {
		if(type.equals(NetworkAction.carF_brand))
			return brands.get(position);
		else if(type.equals(NetworkAction.carF_series))
			return series.get(position);
		else if(type.equals(NetworkAction.carF_model))
			return model.get(position);
		return "";
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.car_info_adapter, null);
			holder.name = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(holder);
		}
		 else {
			 holder = (ViewHolder) convertView.getTag();
			}
		
		if(type.equals(NetworkAction.carF_brand))
		{
			Brand brand=(Brand) brands.get(position);
			holder.name.setText(brand.getName());
		}
		else if(type.equals(NetworkAction.carF_series))
		{
			Series brand=(Series) series.get(position);
			holder.name.setText(brand.getSname());
		}
		else if(type.equals(NetworkAction.carF_model))
		{
			Model brand=(Model) model.get(position);
			holder.name.setText(brand.getMname());
		}
	
		return convertView;
	}
	
	class ViewHolder
	{
		private TextView name;
	}
}
