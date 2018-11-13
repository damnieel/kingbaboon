package com.crevice.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 
 * 编码、解码
 * 
 * @author dengrihui
 * @version 1.0, 2018/08/22
 *
 */
public class URLCoder {

	public static String decode(String s) {
		return decode(s, "UTF-8");
	}

	public static String decode(String s, String enc) {
		try {
			return URLDecoder.decode(s, enc);
		} catch (Exception e) {
			return null;
		}
	}

	public static String encode(String s) {
		return encode(s, "UTF-8");
	}

	public static String encode(String s, String enc) {
		try {
			return URLEncoder.encode(s, enc);
		} catch (Exception e) {
			return null;
		}
	}

}
