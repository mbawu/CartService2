package com.xqxy.baseclass;


/**
 * 网络请求枚举类型
 */
public enum NetworkAction {
	user_register,//注册
	user_resetpwd,//忘记密码
	center_user,//获取用户基本信息
	center_head,//上传头像
	user_login ,	//登录
	car_brand, //品牌
	car_series, //车系
	car_model //类型
	;
	@Override
	public String toString() {
		return this.name().replace('_', '/');
	}

}
