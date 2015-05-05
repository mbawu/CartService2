package com.xqxy.carservice.activity;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarInfoAdapter;
import com.xqxy.model.Brand;
import com.xqxy.model.Car;
import com.xqxy.model.Model;
import com.xqxy.model.Series;

public class CarActivity extends BaseActivity implements OnClickListener,
		OnItemSelectedListener {

	private Spinner brandSpinner;
	private CarInfoAdapter brandAdapter;
	private Spinner seriesSpinner;
	private CarInfoAdapter seriesAdapter;
	private Spinner modelSpinner;
	private CarInfoAdapter modelAdapter;

	private EditText textViewLC;
	private EditText textViewBYPL;

	private MyApplication app;

	private Car car = new Car();

	private String pid = "";

	private int countRequest = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_add);
		app = (MyApplication) getApplication();

		brandSpinner = (Spinner) findViewById(R.id.rst_brand);
		seriesSpinner = (Spinner) findViewById(R.id.rst_series);
		modelSpinner = (Spinner) findViewById(R.id.rst_model);
		textViewLC = (EditText) findViewById(R.id.edit_car_lc);
		textViewBYPL = (EditText) findViewById(R.id.edit_car_bypl);
		brandSpinner.setOnItemSelectedListener(this);
		seriesSpinner.setOnItemSelectedListener(this);
		modelSpinner.setOnItemSelectedListener(this);

		findViewById(R.id.btn_car_save).setOnClickListener(this);

		pid = getIntent().getStringExtra("pid");
		Serializable obj = getIntent().getSerializableExtra("car");
		if (obj != null) {
			car = (Car) obj;
			textViewLC.setText(car.getJourney());
			textViewBYPL.setText(car.getUpkeep());
		}
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_brand);
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_series);
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_model);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.carF_brand) {
			brandAdapter = new CarInfoAdapter(this, NetworkAction.carF_brand,
					responseWrapper.getBrand());
			brandSpinner.setAdapter(brandAdapter);
			brandAdapter.notifyDataSetChanged();
			countRequest++;
			if (car.getId() != null && responseWrapper.getBrand() != null) {
				for (int i = 0; i < responseWrapper.getBrand().size(); i++) {
					if (car.getBid().equals(
							responseWrapper.getBrand().get(i).getBid())) {
						brandSpinner.setSelection(i);
						break;
					}
				}
			}
		} else if (requestType == NetworkAction.carF_series) {
			seriesAdapter = new CarInfoAdapter(this, NetworkAction.carF_series,
					responseWrapper.getSeries());
			seriesSpinner.setAdapter(seriesAdapter);
			seriesAdapter.notifyDataSetChanged();
			countRequest++;
			if (car.getId() != null && responseWrapper.getSeries() != null) {
				for (int i = 0; i < responseWrapper.getSeries().size(); i++) {
					if (car.getSid().equals(
							responseWrapper.getSeries().get(i).getSid())) {
						seriesSpinner.setSelection(i);
						break;
					}
				}
			}
		} else if (requestType == NetworkAction.carF_model) {
			modelAdapter = new CarInfoAdapter(this, NetworkAction.carF_model,
					responseWrapper.getModel());
			modelSpinner.setAdapter(modelAdapter);
			modelAdapter.notifyDataSetChanged();
			countRequest++;
			if (car.getId() != null && responseWrapper.getBrand() != null) {
				for (int i = 0; i < responseWrapper.getModel().size(); i++) {
					if (car.getMid().equals(
							responseWrapper.getModel().get(i).getMid())) {
						modelSpinner.setSelection(i);
						break;
					}
				}
			}
		} else if (requestType.equals(NetworkAction.centerF_add_car)) {
			if (pid == null || "".equals(pid)) {// 返回爱车列表
				this.finish();
			} else {

				car.setId("0000");
				goServiceDetaile();
			}
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Object object = parent.getItemAtPosition(position);
		if (object instanceof Brand) {
			car.setBid(((Brand) object).getBid());
			car.setName(((Brand) object).getName());
		} else if (object instanceof Series) {
			car.setSid(((Series) object).getSid());
			car.setSname(((Series) object).getSname());

		} else if (object instanceof Model) {
			car.setMid(((Model) object).getMid());
			car.setMname(((Model) object).getMname());

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onClick(View v) {
		String lc = textViewLC.getText().toString();
		String bypl = textViewBYPL.getText().toString();

		RequestWrapper request = new RequestWrapper();
		if (car.getId() != null) {
			request.setId(car.getId());
		}
		request.setBid(car.getBid());
		request.setSid(car.getSid());
		request.setMid(car.getMid());
		request.setJourney(lc);
		request.setUpkeep(bypl);
		car.setJourney(lc);
		car.setUpkeep(bypl);
		if (pid == null || "".equals(pid)) {// 登陆后增加爱车
			request.setIdentity(MyApplication.identity);
			sendData(request, NetworkAction.centerF_add_car);
		} else {// 从服务列表跳转而来，目的增加爱车后打开详情页
			if (!MyApplication.loginStat) {// 未登录，在本地保存

				goServiceDetaile();
			} else {// 已登录,提交服务器
				request.setIdentity(MyApplication.identity);
				sendData(request, NetworkAction.centerF_add_car);
			}
		}

	}

	private void goServiceDetaile() {
		app.setCar(car);
		Intent intent = new Intent(this, ServiceDetailActivity.class);
		intent.putExtra("pid", pid);
		intent.putExtra("flag", "1");
		this.startActivity(intent);
		finish();
	}
}
