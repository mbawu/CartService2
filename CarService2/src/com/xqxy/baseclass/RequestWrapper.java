package com.xqxy.baseclass;
/**
 * 
     * 此类描述的是： 请求参数的包装类
     * @author: wake 
     * @version: 2014年12月2日 上午10:51:20
 */
public class RequestWrapper {
	
	private String userName;
	private String password;
	
	//注册
	private String surname;
	private String sex;
	private String phone;
	private String id;
	private String cid;
	private String oid;
	private String bid;
	private String sid;
	private String mid;
	private String code;
	private String lng;
	private String lat;
	private String file;

	//用户标识
	private String identity;
	
	//地址
	private String aid;
	private String name;
	private String car_num;
	private String address;
	private String detailed;
	
	private String page;
	private String status;
	
	private String pid;
	
	private String journey;
	private String upkeep;
	
	//投诉建议
	private String content;
	
	private String paid;
	private String server_time;
	private String note;
	private boolean showDialog=false;
	
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getServer_time() {
		return server_time;
	}
	public void setServer_time(String server_time) {
		this.server_time = server_time;
	}
	public String getPaid() {
		return paid;
	}
	public void setPaid(String paid) {
		this.paid = paid;
	}
	public boolean isShowDialog() {
		return showDialog;
	}
	public void setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCar_num() {
		return car_num;
	}
	public void setCar_num(String car_num) {
		this.car_num = car_num;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDetailed() {
		return detailed;
	}
	public void setDetailed(String detailed) {
		this.detailed = detailed;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
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
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	
//	private AppClient client;
//	private String testName;
//	
//	/**
//	 * 分类的id
//	 */
//	private Long categoryId;
//	
//	/**
//	 * 分页相关参数
//	 */
//	private Integer pageCount;  //每页数量
//	private Integer pageIndex;  //从第几页开始，默认从1开始
//	
//	/**
//	 * 专题id
//	 */
//	private Long subjectId;
//	
//	/**
//	 *请求的版本号
//	 */
//	private String methodVersion;
//	
//	/**
//	 * 淘宝用户名
//	 */
//	private String userName;
//	/**
//	 * 用户隐藏名
//	 */
//	private String userMaskName;
//	/**
//	 * 用户头像
//	 */
//	private String avatarUrl;
//	
//	/**
//	 * 申请提现对象
//	 */
//	private DepositCash depositCash;
//	
//	/**
//	 * 终端ip，仅服务端使用，终端忽略
//	 */
//	private String ip;
//	
//	/**
//	 * 搜索关键字，如果为空，搜索会返回推荐数据
//	 */
//	private String keyword;
//	
//    /**
//     * 商品id
//     */
//	private Long itemId;
//	
//	/**
//	 * 订单支付信息回传数据
//	 */
//	private OrderReport orderReport;
//	
//
//	public Long getCategoryId() {
//		return categoryId;
//	}
//
//	public void setCategoryId(Long categoryId) {
//		this.categoryId = categoryId;
//	}
//
//	public Integer getPageCount() {
//		return pageCount;
//	}
//
//	public void setPageCount(Integer pageCount) {
//		this.pageCount = pageCount;
//	}
//
//	
//
//	public Integer getPageIndex() {
//		return pageIndex;
//	}
//
//	public void setPageIndex(Integer pageIndex) {
//		this.pageIndex = pageIndex;
//	}
//
//	public AppClient getClient() {
//		return client;
//	}
//
//	public void setClient(AppClient client) {
//		this.client = client;
//	}
//
//	public String getTestName() {
//		return testName;
//	}
//
//	public void setTestName(String testName) {
//		this.testName = testName;
//	}
//
//	public Long getSubjectId() {
//		return subjectId;
//	}
//
//	public void setSubjectId(Long subjectId) {
//		this.subjectId = subjectId;
//	}
//
//	public String getMethodVersion() {
//		return methodVersion;
//	}
//
//	public void setMethodVersion(String methodVersion) {
//		this.methodVersion = methodVersion;
//	}
//
//	public String getUserName() {
//		return userName;
//	}
//
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//
//	public DepositCash getDepositCash() {
//		return depositCash;
//	}
//
//	public void setDepositCash(DepositCash depositCash) {
//		this.depositCash = depositCash;
//	}
//
//	public String getUserMaskName() {
//		return userMaskName;
//	}
//
//	public void setUserMaskName(String userMaskName) {
//		this.userMaskName = userMaskName;
//	}
//
//	public String getAvatarUrl() {
//		return avatarUrl;
//	}
//
//	public void setAvatarUrl(String avatarUrl) {
//		this.avatarUrl = avatarUrl;
//	}
//
//	public String getIp() {
//		return ip;
//	}
//
//	public void setIp(String ip) {
//		this.ip = ip;
//	}
//
//	public String getKeyword() {
//		return keyword;
//	}
//
//	public void setKeyword(String keyword) {
//		this.keyword = keyword;
//	}
//
//	public Long getItemId() {
//		return itemId;
//	}
//
//	public void setItemId(Long itemId) {
//		this.itemId = itemId;
//	}
//
//	public OrderReport getOrderReport() {
//		return orderReport;
//	}
//
//	public void setOrderReport(OrderReport orderReport) {
//		this.orderReport = orderReport;
//	}


	
	
	
	

}
