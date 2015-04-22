package com.xqxy.person;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xqxy.baseclass.BaseActivity;
import com.xqxy.carservice.R;


public class AdressActivity extends BaseActivity {

	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_address);
		init();
	}
	private void init() {
		listView=(ListView) findViewById(R.id.listview);
		
	}
	public void btnOnClick(View view) {
		finish();
	}
}
