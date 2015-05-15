package com.xqxy.baseclass;


/**
 * 网络请求枚举类型
 */
public enum NetworkAction {
	userF_register,//注册
	userF_resetpwd,//忘记密码
	userF_send_phone,//获取验证码
	centerF_user_msg,//我的消息
	centerF_update_msg,
	centerF_user_card,//我的储值卡
	indexF_card,//储值卡列表
	orderF_buy_card,//储值卡购买前的接口
	centerF_user_integral,//积分记录
	centerF_user_coupon,//用户优惠券
	centerF_user_address,//用户常用地址
	centerF_add_address,//添加修改地址
	centerF_del_address,//删除地址
	centerF_user,//获取用户基本信息
	centerF_head,//上传头像
	userF_login ,	//登录
	carF_brand, //品
	carF_series, //车系
	carF_model, //类型
	indexF_pay_base,//支付信息
	indexF_banner,//轮播
	indexF_product,//首页服务
	indexF_product_details,//服务详情
	indexF_product_attr,
	indexF_column,//项目分类
	indexF_column_product,//分类项目
	indexF_appraise,//项目评论
	indexF_journey,//行驶里程
	indexF_page,//静态网页
	centerF_user_car,//我的爱车
	centerF_add_car,//增加爱车
	centerF_del_car,//删除爱车
	centerF_user_order,//我的订单
	centerF_affirm_order,//确认订单
	centerF_cancel_order,//取消订单
	centerF_del_order,//删除订单
	centerF_appraise,
	indexF_suggest,//投诉建议
	orderF_index, //立即预约
	cartF_add_cart,
	cartF_index,//购物车列表
	cartF_del_cart,//删除购物车项目
	cartF_update_cart,//修改购物车
	cartF_cart_order,//结算购物车
	;

	@Override
	public String toString() {
		return this.name().replaceAll("F_", "/");
	}

}
