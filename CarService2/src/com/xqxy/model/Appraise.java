package com.xqxy.model;

import java.util.ArrayList;

public class Appraise {
	private String flag;
	private String content;
	private String create_time;
	private String reply;
	private String reply_time;
	private String phone;
	private ArrayList<String> pic;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getReply_time() {
		return reply_time;
	}
	public void setReply_time(String reply_time) {
		this.reply_time = reply_time;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public ArrayList<String> getPic() {
		return pic;
	}
	public void setPic(ArrayList<String> pic) {
		this.pic = pic;
	}
	
	
}
