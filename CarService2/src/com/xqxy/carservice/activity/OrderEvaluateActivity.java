package com.xqxy.carservice.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.baidu.location.BDLocation;
import com.xqxy.baseclass.BaiduLoction;
import com.xqxy.baseclass.BaiduLoction.LocationCallback;
import com.xqxy.baseclass.PhotoActivity;
import com.xqxy.carservice.R;
import com.xqxy.carservice.view.CarImageView;
import com.xqxy.carservice.view.TopTitleView;

public class OrderEvaluateActivity extends PhotoActivity {
	private TopTitleView topTitleView;
	private CarImageView serviceImageView;
	private RadioButton radioBtnGood;
	private EditText editContent;
	private Button btnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_evaluate_layout);
		topTitleView = new TopTitleView(this);
		topTitleView.setTitle("评价订单");
		serviceImageView = (CarImageView) findViewById(R.id.img_service_photo);
		radioBtnGood = (RadioButton) findViewById(R.id.radiobtn_order_eva_level_good);
		editContent = (EditText) findViewById(R.id.edit_order_eva_content);
		btnOk = (Button) findViewById(R.id.btn_order_eva_save);
		
		BaiduLoction.getInstance().setLocationCallback(new LocationCallback() {
			
			@Override
			public void locationResult(BDLocation location) {
				
				Log.i("location", location.getAddrStr());
			}
		});
		BaiduLoction.getInstance().startLocation();
	}

	@Override
	public void uploadImg(String imagePath) {

	}

}
