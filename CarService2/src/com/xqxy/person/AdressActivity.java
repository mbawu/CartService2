package com.xqxy.person;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.Cst;
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
	private ArrayList<Address> datas;
	private TextView addBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_address);
		Log.i(Cst.TAG, "onCreate");
		init();
	}

	private void init() {
		// Log.i(Cst.TAG, "init");
		// // 登录
		// RequestWrapper wrapper = new RequestWrapper();
		// wrapper.setPhone("13466899985");
		// wrapper.setPassword("1");
		// sendData(wrapper, NetworkAction.userF_login);

		addBtn = (TextView) findViewById(R.id.address_add);
		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent().setClass(AdressActivity.this,
						AddressAddActivity.class);
				AdressActivity.this.startActivity(intent);

			}
		});
		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Address address = datas.get(position);
				Intent intent = new Intent();
				intent.setClass(AdressActivity.this, AddressAddActivity.class);
				intent.putExtra(AddressAddActivity.ADDRESS_ID, address.getAid());
				Bundle bundle = new Bundle();
				bundle.putSerializable("address", address);
				intent.putExtras(bundle);
				AdressActivity.this.startActivity(intent);
			}
		});
		adapter = new AddressAdapter(this);
		listView.setAdapter(adapter);
		RequestWrapper wrapper = new RequestWrapper();
		wrapper.setIdentity(MyApplication.identity);
		sendData(wrapper, NetworkAction.centerF_user_address);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType.equals(NetworkAction.centerF_user_address)) {
			datas = responseWrapper.getAddress();
			if (datas.size() > 0) {
				listView.setVisibility(View.VISIBLE);
				adapter.setDataList(datas);
				adapter.notifyDataSetChanged();
			} else {
				listView.setVisibility(View.GONE);
			}
		}
		// else if (requestType.equals(NetworkAction.userF_login)) {
		//
		// MyApplication.identity = responseWrapper.getIdentity().get(0)
		// .getIdentity();
		// RequestWrapper wrapper = new RequestWrapper();
		// wrapper.setIdentity(responseWrapper.getIdentity().get(0)
		// .getIdentity());
		// sendData(wrapper, NetworkAction.centerF_user_address);
		// }
		else if (requestType.equals(NetworkAction.centerF_del_address)) {
			Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
			RequestWrapper wrapper = new RequestWrapper();
			wrapper.setIdentity(MyApplication.identity);
			sendData(wrapper, NetworkAction.centerF_user_address);
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
				convertView = inflater.inflate(R.layout.person_address_item,
						null);
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
				viewHolder.deleteBtn = (TextView) convertView
						.findViewById(R.id.address_delete);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final Address address = getItem(position);
			viewHolder.nameTxt.setText(address.getName());
			viewHolder.phoneTxt.setText(address.getPhone());
			viewHolder.carNumTxt.setText(address.getCar_num());
			viewHolder.locationTxt.setText(address.getAddress());
			viewHolder.locationDelTxt.setText(address.getDetailed());
			viewHolder.deleteBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RequestWrapper wrapper = new RequestWrapper();
					wrapper.setIdentity(MyApplication.identity);
					wrapper.setAid(address.getAid());
					sendData(wrapper, NetworkAction.centerF_del_address);
				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		TextView nameTxt;
		TextView phoneTxt;
		TextView carNumTxt;
		TextView locationTxt;
		TextView locationDelTxt;
		TextView deleteBtn;
	}
}
