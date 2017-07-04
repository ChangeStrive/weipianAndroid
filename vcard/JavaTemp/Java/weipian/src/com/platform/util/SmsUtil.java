package com.platform.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author Saindy Su
 *
 */
public class SmsUtil {
	
	private static String OperID = "ceshfj2";
	private static String OperPass = "csfj123";

	public static JSONObject sendMsg(String mobile, String content) {
		JSONObject json = new JSONObject();
//		json.put("code", 1);
//		json.put("content", "SUCCESS TEST");
		
		URL url = null;
		HttpURLConnection httpurlconnection = null;
		try {
			//URL
			content+="【微片商城】";
			String url_str = "http://221.179.180.158:9007/QxtSms/QxtFirewall";
			String param = "OperID="+OperID+"&OperPass="+OperPass+"&DesMobile="+mobile+"&Content="+content;
			
			url = new URL(url_str);
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setRequestMethod("GET");
			
			httpurlconnection.setRequestProperty("User-Agent", "directclient");  
			OutputStream  os = httpurlconnection.getOutputStream();
		    PrintWriter out = new PrintWriter(new OutputStreamWriter(os, "gb2312"));  
		    out.flush();
			out.println(param);  
			out.close();  
			
			
			java.io.InputStream in = httpurlconnection.getInputStream();
			java.io.BufferedReader breader = new BufferedReader(
					new InputStreamReader(in, "GBK"));
//			new InputStreamReader(in, "UTF-8"));
			String str = breader.readLine();
			StringBuffer s = new StringBuffer();
			s.append(str);
			while (str != null) {
				System.out.println(str);
				str = breader.readLine();
				if(str != null){
					s.append(str);					
				}
			}
			in.close();
			String result = s.toString();
			Document document = null;
			try {
				document = DocumentHelper.parseText(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(result);
			Element root = document.getRootElement();
			String code = root.elementText("code");
			JSONObject r=new JSONObject();
			json.put("code", code);
			return json;

		} catch (Exception e) {
			e.printStackTrace();
			json.put("content", e.getMessage());
		} finally {
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}
		return json;
	}
	
  /**
	 * 构建随机码
	 * @param length 随机码位数长度(最长9位)
	 * @return 随机码
	 * @author hyw
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

		
	
	public static void main(String[] args) throws IOException {
		 JSONObject result_sendMsg = SmsUtil.sendMsg("18750597620", "您正在申请忘记密码身份验证，验证码：461231，切勿将验证码泄露于他人。【微片】");
		 System.out.print(result_sendMsg.toString());
	}
}
