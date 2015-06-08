package com.xqxy.carservice.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.cn.hongwei.TopTitleView;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Order;
import com.xqxy.model.OrderProduct;
import com.xqxy.person.NetworkAction;

public class OrderListActivity extends BaseActivity implements
		View.OnClickListener {
	private TopTitleView topTitleView;
	private RadioGroup radioGroupType;
	private TextView nodata;
	private ListView listView;
	private OrderListAdapter adapter;
	private List<Order> orderList;
	public static boolean EVALUATE_SUCCESS = false;
	private String page = "1";
	private String status = "0";
	private static final String ORDER_STATUS_ACCEPT = "1"; // 等待服务
	private static final String ORDER_STATUS_WAITING = "2"; // 等待服务
	private static final String ORDER_STATUS_FINISH = "3"; // 已完成
	private static final String ORDER_STATUS_CANCEL = "4"; // 已取消

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list_layout);
		topTitleView = new TopTitleView(this);
		nodata = (TextView) findViewById(R.id.nodataTxt);
		listView = (ListView) findViewById(R.id.listview);
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
							status = ORDER_STATUS_WAITING;
							break;
						case R.id.order_list_cancel_radiobtn:
							status = ORDER_STATUS_CANCEL;
							break;
						}
						getOrder();
					}
				});

		adapter = new OrderListAdapter(this);
		listView.setAdapter(adapter);

		getOrder();
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		if(EVALUATE_SUCCESS){
			EVALUATE_SUCCESS = false;
			getOrder();
		}
	}
	
	private void getOrder() {
		RequestWrapper request = new RequestWrapper();
		request.setShowDialog(true);
		request.setIdentity(MyApplication.identity);
		request.setStatus(status);
		// request.setPage(page);
		sendDataByGet(request, NetworkAction.centerF_user_order);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.centerF_user_order) {
			orderList = responseWrapper.getOrder();
			if (orderList != null && orderList.size() > 0) {
				nodata.setVisibility(View.GONE);
			} else {
				nodata.setVisibility(View.VISIBLE);
			}
			adapter.setDataList(orderList);
			adapter.notifyDataSetChanged();
		} else if (requestType == NetworkAction.centerF_cancel_order
				|| requestType == NetworkAction.centerF_del_order
				|| requestType == NetworkAction.centerF_affirm_order) {
			getOrder();
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
			String btnTxt = ((TextView) v).getText().toString();
			Order order = (Order) v.getTag();

			final RequestWrapper request = new RequestWrapper();
			request.setIdentity(MyApplication.identity);
			request.setOid(order.getOid());

			if (btnTxt.equals(getString(R.string.order_btn_cancel))) {

				String serviceTime = order.getServer_time();
				if (serviceTime != null && !"".equals(serviceTime)) {
					try {
						Calendar c = Calendar.getInstance();
						c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.parse(serviceTime));
						long tt = c.getTimeInMillis();
						if ((tt - System.currentTimeMillis()) < 60 * 60 * 1000) {
							ConfirmDialog dlg = new ConfirmDialog(this);
							dlg.setTitle("提示");
							if ("1".equals(order.getPay_mode())) {
								dlg.setMessage("离订单服务时间已不足1小时，此时取消订单将收取10%的违约费.确定取消吗？");
							} else {
								dlg.setMessage("离订单服务时间已不足1小时，此时取消订单将会记录您不良使用记录1次.确定取消吗？");
							}

							dlg.setPositiveButton("取消订单",
									new ConfirmDialog.OnClickListener() {

										@Override
										public void onClick(Dialog dialog,
												View view) {
											sendData(
													request,
													NetworkAction.centerF_cancel_order);
										}
									});

							dlg.setNegativeButton("暂不取消",
									new ConfirmDialog.OnClickListener() {

										@Override
										public void onClick(Dialog dialog,
												View view) {

										}
									});
							dlg.show();
						} else {

							ConfirmDialog dlg = new ConfirmDialog(this);
							dlg.setTitle("提示");
							dlg.setMessage("该操作不可恢复，确定取消吗？");
							dlg.setPositiveButton("取消订单",
									new ConfirmDialog.OnClickListener() {

										@Override
										public void onClick(Dialog dialog,
												View view) {
											sendData(
													request,
													NetworkAction.centerF_cancel_order);
										}
									});

							dlg.setNegativeButton("暂不取消",
									new ConfirmDialog.OnClickListener() {

										@Override
										public void onClick(Dialog dialog,
												View view) {

										}
									});
							dlg.show();
						}
					} catch (Exception e) {
						sendData(request, NetworkAction.centerF_cancel_order);
					}
				} else {
					sendData(request, NetworkAction.centerF_cancel_order);
				}

			} else if (btnTxt.equals(getString(R.string.order_btn_delete))) {

				ConfirmDialog dlg = new ConfirmDialog(this);
				dlg.setTitle("提示");
				dlg.setMessage("该操作不可恢复，确定删除吗？");
				dlg.setPositiveButton("删除",
						new ConfirmDialog.OnClickListener() {

							@Override
							public void onClick(Dialog dialog, View view) {
								sendData(request,
										NetworkAction.centerF_del_order);
							}
						});

				dlg.setNegativeButton("暂不删除",
						new ConfirmDialog.OnClickListener() {

							@Override
							public void onClick(Dialog dialog, View view) {

							}
						});
				dlg.show();

			} else if (btnTxt.equals(getString(R.string.order_btn_finish))) {

				ConfirmDialog dlg = new ConfirmDialog(this);
				dlg.setTitle("提示");
				dlg.setMessage("请确认您已完成了服务，确定确认吗？");
				dlg.setPositiveButton("确认",
						new ConfirmDialog.OnClickListener() {

							@Override
							public void onClick(Dialog dialog, View view) {
								sendData(request,
										NetworkAction.centerF_affirm_order);
							}
						});

				dlg.setNegativeButton("暂不确认",
						new ConfirmDialog.OnClickListener() {

							@Override
							public void onClick(Dialog dialog, View view) {

							}
						});
				dlg.show();

			} else if (btnTxt.equals(getString(R.string.order_btn_evaluate))) {

				Intent intent = new Intent(this, OrderEvaluateActivity.class);
				intent.putExtra("oid", order.getOid());
				intent.putExtra("product", (Serializable) order.getProduct());
				startActivity(intent);
				return;
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
			viewHolder.textOrderTime.setText(order.getServer_time());
			viewHolder.textOrderPayMoney.setText(getString(
					R.string.order_price, order.getPay_price()));

			OrderProductAdapter opAdapter = new OrderProductAdapter(activity);
			opAdapter.setDataList(order.getProduct());
			viewHolder.listViewProduct.setAdapter(opAdapter);

			/*
			 * if ("1".equals(order.getStatus())) {// 待处理
			 * viewHolder.textOrderState
			 * .setText(getString(R.string.order_status_waiting));
			 * 
			 * viewHolder.textBtnOk.setVisibility(View.VISIBLE);
			 * viewHolder.textBtnOk
			 * .setText(getString(R.string.order_btn_evaluate));
			 * viewHolder.textBtnCancel.setVisibility(View.GONE);
			 * viewHolder.textBtnCancel
			 * .setText(getString(R.string.order_btn_cancel)); } else
			 */

			if (ORDER_STATUS_ACCEPT.equals(order.getStatus())
					|| ORDER_STATUS_WAITING.equals(order.getStatus())) {// ---等待服务
				viewHolder.textOrderState
						.setText(R.string.order_status_waiting);

				viewHolder.textBtnOk.setVisibility(View.VISIBLE);
				viewHolder.textBtnOk
						.setText(getString(R.string.order_btn_finish));// 确认完成

				viewHolder.textBtnCancel.setVisibility(View.VISIBLE);
				viewHolder.textBtnCancel
						.setText(getString(R.string.order_btn_cancel));// 取消订单

				String serviceTime = order.getServer_time();
				if (serviceTime != null && !"".equals(serviceTime)) {
					try {
						Calendar c = Calendar.getInstance();
						c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.parse(serviceTime));
						long tt = c.getTimeInMillis();
						if ((tt - System.currentTimeMillis()) < 0) {
							viewHolder.textBtnCancel.setVisibility(View.GONE);
						}
					} catch (Exception e) {

					}
				}

			} else if (ORDER_STATUS_FINISH.equals(order.getStatus())) {// ---服务完成
				viewHolder.textOrderState
						.setText(getString(R.string.order_status_finish));

				viewHolder.textBtnOk
						.setText(getString(R.string.order_btn_evaluate));// 评价

				if ("0".equals(order.getAppnum())) {
					viewHolder.textBtnOk.setVisibility(View.VISIBLE);
				} else {
					viewHolder.textBtnOk.setVisibility(View.GONE);
				}

				viewHolder.textBtnCancel.setVisibility(View.VISIBLE);
				viewHolder.textBtnCancel
						.setText(getString(R.string.order_btn_delete));// 删除

			} else if (ORDER_STATUS_CANCEL.equals(order.getStatus())) {// ---已取消
				viewHolder.textOrderState
						.setText(getString(R.string.order_status_cancel));

				viewHolder.textBtnOk.setVisibility(View.GONE);
				viewHolder.textBtnCancel.setVisibility(View.VISIBLE);
				viewHolder.textBtnCancel
						.setText(getString(R.string.order_btn_delete));// 删除
			} else {
				viewHolder.textBtnOk.setVisibility(View.GONE);
				viewHolder.textBtnCancel.setVisibility(View.GONE);
			}
			viewHolder.textBtnCancel.setOnClickListener(OrderListActivity.this);
			viewHolder.textBtnOk.setOnClickListener(OrderListActivity.this);
			viewHolder.textBtnCancel.setTag(order);
			viewHolder.textBtnOk.setTag(order);
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
