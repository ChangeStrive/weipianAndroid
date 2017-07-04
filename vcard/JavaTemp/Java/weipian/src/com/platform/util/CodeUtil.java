package com.platform.util;

import javax.servlet.http.HttpServletResponse;

/**
 * 二维码在线显示工具
 * @author Administrator
 *
 */
public class CodeUtil {
	
	public static void encoderQRCoder(String content, HttpServletResponse response) {
		try {
			QRCodeUtil.encode(content, null, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void encoderQRCoder(String content,String imageUrl,HttpServletResponse response) {
		try {
			QRCodeUtil.encode(content, imageUrl, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
