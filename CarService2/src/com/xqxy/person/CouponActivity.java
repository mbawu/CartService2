package com.xqxy.person;

import java.sql.Wrapper;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Coupon;
import com.xqxy.model.Credit;
import com.xqxy.model.Message;
import com.xqxy.person.CreditActivity.CreditAdapter;
import com.xqxy.person.CreditActivity.ViewHolder;

/**
 * 优惠券页面
 * @author Administrator
 *
 */
public class CouponActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private ImageView backImageView;//显示图片的控件
	private TextView titleTextView; //标题栏
	private TextView rightBtnTextView; //标题右边的内容
	private ListView listView;//listview控件
	private CouponAdapter adapter;//适配器
	private ArrayList<Coupon> datas;//数据源
	private TextView nodata;//无数据时候显示的控件
	private RequestWrapper requestWrapper;//请求实体类
	private RadioButton noUse; //未使用
	private RadioButton used;//已使用
	private RadioButton expired; //已过期
	private boolean selectModule = false; //模式
	private View line;
	int statu = 1;


	/**
	 * activity的初始化方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_coupon);
		init();
	}

	/**
	 * 初始化控件以及数据
	 */
	private void init() {
		// datas=new ArrayList<Coupon>();
		line = findViewById(R.id.line);
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CouponActivity.this.finish();

			}
		});
		nodata = (TextView) findViewById(R.id.nodataTxt);
		listView = (ListView) findViewById(R.id.listview);
		adapter = new CouponAdapter(this);
		listView.setAdapter(adapter);

		noUse = (RadioButton) findViewById(R.id.coupon_noUse);
		used = (RadioButton) findViewById(R.id.coupon_used);
		expired = (RadioButton) findViewById(R.id.coupon_expired);
		noUse.setOnCheckedChangeListener(this);
		used.setOnCheckedChangeListener(this);
		expired.setOnCheckedChangeListener(this);
		String select = getIntent().getStringExtra("select");
		if (select != null) {
			selectModule = true;
			used.setVisibility(View.GONE);
			expired.setVisibility(View.GONE);
			noUse.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			nodata.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			datas = (ArrayList<Coupon>) getIntent()
					.getSerializableExtra("data");
			adapter.setDataList(datas);
			adapter.notifyDataSetChanged();

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Coupon coupon = datas.get(arg2);
					Intent intent = new Intent();
					Bundle b = new Bundle();
					b.putSerializable("data", coupon);
					intent.putExtras(b);
					setResult(OrderPayActivity.GET_COUPON, intent);
					finish();
				}
			});
		}
		if (!selectModule)
			noUse.setChecked(true);
	}

	/**
	 * 解析数据类
	 */
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.centerF_user_coupon) {
			datas = responseWrapper.getCoupon();
			if (datas.size() > 0) {
				for (int i = 0; i < datas.size(); i++) {
					Coupon c=datas.get(i);
					c.setExpired();
				}
			}
			
			Log.i(Cst.TAG, " datas.size()-->" + datas.size() + "");
			if (datas.size() > 0) {
				nodata.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				adapter.setDataList(datas);
				
			} else {
				nodata.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
			adapter.notifyDataSetChanged();
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
				convertView = inflater.inflate(R.layout.person_coupon_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.coupon_price = (TextView) convertView
						.findViewById(R.id.coupon_price);
				viewHolder.coupon_name = (TextView) convertView
						.findViewById(R.id.coupon_name);
				viewHolder.coupon_num = (TextView) convertView
						.findViewById(R.id.coupon_num);
				viewHolder.coupon_expired = (TextView) convertView
						.findViewById(R.id.coupon_expired);
				viewHolder.coupon_status = (TextView) convertView
						.findViewById(R.id.coupon_status);
				viewHolder.coupon_date = (TextView) convertView
						.findViewById(R.id.coupon_date);
				viewHolder.coupon_layout = (LinearLayout) convertView
						.findViewById(R.id.coupon_layout);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			try {

				final Coupon coupon = getItem(position);
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				String price = decimalFormat.format(Double.valueOf(coupon
						.getPrice()));
				viewHolder.coupon_price.setText(price + "元");
				String name = coupon.getName();
				if (name == null|| name.equals(""))
					name = price + "元优惠券全场使用";
				else
					name = price + "元优惠券限" + name + "使用";
				viewHolder.coupon_name.setText(name);
				viewHolder.coupon_num.setText("号码：" + coupon.getCode());
				String status = coupon.getStatus();
				if (status.equals("1"))
					status = "状态：未使用";
				else if (status.equals("2"))
					status = "状态：已使用";

				if (statu == 1 && coupon.getFlag().equals("1")) {
					viewHolder.coupon_expired.setVisibility(View.VISIBLE);
					viewHolder.coupon_layout.setVisibility(View.VISIBLE);
				} else if (statu == 1 && coupon.getFlag().equals("2")) {
//					datas.remove(position);
//					notifyDataSetChanged();
					viewHolder.coupon_layout.setVisibility(View.GONE);
				} else if (statu == 2) {
					viewHolder.coupon_layout.setVisibility(View.VISIBLE);
					viewHolder.coupon_expired.setVisibility(View.GONE);
					if (coupon.getFlag().equals("2")) {
						viewHolder.coupon_expired.setText("已过期");
						viewHolder.coupon_expired.setVisibility(View.VISIBLE);
					}
				} else if (statu == 3) {
					viewHolder.coupon_layout.setVisibility(View.VISIBLE);
					viewHolder.coupon_expired.setText("已过期");
					viewHolder.coupon_expired.setVisibility(View.VISIBLE);
				}
				viewHolder.coupon_status.setText(status);
				viewHolder.coupon_date.setText(coupon.getOver_time());
				if(coupon.isExpired() || selectModule)
				{
					viewHolder.coupon_expired.setVisibility(View.GONE);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return convertView;
		}
	}

	class ViewHolder {
		TextView coupon_price;
		TextView coupon_name;
		TextView coupon_num;
		TextView coupon_expired;
		TextView coupon_status;
		TextView coupon_date;
		LinearLayout coupon_layout;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		// datas.clear();
		requestWrapper = new RequestWrapper();
		requestWrapper.setIdentity(MyApplication.identity);
		requestWrapper.setShowDialog(true);
		switch (buttonView.getId()) {
		case R.id.coupon_noUse:
			if (isChecked) {
				statu = 1;
				requestWrapper.setStatus("1");
				sendDataByGet(requestWrapper, NetworkAction.centerF_user_coupon);
			}

			break;
		case R.id.coupon_used:
			if (isChecked) {
				statu = 2;
				requestWrapper.setStatus("2");
				sendDataByGet(requestWrapper, NetworkAction.centerF_user_coupon);
			}

			break;
		case R.id.coupon_expired:
			if (isChecked) {
				statu = 3;
				requestWrapper.setStatus("3");
				sendDataByGet(requestWrapper, NetworkAction.centerF_user_coupon);
			}

			break;
		}

	}
}
