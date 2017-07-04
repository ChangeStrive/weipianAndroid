package com.platform.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 敏感词工具类
 * 
 * @author Saindy Su
 * 
 */
public class KeyWordFilter {
	private static Pattern pattern = null;

	/**
	 * 从words.properties初始化正则表达式字符串
	 * */
	private static void initPattern() {
		StringBuffer patternBuf = new StringBuffer("");
		try {
			InputStream in = KeyWordFilter.class.getClassLoader()
					.getResourceAsStream("words.properties");
			Properties pro = new Properties();
			pro.load(in);
			Enumeration enu = pro.propertyNames();
			patternBuf.append("(");
			while (enu.hasMoreElements()) {
				patternBuf.append((String) enu.nextElement() + "|");
			}
			patternBuf.deleteCharAt(patternBuf.length() - 1);
			patternBuf.append(")");

			// 换成UTF-8
			// pattern = Pattern.compile(new
			// String(patternBuf.toString().getBytes("ISO-8859-1"), "UTF-8"));
			// 换成GBK
			// pattern = Pattern.compile(new
			// String(patternBuf.toString().getBytes("ISO-8859-1"), "GBK"));
			pattern = Pattern.compile(new String(patternBuf.toString()
					.getBytes()));

		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}

	/**
	 * 处理敏感词，敏感词过滤器
	 * 
	 * @param str
	 *            要过滤的字符串
	 * @return
	 */
	public static String doFilter(String str) {
		initPattern();
		Matcher m = pattern.matcher(str);
		str = m.replaceAll("**");
		return str;
	}

	public static void main(String[] args) {
		String str = "美国fuck you，中国学院发改委办公室真操你奶奶的乱报道，哎，表示共产党你大爷的，汉字&English shit 这是什么东西?!操你妈! 我真无法忍受，练法轮功吧，怕你不成！！";
		System.out.println("str:" + str);
		initPattern();
		Date d1 = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEE, d MMM yyyy HH:mm:ss:SSS Z");
		System.out.println("start:" + formatter.format(d1));
		System.out.println("共" + str.length() + "个字符，查到："
				+ KeyWordFilter.doFilter(str));
		Date d2 = new Date();
		System.out.println("end:" + formatter.format(d2));
	}

}