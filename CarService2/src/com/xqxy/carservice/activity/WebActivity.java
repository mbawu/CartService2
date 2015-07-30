package com.xqxy.carservice.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.cn.hongwei.TopTitleView;
import com.xqxy.carservice.R;
import com.xqxy.person.NetworkAction;

/**
 * 用户协议、关于我们界面
 * @author Administrator
 *
 */
public class WebActivity extends BaseActivity {
	private TopTitleView topTitleView;
	private WebView webView;

	private String name;

	/**
	 * 界面初始化
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_layout);
		topTitleView = new TopTitleView(this);
		webView = (WebView) findViewById(R.id.webviw);
		name = getIntent().getStringExtra("name");

		if ("about".equals(name)) {
			topTitleView.setTitle(getString(R.string.sys_about));
		} else if ("agreement".equals(name)) {
			topTitleView.setTitle(getString(R.string.sys_service));
		}
		WebSettings settings = webView.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		RequestWrapper request = new RequestWrapper();
		request.setShowDialog(true);
		request.setName(name);
		sendDataByGet(request, NetworkAction.indexF_page);

	}

	/**
	 * 解析服务端数据，加载html
	 */
	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (NetworkAction.indexF_page == requestType) {
			String html = responseWrapper.getHtml();
			if (html != null) {
				webView.loadData(html, "text/html; charset=UTF-8", null);
			}
		}

	}
}
