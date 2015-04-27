package com.xqxy.model;

public class Product {
	private int pid;
	private String name;
	private String pic;
	private int flag;
	private float old_price;
	private float new_price;
	private String content;
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public float getOld_price() {
		return old_price;
	}
	public void setOld_price(float old_price) {
		this.old_price = old_price;
	}
	public float getNew_price() {
		return new_price;
	}
	public void setNew_price(float new_price) {
		this.new_price = new_price;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
