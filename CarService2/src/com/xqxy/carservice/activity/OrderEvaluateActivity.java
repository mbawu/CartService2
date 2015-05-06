package com.xqxy.carservice.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings.System;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xqxy.baseclass.Cst;
import com.xqxy.baseclass.PhotoActivity;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.carservice.view.CarImageView;
import com.xqxy.carservice.view.TopTitleView;
import com.xqxy.model.OrderProduct;

public class OrderEvaluateActivity extends PhotoActivity {
	private TopTitleView topTitleView;

	private ListView listView;
	private RadioButton radioBtnGood;
	private EditText editContent;
	private LinearLayout imgLinearLayout;
	private Button btnOk;
	private ImageButton btnSelect;

	private String oid;
	private List<OrderProduct> product;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_evaluate_layout);
		oid = getIntent().getStringExtra("oid");
		Object obj = getIntent().getSerializableExtra("product");
		if (obj != null) {
			product = (List<OrderProduct>) obj;
		}
		topTitleView = new TopTitleView(this);
		topTitleView.setTitle("评价订单");
		listView = (ListView) findViewById(R.id.listview);
		radioBtnGood = (RadioButton) findViewById(R.id.radiobtn_order_eva_level_good);
		editContent = (EditText) findViewById(R.id.edit_order_eva_content);
		imgLinearLayout = (LinearLayout) findViewById(R.id.layout_order_eva_img);
		btnOk = (Button) findViewById(R.id.btn_order_eva_save);
		btnSelect = (ImageButton) findViewById(R.id.btn_order_eva_select_phone);
		OrderEvaProductAdapter adapter = new OrderEvaProductAdapter(this);
		adapter.setDataList(product);
		listView.setAdapter(adapter);
		btnSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSelectPhotoDialog(Cst.UPLOAD_TEMP
						+ java.lang.System.currentTimeMillis() + ".jpg");
			}
		});
	}

	@Override
	public void uploadImg(String imagePath) {
		CarImageView img = (CarImageView) getLayoutInflater().inflate(
				R.layout.order_eva_img, null);
		img.setTag(imagePath);
		imgLinearLayout.addView(img);
		img.loadImage("file://"+imagePath);

	}

	class OrderEvaProductAdapter extends CarBaseAdapter<OrderProduct> {

		public OrderEvaProductAdapter(Activity activity) {
			super(activity);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder ViewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.order_evaluate_item,
						null);
				ViewHolder = new ViewHolder();
				ViewHolder.textOrderProductName = (TextView) convertView
						.findViewById(R.id.text_order_eva_product_name);
				ViewHolder.textOrderProductPrice = (TextView) convertView
						.findViewById(R.id.text_order_eva_product_price);
				ViewHolder.textOrderProductTime = (TextView) convertView
						.findViewById(R.id.text_order_eva_product_time);
				ViewHolder.imageView = (CarImageView) convertView
						.findViewById(R.id.img_order_eva_product);
				convertView.setTag(ViewHolder);

			} else {
				ViewHolder = (ViewHolder) convertView.getTag();
			}
			OrderProduct op = getDataList().get(position);
			ViewHolder.textOrderProductName.setText(op.getName());
			ViewHolder.textOrderProductPrice.setText(getString(
					R.string.order_price, op.getPrice()));
			ViewHolder.textOrderProductTime.setText(getString(
					R.string.product_time, op.getTime()));
			if (op.getPic() != null && !"".equals(op.getPic())) {
				ViewHolder.imageView.loadImage(op.getPic());
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView textOrderProductName;
		TextView textOrderProductPrice;
		TextView textOrderProductTime;
		CarImageView imageView;
	}
}
