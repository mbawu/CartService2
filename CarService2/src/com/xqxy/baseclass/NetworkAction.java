package com.xqxy.baseclass;


/**
 * 网络请求枚举类型
 */
public enum NetworkAction {
	userF_register,//注册
	userF_resetpwd,//忘记密码
	userF_send_phone,//获取验证码
	centerF_user_msg,//我的消息
	centerF_user_card,//我的储值卡
	centerF_user_integral,//积分记录
	centerF_user_coupon,//用户优惠券
	centerF_user_address,//用户常用地址
	centerF_add_address,//添加修改地址
	centerF_del_address,//删除地址
	centerF_user,//获取用户基本信息
	center_head,//上传头像
	userF_login ,	//登录
	carF_brand, //品
	carF_series, //车系
	carF_model, //类型
	indexF_banner,//轮播
	indexF_product,//首页服务
	indexF_product_details,//服务详情
	indexF_column,//项目分类
	indexF_column_product,//分类项目
	centerF_user_car,//我的爱车
	centerF_add_car,//增加爱车
	centerF_del_car,//删除爱车
	centerF_user_order,//我的订单
	centerF_affirm_order,//确认订单
	centerF_cancel_order,//取消订单
	centerF_del_order,//删除订单
	indexF_suggest,//投诉建议
	;

	@Override
	public String toString() {
		return this.name().replaceAll("F_", "/");
	}

}
