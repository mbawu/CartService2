package com.xqxy.baseclass;

import java.util.ArrayList;

import com.xqxy.model.Banner;
import com.xqxy.model.Brand;
import com.xqxy.model.Model;
import com.xqxy.model.Series;



/**
 * 
 * 此类描述的是： 返回数据的包装类
 * 
 * @author: wake
 * @version: 2014年12月2日 上午10:50:38
 * @param <T>
 */
public class ResponseWrapper {

//	public ResponseWrapper(NetworkAction requestType) {
////		if(requestType.equals(NetworkAction.brand))
////		{
////			msg.toString();
////		}
//	}
	/**
	 * 返回结果
	 */
	private String done;
	/**
	 * 返回信息
	 */
	private String msg ;
	/**
	 * 汽车品牌
	 */
	private ArrayList<Brand> brand;
	/**
	 * 汽车车系数据
	 */
	private ArrayList<Series> series;
	/**
	 * 汽车类型数据
	 */
	private ArrayList<Model> model;
	
	private ArrayList<Banner> banner;
	public String getDone() {
		return done;
	}
	public void setDone(String done) {
		this.done = done;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public ArrayList<Brand> getBrand() {
		return brand;
	}
	public void setBrand(ArrayList<Brand> brand) {
		this.brand = brand;
	}
	public ArrayList<Series> getSeries() {
		return series;
	}
	public void setSeries(ArrayList<Series> series) {
		this.series = series;
	}
	public ArrayList<Model> getModel() {
		return model;
	}
	public void setModel(ArrayList<Model> model) {
		this.model = model;
	}

	


	
}
