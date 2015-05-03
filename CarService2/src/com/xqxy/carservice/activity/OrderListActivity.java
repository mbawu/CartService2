package com.xqxy.carservice.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Order;
import com.xqxy.model.OrderProduct;

public class OrderListActivity extends BaseActivity implements
		View.OnClickListener {

	private RadioGroup radioGroupType;
	private RadioButton radioBtnAll;
	private RadioButton radioBtnUnderway;
	private RadioButton radioBtnCancel;

	private ListView listView;
	private OrderListAdapter adapter;
	private List<Order> orderList;

	private String page = "1";
	private String status = "0";

	private static final String ORDER_STATUS_WAITING = "1"; // 待处理
	private static final String ORDER_STATUS_ACCEPT = "2"; // 已接受，进行中
	private static final String ORDER_STATUS_FINISH = "3"; // 已完成
	private static final String ORDER_STATUS_CANCEL = "4"; // 已取消

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list_layout);
		listView = (ListView) findViewById(R.id.listview);
		radioBtnAll = (RadioButton) findViewById(R.id.order_list_all_radiobtn);
		radioBtnUnderway = (RadioButton) findViewById(R.id.order_list_underway_radiobtn);
		radioBtnCancel = (RadioButton) findViewById(R.id.order_list_cancel_radiobtn);
		radioGroupType = (RadioGroup) findViewById(R.id.order_list_type);
		radioGroupType
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.order_list_all_radiobtn:
							status = "0";
							break;
						case R.id.order_list_underway_radiobtn:
							status = ORDER_STATUS_ACCEPT;
							break;
						case R.id.order_list_cancel_radiobtn:
							status = ORDER_STATUS_CANCEL;
							break;
						}
					}
				});

		adapter = new OrderListAdapter(this);
		listView.setAdapter(adapter);
		
		getOrder();
	}

	private void getOrder() {
		RequestWrapper request = new RequestWrapper();
		request.setIdentity(MyApplication.identity);
		request.setStatus(status);
		request.setPage(page);
		sendDataByGet(request, NetworkAction.centerF_user_order);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.centerF_user_order) {
			orderList = responseWrapper.getOrder();
			adapter.setDataList(orderList);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			String btnTxt = ((TextView) v).getText().toString();
			if (btnTxt.equals(getString(R.string.order_btn_cancel))) {

			} else if (btnTxt.equals(getString(R.string.order_btn_delete))) {

			} else if (btnTxt.equals(getString(R.string.order_btn_finish))) {

			} else if (btnTxt.equals(getString(R.string.order_btn_finish))) {

			}
		}
	}

	class OrderListAdapter extends CarBaseAdapter<Order> {

		public OrderListAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.order_item, null);
				viewHolder = new ViewHolder();
				viewHolder.textOrderSn = (TextView) convertView
						.findViewById(R.id.text_order_sn);
				viewHolder.textOrderState = (TextView) convertView
						.findViewById(R.id.text_order_state);
				viewHolder.textOrderTime = (TextView) convertView
						.findViewById(R.id.text_order_time);
				viewHolder.textOrderPayMoney = (TextView) convertView
						.findViewById(R.id.text_order_pay_money);

				viewHolder.listViewProduct = (ListView) convertView
						.findViewById(R.id.listview);
				viewHolder.textBtnCancel = (TextView) convertView
						.findViewById(R.id.text_order_cancel);
				viewHolder.textBtnOk = (TextView) convertView
						.findViewById(R.id.text_order_ok);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Order order = getDataList().get(position);
			viewHolder.textOrderSn.setText(order.getOrder());
			viewHolder.textOrderTime.setText(order.getCreate_time());
			viewHolder.textOrderPayMoney.setText(getString(
					R.string.order_price, order.getPay_price()));

			OrderProductAdapter opAdapter = new OrderProductAdapter(activity);
			opAdapter.setDataList(order.getProduct());
			viewHolder.listViewProduct.setAdapter(opAdapter);

			if (ORDER_STATUS_WAITING.equals(order.getStatus())) {// 待处理
				viewHolder.textOrderState
						.setText(getString(R.string.order_status_waiting));

				viewHolder.textBtnOk.setVisibility(View.VISIBLE);
				viewHolder.textBtnOk
						.setText(getString(R.string.order_btn_finish));
				viewHolder.textBtnCancel.setVisibility(View.VISIBLE);
				viewHolder.textBtnCancel
						.setText(getString(R.string.order_btn_cancel));
			} else if (ORDER_STATUS_ACCEPT.equals(order.getStatus())) {// 进行中
				viewHolder.textOrderState.setText("服务进行中");

				viewHolder.textBtnOk.setVisibility(View.VISIBLE);
				viewHolder.textBtnOk
						.setText(getString(R.string.order_btn_evaluate));
				viewHolder.textBtnCancel.setVisibility(View.VISIBLE);
				viewHolder.textBtnCancel
						.setText(getString(R.string.order_btn_delete));

			} else if (ORDER_STATUS_FINISH.equals(order.getStatus())) {// 已完成
				viewHolder.textOrderState
						.setText(getString(R.string.order_status_finish));

				viewHolder.textBtnOk.setVisibility(View.GONE);
				viewHolder.textBtnCancel.setVisibility(View.VISIBLE);
				viewHolder.textBtnCancel
						.setText(getString(R.string.order_btn_delete));
			} else if (ORDER_STATUS_CANCEL.equals(order.getStatus())) {// 已取消
				viewHolder.textOrderState
						.setText(getString(R.string.order_status_cancel));

				viewHolder.textBtnOk.setVisibility(View.GONE);
				viewHolder.textBtnCancel.setVisibility(View.VISIBLE);
				viewHolder.textBtnCancel
						.setText(getString(R.string.order_btn_delete));
			}
			viewHolder.textBtnCancel.setOnClickListener(OrderListActivity.this);
			viewHolder.textBtnOk.setOnClickListener(OrderListActivity.this);
			return convertView;
		}
	}

	class OrderProductAdapter extends CarBaseAdapter<OrderProduct> {

		public OrderProductAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderProduct viewHolderProduct;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.order_product_item,
						null);
				viewHolderProduct = new ViewHolderProduct();
				viewHolderProduct.textOrderProductName = (TextView) convertView
						.findViewById(R.id.text_order_product_name);
				viewHolderProduct.textOrderProductPrice = (TextView) convertView
						.findViewById(R.id.text_order_product_price);

				convertView.setTag(viewHolderProduct);

			} else {
				viewHolderProduct = (ViewHolderProduct) convertView.getTag();
			}
			OrderProduct op = getDataList().get(position);
			viewHolderProduct.textOrderProductName.setText(op.getName());
			viewHolderProduct.textOrderProductPrice.setText(getString(
					R.string.order_price, op.getPrice()));
			return convertView;
		}
	}

	class ViewHolder {
		TextView textOrderSn;
		TextView textOrderState;
		TextView textOrderTime;
		TextView textOrderPayMoney;
		ListView listViewProduct;
		TextView textBtnCancel;
		TextView textBtnOk;
	}

	class ViewHolderProduct {
		TextView textOrderProductName;
		TextView textOrderProductPrice;
	}

}
