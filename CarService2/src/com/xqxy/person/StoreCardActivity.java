package com.xqxy.person;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;

import com.xqxy.model.Coupon;
import com.xqxy.model.StoreCard;

/**
 * 储值卡页面
 * @author Administrator
 *
 */
public class StoreCardActivity extends BaseActivity {
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	private ListView listView;
	private StoreCardAdapter adapter;
	private ArrayList<StoreCard> datas;
	private TextView nodata;
	private boolean selectM = false;

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

		nodata = (TextView) findViewById(R.id.nodataTxt);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new StoreCardAdapter(this);
		listView.setAdapter(adapter);
		getData();
		

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(MyApplication.buyStoreCardSuc)
			MyApplication.buyStoreCardSuc=false;
		if (!selectM) {
			getCard();
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					Intent intent = new Intent();
					intent.setClass(StoreCardActivity.this,
							StoreCardDetailActivity.class);
					Bundle mBundle = new Bundle();
					StoreCard card = datas.get(position);
					mBundle.putSerializable(StoreCardDetailActivity.DATA, card);
					// intent.putExtra(StoreCardDetailActivity.BUY_ACTION,
					// "buy");
					intent.putExtras(mBundle);
					StoreCardActivity.this.startActivity(intent);
				}
			});
			rightBtnTextView.setText("购买储值卡");
			rightBtnTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(StoreCardActivity.this,
							BuyStoreCardActivity.class);
					StoreCardActivity.this.startActivity(intent);
				}
			});
		}
	}
	private void getData() {
		final String select = getIntent().getStringExtra("select");
		if (select != null) {
			selectM = true;
			datas = (ArrayList<StoreCard>) getIntent().getSerializableExtra(
					"data");
			adapter.setDataList(datas);
			adapter.notifyDataSetChanged();
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					StoreCard card = datas.get(arg2);
					Intent intent = new Intent();
					Bundle b = new Bundle();
					b.putSerializable("data", card);
					intent.putExtras(b);
					if (select.equals("1"))
						setResult(OrderPayActivity.GET_CLEANCARD, intent);
					else if (select.equals("2"))
						setResult(OrderPayActivity.GET_STORECARD, intent);
					finish();
				}
			});
		}

	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType == (NetworkAction.centerF_user_card)) {
			datas = responseWrapper.getCard();
			if (datas.size() > 0) {
				nodata.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				adapter.setDataList(datas);
				adapter.notifyDataSetChanged();
			} else {
				nodata.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}

		}
	}

	public void getCard() {
		RequestWrapper requestWrapper = new RequestWrapper();
		requestWrapper.setIdentity(MyApplication.identity);
		requestWrapper.setShowDialog(true);
		sendDataByGet(requestWrapper, NetworkAction.centerF_user_card);
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
				viewHolder.scard_date = (TextView) convertView
						.findViewById(R.id.scard_date);
				viewHolder.scard_num = (TextView) convertView
						.findViewById(R.id.scard_num);
				viewHolder.scard_cash = (TextView) convertView
						.findViewById(R.id.scard_cash);
				viewHolder.scard_top_layout = (LinearLayout) convertView
						.findViewById(R.id.scard_top_layout);
				viewHolder.scard_btm_layout = (LinearLayout) convertView
						.findViewById(R.id.scard_btm_layout);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final StoreCard card = getItem(position);
			viewHolder.scard_name.setText(card.getName());
			viewHolder.scard_date.setVisibility(View.VISIBLE);
			viewHolder.scard_date.setText(getString(R.string.storecard_date,
					card.getEnd_time()));
			viewHolder.scard_num.setVisibility(View.VISIBLE);
			viewHolder.scard_num.setText(getString(R.string.storecard_num,
					card.getCode()));
			// 1 储值卡 2 增值卡
			if (card.getFlag().equals("2")) {
				viewHolder.scard_cash.setVisibility(View.VISIBLE);
				viewHolder.scard_cash.setText(getString(
						R.string.storecard_cash, card.getBalance()));
				viewHolder.scard_top_layout
						.setBackgroundResource(R.drawable.storecard_yellow_top);
				viewHolder.scard_btm_layout
						.setBackgroundResource(R.drawable.storecard_yellow_btm);
			} else {
				viewHolder.scard_cash.setVisibility(View.GONE);
				viewHolder.scard_top_layout
						.setBackgroundResource(R.drawable.storecard_green_top);
				viewHolder.scard_btm_layout
						.setBackgroundResource(R.drawable.storecard_green_btm);
			}

			// if(position==1 || (position>1 && position%2!=0))
			// {
			// viewHolder.scard_top_layout.setBackgroundResource(R.drawable.storecard_green_top);
			// viewHolder.scard_btm_layout.setBackgroundResource(R.drawable.storecard_green_btm);
			// }
			// if (position == 0) {
			// viewHolder.scard_top_layout
			// .setBackgroundResource(R.drawable.storecard_yellow_top);
			// viewHolder.scard_btm_layout
			// .setBackgroundResource(R.drawable.storecard_yellow_btm);
			// }
			//
			// if (position == 1 || (position > 1 && position % 2 != 0)) {
			// viewHolder.scard_top_layout
			// .setBackgroundResource(R.drawable.storecard_green_top);
			// viewHolder.scard_btm_layout
			// .setBackgroundResource(R.drawable.storecard_green_btm);
			// }
			return convertView;
		}
	}

	class ViewHolder {
		TextView scard_name;
		TextView scard_date;
		TextView scard_num;
		TextView scard_cash;
		LinearLayout scard_top_layout;
		LinearLayout scard_btm_layout;
	}

}
