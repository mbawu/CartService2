package com.xqxy.person;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.pay.Pay;
import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;

import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.carservice.view.CarImageView;
import com.xqxy.model.Cart;
import com.xqxy.model.Category;
import com.xqxy.model.Coupon;
import com.xqxy.model.OrderProduct;
import com.xqxy.model.PayModel;
import com.xqxy.model.UserInfo;

public class OrderPayActivity extends BaseActivity implements
		OnCheckedChangeListener {

	public static int GET_COUPON = 1001;
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	private CartAdapter adapter;
	private ListView listView;
	private ArrayList<Cart> carts;
	private double total = 0.0;
	private double acTotal = 0.0;
	private double creditePrice = 0.0;
	private TextView priceTxt;
	private TextView priceAcTxt;
	private int payMethod = 1;// 1.支付宝客户端 2.支付宝网页 3. 线下支付
	private RadioButton alipayClient;
	private RadioButton alipayWeb;
	private RadioButton offline;
	private Button payBtn;
	private String oid;
	private LinearLayout couponLayout;
	private TextView couponSelect;
	private ArrayList<Coupon> datas;
	private CheckBox coupon;
	private CheckBox storecard;
	private CheckBox credite;
	private Coupon coupon2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_pay_layout);
		backImageView = (ImageView) findViewById(R.id.imageTopBack);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		titleTextView = (TextView) findViewById(R.id.textTopTitle);
		rightBtnTextView = (TextView) findViewById(R.id.textTopRightBtn);
		listView = (ListView) findViewById(R.id.listview);
		priceTxt = (TextView) findViewById(R.id.order_price_txt);
		priceAcTxt = (TextView) findViewById(R.id.order_total_price_txt);
		couponLayout = (LinearLayout) findViewById(R.id.order_coupon_layout);
		couponSelect = (TextView) findViewById(R.id.select_coupon);
		carts = (ArrayList<Cart>) getIntent().getSerializableExtra("data");
		oid = getIntent().getStringExtra("oid");
		setPrice();
		adapter = new CartAdapter(this);
		adapter.setDataList(carts);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		payBtn = (Button) findViewById(R.id.pay_btn);
		payBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payOrder();
			}

		});
		alipayClient = (RadioButton) findViewById(R.id.alipay_client);
		alipayWeb = (RadioButton) findViewById(R.id.alipay_web);
		offline = (RadioButton) findViewById(R.id.offline);
		coupon = (CheckBox) findViewById(R.id.coupon);
		storecard = (CheckBox) findViewById(R.id.storecard);
		credite = (CheckBox) findViewById(R.id.credite);
		alipayClient.setOnCheckedChangeListener(this);
		alipayWeb.setOnCheckedChangeListener(this);
		offline.setOnCheckedChangeListener(this);
		credite.setOnCheckedChangeListener(this);
		coupon.setOnCheckedChangeListener(this);
		storecard.setOnCheckedChangeListener(this);
		couponSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(OrderPayActivity.this, CouponActivity.class);
				intent.putExtra("select", "select");
				OrderPayActivity.this
						.startActivityForResult(intent, GET_COUPON);
			}
		});
		getCoupon();
		getCredite();
	}

	private void getCredite() {
		RequestWrapper request = new RequestWrapper();
		request.setIdentity(MyApplication.identity);
		sendData(request, NetworkAction.centerF_user);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == GET_COUPON) {
			coupon2 = (Coupon) data.getSerializableExtra("data");
			if (coupon2.getName().equals(""))
				coupon.setText("优惠券" + coupon2.getPrice() + "元");
			else
				coupon.setText(coupon2.getName() + coupon2.getPrice() + "元");

			getNewPrice();
		}
	}

	private void getNewPrice() {

		if (coupon2 != null) {
			if (coupon.isChecked())
				acTotal = total - Double.valueOf(coupon2.getPrice())
						- creditePrice;
			else
				acTotal = total - creditePrice;

		}
		if (acTotal > 0)
			priceAcTxt.setText("￥" + acTotal);
		else
			priceAcTxt.setText("￥0.0");
	}

	private void payOrder() {
		switch (payMethod) {
		// 支付宝客户端
		case 1:
			RequestWrapper wrapper = new RequestWrapper();
			wrapper.setShowDialog(true);
			sendDataByGet(wrapper, NetworkAction.indexF_pay_base);
			break;
		}

	}

	public void getCoupon() {
		RequestWrapper requestWrapper = new RequestWrapper();
		requestWrapper.setIdentity(MyApplication.identity);
		requestWrapper.setStatus("1");
		sendDataByGet(requestWrapper, NetworkAction.centerF_user_coupon);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.indexF_pay_base) {
			Pay pay = new Pay(this);
			if (payMethod == 1) {
				PayModel payModel = responseWrapper.getPay();
				// pay.setPARTNER(payModel.getPay_pid());
				// pay.setSELLER(payModel.getPay_name());
				// pay.setRSA_PRIVATE(payModel.getRsa_private_key());

				try {
					String subject = carts.get(0).getName();
					pay.alipay(carts.get(0).getName(),
							"{\"type\":\"2\",\"identity\":\""
									+ MyApplication.identity + "\",\"oid\":\""
									+ oid + "\"}", acTotal + "");
				} catch (Exception e) {
					Toast.makeText(this, "商家支付信息有误，请重试", Toast.LENGTH_SHORT)
							.show();
				}

			}
		} else if (requestType == null) {
			Toast.makeText(this, "获取信息失败，请重试", Toast.LENGTH_SHORT).show();
		} else if (requestType == NetworkAction.centerF_user_coupon) {
			datas = responseWrapper.getCoupon();
			if (datas.size() > 0) {
				coupon2 = datas.get(0);
				couponLayout.setVisibility(View.VISIBLE);
				coupon.setVisibility(View.VISIBLE);
				if (coupon2.getName().equals(""))
					coupon.setText("优惠券" + coupon2.getPrice() + "元");
				else
					coupon.setText(coupon2.getName() + coupon2.getPrice() + "元");
				getNewPrice();
			} else
				couponLayout.setVisibility(View.GONE);
		} else if (requestType == NetworkAction.centerF_user) {
			UserInfo user = responseWrapper.getUser();
			creditePrice = Double.valueOf(user.getIntegral()) / 10;
			credite.setText("可使用" + user.getIntegral() + "积分 （抵算￥"
					+ creditePrice + "元)");
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		getNewPrice();
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.alipay_client:
				payMethod = 1;
				break;
			case R.id.alipay_web:
				payMethod = 2;
				break;
			case R.id.offline:
				payMethod = 3;
				break;
			case R.id.credite:
				acTotal = acTotal - creditePrice;
				priceAcTxt.setText("￥" + acTotal);
				break;
			}
		}

	}

	private void setPrice() {
		for (int i = 0; i < carts.size(); i++) {
			Cart cart = carts.get(i);
			total = total + Double.valueOf(cart.getReal_price());
		}
		priceTxt.setText("￥" + total);
	}

	class CartAdapter extends CarBaseAdapter<Cart> {

		public CartAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder ViewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.pay_list_item, null);
				ViewHolder = new ViewHolder();
				ViewHolder.textOrderProductName = (TextView) convertView
						.findViewById(R.id.pay_product_name);
				ViewHolder.textOrderProductPrice = (TextView) convertView
						.findViewById(R.id.pay_product_price);
				ViewHolder.textOrderProductTime = (TextView) convertView
						.findViewById(R.id.pay_product_time);
				ViewHolder.imageView = (CarImageView) convertView
						.findViewById(R.id.img_pay);
				convertView.setTag(ViewHolder);

			} else {
				ViewHolder = (ViewHolder) convertView.getTag();
			}
			Cart op = getItem(position);
			ViewHolder.textOrderProductName.setText(op.getName());
			ViewHolder.textOrderProductPrice.setText(getString(
					R.string.order_price, op.getReal_price()));
			ViewHolder.textOrderProductTime.setText(getString(
					R.string.product_time, op.getTime()));
			if (op.getPic() != null && !"".equals(op.getPic())) {
				ViewHolder.imageView.loadImage(op.getPic());
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView textOrderProductName;
		TextView textOrderProductPrice;
		TextView textOrderProductTime;
		CarImageView imageView;
	}

}
