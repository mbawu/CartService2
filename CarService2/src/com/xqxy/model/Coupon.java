package com.xqxy.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class Coupon implements Serializable {
	private String cid;
	private String coloumnid;
	private String code;
	private String price;
	private String over_time;
	private String status;
	private String name;
	private String product_name;
	private String pid;
	private String flag;
	private boolean isExpired = false;
	private Calendar calendar;
	SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd"); 
	public Coupon() {
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired() {
		this.isExpired = isAlmostExpired(over_time);
	}

	private boolean isAlmostExpired(String time) {
		Log.i("test", "time-->"+time);
		Date date = null;
		try {
			date = dateformat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long a=date.getTime()-System.currentTimeMillis();
		Log.i("test", "a-->"+a);

			long five=1*1000*60*60*24*5;
//			long b=date.getTime()-five;
//			Log.i("test", "b-->"+b);
			if(a>five)
				return true;
			else
				return false;
			
		

//		return false;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getColoumnid() {
		return coloumnid;
	}

	public void setColoumnid(String coloumnid) {
		this.coloumnid = coloumnid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOver_time() {
		return over_time;
	}

	public void setOver_time(String over_time) {
		this.over_time = over_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
