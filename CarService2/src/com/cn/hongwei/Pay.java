package com.cn.hongwei;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.xqxy.carservice.activity.OrderListActivity;
import com.xqxy.person.Cst;
import com.xqxy.person.NetworkAction;

public class Pay {

	private Activity activity;
	private String oid;
	public Pay(Activity activity) {
		this.activity = activity;
	}
	
	public Pay(Activity activity,String oid) {
		this.activity = activity;
		this.oid=oid;
	}

	private  String PARTNER = "";
	private String SELLER = "";
	private String RSA_PRIVATE = "";
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	
	
	private static String mMode = "01";// 设置测试模式:01为测试环境00为正式环境
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					
					Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(activity, OrderListActivity.class);
					activity.startActivity(intent);
//					Intent mIntent = new Intent(Constants.APP_BORADCASTRECEIVER); 
//					activity.sendBroadcast(mIntent); 
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”
					// 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(activity, "支付结果确认中", Toast.LENGTH_SHORT)
								.show();

					} else {
						Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT)
								.show();
						RequestWrapper wrapper=new RequestWrapper();
						wrapper.setIdentity(MyApplication.identity);
						wrapper.setOid(oid);
						sendData(wrapper, NetworkAction.orderF_pay_defeated);
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(activity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT)
						.show();
				break;
			}
			default:
				break;
			}
		};
	};

	
	
	public String getPARTNER() {
		return PARTNER;
	}

	public void setPARTNER(String pARTNER) {
		PARTNER = pARTNER;
	}

	public String getSELLER() {
		return SELLER;
	}

	public void setSELLER(String sELLER) {
		SELLER = sELLER;
	}

	public String getRSA_PRIVATE() {
		return RSA_PRIVATE;
	}

	public void setRSA_PRIVATE(String rSA_PRIVATE) {
		RSA_PRIVATE = rSA_PRIVATE;
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void alipay(String subject, String body, String price) {
		String orderInfo = getOrderInfo(subject, body, price);
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check() {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask payTask = new PayTask(activity);
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(activity);
		String version = payTask.getVersion();
		Toast.makeText(activity, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\""+"123456" + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://car.xinlingmingdeng.com/api.php/pay/index"
				+ "\"";
//		Log.i("test","notify_url---->"+"&notify_url=" + "\"" + "http://"+Constants.HOST+"/mobile/api/payment/alipayAndroid/notify_url.php"
//				+ "\"");
//		orderInfo += "&notify_url=" + "\"" + Constants.URL_ORDER_PAYMENT+"&key="+myapp.getLoginKey()+"&pay_sn="+subject
//				+ "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	
	
	
	
	
	
	
	
	public void sendData(RequestWrapper requestWrapper,
			final NetworkAction requestType) {
		String url = Cst.HOST;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		MyApplication.client.postWithURL(url, getMap(requestWrapper),requestType, 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						onResponseEvent(response, requestType);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i(Cst.TAG, "Volley error:" + error.toString());
						Toast.makeText(activity, "访问服务器失败，请重试",
								Toast.LENGTH_SHORT).show();
					}
				});
	}
	
	/**
	 * get请求方式
	 * 
	 * @param requestWrapper
	 * @param requestType
	 */
	public void sendDataByGet(RequestWrapper requestWrapper,
			final NetworkAction requestType) {
		String url = Cst.HOST;
		HashMap<String, String> paramMap = new HashMap<String, String>();
		MyApplication.client.getWithURL(url, getMap(requestWrapper),
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						onResponseEvent(response, requestType);
					}
				}, requestType, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i(Cst.TAG, "Volley error:" + error.toString());
						Toast.makeText(activity, "访问服务器失败，请重试",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	public static HashMap<String, String> getMap(Object thisObj) {
		HashMap<String, String> map = new HashMap<String, String>();
		Class c;
		try {
			c = Class.forName(thisObj.getClass().getName());
			Method[] m = c.getMethods();
			Field[] fileds = c.getFields();
			for (int i = 0; i < m.length; i++) {
				String method = m[i].getName();
				if (method.startsWith("get")) {
					try {
						Object value = m[i].invoke(thisObj);
						if (value != null) {

							String key = method.substring(3);
							key = key.substring(0, 1).toLowerCase()
									+ key.substring(1);
							if (key.equals("class"))
								continue;
							if (value instanceof String) {
								map.put(key, value.toString());
							} else if (value instanceof Map) {
								Map<String, String> valueMap = (Map<String, String>) value;
								Set<String> keySet = valueMap.keySet();
								for (String k : keySet) {
									map.put(k, valueMap.get(k));
								}
							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("error:" + method);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	public void onResponseEvent(JSONObject response, NetworkAction requestType) {

		boolean done = false;
		String msg = "";
		Log.i("response", response.toString());
		try {
			done = response.getBoolean("done");
			msg = response.getString("msg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (done) {
			ResponseWrapper responseWrappe = jsonToClass(response.toString());
			showAttr(responseWrappe,requestType);
		}
		else {
//			dismiss();
		}

	}

	public ResponseWrapper jsonToClass(String json) {
		return JsonUtil.fromJson(json, ResponseWrapper.class);
	}
	public void showAttr(ResponseWrapper responseWrappe,NetworkAction requestType) {
		if(requestType==NetworkAction.orderF_pay_defeated)
		{
			
		}
	}
}
