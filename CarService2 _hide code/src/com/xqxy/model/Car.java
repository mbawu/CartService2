package com.xqxy.model;

import java.io.Serializable;

public class Car implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String year;
	private String journey;
	private String upkeep;
	private String create_time;
	private String key;
	private String bid;
	private String sid;
	private String mid;
	private String name;
	private String sname;
	private String mname;
	private String journey_value;



	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJourney() {
		return journey;
	}

	public void setJourney(String journey) {
		this.journey = journey;
	}

	public String getUpkeep() {
		return upkeep;
	}

	public void setUpkeep(String upkeep) {
		this.upkeep = upkeep;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getJourney_value() {
		return journey_value;
	}

	public void setJourney_value(String journey_value) {
		this.journey_value = journey_value;
	}

}
