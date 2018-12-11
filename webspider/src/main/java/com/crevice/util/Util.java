package com.crevice.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

public class Util {

	public static String format(String pattern, Date input) {
		return (new SimpleDateFormat(pattern)).format(input);
	}

	public static String millis() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static Date add(Date date, int field, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(field, amount);
		return c.getTime();
	}

	public static String add(Date date, int field, int amount, String pattern) {
		return format(pattern, add(date, field, amount));
	}

	public static String day(Date date, int amount, String pattern) {
		return add(date, Calendar.DAY_OF_MONTH, amount, pattern);
	}

	public static String month(Date date, int amount, String pattern) {
		return add(date, Calendar.MONTH, amount, pattern);
	}

	public static Date parse(String pattern, String source) {
		try {
			return (new SimpleDateFormat(pattern)).parse(source);
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean matches(String regex, CharSequence input) {
		return Pattern.matches(regex, input);
	}

	public static final String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static boolean eq(boolean ignoreCase, Object input, Object... another) {
		if (input == null || another == null) {
			return false;
		}
		String str = String.valueOf(input);
		for (Object a : another) {
			String s = String.valueOf(a);
			if (ignoreCase ? str.equalsIgnoreCase(s) : str.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean equals(Object input, Object... another) {
		return eq(false, input, another);
	}

	public static boolean contains(boolean ignoreCase, Object input, Object... another) {

		if (input == null || another == null) {
			return false;
		}

		String str = String.valueOf(input);
		if (ignoreCase) {
			str = str.toLowerCase();
		}

		for (Object i : another) {
			String s = String.valueOf(i);
			if (ignoreCase) {
				s = s.toLowerCase();
			}
			if (str.contains(s)) {
				return true;
			}
		}

		return false;

	}

	public static <T extends Map<?, ?>> T trim(T m) {

		if (m == null || m.isEmpty()) {
			return m;
		}

		for (Iterator<?> i = m.keySet().iterator(); i.hasNext();) {
			Object k = i.next();
			Object v = m.get(k);
			if (k == null || v == null) {
				i.remove();
			}
		}

		return m;

	}

	public static boolean isNull(Object... input) {
		for (Object o : input) {
			if (o == null) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmpty(boolean trim, String... input) {
		for (String s : input) {
			if (s == null || "".equals(trim ? s.trim() : s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEmpty(String... input) {
		return isEmpty(false, input);
	}

	public static boolean isLength(String input, int n, int m) {
		if (input == null || n < 0 || m < 0 || n > m) {
			return false;
		}
		int len = input.length();
		return len >= n && len <= m;
	}

	public static boolean isNumber(String input, int n, int m) {
		return isLength(input, n, m) && matches("[0-9]*", input);
	}

	public static boolean isNumber(String input, int n, int m, int min, int max) {
		if (!isNumber(input, n, m)) {
			return false;
		}
		try {
			int i = Integer.parseInt(input);
			return i >= min && i <= max;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isWord(String input, int n, int m) {
		return isLength(input, n, m) && matches("[a-zA-Z_0-9]*", input);
	}

	public static boolean isEmail(String input, int n, int m) {
		return isLength(input, n, m) && matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$", input);
	}

	public static boolean isTime(String input) {
		return isLength(input, 8, 8) && matches("[0-9]{2}:[0-9]{2}:[0-9]{2}", input);
	}

	public static boolean isDate(String input) {
		return isLength(input, 10, 10) && matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", input);
	}

	public static boolean isMonth(String input) {
		return isLength(input, 7, 7) && matches("[0-9]{4}-[0-9]{2}", input);
	}

	public static boolean isYear(String input) {
		return isLength(input, 4, 4) && matches("[0-9]{4}", input);
	}

	public static boolean isDate(Date input, Date another, int field, int min, int max) {
		if (input == null || another == null || min > max) {
			return false;
		}
		if (input.compareTo(add(another, field, min)) < 0) {
			return false;
		}
		if (input.compareTo(add(another, field, max)) > 0) {
			return false;
		}
		return true;
	}

	public static boolean isDate(String input, Date another, int min, int max) {

		if (!isDate(input) || another == null || min > max) {
			return false;
		}

		String pattern = "yyyy-MM-dd";
		Date d1 = parse(pattern, input);
		Date d2 = parse(pattern, format(pattern, another));

		return isDate(d1, d2, Calendar.DAY_OF_MONTH, min, max);

	}

	public static boolean isMonth(String input, Date another, int min, int max) {

		if (!isMonth(input) || another == null || min > max) {
			return false;
		}

		String pattern = "yyyy-MM";
		Date d1 = parse(pattern, input);
		Date d2 = parse(pattern, format(pattern, another));

		return isDate(d1, d2, Calendar.MONTH, min, max);

	}

	public static boolean isYear(String input, Date another, int min, int max) {

		if (!isYear(input) || another == null || min > max) {
			return false;
		}

		String pattern = "yyyy";
		Date d1 = parse(pattern, input);
		Date d2 = parse(pattern, format(pattern, another));

		return isDate(d1, d2, Calendar.YEAR, min, max);

	}

	public static boolean isDayInMonth(String input, Date another, int min, int max) {
		return !isDate(input) ? false : isMonth(input.substring(0, 7), another, min, max);
	}

	public static boolean isDayInYear(String input, Date another, int min, int max) {
		return !isDate(input) ? false : isYear(input.substring(0, 4), another, min, max);
	}

	public static boolean isMonthInYear(String input, Date another, int min, int max) {
		return !isMonth(input) ? false : isYear(input.substring(0, 4), another, min, max);
	}

	public static String random(int len) {
		if (len < 1 || len > 8) {
			return "";
		}
		int length = len + 2;
		String r = String.valueOf(Math.random());
		return r.length() < length ? random(len) : r.substring(2, length);
	}

	public static String random() {
		return millis().concat(random(5));
	}

	public static String random(String w, int n, int m) {

		if (w == null || n < 1 || m < 1 || n > m) {
			return null;
		}

		w = w.toUpperCase();

		List<String> list = new ArrayList<String>();

		// 大写字母(Upper)
		if (w.contains("U")) {
			for (int i = 'A'; i <= 'Z'; i++) {
				list.add(String.valueOf((char) i));
			}
		}

		// 小写字母(Lower)
		if (w.contains("L")) {
			for (int i = 'a'; i <= 'z'; i++) {
				list.add(String.valueOf((char) i));
			}
		}

		// 数字(Digit)
		if (w.contains("D")) {
			for (int i = 0; i <= 9; i++) {
				list.add(String.valueOf(i));
			}
		}

		if (list.isEmpty()) {
			return null;
		}

		Random r = new Random();
		StringBuilder sb = new StringBuilder();

		int length = n == m ? n : (r.nextInt(m + 1 - n) + n);

		for (int i = 0; i < length; i++) {
			sb.append(list.get(r.nextInt(list.size())));
		}

		return sb.toString();

	}

	public static String digest(String algorithm, String input, String charsetName) throws Exception {
		byte[] bytes = MessageDigest.getInstance(algorithm).digest(input.getBytes(charsetName));
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(String.format("%02x", Integer.valueOf(bytes[i] & 0xFF)));
		}
		return sb.toString();
	}

	public static String sha1(String input, String charsetName) throws Exception {
		return digest("SHA-1", input, charsetName);
	}

	public static String sha1(String input) throws Exception {
		return sha1(input, "UTF-8");
	}

	public static String md5(String input, String charsetName) throws Exception {
		return digest("MD5", input, charsetName);
	}

	public static String md5(String input) throws Exception {
		return md5(input, "UTF-8");
	}

	public static String sortConcat(String... input) {

		if (input == null) {
			return null;
		}

		List<String> list = new ArrayList<String>();
		for (String s : input) {
			if (s != null) {
				list.add(s);
			}
		}

		Collections.sort(list);

		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(s);
		}

		return sb.toString();

	}

	public static SortedMap<String, String> sortMap(Map<String, String> map) {
		return new TreeMap<String, String>(map);
	}

	public static String toString(Object input, String defaultValue) {
		return input == null ? defaultValue : String.valueOf(input);
	}

	public static String toString(Object input) {
		return toString(input, null);
	}

	public static String toString(String pattern, Date input, String defaultValue) {
		try {
			return format(pattern, input);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Long parseLong(Object input, Long defaultValue) {
		try {
			return Long.parseLong(String.valueOf(input));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Integer parseInt(Object input, Integer defaultValue) {
		try {
			return Integer.parseInt(String.valueOf(input));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Date parseDate(String pattern, Object input, Date defaultValue) {
		Date date = parse(pattern, String.valueOf(input));
		return date == null ? defaultValue : date;
	}

	public static Map<String, Object> map(Object... input) {
		if (input == null || input.length < 2 || input.length % 2 != 0) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < input.length; i += 2) {
			map.put(toString(input[i]), input[i + 1]);
		}
		return map;
	}

	public static String subSign(String input) {

		if (input == null) {
			return null;
		}

		boolean l = input.contains("【");
		boolean r = input.contains("】");
		boolean s = input.startsWith("【");
		boolean e = input.endsWith("】");

		if ((!l || !r) || (!s && !e) || (s && e)) {
			return null;
		}

		int beginIndex = 0;
		int endIndex = 0;

		if (s) {
			beginIndex = 1;
			endIndex = input.indexOf("】");
		} else if (e) {
			beginIndex = input.lastIndexOf("【") + 1;
			endIndex = input.length() - 1;
		}

		return input.substring(beginIndex, endIndex);

	}

}
