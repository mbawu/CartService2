package com.xqxy.carservice.widget;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.Cst;
import com.xqxy.baseclass.JsonUtil;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.model.Cart;
import com.xqxy.model.ProductAttr;
import com.xqxy.person.CartActivity;

public class CartDialog extends Dialog {

	private Cart cart;
	TextView cart_name;
	TextView cart_real_price;
	// TextView cart_attrname;
	TextView cart_time;
	TextView cart_confirm;
	Context context;
	RadioGroup attrGroup;

	ProductAttr attrSelect;
	
	public CartDialog(final Context context, final Cart cart) {
		super(context, R.style.confirmDialog);
		this.setContentView(R.layout.cart_dialog);
		this.cart = cart;
		cart_name = (TextView) findViewById(R.id.cart_name);
		cart_real_price = (TextView) findViewById(R.id.cart_real_price);
		// cart_attrname=(TextView) findViewById(R.id.cart_attrname);
		cart_time = (TextView) findViewById(R.id.cart_time);
		cart_name.setText(cart.getName());
		cart_real_price.setText("￥" + cart.getReal_price());
		cart_time.setText("时长：" + cart.getTime() + "分钟");
		cart_confirm = (TextView) findViewById(R.id.cart_confirm);
		this.context = context;
		cart_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(cart.getPaid().equals(attrSelect.getId()))
				{
					dismiss();
				}
				else
				{
					RequestWrapper wrapper=new RequestWrapper();
					wrapper.setIdentity(MyApplication.identity);
					wrapper.setId(cart.getId());
					wrapper.setPaid(attrSelect.getId());
					sendData(wrapper, NetworkAction.cartF_update_cart);
				}
				

			}

		});
		attrGroup = (RadioGroup) findViewById(R.id.attr_group);
		getAttr(cart.getPid());
	}

	private void getAttr(String pid) {
		RequestWrapper wrapper = new RequestWrapper();
		wrapper.setPid(pid);
		sendDataByGet(wrapper, NetworkAction.indexF_product_attr);

	}

	public void showAttr(ResponseWrapper responseWrappe,NetworkAction requestType) {
		if(requestType==NetworkAction.indexF_product_attr)
		{
			attrGroup.removeAllViews();
			ArrayList<ProductAttr> attrs = responseWrappe.getAttr();
			for (int i = 0; i < attrs.size(); i++) {
				ProductAttr attr = attrs.get(i);
				RadioButton radioBtn = (RadioButton) getLayoutInflater()
						.inflate(R.layout.product_dretail_attr_item,
								null);
				radioBtn.setTag(attr);
				radioBtn.setId(i);
				radioBtn.setText(attr.getName());
				radioBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked)
						{
							attrSelect=(ProductAttr) ((RadioButton)buttonView).getTag();
							setAttr(attrSelect);
						}
						
					}
				});
				if (i == 0) {
					radioBtn.setChecked(true);
					setAttr(attr);
				} else {
					radioBtn.setChecked(false);
				}
				attrGroup.addView(radioBtn);
			}
		}
		else if(requestType==NetworkAction.cartF_update_cart)
		{
			((CartActivity)context).changeAttr();
			dismiss();
		}
		

	}

	private void setAttr(ProductAttr attr) {
		if (attr != null) {
			cart_real_price.setText(context.getString(R.string.product_price,
					attr.getReal_price()));
			cart_time.setText("时长：" + attr.getTime() + "分钟");
		}
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
						Toast.makeText(context, "访问服务器失败，请重试",
								Toast.LENGTH_SHORT).show();
						dismiss();
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
						Toast.makeText(context, "访问服务器失败，请重试",
								Toast.LENGTH_SHORT).show();
						dismiss();
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

}
