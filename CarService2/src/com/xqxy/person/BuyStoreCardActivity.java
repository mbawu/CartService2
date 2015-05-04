package com.xqxy.person;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;

import com.xqxy.model.StoreCard;

public class BuyStoreCardActivity extends BaseActivity {
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	
	private ListView listView;
	private StoreCardAdapter adapter;
	private ArrayList<StoreCard> datas;
	private TextView nodata;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_storecard);
		init();
	}

	private void init() {
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		nodata=(TextView) findViewById(R.id.nodataTxt);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new StoreCardAdapter(this);
		listView.setAdapter(adapter);
		
		getCard();
	}
	
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		 if (requestType.equals(NetworkAction.indexF_card)) {
				datas = responseWrapper.getCard();
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
	}
	
	public void getCard()
	{
		RequestWrapper requestWrapper = new RequestWrapper();
		requestWrapper.setIdentity(MyApplication.identity);
		requestWrapper.setShowDialog(true);
		sendDataByGet(requestWrapper, NetworkAction.indexF_card);
	}
	
	
	class StoreCardAdapter extends CarBaseAdapter<StoreCard> {

		public StoreCardAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.person_storecard_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.scard_name = (TextView) convertView
						.findViewById(R.id.scard_name);
				viewHolder.scard_datelong = (TextView) convertView
						.findViewById(R.id.scard_datelong);
				viewHolder.scard_price = (TextView) convertView
						.findViewById(R.id.scard_price);
				viewHolder.scard_worth = (TextView) convertView
						.findViewById(R.id.scard_worth);
				viewHolder.scard_buynow = (TextView) convertView
						.findViewById(R.id.scard_buynow);
//				viewHolder.scard_num = (TextView) convertView
//						.findViewById(R.id.scard_num);
//				viewHolder.scard_cash = (TextView) convertView
//						.findViewById(R.id.scard_cash);
				viewHolder.scard_top_layout = (LinearLayout) convertView
						.findViewById(R.id.scard_top_layout);
				viewHolder.scard_btm_layout = (LinearLayout) convertView
						.findViewById(R.id.scard_btm_layout);
				viewHolder.scard_price_layout = (LinearLayout) convertView
						.findViewById(R.id.scard_price_layout);
				viewHolder.scard_buy_layout = (RelativeLayout) convertView
						.findViewById(R.id.scard_buy_layout);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final StoreCard card = getItem(position);
			viewHolder.scard_buynow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent();
					intent.setClass(BuyStoreCardActivity.this, StoreCardDetailActivity.class);
					Bundle mBundle = new Bundle();     
			        mBundle.putSerializable(StoreCardDetailActivity.DATA,  card);     
			        intent.putExtra(StoreCardDetailActivity.BUY_ACTION, "buy");
			        intent.putExtras(mBundle);     
			        BuyStoreCardActivity.this.startActivity(intent);
				}
			});
			
		
			viewHolder.scard_name.setText(card.getName());
			viewHolder.scard_price_layout.setVisibility(View.VISIBLE);
			viewHolder.scard_price.setText(getString(R.string.storecard_price,card.getPrice()));
			viewHolder.scard_worth.setText(getString(R.string.storecard_worth,card.getWorth()));
			//购买
			viewHolder.scard_buy_layout.setVisibility(View.VISIBLE);
			if(card.getOver_time().equals("0"))
				viewHolder.scard_datelong.setText(getString(R.string.storecard_date,"永久"));
			else
				viewHolder.scard_datelong.setText(getString(R.string.storecard_date,card.getOver_time())+"年");
//			viewHolder.scard_date.setText(getString(R.string.storecard_date,card.getEnd_time()));
//			viewHolder.scard_num.setText(getString(R.string.storecard_num,card.getCid()));
			//1 储值卡 2 增值卡
//			if(card.getFlag().equals("2"))
//			{
//				viewHolder.scard_cash.setVisibility(View.VISIBLE);
//				viewHolder.scard_cash.setText(getString(R.string.storecard_cash,card.getBalance()));
//			}
		
			if(position==0)
			{
				viewHolder.scard_top_layout.setBackgroundResource(R.drawable.storecard_yellow_top);
				viewHolder.scard_btm_layout.setBackgroundResource(R.drawable.storecard_yellow_btm);
			}
			
			if(position==1 || (position>1 && position%2!=0))
			{
				viewHolder.scard_top_layout.setBackgroundResource(R.drawable.storecard_green_top);
				viewHolder.scard_btm_layout.setBackgroundResource(R.drawable.storecard_green_btm);
			}
			return convertView;
		}
	}

	class ViewHolder {
		TextView scard_name;
//		TextView scard_date;
//		TextView scard_num;
//		TextView scard_cash;
		TextView scard_price;
		TextView scard_worth;
		TextView scard_datelong;
		TextView scard_buynow;
		
		LinearLayout scard_top_layout;
		LinearLayout scard_btm_layout;
		LinearLayout scard_price_layout;
		RelativeLayout scard_buy_layout;
	}

}
