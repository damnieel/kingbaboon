package com.crevice.util;

import java.util.HashMap;

public class SpiderUtil {
	
	public static void main(String[] args) throws Exception {
		String html = HttpRequest.get("https://www.mzitu.com", new HashMap<String, String>(),"gb2312");
		System.out.println(html);
	}
}
