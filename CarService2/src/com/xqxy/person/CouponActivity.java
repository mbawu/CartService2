package com.xqxy.person;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Coupon;
import com.xqxy.model.Credit;
import com.xqxy.model.Message;
import com.xqxy.person.CreditActivity.CreditAdapter;
import com.xqxy.person.CreditActivity.ViewHolder;

public class CouponActivity extends BaseActivity {
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	private ListView listView;
	private CouponAdapter adapter;
	private ArrayList<Coupon> datas;
	private TextView nodata;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_coupon);
		init();
	}
	
	private void init() {
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CouponActivity.this.finish();
				
			}
		});
		nodata=(TextView) findViewById(R.id.nodataTxt);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new CouponAdapter(this);
		listView.setAdapter(adapter);

		if(datas.size()>0)
		{
			nodata.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			adapter.setDataList(datas);
			adapter.notifyDataSetChanged();
		}
		else
		{
			nodata.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
	}
	
	
	class CouponAdapter extends CarBaseAdapter<Coupon> {

		public CouponAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.person_message_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.msg_content = (TextView) convertView
						.findViewById(R.id.msg_content);
				viewHolder.msg_date = (TextView) convertView
						.findViewById(R.id.msg_date);
				viewHolder.msg_layout = (LinearLayout) convertView
						.findViewById(R.id.msg_layout);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final Coupon coupon = getItem(position);
//			viewHolder.msg_content.setText(coupon.getContent());
//			viewHolder.msg_date.setText(coupon.getCreate_time());
//			//未读
//			if(coupon.getStatus().equals("1"))
//				viewHolder.msg_layout.setBackground(getResources().getDrawable(R.drawable.edit_text_selector));
//			else
//				viewHolder.msg_layout.setBackground(getResources().getDrawable(R.drawable.style_text_selector));
//			viewHolder.credit_integral.setText(msg.getIntegral());
			
			return convertView;
		}
	}

	class ViewHolder {
		TextView msg_content;
		TextView msg_date;
		LinearLayout msg_layout;
//		TextView credit_integral;
//		TableRow credit_title;
	}
}
