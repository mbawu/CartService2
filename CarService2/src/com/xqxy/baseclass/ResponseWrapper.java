package com.xqxy.baseclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xqxy.model.Address;
import com.xqxy.model.Appraise;
import com.xqxy.model.Banner;
import com.xqxy.model.Brand;
import com.xqxy.model.Car;
import com.xqxy.model.Cart;
import com.xqxy.model.Category;
import com.xqxy.model.CategoryProduct;
import com.xqxy.model.Coupon;
import com.xqxy.model.Credit;
import com.xqxy.model.Journey;
import com.xqxy.model.Message;
import com.xqxy.model.Model;
import com.xqxy.model.Order;
import com.xqxy.model.PayModel;
import com.xqxy.model.Product;
import com.xqxy.model.ProductAttr;
import com.xqxy.model.ProductDetails;
import com.xqxy.model.Series;
import com.xqxy.model.StoreCard;
import com.xqxy.model.UserInfo;
import com.xqxy.model.WebInfo;

/**
 * 
 * 此类描述的是： 返回数据的包装类
 * 
 * @author: wake
 * @version: 2014年12月2日 上午10:50:38
 * @param <T>
 */
public class ResponseWrapper {

	// public ResponseWrapper(NetworkAction requestType) {
	// // if(requestType.equals(NetworkAction.brand))
	// // {
	// // msg.toString();
	// // }
	// }
	/**
	 * 返回结果
	 */
	private String done;
	/**
	 * 返回信息
	 */
	private String msg;

	private String src;

	private UserInfo user;

	
	private String cid;

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

	/**
	 * 用户标识
	 */
	private ArrayList<UserInfo> identity;

	private ArrayList<Banner> banner;

	private ArrayList<Product> product;

	private ArrayList<Category> column;

	private ArrayList<Car> car;

	private ArrayList<Order> order;

	private ArrayList<WebInfo> web;
	
	public ArrayList<WebInfo> getWeb() {
		return web;
	}

	public void setWeb(ArrayList<WebInfo> web) {
		this.web = web;
	}

	/**
	 * 用户常用地址
	 * 
	 * @return
	 */
	private ArrayList<Address> address;

	private ArrayList<Credit> integral;

	private ArrayList<Message> info;

	private ArrayList<Coupon> coupon;

	private ArrayList<StoreCard> card;

	private ProductDetails product_details;

	private ArrayList<ProductAttr> attr;
	
	private List<Appraise> appraise;
	
	private ArrayList<Journey> journey;

	
	private PayModel pay;

	private ArrayList<Cart> cart;
	
	
	
	public ArrayList<Cart> getCart() {
		return cart;
	}

	public void setCart(ArrayList<Cart> cart) {
		this.cart = cart;
	}
	
	public String html;

	/**
	 * 获取支付宝支付信息
	 * @return
	 */
	public PayModel getPay() {
		return pay;
	}

	public void setPay(PayModel pay) {
		this.pay = pay;
	}


	private ArrayList<CategoryProduct> column_product;


	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	/**
	 * 我的储值卡
	 * 
	 * @return
	 */
	public ArrayList<StoreCard> getCard() {
		return card;
	}

	public void setCard(ArrayList<StoreCard> card) {
		this.card = card;
	}

	/**
	 * 我的优惠券
	 * 
	 * @return
	 */
	public ArrayList<Coupon> getCoupon() {
		return coupon;
	}

	public void setCoupon(ArrayList<Coupon> coupon) {
		this.coupon = coupon;
	}

	/**
	 * 我的消息
	 * 
	 * @return
	 */
	public ArrayList<Message> getInfo() {
		return info;
	}

	public void setInfo(ArrayList<Message> info) {
		this.info = info;
	}

	/**
	 * 积分记录
	 * 
	 * @return
	 */
	public ArrayList<Credit> getIntegral() {
		return integral;
	}

	public void setIntegral(ArrayList<Credit> integral) {
		this.integral = integral;
	}

	public ArrayList<Address> getAddress() {
		return address;
	}

	public void setAddress(ArrayList<Address> address) {
		this.address = address;
	}

	public ArrayList<UserInfo> getIdentity() {
		return identity;
	}

	public void setIdentity(ArrayList<UserInfo> identity) {
		this.identity = identity;
	}

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

	public ArrayList<Banner> getBanner() {
		return banner;
	}

	public void setBanner(ArrayList<Banner> banner) {
		this.banner = banner;
	}

	public ArrayList<Product> getProduct() {
		return product;
	}

	public void setProduct(ArrayList<Product> product) {
		this.product = product;
	}

	public ArrayList<Category> getColumn() {
		return column;
	}

	public void setColumn(ArrayList<Category> column) {
		this.column = column;
	}

	public ArrayList<Car> getCar() {
		return car;
	}

	public void setCar(ArrayList<Car> car) {
		this.car = car;
	}

	public ArrayList<Order> getOrder() {
		return order;
	}

	public void setOrder(ArrayList<Order> order) {
		this.order = order;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public ProductDetails getProduct_details() {
		return product_details;
	}

	public void setProduct_details(ProductDetails product_details) {
		this.product_details = product_details;
	}

	public ArrayList<ProductAttr> getAttr() {
		return attr;
	}

	public void setAttr(ArrayList<ProductAttr> attr) {
		this.attr = attr;
	}

	public ArrayList<CategoryProduct> getColumn_product() {
		return column_product;
	}

	public void setColumn_product(ArrayList<CategoryProduct> column_product) {
		this.column_product = column_product;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public List<Appraise> getAppraise() {
		return appraise;
	}

	public void setAppraise(List<Appraise> appraise) {
		this.appraise = appraise;
	}

	public ArrayList<Journey> getJourney() {
		return journey;
	}

	public void setJourney(ArrayList<Journey> journey) {
		this.journey = journey;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	
}
