package com.xqxy.person;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

public class StoreCardDetailActivity extends BaseActivity {
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;
	
	private View line;
	private TextView content;
	private ListView listView;
	private StoreCardAdapter adapter;
	private ArrayList<StoreCard> datas;
	private TextView nodata;
	public static String BUY_ACTION="buy";
	public static String DATA="data";
	private boolean buyModule=false;
	
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
		line=findViewById(R.id.scard_line);
		content=(TextView) findViewById(R.id.scard_content);
		line.setVisibility(View.VISIBLE);
		content.setVisibility(View.VISIBLE);
		getCard();
	}
	
	
	
	public void getCard()
	{
		Intent intent=getIntent();
		String action=intent.getStringExtra(BUY_ACTION);
		if(action!=null)
		{
			buyModule=true;
			rightBtnTextView.setText("立即购买");
		}
		else 
			buyModule=false;
		StoreCard card= (StoreCard) intent.getSerializableExtra(DATA);
		datas=new ArrayList<StoreCard>();
		datas.add(card);
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

			viewHolder.scard_buynow.setVisibility(View.GONE);
			final StoreCard card = getItem(position);
			viewHolder.scard_name.setText(card.getName());
			
			
			
			content.setText(Html.fromHtml(card.getContent()));
			if(!buyModule)
			{
				viewHolder.scard_date.setVisibility(View.VISIBLE);
				viewHolder.scard_num.setVisibility(View.VISIBLE);
				viewHolder.scard_date.setText(getString(R.string.storecard_date,card.getEnd_time()));
				viewHolder.scard_num.setText(getString(R.string.storecard_num,card.getCid()));
			}
			else
			{
				viewHolder.scard_price_layout.setVisibility(View.VISIBLE);
				viewHolder.scard_price.setText(getString(R.string.storecard_price,card.getPrice()));
				viewHolder.scard_worth.setText(getString(R.string.storecard_worth,card.getWorth()));
				//购买
				viewHolder.scard_buy_layout.setVisibility(View.VISIBLE);
				if(card.getOver_time().equals("0"))
					viewHolder.scard_datelong.setText(getString(R.string.storecard_date,"永久"));
				else
					viewHolder.scard_datelong.setText(getString(R.string.storecard_date,card.getOver_time())+"年");
			}
			
			return convertView;
		}
	}

	class ViewHolder {
		TextView scard_name;
		TextView scard_date;
		TextView scard_num;
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
