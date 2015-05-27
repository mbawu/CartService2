package com.cn.hongwei;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xqxy.person.NetworkAction;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: jimmy Date: 8/9/13 Time: 1:52 PM To change
 * this template use File | Settings | File Templates.
 */
public class MyRequest extends Request<JSONObject> {
	private final Response.Listener<JSONObject> mListener;
	private final Map<String, String> mParams;
	private final NetworkAction requestType;

	public MyRequest(int method, String url, Map<String, String> params,NetworkAction requestType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
		mParams = params;
		this.requestType=requestType;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mParams;
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		mListener.onResponse(response);
	}

	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
		finally
		{
			//不管是否返回结果成功都将request集合中该请求移除
			MyApplication.client.removeRequest(this);
		}
	}

	public NetworkAction getRequestType() {
		return requestType;
	}

	@Override
	public void setTag(Object tag) {
		// TODO Auto-generated method stub
		super.setTag(tag);
	}

}
