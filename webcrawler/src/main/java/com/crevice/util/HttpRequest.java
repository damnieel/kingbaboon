package com.crevice.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 发起 HTTP 请求
 * 
 * @author dengrihui
 * @version 1.0, 2018/08/22
 *
 */
public class HttpRequest {

	public static String get(String url, Map<String, String> data) throws Exception {
		return get(url, data, "UTF-8");
	}

	public static String get(String url, Map<String, String> data, String charset) throws Exception {
		return request("GET", url, data, charset);
	}

	public static String post(String url, Map<String, String> data) throws Exception {
		return post(url, data, "UTF-8");
	}

	public static String post(String url, Map<String, String> data, String charset) throws Exception {
		return request("POST", url, data, charset);
	}

	public static String post(String url, String data) throws Exception {
		return post(url, data, "UTF-8");
	}

	public static String post(String url, String data, String charset) throws Exception {
		return request("POST", url, data, charset);
	}

	public static String request(String method, String url, Map<String, String> data, String charset) throws Exception {

		boolean flag = false;

		// 组装参数
		StringBuffer output = new StringBuffer();
		for (String key : data.keySet()) {
			String value = data.get(key);
			if (key == null || value == null) {
				continue;
			}
			if (flag) {
				output.append("&");
			} else {
				flag = true;
			}
			output.append(key).append("=").append(URLCoder.encode(value, charset));
		}

		return request(method, url, output.toString(), charset);

	}

	public static String request(String method, String url, String output, String charset) throws Exception {

		// 校验空参
		if (method == null || url == null || output == null || charset == null) {
			return null;
		}

		// 校验请求方式
		if (!"GET".equalsIgnoreCase(method) && !"POST".equalsIgnoreCase(method)) {
			return null;
		}

		// 请求 URL 地址
		String spec = "POST".equalsIgnoreCase(method) ? url : url + "?" + output;

		// 发起连接
		HttpURLConnection conn = (HttpURLConnection) ((new URL(spec)).openConnection());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod(method);
		conn.connect();

		// 输出参数
		if ("POST".equalsIgnoreCase(method)) {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), charset));
			bw.write(output.toString());
			bw.flush();
			bw.close();
		}

		// 读取响应
		StringBuffer input = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
		boolean flag = false;
		String line = null;
		while ((line = br.readLine()) != null) {
			if (flag) {
				input.append("\n");
			} else {
				flag = true;
			}
			input.append(line);
		}
		br.close();

		// 断开连接
		conn.disconnect();

		return input.toString();

	}

	public static void main(String[] args) throws Exception {

		String url = "http://www.w3school.com.cn/h.asp";

		Map<String, String> data = new HashMap<String, String>();
		data.put("hello", "你好");
		data.put("world", "世界");

		// System.out.println(get(url, data));
		// System.out.println(post(url, data));

		// System.out.println(get(url, data, "GB2312"));
		// System.out.println(post(url, data, "GB2312"));

		System.out.println(request("GET", url, data, "GB2312"));

	}

}
