package com.xqxy.person;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.carservice.R;

import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.carservice.view.CarImageView;
import com.xqxy.model.Cart;
import com.xqxy.model.Category;
import com.xqxy.model.OrderProduct;

public class OrderPayActivity extends BaseActivity {

	private CartAdapter adapter;
	private ListView listView;
	private ArrayList<Cart> cart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_pay_layout);
		listView = (ListView) findViewById(R.id.listview);
		cart =(ArrayList<Cart>) getIntent().getSerializableExtra("data");
		Log.i("test", cart.size()+"");
		Log.i("test",cart.get(0).getName()+"");
		adapter = new CartAdapter(this);
		adapter.setDataList(cart);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	class CartAdapter extends CarBaseAdapter<Cart> {

		public CartAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder ViewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.pay_list_item,
						null);
				ViewHolder = new ViewHolder();
				ViewHolder.textOrderProductName = (TextView) convertView
						.findViewById(R.id.pay_product_name);
				ViewHolder.textOrderProductPrice = (TextView) convertView
						.findViewById(R.id.pay_product_price);
				ViewHolder.textOrderProductTime = (TextView) convertView
						.findViewById(R.id.pay_product_time);
				ViewHolder.imageView = (CarImageView) convertView
						.findViewById(R.id.img_pay);
				convertView.setTag(ViewHolder);

			} else {
				ViewHolder = (ViewHolder) convertView.getTag();
			}
			Cart op = getItem(position);
			ViewHolder.textOrderProductName.setText(op.getName());
			ViewHolder.textOrderProductPrice.setText(getString(
					R.string.order_price, op.getReal_price()));
			ViewHolder.textOrderProductTime.setText(getString(
					R.string.product_time, op.getTime()));
			if (op.getPic() != null && !"".equals(op.getPic())) {
				ViewHolder.imageView.loadImage(op.getPic());
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView textOrderProductName;
		TextView textOrderProductPrice;
		TextView textOrderProductTime;
		CarImageView imageView;
	}

}
