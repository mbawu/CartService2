package com.xqxy.person;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
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

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.JsonUtil;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.cn.hongwei.SlideListView;
import com.cn.hongwei.SlideListView.RemoveDirection;
import com.cn.hongwei.SlideListView.RemoveListener;
import com.xqxy.carservice.R;
import com.xqxy.carservice.activity.CarListActivity;
import com.xqxy.carservice.activity.ConfirmDialog;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Address;

/**
 * 地址管理页面
 * @author Administrator
 *
 */
public class AdressActivity extends BaseActivity {

	private SlideListView listView; //listview控件
	private AddressAdapter adapter; //适配器
	private ArrayList<Address> datas; //数据源
	private TextView addBtn; //添加按钮
	private RequestWrapper wrapper;//发请求实体类
	private boolean selectAddress=false; //是否是选择地址的模式

	/**
	 * activity的初始化方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_address);
		Log.i(Cst.TAG, "onCreate");
		init();
	}

	/**
	 * 初始化控件以及数据
	 */
	private void init() {
		// Log.i(Cst.TAG, "init");
		// // 登录
		// RequestWrapper wrapper = new RequestWrapper();
		// wrapper.setPhone("13466899985");
		// wrapper.setPassword("1");
		// sendData(wrapper, NetworkAction.userF_login);
		check();
		addBtn = (TextView) findViewById(R.id.address_add);
		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent().setClass(AdressActivity.this,
						AddressAddActivity.class);
				AdressActivity.this.startActivity(intent);

			}
		});
		listView = (SlideListView) findViewById(R.id.listview);
		listView.setRemoveListener(new RemoveListener() {
			
			@Override
			public void removeItem(RemoveDirection direction, int position) {
				deleteAddress(datas.get(position));
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Address address = datas.get(position);
				if(!selectAddress)
				{
					Intent intent = new Intent();
					intent.setClass(AdressActivity.this, AddressAddActivity.class);
					intent.putExtra(AddressAddActivity.ADDRESS_ID, address.getAid());
					Bundle bundle = new Bundle();
					bundle.putSerializable("address", address);
					intent.putExtras(bundle);
					AdressActivity.this.startActivity(intent);
				}
				else
				{
//					CallServiceActivity.address=address;
					MyApplication.ed.putString("address", JsonUtil.toJson(address));
					MyApplication.ed.commit();
					finish();
				}
				
			}
		});
		adapter = new AddressAdapter(this);
		listView.setAdapter(adapter);
		wrapper = new RequestWrapper();
		wrapper.setIdentity(MyApplication.identity);
		wrapper.setShowDialog(true);
		sendData(wrapper, NetworkAction.centerF_user_address);
	}

	/**
	 * 检查是选择模式还是管理模式
	 */
	private void check() {
		Intent intent=getIntent();
		String select=intent.getStringExtra("select");
		if(select!=null)
		{
			selectAddress=true;
		}
		
	}

	
	/**
	 * 解析数据类
	 */
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType==(NetworkAction.centerF_user_address)) {
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
		else if (requestType==(NetworkAction.centerF_del_address)) {
			Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
			RequestWrapper wrapper = new RequestWrapper();
			wrapper.setIdentity(MyApplication.identity);
			sendData(wrapper, NetworkAction.centerF_user_address);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(MyApplication.refresh)
		{
			sendData(wrapper, NetworkAction.centerF_user_address);
			MyApplication.refresh=false;
		}
	}
	
	/**
	 * 按钮点击事件
	 * @param view
	 */
	public void btnOnClick(View view) {
		finish();
	}

	/**
	 * 删除地址
	 * @param address
	 */
	public void deleteAddress(Address address)
	{
		RequestWrapper wrapper = new RequestWrapper();
		wrapper.setIdentity(MyApplication.identity);
		wrapper.setAid(address.getAid());
		wrapper.setShowDialog(true);
		sendData(wrapper, NetworkAction.centerF_del_address);
	}
	
	/**
	 * 地址适配器
	 * @author Administrator
	 *
	 */
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
				
					
					ConfirmDialog dlg = new ConfirmDialog(AdressActivity.this);
					dlg.setTitle("提示");
					dlg.setMessage("确定删除地址吗？删除后将无法恢复.");
					dlg.setPositiveButton("删除",
							new ConfirmDialog.OnClickListener() {

								@Override
								public void onClick(Dialog dialog, View view) {
									RequestWrapper wrapper = new RequestWrapper();
									wrapper.setIdentity(MyApplication.identity);
									wrapper.setAid(address.getAid());
									wrapper.setShowDialog(true);
									sendData(wrapper, NetworkAction.centerF_del_address);
								}
							});

					dlg.setNegativeButton("取消",
							new ConfirmDialog.OnClickListener() {

								@Override
								public void onClick(Dialog dialog, View view) {

								}
							});
					dlg.show();
					
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
