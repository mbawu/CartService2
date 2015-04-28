package com.xqxy.person;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;

import com.xqxy.model.StoreCard;

public class StoreCardActivity extends BaseActivity {
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
		 if (requestType.equals(NetworkAction.centerF_user_card)) {
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
		sendData(requestWrapper, NetworkAction.centerF_user_card);
	}
	
	
	class StoreCardAdapter extends CarBaseAdapter<StoreCard> {

		public StoreCardAdapter(Activity activity) {
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

			final StoreCard msg = getItem(position);
			viewHolder.msg_content.setText(msg.getContent());
			viewHolder.msg_date.setText(msg.getCreate_time());
			//未读
			if(msg.getStatus().equals("1"))
				viewHolder.msg_layout.setBackground(getResources().getDrawable(R.drawable.edit_text_selector));
			else
				viewHolder.msg_layout.setBackground(getResources().getDrawable(R.drawable.style_text_selector));
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
