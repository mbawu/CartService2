package com.xqxy.carservice.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.cn.hongwei.CarImageView;
import com.xqxy.carservice.R;
import com.xqxy.carservice.adapter.GoodsDetailsViewPagerAdapter;

/**
 * 评论图片放大显示界面
 * @author Administrator
 *
 */
public class ImageDisplayActivity extends Activity {

	private ViewPager viewPager;//图片容器
	private TextView imgNumTextView;//图片数量
	private Intent intent;
	private ArrayList<String> imgUrls;//图片URL
	private GoodsDetailsViewPagerAdapter pagerAdapter;//容器适配器
	private ArrayList<View> ViewArrayList = new ArrayList<View>();//图片数据源

	/**
	 * 界面初始化
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_display_layout);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		imgNumTextView = (TextView) findViewById(R.id.textGoodsPicNum);
		intent = getIntent();
		pagerAdapter = new GoodsDetailsViewPagerAdapter(this);

		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {

				int index = position;
				if (imgUrls.size() == 2) {

					imgNumTextView.setText((index % 2 + 1) + " /2 ");
				} else {
					imgNumTextView.setText((position % ViewArrayList.size() + 1)
							+ " / " + ViewArrayList.size());
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		setImage();
	}

	/**
	 * 动态加载图片
	 */
	private void setImage() {
		int imgIndex = intent.getIntExtra("imgIndex", 0);
		imgUrls = intent.getStringArrayListExtra("imgUrls");
		boolean repat = true;
		for (int i = 0; i < imgUrls.size(); i++) {
			String url = imgUrls.get(i);
			CarImageView imageView = new CarImageView(this);
			LayoutParams layoutparam = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			imageView.setLayoutParams(layoutparam);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			imageView.loadImage(url);
			ViewArrayList.add(imageView);
			if (imgUrls.size() == 2 && i == 1 && repat) {//处理图片只有2张的特殊情况
				i = -1;
				repat = false;
			}
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		pagerAdapter.setArrayList(ViewArrayList);
		pagerAdapter.notifyDataSetChanged();
		viewPager.setCurrentItem(imgIndex);
		imgNumTextView.setText((imgIndex + 1) + " / " + imgUrls.size());
	}
}
