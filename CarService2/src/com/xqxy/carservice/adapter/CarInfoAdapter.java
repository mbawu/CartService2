/**
 * ProjectName:AndroidShopNC2014Moblie
 * PackageName:net.shopnc.android.adapter
 * FileNmae:AddressListViewAdapter.java
 */
package com.xqxy.carservice.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xqxy.carservice.R;
import com.xqxy.model.Brand;
import com.xqxy.model.Journey;
import com.xqxy.model.Model;
import com.xqxy.model.Series;
import com.xqxy.person.NetworkAction;

/**
 * 车型、车系、品牌适配器
 * @author Administrator
 *
 */
public class CarInfoAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Brand> brands;
	private ArrayList<Series> series;
	private ArrayList<Model> model;
	private ArrayList<Journey> lc;
	private NetworkAction type;
	private LayoutInflater inflater;
	private ViewHolder holder;

	/**
	 * 初始化
	 * @param context 上下文
	 * @param type 类型
	 * @param data 填充的列表数据
	 */
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
		else if(type.equals(NetworkAction.indexF_journey))
			lc=(ArrayList<Journey>) data;
		this.inflater = LayoutInflater.from(context);
	}

	/**
	 * 获取总行数
	 */
	@Override
	public int getCount() {
		if(type.equals(NetworkAction.carF_brand))
			return brands.size();
		else if(type.equals(NetworkAction.carF_series))
			return series.size();
		else if(type.equals(NetworkAction.carF_model))
			return model.size();
		else if(type.equals(NetworkAction.indexF_journey))
			return lc.size();
		return 0;
	}





	
	/**
	 * 获取上下文
	 * @return
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * 设置上下文
	 * @param context 上下文
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * 获取列表item
	 */
	@Override
	public Object getItem(int position) {
		if(type.equals(NetworkAction.carF_brand))
			return brands.get(position);
		else if(type.equals(NetworkAction.carF_series))
			return series.get(position);
		else if(type.equals(NetworkAction.carF_model))
			return model.get(position);
		else if(type.equals(NetworkAction.indexF_journey))
			return lc.get(position);
		return "";
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取item视图
	 */
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
		else if(type.equals(NetworkAction.indexF_journey))
		{
			Journey journey=(Journey) lc.get(position);
			holder.name.setText(journey.getValue());
		}
		return convertView;
	}
	
	class ViewHolder
	{
		private TextView name;
	}
}
