package com.crevice.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * JSON 工具类
 * 
 * @author chenfan
 * @version 1.0, 2016/07/08
 *
 */
public class JSONUtil {

	public static Map<String, String> toMap(String input) {

		if (input == null) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();

		JSONObject jsonObject = JSONObject.fromObject(input);
		for (Object k : jsonObject.keySet()) {
			Object v = jsonObject.get(k);
			map.put(Util.toString(k), Util.toString(v));
		}

		return map;

	}

	public static Map<String, String> toMap(String input, Map<String, String> defaultValue) {
		try {
			return toMap(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static List<String> toList(String input) {

		if (input == null) {
			return null;
		}

		List<String> list = new ArrayList<String>();

		JSONArray jsonArray = JSONArray.fromObject(input);
		for (Iterator<?> i = jsonArray.iterator(); i.hasNext();) {
			list.add(i.next().toString());
		}

		return list;

	}

	public static List<String> toList(String input, List<String> defaultValue) {
		try {
			return toList(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static List<Map<String, String>> toMapList(String input) {

		if (input == null) {
			return null;
		}

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		JSONArray jsonArray = JSONArray.fromObject(input);
		for (Iterator<?> i = jsonArray.iterator(); i.hasNext();) {
			list.add(toMap(i.next().toString()));
		}

		return list;

	}

	public static List<Map<String, String>> toMapList(String input, List<Map<String, String>> defaultValue) {
		try {
			return toMapList(input);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * <b>JSON 字符串如下：</b>
	 * 
	 * <pre>
	 * {
	 * 	"status": "0",
	 * 	"message": "正确",
	 *  "result1": ["a", "b", "c"],
	 * 	"result2": [
	 * 		{
	 * 			"status": "0",
	 * 			"message": "正确",
	 * 			"imsi": "460040260908676",
	 * 			"msisdn": "1064826090209",
	 * 			"iccid": "898602B2221430000006"
	 * 		},
	 * 		{
	 * 			"status": "0",
	 * 			"message": "正确",
	 * 			"imsi": "460040260900788",
	 * 			"msisdn": "1064826090212",
	 * 			"iccid": "898602B2221340000878"},
	 * 		{
	 * 			"status": "25",
	 * 			"message": "ICCID号不是所查询的集团下的用户",
	 * 			"iccid": "89860010011631234571"
	 * 		}
	 * 	]
	 * }
	 * </pre>
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) {

		// 示例1：
		// String json = "{\"status\":\"0\",\"message\":\"正确\"}";
		// Map<String, String> map = toMap(json, null);
		// System.out.println(map);

		// 示例2：
		// String json = "[\"a\",\"b\",\"c\"]";
		// List<String> arr = toList(json, null);
		// System.out.println(arr);

		// 示例3：
		// String json =
		// "[{\"status\":\"0\",\"message\":\"正确\",\"imsi\":\"460040260908676\",\"msisdn\":\"1064826090209\",\"iccid\":\"898602B2221430000006\"},{\"status\":\"0\",\"message\":\"正确\",\"imsi\":\"460040260900788\",\"msisdn\":\"1064826090212\",\"iccid\":\"898602B2221340000878\"},{\"status\":\"25\",\"message\":\"ICCID号不是所查询的集团下的用户\",\"iccid\":\"89860010011631234571\"}]";
		// List<Map<String, String>> list = toMapList(json, null);
		// System.out.println(list);

		// 示例4：
		String json = "{\"status\":\"0\",\"message\":\"正确\",\"result1\":[\"a\",\"b\",\"c\"],\"result2\":[{\"status\":\"0\",\"message\":\"正确\",\"imsi\":\"460040260908676\",\"msisdn\":\"1064826090209\",\"iccid\":\"898602B2221430000006\"},{\"status\":\"0\",\"message\":\"正确\",\"imsi\":\"460040260900788\",\"msisdn\":\"1064826090212\",\"iccid\":\"898602B2221340000878\"},{\"status\":\"25\",\"message\":\"ICCID号不是所查询的集团下的用户\",\"iccid\":\"89860010011631234571\"}]}";

		Map<String, String> map = toMap(json, null);
		System.out.println(map);

		if (map == null || map.isEmpty()) {
			return;
		}

		List<String> arr = toList(map.get("result1"), null);
		System.out.println(arr);

		List<Map<String, String>> list = toMapList(map.get("result2"), null);
		System.out.println(list);

	}

}
