package com.platform.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

/**
 * 常用的String转换方法。
 * 
 * @author Eric
 * @version 1.0
 */
public class StringUtil {
	private final static String[] hex = { "00", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B",
			"1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26",
			"27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31",
			"32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C",
			"3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47",
			"48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52",
			"53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D",
			"5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68",
			"69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73",
			"74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E",
			"7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
			"8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94",
			"95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
			"A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA",
			"AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5",
			"B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0",
			"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB",
			"CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6",
			"D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1",
			"E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC",
			"ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
			"F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

	private final static byte[] val = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00, 0x01,
			0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

	/**
	 * URL编码，其功能等同于javascript的escape函数
	 * 
	 * @param sourceStr
	 *            需要转换的字符串
	 * @return 返回已完成的字符串
	 */
	public static String escape(String sourceStr) {
		StringBuffer sbuf = new StringBuffer();
		int len = sourceStr.length();
		for (int i = 0; i < len; i++) {
			int ch = sourceStr.charAt(i);
			if ('A' <= ch && ch <= 'Z') {
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') {
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') {
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' || ch == '.' || ch == '!'
					|| ch == '~' || ch == '*' || ch == '\'' || ch == '('
					|| ch == ')') {
				sbuf.append((char) ch);
			} else if (ch <= 0x007F) {
				sbuf.append('%');
				sbuf.append(hex[ch]);
			} else if (ch == ' ') {
				sbuf.append("%20");
			} else {
				sbuf.append('%');
				sbuf.append('u');
				sbuf.append(hex[(ch >>> 8)]);
				sbuf.append(hex[(0x00FF & ch)]);
			}
		}
		return sbuf.toString();
	}

	/***************************************************************************
	 * URL解码，功能相当于javascript的unescape函数。本方法保证 不论参数s是否经过escape()编码，均能得到正确的“解码”结果
	 * 
	 * @param sourceStr
	 *            需要解码的字符串
	 * @return 返回已解码的字符串
	 */
	public static String unescape(String sourceStr) {
		StringBuffer sbuf = new StringBuffer();
		int i = 0;
		int len = sourceStr.length();
		while (i < len) {
			int ch = sourceStr.charAt(i);
			if ('A' <= ch && ch <= 'Z') {
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') {
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') {
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' || ch == '.' || ch == '!'
					|| ch == '~' || ch == '*' || ch == '\'' || ch == '('
					|| ch == ')') {
				sbuf.append((char) ch);
			} else if (ch == '%') {
				int cint = 0;
				if ('u' != sourceStr.charAt(i + 1)) {
					cint = (cint << 4) | val[sourceStr.charAt(i + 1)];
					cint = (cint << 4) | val[sourceStr.charAt(i + 2)];
					i += 2;
				} else {
					cint = (cint << 4) | val[sourceStr.charAt(i + 2)];
					cint = (cint << 4) | val[sourceStr.charAt(i + 3)];
					cint = (cint << 4) | val[sourceStr.charAt(i + 4)];
					cint = (cint << 4) | val[sourceStr.charAt(i + 5)];
					i += 5;
				}
				sbuf.append((char) cint);
			} else {
				sbuf.append((char) ch);
			}
			i++;
		}
		return sbuf.toString();
	}

	/**
	 * 转换字符串中HTML/XML敏感的字符。
	 * 
	 * @param src
	 *            源字符串
	 * @return 转换后的字符串
	 */
	public static String XMLEscape(String src) {
		if (src == null)
			return null;
		String rtnVal = src.replaceAll("&", "&amp;");
		rtnVal = rtnVal.replaceAll("\"", "&quot;");
		rtnVal = rtnVal.replaceAll("<", "&lt;");
		rtnVal = rtnVal.replaceAll(">", "&gt;");
		return rtnVal;
	}

	/**
	 * 获取query中的参数值
	 * 
	 * @param query
	 *            样例：a=1&b=2，注意：不带?符号
	 * @param param
	 *            参数名，如：a
	 * @return 参数值
	 */
	public static String getParameter(String query, String param) {
		Pattern p = Pattern.compile("&" + param + "=([^&]*)");
		Matcher m = p.matcher("&" + query);
		if (m.find())
			return m.group(1);
		return null;
	}

	/**
	 * 将query的值转换为哈希表，哈希表的格式类似request.getParameterMap
	 * 
	 * @param query
	 *            样例：a=1,b=2，其中splitStr=,
	 * @param splitStr
	 *            多个参数分隔符
	 * @return 哈希表，key值为query的参数名，value为一个String[]，为参数对应的值
	 */
	public static Map getParameterMap(String query, String splitStr) {
		Map rtnVal = new HashMap();
		if (isNull(query))
			return rtnVal;
		String[] parameters = query.split("\\s*" + splitStr + "\\s*");
		for (int i = 0; i < parameters.length; i++) {
			int j = parameters[i].indexOf('=');
			if (j > -1)
				rtnVal.put(parameters[i].substring(0, j),
						new String[] { parameters[i].substring(j + 1) });
		}
		return rtnVal;
	}

	/**
	 * 替换URL中?后面部分的参数。
	 * 
	 * @param query
	 *            URL?后面部分的字符串
	 * @param param
	 *            参数名
	 * @param value
	 *            参数值
	 * @return 替换后的字符串
	 */
	public static String setQueryParameter(String query, String param,
			String value) {
		String rtnVal = null;
		try {
			String m_query = isNull(query) ? "" : "&" + query;
			String m_param = "&" + param + "=";
			String m_value = URLEncoder.encode(value, "UTF-8");
			Pattern p = Pattern.compile(m_param + "[^&]*");
			Matcher m = p.matcher(m_query);
			if (m.find())
				rtnVal = m.replaceFirst(m_param + m_value);
			else
				rtnVal = m_query + m_param + m_value;
			rtnVal = rtnVal.substring(1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rtnVal;
	}

	/**
	 * 替换字符串中的指定字符，类似String.replaceAll的方法，但去除了正则表达式的应用
	 * 
	 * @param srcText
	 *            源字符串
	 * @param fromStr
	 *            需要替换的字符串
	 * @param toStr
	 *            替换为的字符串
	 * @return 替换后的字符串
	 */
	public static String replace(String srcText, String fromStr, String toStr) {
		if (srcText == null)
			return null;
		StringBuffer rtnVal = new StringBuffer();
		String rightText = srcText;
		for (int i = rightText.indexOf(fromStr); i > -1; i = rightText
				.indexOf(fromStr)) {
			rtnVal.append(rightText.substring(0, i));
			rtnVal.append(toStr);
			rightText = rightText.substring(i + fromStr.length());
		}
		rtnVal.append(rightText);
		return rtnVal.toString();
	}



	/**
	 * 往url中加前缀
	 * 
	 * @param url
	 * @param urlPrefix
	 * @return 格式化后的URL
	 */
	public static String formatUrl(String url, String urlPrefix) {
		if (!url.startsWith("/")) {
			return url;
		}
		return urlPrefix + url;
	}

	/**
	 * 连接字符串，常用与HQL语句的拼装，当左边值为空时返回右边值，当右边值为空时返回左边值，左右的值都不为空时返回左边值+连接串+右边值
	 * 
	 * @param leftStr
	 *            左边的值
	 * @param linkStr
	 *            连接字符串
	 * @param rightStr
	 *            右边的值
	 * @return 连接后的字符串
	 */
	public static String linkString(String leftStr, String linkStr,
			String rightStr) {
		if (isNull(leftStr))
			return rightStr;
		if (isNull(rightStr))
			return leftStr;
		return leftStr + linkStr + rightStr;
	}

	/**
	 * 判断一个字符串是否为null或空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		return str == null || str.trim().length() == 0 ;
	}
	/**
	 * 判断一个字符串是否为null或空或为null字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullStr(String str) {
		return str == null || str.trim().length() == 0 || str.equals("null");
	}

	/**
	 * 判断一个字符串是否为null或空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String str) {
		return !isNull(str);
	}
	/**
	 * 判断一个字符串是否为null或空或null字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotNullStr(String str) {
		return !isNullStr(str);
	}

	/**
	 * 过滤字符串null
	 * 
	 * @param s
	 *            要过滤的字符串
	 * @return 过滤后的字符串
	 */
	public static String getString(String s) {
		return s == null ? "" : (s.equals("null") ? "" : s);
	}
	
	/**
	 * 把 字符串格式为 1,2,3 转化为 '1','2','3'
	 * @param params
	 * @return
	 */
	public static String formatOfSqlParams(String params){
		if(isNotNullStr(params)){
			String[] s=params.split(",");
			params="";
			if(s!=null&&s.length>0){
				for(int i=0;i<s.length;i++){
					params+="'"+s[i]+"'";
					if(i<s.length-1)
						params+=",";
				}
			}
			return params;
		}
		return "";
	}


	/**
	 * 把 字符串格式为 1,2,3 转化为 '1','2','3'
	 * @param params
	 * @return
	 */
	public static String formatOfSqlResult(List list){
		Object[] strs=list.toArray();
		String str="";
		if(strs!=null&&strs.length>0){
			for(int i=0;i<strs.length;i++){
				str+="'"+strs[i]+"'";
				if(i<strs.length-1)
					str+=",";
			}
		}else{
			str="null";
		}
		return str;
	}
	/**
	 * 把字符串格式为 dms_color_name改为dmsColorName
	 * @param s
	 * @return
	 */
	public static String formatString(String s){
		String[] items=s.split("\\_");
		if(s.length()>0){
			s="";
			for(int i=0;i<items.length;i++){
				if(i==0) s+=items[0];
				else{
					s+=items[i].substring(0,1).toUpperCase()+items[i].substring(1); 
				}
			}
		}
		return s;
	}
	
	/**
	 * 把前台传过来的参数封装承map
	 * @param request
	 * @param params
	 * @return
	 */
	public static Map<String,String> getParams(HttpServletRequest request,String params){
		Map map=new HashMap();
		if(StringUtil.isNotNull(params)){
			String[] param=params.split(",");
			for(int i=0;i<param.length;i++){
				map.put(param[i], request.getParameter(param[i]));
			}
		}
		return map;
	}
	
	/**
	 * 把数组转为字符串，用逗号隔开
	 * @param array
	 * @return
	 */
	public static String arrayToString(String[] array){
		String result="";
		if(array!=null&&array.length>0){
			for(int i=0;i<array.length;i++){
				result+=array[i];
				if(i<array.length-1){
					result+=",";
				}
			}
		}
		return result;
	}
	
	public static JSONObject listToString(List list){
		JSONObject object=new JSONObject();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] o=(Object[]) list.get(i);
				for(int k=0;k<o.length;k++){
					String value="";
					if(object.containsKey(String.valueOf(k))){
						value= object.getString(String.valueOf(k));
					}
					value+=o[k];
					if(i<list.size()-1){
						value+=",";
					}
					object.put(String.valueOf(k), value);
				}
			}
		}
		return object;
	}
	
	public static void searchEncoding(Object o)throws Exception {
		Field[] field = o.getClass().getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			//获取访问权限
			boolean a = field[i].isAccessible();
			//设置访问权限
			field[i].setAccessible(true);
			Object value=field[i].get(o);
			if(value != null&&value instanceof String&&!"".equals(value)){
				field[i].set(o, new String(((String) value).getBytes("ISO-8859-1"),"UTF-8"));
			}
			//还原访问权限
			field[i].setAccessible(a);
		}
	}
	
	public static String getString(Object o){
		if(o==null)
			return "";
		else
			return (String)o;
	}

	public static String getStringOfBigDecimal(BigDecimal value) {
		// TODO Auto-generated method stub
		if(value==null)
			return "";
		else
			return value.toString();
	}
	
	/**
	 * 校验邮件地址
	 * @param mail
	 * @author Saindy Su
	 */
	public static boolean checkMail(String mail) {
		  String regex = "[a-zA-Z0-9_]{1,}+@[a-zA-Z0-9]+(\\.[a-zA-Z]+){1,3}";
		  return mail.matches(regex);
	}
	
	/**
	 * 获得加密过的url,从http:开始
	 * @param request
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getFormatUrl(HttpServletRequest request,String url) throws UnsupportedEncodingException{
		String hxltUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		url=URLEncoder.encode(hxltUrl+url,"utf-8");
		return url;
	}
	
	
	/**
	 * 获得加密过的url,从http:开始
	 * @param request
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getFormatUrl2(HttpServletRequest request,String url) throws UnsupportedEncodingException{
		String hxltUrl = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/";
		url=URLEncoder.encode(hxltUrl+url,"utf-8");
		return url;
	}
	/**
	 * 获得当前网址
	 * @param request
	 * @param url
	 * @return
	 */
	public static String getCurrentUrl(HttpServletRequest request,String url){
		String hxltUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+url;
		return hxltUrl;
	}
	
	/**
	 * 获得当前网址
	 * @param request
	 * @param url
	 * @return
	 */
	public static String getCurrentUrl2(HttpServletRequest request,String url){
		String hxltUrl = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/"+url;
		return hxltUrl;
	}
	
	/**
	 * 获得加密过的url
	 * @param request
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getFormatUrl(String url) throws UnsupportedEncodingException{
		return URLEncoder.encode(url,"utf-8");
	}
	
	/**
	 * 获得不为空字符串
	 * @param request
	 * @param url
	 * @return
	 */
	public static String getNotNullString(Object value){
		if(value!=null){
			return (String)value;
		}
		return "";
	}
	
	/**
	 * 获得当前网址
	 * @param request
	 * @param url
	 * @return
	 */
	public static String getLocationUrl(HttpServletRequest request){
		String strBackUrl = request.getScheme()+"://" + request.getServerName() //服务器地址
                + request.getContextPath()      //项目名称
                + request.getServletPath();      //请求页面或其他地址
		if(StringUtil.isNotNull(request.getQueryString())){
			strBackUrl= strBackUrl+ "?" + (request.getQueryString()); //参数
		}
		System.out.println("服务器地址："+strBackUrl);
		return strBackUrl;
	}

	/**
	 * 获得当前网址
	 * @param request
	 * @param url
	 * @return
	 */
	public static String getLocationUrlPort(HttpServletRequest request){
		String strBackUrl = request.getScheme()+"://"+ request.getServerName() //服务器地址
				+":"+request.getServerPort()
                + request.getContextPath()      //项目名称
                + request.getServletPath();      //请求页面或其他地址
		if(StringUtil.isNotNull(request.getQueryString())){
			strBackUrl= strBackUrl+ "?" + (request.getQueryString()); //参数
		}
		System.out.println("服务器地址："+strBackUrl);
		return strBackUrl;
	}
	
	public static String getRedirectLogin(HttpServletRequest request) {
		// TODO Auto-generated method stub
		try {
			String fdShopNo=request.getParameter("fdShopNo");
			return "redirect:/wx/login?backUrl="+StringUtil.getFormatUrl(StringUtil.getLocationUrlPort(request))+"&fdShopNo="+fdShopNo;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
