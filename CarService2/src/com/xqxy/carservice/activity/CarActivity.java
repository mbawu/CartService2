package com.xqxy.carservice.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.baseclass.MyApplication;
import com.xqxy.baseclass.NetworkAction;
import com.xqxy.baseclass.RequestWrapper;
import com.xqxy.baseclass.ResponseWrapper;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarInfoAdapter;
import com.xqxy.model.Brand;
import com.xqxy.model.Model;
import com.xqxy.model.Series;
import com.xqxy.person.RegisterActivity;

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

	private String bid = "";
	private String sid = "";
	private String mid = "";
	private int countRequest = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_add);

		brandSpinner = (Spinner) findViewById(R.id.rst_brand);
		seriesSpinner = (Spinner) findViewById(R.id.rst_series);
		modelSpinner = (Spinner) findViewById(R.id.rst_model);
		textViewLC = (EditText) findViewById(R.id.edit_car_lc);
		textViewBYPL = (EditText) findViewById(R.id.edit_car_bypl);
		brandSpinner.setOnItemSelectedListener(this);
		seriesSpinner.setOnItemSelectedListener(this);
		modelSpinner.setOnItemSelectedListener(this);

		findViewById(R.id.btn_car_save).setOnClickListener(this);

		sendDataByGet(new RequestWrapper(), NetworkAction.carF_brand);
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_series);
		sendDataByGet(new RequestWrapper(), NetworkAction.carF_model);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		// TODO Auto-generated method stub
		super.showResualt(responseWrapper, requestType);
		if (requestType.equals(NetworkAction.carF_brand)) {
			brandAdapter = new CarInfoAdapter(this, NetworkAction.carF_brand,
					responseWrapper.getBrand());
			brandSpinner.setAdapter(brandAdapter);
			brandAdapter.notifyDataSetChanged();
			countRequest++;
		} else if (requestType.equals(NetworkAction.carF_series)) {
			seriesAdapter = new CarInfoAdapter(this, NetworkAction.carF_series,
					responseWrapper.getSeries());
			seriesSpinner.setAdapter(seriesAdapter);
			seriesAdapter.notifyDataSetChanged();
			countRequest++;
		} else if (requestType.equals(NetworkAction.carF_model)) {
			modelAdapter = new CarInfoAdapter(this, NetworkAction.carF_model,
					responseWrapper.getModel());
			modelSpinner.setAdapter(modelAdapter);
			modelAdapter.notifyDataSetChanged();
			countRequest++;
		} else if (requestType.equals(NetworkAction.centerF_add_car)) {
			/*
			 * Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
			 * MyApplication.identity = responseWrapper.getIdentity().get(0)
			 * .getIdentity();
			 */
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Object object = parent.getItemAtPosition(position);
		if (object instanceof Brand)
			bid = ((Brand) object).getBid();
		else if (object instanceof Series)
			sid = ((Series) object).getSid();
		else if (object instanceof Model)
			mid = ((Model) object).getMid();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onClick(View v) {
		String lc = textViewLC.getText().toString();
		String bypl = textViewBYPL.getText().toString();
		RequestWrapper request = new RequestWrapper();
		request.setIdentity(MyApplication.identity);
		request.setBid(bid);
		request.setSid(sid);
		request.setMid(mid);
		request.setJourney(lc);
		request.setUpkeep(bypl);
		sendData(request, NetworkAction.centerF_add_car);
	}
}
