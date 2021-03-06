package com.xqxy.person;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;

import com.xqxy.model.Credit;


/**
 * 积分页面
 * @author Administrator
 *
 */
public class CreditActivity extends BaseActivity {

	private ListView listView;//listview控件
	private CreditAdapter adapter;//适配器
	private ArrayList<Credit> datas;//数据源
	private TextView nodata;//无数据时候显示的控件
	/**
	 * activity的初始化方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_credit);
		init();
	}
	/**
	 * 初始化控件以及数据
	 */
	private void init() {
//		RequestWrapper wrapper = new RequestWrapper();
//		wrapper.setPhone("13466899985");
//		wrapper.setPassword("1");
//		sendData(wrapper, NetworkAction.userF_login);
		nodata=(TextView) findViewById(R.id.nodataTxt);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new CreditAdapter(this);
		listView.setAdapter(adapter);
		RequestWrapper wrapper = new RequestWrapper();
		wrapper.setIdentity(MyApplication.identity);
		sendData(wrapper, NetworkAction.centerF_user_integral);
	}
	/**
	 * 解析数据类
	 */
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType==(NetworkAction.centerF_user_integral)) {
			datas=responseWrapper.getIntegral();
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
//		else if (requestType.equals(NetworkAction.userF_login)) {
//
//			MyApplication.identity = responseWrapper.getIdentity().get(0)
//					.getIdentity();
//			RequestWrapper wrapper = new RequestWrapper();
//			wrapper.setIdentity(responseWrapper.getIdentity().get(0)
//					.getIdentity());
////			sendData(wrapper, NetworkAction.centerF_user_integral);
//		}
	}

	public void btnOnClick(View view) {
		finish();
	}

	class CreditAdapter extends CarBaseAdapter<Credit> {

		public CreditAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.person_credit_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.credit_date = (TextView) convertView
						.findViewById(R.id.credit_date);
				viewHolder.credit_note = (TextView) convertView
						.findViewById(R.id.credit_note);
				viewHolder.credit_integral = (TextView) convertView
						.findViewById(R.id.credit_integral);
				viewHolder.credit_title = (TableRow) convertView
						.findViewById(R.id.credit_title);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			if(position>0)
				viewHolder.credit_title.setVisibility(View.GONE);
			final Credit credit = getItem(position);
			viewHolder.credit_date.setText(credit.getCreate_time());
			viewHolder.credit_note.setText(credit.getNote());
			viewHolder.credit_integral.setText(credit.getIntegral());
			
			return convertView;
		}
	}

	class ViewHolder {
		TextView credit_date;
		TextView credit_note;
		TextView credit_integral;
		TableRow credit_title;
	}
}
