package com.xqxy.person;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.Pay;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;

import com.xqxy.model.StoreCard;

public class StoreCardDetailActivity extends BaseActivity {
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	private View line;
	private WebView content;
	private ListView listView;
	private StoreCardAdapter adapter;
	private ArrayList<StoreCard> datas;
	private TextView nodata;
	public static String BUY_ACTION = "buy";
	public static String DATA = "data";
	private boolean buyModule = false;

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
		rightBtnTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(StoreCardDetailActivity.this,
						PayCarInfoActivity.class);

				StoreCard card = datas.get(0);
				if (buyModule) {
					// 1储值卡 2增值卡
					if (card.getFlag().equals("1")) {
						Bundle mBundle = new Bundle();
						mBundle.putSerializable(StoreCardDetailActivity.DATA,
								card);
						intent.putExtras(mBundle);
						StoreCardDetailActivity.this.startActivity(intent);
					} else {
						RequestWrapper wrapper = new RequestWrapper();
						wrapper.setCid(card.getCid());
						wrapper.setIdentity(MyApplication.identity);
						sendData(wrapper, NetworkAction.orderF_buy_card);
					}
				}

				// Pay pay=new Pay(StoreCardDetailActivity.this);
				// pay.alipay("储值卡",
				// "{\"type\":\"1\",\"identity\":\"afasdfasdfasdf\",\"cid\":\"123456\"}",
				// "0.01");
			}
		});
		nodata = (TextView) findViewById(R.id.nodataTxt);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new StoreCardAdapter(this);
		listView.setAdapter(adapter);
		line = findViewById(R.id.scard_line);
		content = (WebView) findViewById(R.id.scard_content);
		WebSettings settings = content.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		line.setVisibility(View.VISIBLE);
		content.setVisibility(View.VISIBLE);
		getCard();
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.orderF_buy_card) {
			// 跳转至支付页面
		}
	}

	public void getCard() {
		Intent intent = getIntent();
		String action = intent.getStringExtra(BUY_ACTION);
		if (action != null) {
			buyModule = true;
			rightBtnTextView.setText("立即购买");
		} else
			buyModule = false;
		StoreCard card = (StoreCard) intent.getSerializableExtra(DATA);
		datas = new ArrayList<StoreCard>();
		datas.add(card);
		content.loadData(card.getContent(),
				"text/html; charset=UTF-8", null);
		adapter.setDataList(datas);
		adapter.notifyDataSetChanged();
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
				viewHolder.scard_num = (TextView) convertView
						.findViewById(R.id.scard_num);
				viewHolder.scard_date = (TextView) convertView
						.findViewById(R.id.scard_date);
				// viewHolder.scard_cash = (TextView) convertView
				// .findViewById(R.id.scard_cash);
				viewHolder.scard_top_layout = (LinearLayout) convertView
						.findViewById(R.id.scard_top_layout);
				viewHolder.scard_btm_layout = (LinearLayout) convertView
						.findViewById(R.id.scard_btm_layout);
				viewHolder.scard_price_layout = (LinearLayout) convertView
						.findViewById(R.id.scard_price_layout);
				viewHolder.scard_buy_layout = (LinearLayout) convertView
						.findViewById(R.id.scard_buy_layout);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.scard_buynow.setVisibility(View.GONE);
			final StoreCard card = getItem(position);
			viewHolder.scard_name.setText(card.getName());
			
			if (!buyModule) {
				viewHolder.scard_date.setVisibility(View.VISIBLE);
				viewHolder.scard_num.setVisibility(View.VISIBLE);
				viewHolder.scard_date.setText(getString(
						R.string.storecard_date, card.getEnd_time()));
				viewHolder.scard_num.setText(getString(R.string.storecard_num,
						card.getCid()));
			} else {
				if (card.getFlag().equals("2")) {
					viewHolder.scard_worth.setVisibility(View.VISIBLE);
				} else
					viewHolder.scard_worth.setVisibility(View.GONE);
				viewHolder.scard_price_layout.setVisibility(View.VISIBLE);
				viewHolder.scard_price.setText(getString(
						R.string.storecard_price, card.getPrice()));
				viewHolder.scard_worth.setText(getString(
						R.string.storecard_worth, card.getWorth()));
				// 购买
				viewHolder.scard_buy_layout.setVisibility(View.VISIBLE);
				if (card.getOver_time().equals("0"))
					viewHolder.scard_datelong.setText(getString(
							R.string.storecard_date, "永久"));
				else
					viewHolder.scard_datelong
							.setText(getString(R.string.storecard_date,
									card.getOver_time())
									+ "年");
			}
			// 1 储值卡 2 增值卡
			if (card.getFlag().equals("2")) {
				viewHolder.scard_top_layout
						.setBackgroundResource(R.drawable.storecard_yellow_top);
				viewHolder.scard_btm_layout
						.setBackgroundResource(R.drawable.storecard_yellow_btm);
			} else {
				viewHolder.scard_top_layout
						.setBackgroundResource(R.drawable.storecard_green_top);
				viewHolder.scard_btm_layout
						.setBackgroundResource(R.drawable.storecard_green_btm);
			}
			return convertView;
		}
	}

	class ViewHolder {
		TextView scard_name;
		TextView scard_date;
		TextView scard_num;
		// TextView scard_cash;
		TextView scard_price;
		TextView scard_worth;
		TextView scard_datelong;
		TextView scard_buynow;

		LinearLayout scard_top_layout;
		LinearLayout scard_btm_layout;
		LinearLayout scard_price_layout;
		LinearLayout scard_buy_layout;
	}

}
