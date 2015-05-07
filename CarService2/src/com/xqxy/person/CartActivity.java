package com.xqxy.person;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.Cst;
import com.xqxy.baseclass.JsonUtil;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.activity.CarListActivity;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.carservice.widget.CartDialog;
import com.xqxy.carservice.widget.ConfirmDialog;
import com.xqxy.model.Cart;
import com.xqxy.model.Coupon;
import com.xqxy.model.ProductAttr;
import com.xqxy.person.CouponActivity.CouponAdapter;
import com.xqxy.person.CouponActivity.ViewHolder;

public class CartActivity extends BaseActivity implements OnClickListener {
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	private ListView listView;
	private CartAdapter adapter;
	private ArrayList<Cart> datas;
	private TextView nodata;
	private TextView callBtn;
	private RequestWrapper cartWrapper;
	private RequestWrapper delWrapper;
//	private Cart cartSelect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart);
		init();

	}

	private void init() {
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		backImageView.setOnClickListener(this);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);

		nodata = (TextView) findViewById(R.id.nodataTxt);
		callBtn = (TextView) findViewById(R.id.cart_call);
		callBtn.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new CartAdapter(this);
		listView.setAdapter(adapter);
		registerBoradcastReceiver();
		cartWrapper = new RequestWrapper();
		cartWrapper.setIdentity(MyApplication.identity);
		cartWrapper.setShowDialog(true);
		sendDataByGet(cartWrapper, NetworkAction.cartF_index);
		
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.cartF_index) {
			datas = responseWrapper.getCart();
			Log.i(Cst.TAG, " datas.size()-->" + datas.size() + "");
			if (datas.size() > 0) {
				nodata.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				callBtn.setVisibility(View.VISIBLE);
				adapter.setDataList(datas);
				adapter.notifyDataSetChanged();
			} else {
				nodata.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				callBtn.setVisibility(View.GONE);
			}

		} else if (requestType == NetworkAction.cartF_del_cart) {
			Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
			// datas.clear();
			sendDataByGet(cartWrapper, NetworkAction.cartF_index);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageTopBack:
			finish();
			break;
		case R.id.cart_call:
			ArrayList<Cart> tempCats=new ArrayList<Cart>();
			for (int i = 0; i < datas.size(); i++) {
				Cart cart=datas.get(i);
				if(cart.isChecked())
				{
					Cart cart1=new Cart();
					cart1.setId(cart.getId());
					tempCats.add(cart1);
				}
			}
			if(tempCats.size()<=0)
			{
				Toast.makeText(this, "请选择要预约的服务", Toast.LENGTH_SHORT).show();
				return;
			}
			String json=JsonUtil.toJson(tempCats);
			Log.i("test", "json--->"+json);
			Intent intent=new Intent();
			intent.setClass(this, CallServiceActivity.class);
			intent.putExtra("cart",json);
			Bundle b=new Bundle();
			b.putSerializable("data", tempCats);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}

	}

	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Cst.CART_CAHNGE)) {
//				datas.clear();
				sendDataByGet(cartWrapper, NetworkAction.cartF_index);
				
			}
		}

	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Cst.CART_CAHNGE);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
		
	}

	public void changeAttr()
	{
		sendDataByGet(cartWrapper, NetworkAction.cartF_index);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
	
	class CartAdapter extends CarBaseAdapter<Cart> {

		public CartAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.cart_item, null);
				viewHolder = new ViewHolder();
				viewHolder.cart_name = (TextView) convertView
						.findViewById(R.id.cart_name);
				viewHolder.cart_real_price = (TextView) convertView
						.findViewById(R.id.cart_real_price);
				viewHolder.cart_attrname = (TextView) convertView
						.findViewById(R.id.cart_attrname);
				viewHolder.cart_time = (TextView) convertView
						.findViewById(R.id.cart_time);
				viewHolder.cart_edit = (TextView) convertView
						.findViewById(R.id.cart_edit);
				viewHolder.cart_delete = (TextView) convertView
						.findViewById(R.id.cart_delete);
				viewHolder.cart_check = (CheckBox) convertView
						.findViewById(R.id.cart_check);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final Cart cart = getItem(position);
			viewHolder.cart_check.setChecked(cart.isChecked());
			viewHolder.cart_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					cart.setChecked(isChecked);
				}
			});
			viewHolder.cart_name.setText(cart.getName());
			viewHolder.cart_real_price.setText("￥" + cart.getReal_price());
			viewHolder.cart_attrname.setText(cart.getAttrname());
			viewHolder.cart_time.setText("时长：" + cart.getTime() + "分钟");
			if(cart.getFlag().equals("2"))
				viewHolder.cart_edit.setVisibility(View.VISIBLE);
			else
				viewHolder.cart_edit.setVisibility(View.GONE);
			viewHolder.cart_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CartDialog editDialog = new CartDialog(CartActivity.this,cart);
					editDialog.show();
				}
			});
			viewHolder.cart_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					delWrapper = new RequestWrapper();
					delWrapper.setIdentity(MyApplication.identity);
					delWrapper.setId(cart.getId());
					sendData(delWrapper, NetworkAction.cartF_del_cart);

				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		TextView cart_name;
		TextView cart_real_price;
		TextView cart_attrname;
		TextView cart_time;
		TextView cart_edit;
		TextView cart_delete;
		CheckBox cart_check;
	}
}
