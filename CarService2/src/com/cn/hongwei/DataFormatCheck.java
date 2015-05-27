package com.cn.hongwei;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataFormatCheck {

	public static boolean isMobile(String mobile) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|177)\\d{8}$");

		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	public static boolean isTelephone(String mobile) {
		Pattern p = Pattern
				.compile("^(0(10|2[0-9]|[3-9]\\d{2})-?)?[0-9]\\d{6,7}$");

		Matcher m = p.matcher(mobile);
		return m.matches();
	}
}
