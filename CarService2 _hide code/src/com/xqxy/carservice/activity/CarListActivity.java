package com.xqxy.carservice.activity;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.hongwei.BaseActivity;
import com.cn.hongwei.ConfirmDialog;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.cn.hongwei.SlideListView;
import com.cn.hongwei.TopTitleView;
import com.cn.hongwei.SlideListView.RemoveDirection;
import com.cn.hongwei.SlideListView.RemoveListener;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.Car;
import com.xqxy.person.NetworkAction;

public class CarListActivity extends BaseActivity {
	private TopTitleView topTitleView;
	private SlideListView listView;
	private TextView nodata;
	private TextView textCarAdd;
	private CarAdapter adapter;
	private List<Car> carList;
	private MyApplication app;
	private Car delCar;
	private String[] upkeeps;
	private String pid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_list_layout);
		topTitleView = new TopTitleView(this);
		app = (MyApplication) getApplication();
		pid = getIntent().getStringExtra("pid");
		nodata = (TextView) findViewById(R.id.nodataTxt);
		listView = (SlideListView) findViewById(R.id.listview);
		listView.setRemoveListener(new RemoveListener() {
			
			@Override
			public void removeItem(RemoveDirection direction, int position) {
				deleteCar(carList.get(position));
			}
		});
		textCarAdd = (TextView) findViewById(R.id.text_car_list_add);
		adapter = new CarAdapter(this);
		listView.setAdapter(adapter);
		textCarAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult((new Intent(CarListActivity.this,
						CarActivity.class)), 1);
			}
		});
		upkeeps = getResources().getStringArray(R.array.upkeep);
		getCarList();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Car car=carList.get(arg2);
				if (pid != null) {
					app.setCar(car);
					setResult(RESULT_OK);
					finish();
				} else {
					Intent intent = new Intent(CarListActivity.this,
							CarActivity.class);
					intent.putExtra("car", car);
					//startActivity(intent);
					startActivityForResult(intent, 1);
				}
				
			}
		});
	}

	private void getCarList() {
		RequestWrapper request = new RequestWrapper();
		request.setShowDialog(true);
		request.setIdentity(MyApplication.identity);
		sendData(request, NetworkAction.centerF_user_car);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.centerF_user_car) {
			carList = responseWrapper.getCar();
			adapter.setDataList(carList);
			adapter.notifyDataSetChanged();

		} else if (requestType == NetworkAction.centerF_del_car) {
			if (delCar != null && carList.contains(delCar)) {
				carList.remove(delCar);
				delCar = null;
				adapter.notifyDataSetChanged();
			}
		}
		if (carList != null && carList.size() > 0) {
			nodata.setVisibility(View.GONE);
		} else {
			nodata.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			getCarList();
		}
	}

	private void deleteCar(Car car) {
		delCar = car;
		RequestWrapper request = new RequestWrapper();
		request.setIdentity(MyApplication.identity);
		request.setId(car.getId());
		sendData(request, NetworkAction.centerF_del_car);
	}

	class CarAdapter extends CarBaseAdapter<Car> {

		public CarAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.car_item, null);
				viewHolder = new ViewHolder();
				viewHolder.textPP = (TextView) convertView
						.findViewById(R.id.text_car_item_pp);
				viewHolder.textYear = (TextView) convertView
						.findViewById(R.id.text_car_item_year);
				viewHolder.textLC = (TextView) convertView
						.findViewById(R.id.text_car_item_lc);
				viewHolder.textXL = (TextView) convertView
						.findViewById(R.id.text_car_item_xl);
				viewHolder.textPL = (TextView) convertView
						.findViewById(R.id.text_car_item_pl);
				viewHolder.textBYPL = (TextView) convertView
						.findViewById(R.id.text_car_item_bypl);
				viewHolder.textDelete = (TextView) convertView
						.findViewById(R.id.text_car_item_delete);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final Car car = this.getDataList().get(position);

			viewHolder.textPP.setText(getString(R.string.car_item_pp,
					car.getName()));
			viewHolder.textYear.setText(getString(R.string.car_item_year,
					car.getYear()));
			viewHolder.textLC.setText(getString(R.string.car_item_lc,
					car.getJourney_value() + ""));
			viewHolder.textXL.setText(getString(R.string.car_item_xl,
					car.getSname()));
			viewHolder.textPL.setText(getString(R.string.car_item_pl,
					car.getMname()));
			viewHolder.textBYPL.setText(getString(R.string.car_item_bypl, ""));
			if (car.getUpkeep() != null && !"".equals(car.getUpkeep())) {
				try {
					int posi = Integer.parseInt(car.getUpkeep());
					
					if (posi <= upkeeps.length) {
						viewHolder.textBYPL.setText(getString(
								R.string.car_item_bypl, upkeeps[posi - 1]));
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			viewHolder.textDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ConfirmDialog dlg = new ConfirmDialog(CarListActivity.this);
					dlg.setTitle("提示");
					dlg.setMessage("确定删除爱车吗？删除后将无法恢复.");
					dlg.setPositiveButton("删除",
							new ConfirmDialog.OnClickListener() {

								@Override
								public void onClick(Dialog dialog, View view) {
									deleteCar(car);
								}
							});

					dlg.setNegativeButton("取消",
							new ConfirmDialog.OnClickListener() {

								@Override
								public void onClick(Dialog dialog, View view) {

								}
							});
					dlg.show();
				}
			});

//			convertView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					if (pid != null) {
//						app.setCar(car);
//						setResult(RESULT_OK);
//						finish();
//					} else {
//						Intent intent = new Intent(CarListActivity.this,
//								CarActivity.class);
//						intent.putExtra("car", car);
//						//startActivity(intent);
//						startActivityForResult(intent, 1);
//					}
//
//				}
//			});
			return convertView;
		}

	}

	class ViewHolder {
		TextView textDelete;
		TextView textPP;
		TextView textYear;
		TextView textLC;
		TextView textXL;
		TextView textPL;
		TextView textBYPL;
	}

}
