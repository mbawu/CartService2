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

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CarInfoAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Object> data;
	private NetworkAction type;
	private LayoutInflater inflater;
	private ViewHolder holder;

	public CarInfoAdapter(Context context,NetworkAction type,ArrayList<Object> data) {
		this.context = context;
		this.type=type;
		this.data=data;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}



	public ArrayList<Object> getArrayList() {
		return data;
	}

	public void setArrayList(ArrayList<Object> arrayList) {
		this.data = arrayList;
	}

	

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listivew_voucher_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(holder);
		}
		 else {
			 holder = (ViewHolder) convertView.getTag();
			}
		
		if(type.equals(NetworkAction.brand))
		{
			Brand brand=(Brand) data.get(position);
			holder.name.setText(brand.getName());
		}
	
		return convertView;
	}
	
	class ViewHolder
	{
		private TextView name;
	}
}
