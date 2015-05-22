package com.xqxy.model;

public class PayModel {

	private String pay_name;
	private String pay_pid;
	private String pay_key;
	private String alipay_public_key;
	private String rsa_private_key;
	
	
	
	public String getAlipay_public_key() {
		return alipay_public_key;
	}
	public void setAlipay_public_key(String alipay_public_key) {
		this.alipay_public_key = alipay_public_key;
	}
	public String getRsa_private_key() {
		return rsa_private_key;
	}
	public void setRsa_private_key(String rsa_private_key) {
		this.rsa_private_key = rsa_private_key;
	}
	public String getPay_name() {
		return pay_name;
	}
	public void setPay_name(String pay_name) {
		this.pay_name = pay_name;
	}
	public String getPay_pid() {
		return pay_pid;
	}
	public void setPay_pid(String pay_pid) {
		this.pay_pid = pay_pid;
	}
	public String getPay_key() {
		return pay_key;
	}
	public void setPay_key(String pay_key) {
		this.pay_key = pay_key;
	}
	
	
}
