/**
 * File:JsonUtil.java
 * Date:
 *
 * 四川长虹网络科技有限责任公司 (智能应用研发部)© 版权所有 
 */
package com.xqxy.baseclass;

import com.google.gson.GsonBuilder;

/**
 * 用于生产JSON格式文本和通过JSON文本创建实例
 * 
 */
public class JsonUtil {
	private static GsonBuilder instance;

	private static synchronized GsonBuilder build() {
		if (instance == null) {
			instance = new GsonBuilder();
		}
		return instance;
	}
	
	private static GsonBuilder getInstance(){
		return build();
	}
	
	/**
	 * 将obj对象生产对应的JSON字符串
	 * 
	 * @param obj 要转换成JSON字符串的类实例
	 * @return String 根据obj生产的JSON字符串
	 */
	public static String toJson(Object obj){
		return getInstance().create().toJson(obj);
	}
	
	/**
	 * 根据传入的JSON字符串创建相应类型的实例
	 * 
	 * @param json JSON格式的字符串
	 * @param clazz 对应的类的类型参数
	 * @return T JSON所对应的类实例
	 */
	public static <T> T fromJson(String json, Class<T> clazz){
//		return getInstance().create().fromJson(json, clazz);
		try {
			return getInstance().create().fromJson(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
