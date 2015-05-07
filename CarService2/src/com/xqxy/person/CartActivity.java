package com.xqxy.person;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.Cst;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Cart;
import com.xqxy.model.Coupon;
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
	private RequestWrapper wrapper;

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
		listView = (ListView) findViewById(R.id.listview);
		adapter = new CartAdapter(this);
		listView.setAdapter(adapter);

		wrapper = new RequestWrapper();
		wrapper.setIdentity(MyApplication.identity);
		wrapper.setShowDialog(true);
		sendDataByGet(wrapper, NetworkAction.cartF_index);
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
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageTopBack:
			finish();
			break;

		}

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

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Cart cart = getItem(position);
			viewHolder.cart_name.setText(cart.getName());
			viewHolder.cart_real_price.setText(cart.getReal_price());
			viewHolder.cart_attrname.setText(cart.getAttrname());
			viewHolder.cart_time.setText(cart.getTime());
			viewHolder.cart_edit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			viewHolder.cart_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
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
