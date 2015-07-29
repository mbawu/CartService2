package com.xqxy.carservice.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.hongwei.CarImageView;
import com.cn.hongwei.MyApplication;
import com.cn.hongwei.PhotoActivity;
import com.cn.hongwei.RequestWrapper;
import com.cn.hongwei.ResponseWrapper;
import com.cn.hongwei.TopTitleView;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.CarBaseAdapter;
import com.xqxy.model.OrderProduct;
import com.xqxy.person.Cst;
import com.xqxy.person.NetworkAction;
/**
 * 订单评价界面
 * @author Administrator
 *
 */
public class OrderEvaluateActivity extends PhotoActivity implements
		View.OnClickListener {
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
		btnSelect.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	@Override
	public void showResualt(ResponseWrapper responseWrapper,
			NetworkAction requestType) {
		super.showResualt(responseWrapper, requestType);
		if (requestType == NetworkAction.centerF_appraise) {
			Toast.makeText(this, "评价成功", Toast.LENGTH_SHORT).show();
			OrderListActivity.EVALUATE_SUCCESS = true;
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_order_eva_select_phone) {
			if (imgLinearLayout.getChildCount() < 3) {
				showSelectPhotoDialog(Cst.UPLOAD_TEMP
						+ System.currentTimeMillis() + ".jpg");
			} else {
				Toast.makeText(this, "已经上传3张了,不能再传了.", Toast.LENGTH_SHORT)
						.show();
			}

		} else if (v.getId() == R.id.btn_order_eva_save) {

			RequestWrapper request = new RequestWrapper();
			request.setIdentity(MyApplication.identity);
			request.setOid(oid);
			request.setContent(editContent.getText().toString());
			request.setShowDialog(true);
			if (radioBtnGood.isChecked()) {
				request.setFlag("1");
			} else {
				request.setFlag("2");
			}

			int childCount = imgLinearLayout.getChildCount();
			request.setFilenum(childCount + "");
			if (childCount > 0) {
				String path;
				Map<String, String> fileMap = new HashMap<String, String>();
				for (int i = 0; i < childCount; i++) {
					path = imgLinearLayout.getChildAt(i).getTag().toString();
					fileMap.put("file" + (i + 1), fileToString(path));
				}
				request.setFiles(fileMap);
			}
			sendData(request, NetworkAction.centerF_appraise);
		}

	}

	@Override
	public void uploadImg(String imagePath) {
		CarImageView img = (CarImageView) getLayoutInflater().inflate(
				R.layout.order_eva_img, imgLinearLayout, false);
		img.setTag(imagePath);
		imgLinearLayout.addView(img);
		img.loadImage("file://" + imagePath);
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
