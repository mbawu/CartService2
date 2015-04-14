package com.xqxy.baseclass;

import java.util.List;

/**
 * 
     * 此类描述的是： 返回数据的包装类
     * @author: wake 
     * @version: 2014年12月2日 上午10:50:38
 */
public class ResponseWrapper {
	
//	private String code;
//	private String message;
//	private String serviceName;
//	
//	/**
//	 * 淘宝用户名
//	 */
//	private String userName;
//	
//	private Long categroyId;  //分类id
//	/**
//	 * 分页相关属性
//	 */
//	private Integer pageCount;
//	private Integer pageIndex;
//	private Integer pageTotal;
//	private Long itemTotal;
//	
//	/**
//	 * 签到开始日期
//	 */
//	private String beginDate;
//	/**
//	 * 签到结束日期
//	 */
//	private String endDate;
//	
//	/**
//	 * 在开始和结束日期间的签到记录
//	 */
//	private List<SigninCredits> signins;
//	
//	/**
//	 * 品类
//	 */
//	private GmallCategory category;
//   
//
//	/**
//     * 商品列表中的商品信息
//     */
//	private List<GmallItem> items;
//	/**
//	 * 面板list
//	 */
//	private List<GmallPanel> panels;
//	/**
//	 * 商品分类
//	 */
//	private List<GmallCategory> categorys;
//	
//	/**
//	 * 专题
//	 */
//	private GmallSubject subject;
//	
//	/**
//	 * 终端app升级对象
//	 */
//	private GmallAppUpgrade appUpgrade;
//	
//	/**
//	 * 用户积分
//	 */
//	private GmallCredits credits;
//	
//	/**
//	 * 搜索关键字
//	 */
//	private String keyword;
//	
//	/**
//	 * 商品详情对象
//	 */
//	private GmallItemDetail itemDetail;
//	
//	/**
//	 * 用户提现申请列表
//	 */
//	private List<CashApply> cashApplys;
//	
//	
//	public ResponseWrapper() {
//		this.code = "1000";
//	}
//	
//	
//	
//
//
//	
//
//
//
//
//	public Long getCategroyId() {
//		return categroyId;
//	}
//
//
//
//
//
//
//
//
//
//
//	public void setCategroyId(Long categroyId) {
//		this.categroyId = categroyId;
//	}
//
//
//
//
//
//
//
//
//
//
//	public Integer getPageTotal() {
//		return pageTotal;
//	}
//
//
//
//
//
//
//
//
//
//
//	public void setPageTotal(Integer pageTotal) {
//		this.pageTotal = pageTotal;
//	}
//
//
//
//
//
//
//
//
//
//
//	public Long getItemTotal() {
//		return itemTotal;
//	}
//
//
//
//
//
//
//
//
//
//
//	public void setItemTotal(Long itemTotal) {
//		this.itemTotal = itemTotal;
//	}
//
//
//
//
//
//
//
//
//
//
//	public Integer getPageCount() {
//		return pageCount;
//	}
//
//
//
//
//
//	public void setPageCount(Integer pageCount) {
//		this.pageCount = pageCount;
//	}
//
//
//
//
//
//	public Integer getPageIndex() {
//		return pageIndex;
//	}
//
//
//
//
//
//	public void setPageIndex(Integer pageIndex) {
//		this.pageIndex = pageIndex;
//	}
//
//
//
//
//
//	
//
//
//
//
//
//	
//
//
//
//
//
//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}
//
//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
//
//	public String getServiceName() {
//		return serviceName;
//	}
//
//	public void setServiceName(String serviceName) {
//		this.serviceName = serviceName;
//	}
//
//	public List<GmallCategory> getCategorys() {
//		return categorys;
//	}
//
//	public void setCategorys(List<GmallCategory> categorys) {
//		this.categorys = categorys;
//	}
//
//
//
//	public List<GmallPanel> getPanels() {
//		return panels;
//	}
//
//
//
//	public void setPanels(List<GmallPanel> panels) {
//		this.panels = panels;
//	}
//
//
//
//
//
//	public List<GmallItem> getItems() {
//		return items;
//	}
//
//
//
//
//
//	public void setItems(List<GmallItem> items) {
//		this.items = items;
//	}
//
//
//
//
//
//
//
//
//
//
//	public GmallSubject getSubject() {
//		return subject;
//	}
//
//
//
//
//
//
//
//
//
//
//	public void setSubject(GmallSubject subject) {
//		this.subject = subject;
//	}
//
//
//
//
//
//
//
//
//
//
//	public GmallAppUpgrade getAppUpgrade() {
//		return appUpgrade;
//	}
//
//
//
//
//
//
//
//
//
//
//	public void setAppUpgrade(GmallAppUpgrade appUpgrade) {
//		this.appUpgrade = appUpgrade;
//	}
//
//
//
//
//
//
//
//
//
//
//	public GmallCredits getCredits() {
//		return credits;
//	}
//
//
//
//
//
//
//
//
//
//
//	public void setCredits(GmallCredits credits) {
//		this.credits = credits;
//	}
//
//
//
//
//
//
//
//
//
//
//	public String getUserName() {
//		return userName;
//	}
//
//
//
//
//
//
//
//
//
//
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//	
//	 public String getBeginDate() {
//			return beginDate;
//		}
//
//
//
//
//
//
//
//
//
//
//		public void setBeginDate(String beginDate) {
//			this.beginDate = beginDate;
//		}
//
//
//
//
//
//
//
//
//
//
//		public String getEndDate() {
//			return endDate;
//		}
//
//
//
//
//
//
//
//
//
//
//		public void setEndDate(String endDate) {
//			this.endDate = endDate;
//		}
//
//
//
//
//
//
//
//
//
//
//		public List<SigninCredits> getSignins() {
//			return signins;
//		}
//
//
//
//
//
//
//
//
//
//
//		public void setSignins(List<SigninCredits> signins) {
//			this.signins = signins;
//		}
//
//
//
//
//
//
//
//
//
//
//		public GmallCategory getCategory() {
//			return category;
//		}
//
//
//
//
//
//
//
//
//
//
//		public void setCategory(GmallCategory category) {
//			this.category = category;
//		}
//
//
//
//
//
//
//
//
//
//
//		public String getKeyword() {
//			return keyword;
//		}
//
//
//
//
//
//
//
//
//
//
//		public void setKeyword(String keyword) {
//			this.keyword = keyword;
//		}
//
//
//
//
//
//
//
//
//
//
//		public GmallItemDetail getItemDetail() {
//			return itemDetail;
//		}
//
//
//
//
//
//
//
//
//
//
//		public void setItemDetail(GmallItemDetail itemDetail) {
//			this.itemDetail = itemDetail;
//		}
//
//
//
//
//
//
//
//
//
//
//		public List<CashApply> getCashApplys() {
//			return cashApplys;
//		}
//
//
//
//
//
//
//
//
//
//
//		public void setCashApplys(List<CashApply> cashApplys) {
//			this.cashApplys = cashApplys;
//		}









	
	

}
