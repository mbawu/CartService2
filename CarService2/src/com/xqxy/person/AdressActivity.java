package com.xqxy.person;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Address;


public class AdressActivity extends BaseActivity {

	private ListView listView;
	private AddressAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_address);
		init();
	}
	private void init() {
		listView=(ListView) findViewById(R.id.listview);
		adapter=new AddressAdapter(this);
		listView.setAdapter(adapter);
		RequestWrapper wrapper=new RequestWrapper();
		wrapper.setIdentity(MyApplication.identity);
		sendData(wrapper, NetworkAction.carF_brand);
	}
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if(requestType.equals(NetworkAction.centerF_user_address))
		{
			adapter.setDataList(responseWrapper.getAddress());
			adapter.notifyDataSetChanged();
		}
	}
	public void btnOnClick(View view) {
		finish();
	}
	
	
	class AddressAdapter extends CarBaseAdapter<Address> {

		public AddressAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.person_address_item, null);
				viewHolder = new ViewHolder();
				viewHolder.nameTxt = (TextView) convertView
						.findViewById(R.id.address_name);
				viewHolder.phoneTxt = (TextView) convertView
						.findViewById(R.id.address_phone);
				viewHolder.carNumTxt = (TextView) convertView
						.findViewById(R.id.address_car_num);
				viewHolder.locationTxt = (TextView) convertView
						.findViewById(R.id.address_location);
				viewHolder.locationDelTxt = (TextView) convertView
						.findViewById(R.id.address_location_detail);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Address address=getItem(position);
			viewHolder.nameTxt.setText(address.getName());
			viewHolder.phoneTxt.setText(address.getPhone());
			viewHolder.carNumTxt.setText(address.getCar_num());
			viewHolder.locationTxt.setText(address.getAddress());
			viewHolder.locationDelTxt.setText(address.getDetailed());
			return convertView;
		}
	}
	

	class ViewHolder {
		TextView nameTxt;
		TextView phoneTxt;
		TextView carNumTxt;
		TextView locationTxt;
		TextView locationDelTxt;
	}
}
