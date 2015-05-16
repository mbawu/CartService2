package com.xqxy.carservice.activity;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarInfoAdapter;
import com.xqxy.carservice.view.TopTitleView;
import com.xqxy.model.Brand;
import com.xqxy.model.Car;
import com.xqxy.model.Journey;
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

	private Spinner lcSpinner;
	private CarInfoAdapter lcAdapter;
	private Spinner byplSpinner;

	private MyApplication app;
	private Dialog myProgressDialog;
	private Car car = new Car();

	private String pid = "";

	private int countRequest = 0;

	private String[] upkeeps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_add);
		app = (MyApplication) getApplication();
		new TopTitleView(this);
		brandSpinner = (Spinner) findViewById(R.id.rst_brand);
		seriesSpinner = (Spinner) findViewById(R.id.rst_series);
		modelSpinner = (Spinner) findViewById(R.id.rst_model);
		lcSpinner = (Spinner) findViewById(R.id.rst_lc);
		byplSpinner = (Spinner) findViewById(R.id.rst_bypl);
		brandSpinner.setOnItemSelectedListener(this);
		seriesSpinner.setOnItemSelectedListener(this);
		modelSpinner.setOnItemSelectedListener(this);
		lcSpinner.setOnItemSelectedListener(this);
		byplSpinner.setOnItemSelectedListener(this);

		findViewById(R.id.btn_car_save).setOnClickListener(this);

		pid = getIntent().getStringExtra("pid");
		Serializable obj = getIntent().getSerializableExtra("car");
		if (obj != null) {
			car = (Car) obj;
		}
		myProgressDialog = createDialog();
		myProgressDialog.show();
		respCount = 0;

		upkeeps = getResources().getStringArray(R.array.upkeep);

		sendDataByGet(new RequestWrapper(), NetworkAction.carF_brand);
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_series);
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_model);
		sendDataByGet(new RequestWrapper(), NetworkAction.indexF_journey);
		byplSpinner.setAdapter(new ArrayAdapter<String>(this,
				R.layout.car_info_adapter, R.id.textView, upkeeps));

		if (car.getId() != null && car.getUpkeep() != null
				&& !"".equals(car.getUpkeep())) {
			try {
				int posi = Integer.parseInt(car.getUpkeep());
				if (posi <= upkeeps.length) {
					byplSpinner.setSelection(posi - 1);
				}
			} catch (NumberFormatException e) {

				e.printStackTrace();
			}
		}
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);

		if (requestType == (NetworkAction.centerF_add_car)) {
			if (pid == null || "".equals(pid)) {// 返回爱车列表
				this.setResult(RESULT_OK);
				this.finish();
			} else {
				car.setId("0000");
				goServiceDetaile();
			}
		} else {
			respCount++;
			if (requestType == NetworkAction.carF_brand) {
				brandAdapter = new CarInfoAdapter(this,
						NetworkAction.carF_brand, responseWrapper.getBrand());
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

				seriesAdapter = new CarInfoAdapter(this,
						NetworkAction.carF_series, responseWrapper.getSeries());
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
				modelAdapter = new CarInfoAdapter(this,
						NetworkAction.carF_model, responseWrapper.getModel());
				modelSpinner.setAdapter(modelAdapter);
				modelAdapter.notifyDataSetChanged();
				countRequest++;
				if (car.getId() != null && responseWrapper.getModel() != null) {
					for (int i = 0; i < responseWrapper.getModel().size(); i++) {
						if (car.getMid().equals(
								responseWrapper.getModel().get(i).getMid())) {
							modelSpinner.setSelection(i);
							break;
						}
					}
				}
			} else if (requestType == NetworkAction.indexF_journey) {
				ArrayList<Journey> journeys = responseWrapper.getJourney();
				lcAdapter = new CarInfoAdapter(this,
						NetworkAction.indexF_journey, journeys);
				lcSpinner.setAdapter(lcAdapter);
				lcAdapter.notifyDataSetChanged();
				countRequest++;
				if (car.getId() != null && responseWrapper.getJourney() != null) {
					for (int i = 0; i < responseWrapper.getJourney().size(); i++) {
						if (car.getJourney() != null
								&& car.getJourney().equals(
										responseWrapper.getJourney().get(i)
												.getKey())) {
							lcSpinner.setSelection(i);
							break;
						}
					}
				}
			}
			if (respCount == 4) {
				myProgressDialog.dismiss();
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

		} else if (object instanceof Journey) {
			car.setKey(((Journey) object).getKey());
			car.setJourney(((Journey) object).getValue());
		} else if (object instanceof String) {
			car.setUpkeep(position + 1 + "");
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onClick(View v) {

		if (car.getBid() == null) {
			Toast.makeText(this, "请选择品牌.", Toast.LENGTH_SHORT).show();
			return;
		}

		if (car.getSid() == null) {
			Toast.makeText(this, "请选择车系.", Toast.LENGTH_SHORT).show();
			return;
		}

		if (car.getMid() == null) {
			Toast.makeText(this, "请选择排量.", Toast.LENGTH_SHORT).show();
			return;
		}

		if (car.getJourney() == null) {
			Toast.makeText(this, "请选择已驾公里.", Toast.LENGTH_SHORT).show();
			return;
		}

		RequestWrapper request = new RequestWrapper();
		if (car.getId() != null) {
			request.setId(car.getId());
		}
		request.setBid(car.getBid());
		request.setSid(car.getSid());
		request.setMid(car.getMid());
		request.setJourney(car.getKey());
		request.setUpkeep(car.getUpkeep());

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
