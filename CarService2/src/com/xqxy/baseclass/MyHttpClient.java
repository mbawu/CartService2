package com.xqxy.baseclass;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class MyHttpClient extends Object {
	private RequestQueue mRequestQueue; // 网络请求的请求队列，谷歌封装好的。
	private ArrayList<MyRequest> requests;// 记录所有正在队列中但是还未返回结果的请求
	private static MyHttpClient client;

	public static MyHttpClient getInstance(Context con) {
		if (client == null) {
			client = new MyHttpClient(con);
			return client;
		}
		return client;
	}

	private MyHttpClient(Context ctx) {
		super();
		mRequestQueue = Volley.newRequestQueue(ctx);// 队列的初始化
		requests = new ArrayList<MyRequest>();
	}

	public ArrayList<MyRequest> getRequests() {
		return requests;
	}

	public void setRequests(ArrayList<MyRequest> requests) {
		this.requests = requests;
	}

	/**
	 * 从请求集合中移除已经返回结果的请求
	 * 
	 * @param req
	 */
	public void removeRequest(MyRequest req) {
		if (requests.contains(req))
			requests.remove(req);
	}

	/**
	 * 检测是否有还未返回结果的同一类型的请求
	 * 
	 * @param req
	 */
	public void isExsit(MyRequest req) {
		boolean exsit = false;
		for (int i = 0; i < requests.size(); i++) {
			// 检测队列中还未返回结果的请求是否和新的请求类型一样
			if (requests.get(i).getRequestType() == req.getRequestType()) {
				requests.remove(i);
				exsit = true;
			}
		}
		// 如果请求类型一样的话将这一类型的请求全部取消再把新的请求加入队列中
		if (exsit)
			MyApplication.client.mRequestQueue.cancelAll(req.getTag());
	}

	public void addRequest(MyRequest req) { // 将请求加入谷歌写好的队列中，谷歌会自动执行网络操作，并将返回的数据回调给请求的回调接口onResponse。
		isExsit(req);
		requests.add(req);
		mRequestQueue.add(req);
	}

	/**
	 * 将Map的参数转换为正确的网址数据格式
	 */
	private String encodeParameters(Map<String, String> params,
			String paramsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(),
						paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(),
						paramsEncoding));
				encodedParams.append('&');
			}
			return encodedParams.toString();
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: "
					+ paramsEncoding, uee);
		}
	}

	/**
	 * 向服务器发送数据使用get方式
	 * 
	 * @param url
	 * @param params
	 * @param listener
	 * @param requestType
	 * @param errorListener
	 */
	public void getWithURL(String url, Map<String, String> params,
			Response.Listener<JSONObject> listener, NetworkAction requestType,
			Response.ErrorListener errorListener) {
		url += requestType;
		if (params != null && params.size() > 0) {
			if (url.contains("?")) {
				url = url + "&" + encodeParameters(params, "UTF-8");
			} else {
				url = url + "?" + encodeParameters(params, "UTF-8");
			}
		}
		Log.i(Cst.TAG, "Url-->" + url);
		MyRequest jsObjRequest = new MyRequest(Request.Method.GET, url, params,
				requestType, listener, errorListener);
		jsObjRequest.setTag(requestType);
		this.addRequest(jsObjRequest);
	}

	/**
	 * 向服务器发送数据，固定封装client信息
	 * 
	 * @param request
	 * @param requestType
	 * @param listener
	 * @param errorListener
	 */
	public void postWithURL(String url, HashMap<String, String> paramMap,
			NetworkAction requestType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		// Hashtable<String, String> paramMap = new Hashtable<String, String>();
		// //终端固定信息
		// paramMap.put("appKey", Cst.appkey);
		// paramMap.put("method", requestType.toString());
		// Log.i(Cst.TAG, "Method->"+requestType.toString());
		// paramMap.put("v", "1");
		// paramMap.put("format", "json");
		// //终端信息
		// AppClient client=new AppClient();
		// client.setAppName(AppInfo.getPackageName());
		// client.setAppVersion(AppInfo.getVersion());
		// client.setSn(AppInfo.getSn());
		// client.setMac(AppInfo.getMac());
		// client.setTermType(AppInfo.getTerminalType());
		// client.setAppType("tv");
		// request.setClient(client);
		// paramMap.put("requestBody", JsonUtil.toJson(request));
		// //加密
		// String sign = RopUtils.sign(paramMap, null, Cst.secret);
		// paramMap.put("sign", sign);
		// Log.i(Cst.TAG, "Method->"+MyApplication.getUrl(paramMap,
		// Cst.testurl));
		// 向服务器发数据
		Log.i(Cst.TAG,
				MyApplication.getUrl(paramMap, url + requestType.toString()));
		MyRequest jsObjRequest = new MyRequest(Request.Method.POST, url
				+ requestType.toString(), paramMap, requestType, listener,
				errorListener);
		jsObjRequest.setTag(requestType);
		this.addRequest(jsObjRequest);
	}

	/**
	 * 获取当前终端的网络请求队列
	 * 
	 * @return
	 */
	public RequestQueue getmRequestQueue() {
		return mRequestQueue;
	}

	/**
	 * 设置当前终端的网络请求队列
	 * 
	 * @param mRequestQueue
	 */
	public void setmRequestQueue(RequestQueue mRequestQueue) {
		this.mRequestQueue = mRequestQueue;
	}

}