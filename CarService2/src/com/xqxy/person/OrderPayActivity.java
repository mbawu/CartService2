package com.xqxy.person;

import java.io.Serializable;
import java.text.DecimalFormat;
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

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.CarImageView;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.Pay;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.xqxy.carservice.R;

import com.xqxy.carservice.activity.OrderListActivity;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Address;
import com.xqxy.model.Cart;
import com.xqxy.model.Category;
import com.xqxy.model.Coupon;
import com.xqxy.model.OrderProduct;
import com.xqxy.model.PayModel;
import com.xqxy.model.PayType;
import com.xqxy.model.StoreCard;
import com.xqxy.model.UserInfo;
import com.xqxy.model.WebInfo;

public class OrderPayActivity extends BaseActivity implements
		OnCheckedChangeListener {

	public static int GET_COUPON = 1001;
	public static int GET_CLEANCARD = 1002;
	public static int GET_STORECARD = 1003;
	private ImageView backImageView;
	private TextView titleTextView;
	private TextView rightBtnTextView;

	private CartAdapter adapter;
	private ListView listView;
	private ArrayList<Cart> carts;
	private double total = 0.0;
	private double acTotal = 0.0;
	private double creditePrice = 0.0;
	private double creditePriceUse = 0.0;// 实际消耗的积分金额
	private double couponPriceUse = 0.0;// 实际消耗的优惠金额
	private double cleancardPriceUse = 0.0;
	private double storecardPriceUse = 0.0;
	private double storecardPriceTotal = 0.0;// 使用增值卡的服务的集合的总金额

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
	private TextView couponNum;
	private LinearLayout storecardLayout;
	private TextView storecardSelect;
	private TextView storecardNum;
	private LinearLayout clearcardLayout;
	private TextView clearcardSelect;
	private TextView clearcardNum;
	private View line1;
	private View line2;
	private View line3;
	private ArrayList<Coupon> datas;
	private ArrayList<StoreCard> cardDatas;
	private ArrayList<Coupon> couponData;
	private ArrayList<StoreCard> cleancardDatas;
	private ArrayList<StoreCard> storecardDatas;
	private CheckBox coupon;
	private CheckBox storecard_cb;
	private CheckBox cleancard_cb;
	private CheckBox credite;
	private Coupon coupon2 = null;
	private StoreCard cleanCard = null;
	private StoreCard storeCard = null;
	private Address address;
	private boolean couponB = false;
	private boolean storecardB = false;
	private boolean cleancardB = false;
	private boolean crediteB = false;
	private boolean offlineB = false;
	private Cart cleancart; // 使用储值卡的服务
	private ArrayList<Cart> storecardCarts;// 使用增值卡的服务集合

	private boolean step1 = true;// 支付第一步
	private Pay payClass;
	private int credtiIntegral = 10;

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

		carts = (ArrayList<Cart>) getIntent().getSerializableExtra("data");
		address = (Address) getIntent().getSerializableExtra("address");
		oid = getIntent().getStringExtra("oid");
		setPrice();
		payClass = new Pay(this, oid);
		cleancardDatas = new ArrayList<StoreCard>();
		storecardDatas = new ArrayList<StoreCard>();
		couponData = new ArrayList<Coupon>();
		storecardLayout = (LinearLayout) findViewById(R.id.order_storecard_layout);
		storecardSelect = (TextView) findViewById(R.id.select_storecard);
		storecardNum = (TextView) findViewById(R.id.storecard_num);
		clearcardLayout = (LinearLayout) findViewById(R.id.order_cleancard_layout);
		clearcardNum = (TextView) findViewById(R.id.cleancard_num);
		clearcardSelect = (TextView) findViewById(R.id.select_cleancard);
		couponLayout = (LinearLayout) findViewById(R.id.order_coupon_layout);
		couponSelect = (TextView) findViewById(R.id.select_coupon);
		couponNum = (TextView) findViewById(R.id.coupon_num);
		line1 = findViewById(R.id.coupon_line);
		line2 = findViewById(R.id.storecard_line);
		line3 = findViewById(R.id.cleancard_line);
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
		storecard_cb = (CheckBox) findViewById(R.id.storecard_cb);
		cleancard_cb = (CheckBox) findViewById(R.id.cleancard_cb);
		credite = (CheckBox) findViewById(R.id.credite);
		alipayClient.setOnCheckedChangeListener(this);
		alipayWeb.setOnCheckedChangeListener(this);
		offline.setOnCheckedChangeListener(this);
		credite.setOnCheckedChangeListener(this);
		coupon.setOnCheckedChangeListener(this);
		storecard_cb.setOnCheckedChangeListener(this);
		cleancard_cb.setOnCheckedChangeListener(this);
		couponSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(OrderPayActivity.this, CouponActivity.class);
				intent.putExtra("select", "select");
				intent.putExtra("data", (Serializable) couponData);
				OrderPayActivity.this
						.startActivityForResult(intent, GET_COUPON);
			}
		});

		clearcardSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(OrderPayActivity.this, StoreCardActivity.class);
				intent.putExtra("select", "1");
				intent.putExtra("data", (Serializable) cleancardDatas);
				OrderPayActivity.this.startActivityForResult(intent,
						GET_CLEANCARD);

			}
		});
		storecardSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(OrderPayActivity.this, StoreCardActivity.class);
				intent.putExtra("select", "2");
				intent.putExtra("data", (Serializable) storecardDatas);
				OrderPayActivity.this.startActivityForResult(intent,
						GET_STORECARD);

			}
		});
		getCoupon();
//		getCredite();
		getStoreCard();
		getWebIntegral();
	}

	private void getWebIntegral() {
		RequestWrapper requestWrapper = new RequestWrapper();

		sendDataByGet(requestWrapper, NetworkAction.indexF_web_base);
	}

	private void getStoreCard() {
		RequestWrapper requestWrapper = new RequestWrapper();
		requestWrapper.setIdentity(MyApplication.identity);
		sendDataByGet(requestWrapper, NetworkAction.centerF_user_card);

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
			if(coupon2!=null)
			{
				coupon.setChecked(false);
			}
			coupon2 = (Coupon) data.getSerializableExtra("data");
			coupon.setText(coupon2.getPrice() + "元" + coupon2.getName() + "优惠券");
			coupon.setChecked(true);
//			couponPriceUse=0.0;
		} else if (resultCode == GET_CLEANCARD) {
			cleanCard = (StoreCard) data.getSerializableExtra("data");
			cleancard_cb.setText(cleanCard.getName());
			cleancard_cb.setChecked(true);
		} else if (resultCode == GET_STORECARD) {
			storeCard = (StoreCard) data.getSerializableExtra("data");
			storecard_cb.setText(storeCard.getName() + "（可用余额"
					+ storeCard.getBalance() + "）");
			storecard_cb.setChecked(true);
		}
	}

	private void getNewPrice(Object ob) {

		// 选择优惠券以后
		if (ob instanceof Coupon) {
			coupon.setVisibility(View.VISIBLE);
			if (coupon.isChecked()) {
				if (offlineB) {
					couponB = false;
					coupon.setChecked(false);
					Toast.makeText(this, "线下支付不可使用积分和优惠券", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (storecardB || cleancardB) {
					couponB = false;
					coupon.setChecked(false);
					Toast.makeText(this, "储值卡和增值卡不可与积分和优惠券同时使用",
							Toast.LENGTH_SHORT).show();
					return;
				}
				couponB = true;
				// acTotal = acTotal - Double.valueOf(coupon2.getPrice());
				checkPrice(Double.valueOf(coupon2.getPrice()), PayType.优惠券,
						coupon.isChecked());
			} else {
				if (couponB) {
					couponB = false;
					checkPrice(Double.valueOf(coupon2.getPrice()), PayType.优惠券,
							coupon.isChecked());
				}
			}

		} else if (ob instanceof Double) {
			if (credite.isChecked()) {
				if (offlineB) {
					crediteB = false;
					credite.setChecked(false);
					Toast.makeText(this, "线下支付不可使用积分和优惠券", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (storecardB || cleancardB) {
					crediteB = false;
					credite.setChecked(false);
					Toast.makeText(this, "储值卡和增值卡不可与积分和优惠券同时使用",
							Toast.LENGTH_SHORT).show();
					return;
				}
				crediteB = true;
				checkPrice(creditePrice, PayType.积分, credite.isChecked());
			} else {
				if (crediteB) {
					crediteB = false;
					checkPrice(creditePrice, PayType.积分, credite.isChecked());
				}

			}
		} else if (ob instanceof StoreCard) {
			// cleanCard
			if (((StoreCard) ob).getFlag().equals("1")) {
				cleancard_cb.setVisibility(View.VISIBLE);
				if (cleancard_cb.isChecked()) {
					if (crediteB || couponB) {
						cleancardB = false;
						cleancard_cb.setChecked(false);
						Toast.makeText(this, "储值卡和增值卡不可与积分和优惠券同时使用",
								Toast.LENGTH_SHORT).show();
						return;
					}
					cleancardB = true;
					if (cleancart == null) {
						for (int i = 0; i < carts.size(); i++) {
							Cart cartTemp = carts.get(i);
							if (cartTemp.getPid().equals(cleanCard.getPid()))
								cleancart = cartTemp;
						}
					}
					checkPrice(Double.valueOf(cleancart.getReal_price()),
							PayType.储值卡, cleancard_cb.isChecked());
				} else {
					if (cleancardB) {
						cleancardB = false;
						checkPrice(Double.valueOf(cleancart.getReal_price()),
								PayType.储值卡, cleancard_cb.isChecked());
					}
				}
			}
			// storeCard
			else if (((StoreCard) ob).getFlag().equals("2")) {
				storecard_cb.setVisibility(View.VISIBLE);
				if (storecard_cb.isChecked()) {
					if (crediteB || couponB) {
						storecardB = false;
						storecard_cb.setChecked(false);
						Toast.makeText(this, "储值卡和增值卡不可与积分和优惠券同时使用",
								Toast.LENGTH_SHORT).show();
						return;
					}
					storecardB = true;
					if (storecardCarts == null) {
						storecardCarts = new ArrayList<Cart>();
						if (storeCard.getColid().equals("0")) {
							for (int i = 0; i < carts.size(); i++) {
								Cart cartTemp = carts.get(i);
								if (cleancart != null && cleancardB)
									continue;
								else {
									storecardCarts.add(cartTemp);
								}
							}
						} else {
							for (int i = 0; i < carts.size(); i++) {
								Cart cartTemp = carts.get(i);
								if (cartTemp.getPid()
										.equals(storeCard.getPid()))
									storecardCarts.add(cartTemp);
							}
						}
						for (int i = 0; i < storecardCarts.size(); i++) {
							Cart cartTemp = carts.get(i);
							storecardPriceTotal = storecardPriceTotal
									+ Double.valueOf(cartTemp.getReal_price());
						}

					}
					checkPrice(storecardPriceTotal, PayType.增值卡,
							storecard_cb.isChecked());
				} else {
					if (storecardB) {
						storecardB = false;
						checkPrice(storecardPriceTotal, PayType.增值卡,
								storecard_cb.isChecked());
					}
				}
			}
		}

	}

	private void checkPrice(double price, PayType type, boolean ischeck) {
		double temp = acTotal;
		if (type == PayType.积分) {

			if (ischeck) {
				if (acTotal <= 0) {
					Toast.makeText(this, "无需使用积分了", Toast.LENGTH_SHORT).show();
					credite.setChecked(false);
					return;
				}

				temp = acTotal - price;
				if (temp <= 0) {
					creditePriceUse = acTotal;
				} else {
					creditePriceUse = price;
				}
				acTotal = acTotal - price;
			} else {
				if (creditePriceUse == 0.0)
					return;
				acTotal = acTotal + price;
				creditePriceUse = 0.0;
			}
		} else if (type == PayType.优惠券) {

			if (ischeck) {
				if (acTotal <= 0) {
					Toast.makeText(this, "无需使用优惠券了", Toast.LENGTH_SHORT).show();
					coupon.setChecked(false);
					return;
				}

				temp = acTotal - price;
				if (temp <= 0) {
					couponPriceUse = acTotal;
				} else {
					couponPriceUse = price;
				}
				acTotal = acTotal - price;
			} else {
				if (couponPriceUse == 0.0)
					return;
				acTotal = acTotal + price;
				couponPriceUse = 0.0;
			}
		} else if (type == PayType.储值卡) {
			if (ischeck) {
				if (acTotal <= 0) {
					Toast.makeText(this, "无需使用储值卡了", Toast.LENGTH_SHORT).show();
					cleancard_cb.setChecked(false);
					cleancardB = false;
					return;
				}

				temp = acTotal - price;
				if (temp <= 0) {
					cleancardPriceUse = acTotal;
				} else {
					cleancardPriceUse = price;
				}
				// cleancardPriceUse = price;
				acTotal = acTotal - price;
			} else {
				if (cleancardPriceUse == 0.0)
					return;
				acTotal = acTotal + price;
				cleancardPriceUse = 0.0;
			}

		} else if (type == PayType.增值卡) {
			if (ischeck) {
				if (acTotal <= 0) {
					Toast.makeText(this, "无需使用增值卡了", Toast.LENGTH_SHORT).show();
					storecard_cb.setChecked(false);
					storecardB = false;
					return;
				}
				temp = acTotal - price;
				if (temp <= 0) {
					storecardPriceUse = acTotal;
				} else {
					storecardPriceUse = price;
				}
				// storecardPriceUse = price;
				acTotal = acTotal - price;
			} else {
				if (storecardPriceUse == 0.0)
					return;
				acTotal = acTotal + price;
				storecardPriceUse = 0.0;
			}

		}
		DecimalFormat    df   = new DecimalFormat("0.00");   
		if (acTotal > 0)
			priceAcTxt.setText("￥" + df.format(acTotal));
		else
			priceAcTxt.setText("￥" + "0.0");

		Log.i("test", "实际使用积分：" + creditePriceUse * credtiIntegral);
		Log.i("test", "实际优惠金额：" + couponPriceUse);
		Log.i("test", "实际储值卡扣减金额：" + cleancardPriceUse);
		Log.i("test", "实际增值卡扣减金额：" + storecardPriceUse);
	}

	private void payOrder() {
		if (step1) {
			RequestWrapper wrapper = new RequestWrapper();
			wrapper.setIdentity(MyApplication.identity);
			wrapper.setOid(oid);
			if (offlineB)
				wrapper.setPay_mode("2");
			else
				wrapper.setPay_mode("1");
			if (couponB) {
				wrapper.setCid(coupon2.getCid());
				wrapper.setCoupon_price(couponPriceUse + "");
			}
			if (cleancardB) {
				wrapper.setXc_ucid(cleanCard.getId());
				wrapper.setXc_card_price(cleancardPriceUse + "");
			}
			if (storecardB) {
				wrapper.setZz_ucid(storeCard.getId());
				wrapper.setZz_card_price(storecardPriceUse + "");
			}
			if (crediteB) {
				wrapper.setIntegral(creditePriceUse * credtiIntegral + "");
				wrapper.setIntegral_price(creditePriceUse + "");
			}
			sendData(wrapper, NetworkAction.orderF_pay_order);
			return;
		}

		Intent intent = new Intent();
		intent.setClass(this, OrderListActivity.class);
		if (offlineB) {
			Toast.makeText(this, "预约成功", Toast.LENGTH_SHORT).show();
			startActivity(intent);
			finish();
			return;
		}

		if (acTotal <= 0) {
			Toast.makeText(this, "预约成功", Toast.LENGTH_SHORT).show();
			startActivity(intent);
			finish();
			return;
		} else {
			if (couponB)
				Toast.makeText(this, "优惠券使用成功", Toast.LENGTH_SHORT).show();
			else if (cleancardB)
				Toast.makeText(this, "储值卡使用成功", Toast.LENGTH_SHORT).show();
			else if (storecardB)
				Toast.makeText(this, "增值卡使用成功", Toast.LENGTH_SHORT).show();
			switch (payMethod) {
			// 支付宝客户端
			case 1:
				RequestWrapper wrapper = new RequestWrapper();
				wrapper.setShowDialog(true);
				sendDataByGet(wrapper, NetworkAction.indexF_pay_base);
				break;
			}
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
			// Pay pay = new Pay(this,oid);
			if (payMethod == 1) {
				PayModel payModel = responseWrapper.getPay();
				payClass.setPARTNER(payModel.getPay_pid());
				payClass.setSELLER(payModel.getPay_name());
				// payClass.setRSA_PRIVATE(payModel.getRsa_private_key());
				payClass.setRSA_PRIVATE("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMkMCO2MoYYtn+l7PJ3GLJ+AiVUO5XYrtv8LH4eYMEaQBTsnTeS6s61MBZ/yXR7hnH+wOJqvFjGVTm8uz0KXCNIOWAZ88KRAubIGGxtwhwLcwMbvpb8+wvTSOdvFHiApVK9vJPmsgvCWybqqqYWtSEW9VyZEGWBwch8Js4yakqRdAgMBAAECgYBVrjVX16k239bY0FaC/uQhjcv5XgHYnMS+aOUlCmz4hYRVM2j048STRGTZR5b8BDaIDHfzJE8XDoSAybg2rttouKVapsHmLVDoPhtg48zX7ZPPOe+2hHxcIulRCN2ELshDW6BMe/9QilAqphRp3pyM9YrZCT52V2p8O4UurQzoWQJBAOXKXF2I32PzHgyj50MPwkrvFD9hrTzhJ8lvqz51TiEYde3OwjzW1bIKUCYg2tDjorCeJqlAH+8PDe+uYFROFkcCQQDf+mU2oFmTODgkeZPplillCZ7DtQG+1eXmCcg8d3Zad2p4RAyJpmF0f0iVcbLOshLC4oz+x0p152MZPMCcd247AkBrTWqCNubx2lYe2u6jzxkQOsH+stLdido1YxLY8JgSNkTjTlg/ZqaVI+G3XEIxpwqSZNdy00HWNPZyBMBwvaIDAkEAgbGffBM76zipoc1YrfDKxXvdmBuvCA8Z0aumbAUM3nO5jixxSh+y3N97azXsQS3yGTFQTZOe9UjoJEv+iFvL0wJAI8VbugyH56Cvud/vcYb/mdOO/cIyM7mhTYy5Gh6Klv9+SmZAQgVIQJxhAK+sGenlhRw9ex0Dwr9z3jqmeoVxkA==");
				try {
					String subject = carts.get(0).getName();
					payClass.alipay(carts.get(0).getName(),
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

			for (int i = 0; i < datas.size(); i++) {
				Coupon coupon = datas.get(i);
				if( coupon.getColoumnid().equals("0"))
				{
					couponData.add(coupon);
					continue;
				}
				
				
				for (int j = 0; j < carts.size(); j++) {
					Cart cart = carts.get(j);
					if (cart.getPid().equals(coupon.getPid())) {
						if (couponData.size() > 0) {
							for (int k = 0; k < couponData.size(); k++) {
								Coupon couponTemp = datas.get(k);
								if (!couponTemp.getCid()
										.equals(coupon.getCid()))
									couponData.add(coupon);
							}
						} else
							couponData.add(coupon);
					}
				}
			}

			if (couponData.size() > 0) {
				couponLayout.setVisibility(View.VISIBLE);
				couponNum.setText(getString(R.string.coupon_use,
						couponData.size() + ""));
			} else {
				couponLayout.setVisibility(View.GONE);
				line1.setVisibility(View.GONE);
			}
			// coupon2 = datas.get(0);
			// line1.setVisibility(View.VISIBLE);
			// couponLayout.setVisibility(View.VISIBLE);
			// coupon.setVisibility(View.VISIBLE);
			// if (coupon2.getName().equals(""))
			// coupon.setText("优惠券" + coupon2.getPrice() + "元");
			// else
			// coupon.setText(coupon2.getName() + coupon2.getPrice() + "元");
			// getNewPrice();

		} else if (requestType == NetworkAction.centerF_user) {
			UserInfo user = responseWrapper.getUser();
			creditePrice = Double.valueOf(user.getIntegral()) / credtiIntegral;
			credite.setText("可使用" + user.getIntegral() + "积分 （抵算￥"
					+ creditePrice + "元)");
		} else if (requestType == NetworkAction.indexF_web_base) {
			ArrayList<WebInfo> infoList=responseWrapper.getWeb();
			WebInfo info=infoList.get(0);
			credtiIntegral=Integer.valueOf(info.getWeb_integral());
			getCredite();
		} else if (requestType == NetworkAction.centerF_user_card) {
			cardDatas = responseWrapper.getCard();
			for (int i = 0; i < cardDatas.size(); i++) {
				StoreCard card = cardDatas.get(i);
				// 只判断有效的卡
				if (card.getState().equals("1")) {
					// 储值卡
					if (card.getFlag().equals("1")) {
						for (int j = 0; j < carts.size(); j++) {
							Cart cart = carts.get(j);
							// 储值卡的服务ID和购买的服务ID相符，并且地址上的车牌号和购买储值卡时候的车牌号要相符
							if (cart.getPid().equals(card.getPid())
									&& address.getCar_num().equals(
											card.getCar_num())) {

								if (cleancardDatas.size() > 1) {
									for (int k = 0; k < cleancardDatas.size(); k++) {
										StoreCard cardTemp = cleancardDatas
												.get(k);
										if (!cardTemp.getCid().equals(
												card.getCid())) {
											cleancardDatas.add(card);
										}
									}
								} else {
									cleancardDatas.add(card);
								}
							}
						}
					}
					// 增值卡
					else if (card.getFlag().equals("2")) {

						// 先判断是否是全场使用的
						if (card.getColid().equals("0")) {
							storecardDatas.add(card);
						} else {
							for (int j = 0; j < carts.size(); j++) {
								Cart cart = carts.get(j);
								if (cart.getPid().equals(card.getPid())) {
									if (storecardDatas.size() > 1) {
										for (int k = 0; k < storecardDatas
												.size(); k++) {
											StoreCard cardTemp = storecardDatas
													.get(k);
											if (!cardTemp.getCid().equals(
													card.getCid())) {
												storecardDatas.add(card);
											}
										}
									} else {
										storecardDatas.add(card);
									}
								}
							}
						}

					}
				}

			}

			if (cleancardDatas.size() > 0) {
				clearcardLayout.setVisibility(View.VISIBLE);
				clearcardNum.setText(getString(R.string.cleancard_use,
						cleancardDatas.size() + ""));
			} else {
				clearcardLayout.setVisibility(View.GONE);
				line3.setVisibility(View.GONE);
			}

			if (storecardDatas.size() > 0) {
				storecardLayout.setVisibility(View.VISIBLE);
				storecardNum.setText(getString(R.string.sotrecard_use,
						storecardDatas.size() + ""));
			} else {
				storecardLayout.setVisibility(View.GONE);
				line2.setVisibility(View.GONE);
			}
		} else if (requestType == NetworkAction.orderF_pay_order) {
			step1 = false;
			payOrder();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// getNewPrice();
		// if (isChecked) {
		switch (buttonView.getId()) {
		case R.id.alipay_client:
			payMethod = 1;
			break;
		case R.id.alipay_web:
			payMethod = 2;
			break;
		case R.id.offline:
			payMethod = 3;
			offlineB = isChecked;
			if (isChecked) {
				coupon.setChecked(false);
				credite.setChecked(false);
			}
			break;
		case R.id.credite:
			// acTotal = acTotal - creditePrice;
			// priceAcTxt.setText("￥" + acTotal);
			getNewPrice(creditePrice);
			break;
		case R.id.coupon:
			if (coupon2 != null)
				getNewPrice(coupon2);
			break;
		case R.id.storecard_cb:
			// if (storeCard != null)
			if (isChecked) {
				storecardB = true;
				if (couponB)
					coupon.setChecked(false);
				if (cleancardB)
					credite.setChecked(false);
			}
			getNewPrice(storeCard);
			break;
		case R.id.cleancard_cb:
			// if (cleanCard != null)
			if (isChecked) {
				cleancardB = true;
				if (couponB)
					coupon.setChecked(false);
				if (cleancardB)
					credite.setChecked(false);
			}
			getNewPrice(cleanCard);
			break;
		// }
		}

	}

	private void setPrice() {
		for (int i = 0; i < carts.size(); i++) {
			Cart cart = carts.get(i);
			total = total + Double.valueOf(cart.getReal_price());
		}
		acTotal = total;
		priceTxt.setText("￥" + total);
		priceAcTxt.setText("￥" + total);
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
