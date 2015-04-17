package com.xqxy.baseclass;


/**
 * 网络请求枚举类型
 */
public enum NetworkAction {
	login ,	//登录接口
	brand,
	ch_gmall_catgory_get, //品类接口
	ch_gmall_subject_get, //专题接口
	ch_gmall_item_list, //商品列表接口
	ch_gmall_item_detail, //商品详情接口
	ch_gmall_item_search, //商品搜索接口
//	ch_gmall_user_authority_report, //用户授权信息回传接口
	ch_gmall_taobao_logion_report, //用户授权信息回传接口
	ch_gmall_user_logout_report, //用户退出登录回传接口
	ch_gmall_order_pay_report, //订单支付信息回传接口
	ch_gmall_user_credits_detail, //用户积分详情接口
	ch_gmall_user_signin, //用户签到接口
	ch_gmall_user_signin_search, //用户签到历史查询接口、
	
	ch_gmall_user_credits_cash, //用户积分提现申请接口
	ch_gmall_user_credits_cash_list, //用户积分提现列表接口
	ch_gmall_app_update, //终端版本更新接口
	ch_gmall_category_get
	;
	@Override
	public String toString() {
		return this.name().replace('_', '.');
	}

}
