package com.xqxy.model;

import java.io.Serializable;

public class ProductAttr implements Serializable{
	private String id;
	private String name;
	private String price;
	private String real_price;
	private String time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getReal_price() {
		return real_price;
	}

	public void setReal_price(String real_price) {
		this.real_price = real_price;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
