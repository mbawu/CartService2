/**
 * ProjectName:AndroidShopNC2014Moblie
 * PackageName:net.shopnc.android.adapter
 * FileNmae:AddressListViewAdapter.java
 */
package com.xqxy.carservice.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 评价图片，大图适配器
 * @author Administrator
 *
 */
public class GoodsDetailsViewPagerAdapter extends PagerAdapter{
	private Context context;
	private ArrayList<View> arrayList;
	
	public GoodsDetailsViewPagerAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		if(arrayList != null && arrayList.size() == 1){
			return arrayList.size();
		}else{
			return Integer.MAX_VALUE;
		}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		//((ViewPager)container).removeView(arrayList.get(position % arrayList.size()));
	}

	public ArrayList<View> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<View> arrayList) {
		this.arrayList = arrayList;
	}

	/**
	 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
	 */
	@Override
	public Object instantiateItem(View container, int position) {
//		((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
//		return mImageViews[position % mImageViews.length];
		
		ImageView views = (ImageView) arrayList.get(position % arrayList.size());
		 try{
			 if(views.getParent()==null){
			   ((ViewGroup) container).addView(views); 
			 }else{
			      ((ViewGroup)views.getParent()).removeView(views);
			      ((ViewGroup) container).addView(views);
			}
		}catch(Exception e){
			   e.printStackTrace();
		}
	return views; 
		
	}
	
}
