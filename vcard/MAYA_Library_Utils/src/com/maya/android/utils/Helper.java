package com.maya.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

import com.maya.android.utils.base.AES;
import com.maya.android.utils.base.MD5;

import android.annotation.SuppressLint;
import android.os.SystemClock;

/**
 * 通用帮助类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-1
 *
 */
public class Helper {
	/**
	 * 判断对象是否为NULL
	 * @param object 判断对象
	 * @return boolean 是否为空
	 */
	public static final boolean isNull(Object object){
		return null == object;
	}
	/**
	 * 判断对象是否不为NULL
	 * @param object 判断对象
	 * @return boolean 是否不为null
	 */
	public static final boolean isNotNull(Object object){
		return !Helper.isNull(object);
	}
	/**
	 * 若判断对象为null输出空值
	 * @param obj 输出对象
	 * @return 
	 */
	public static String ifNull(Object obj){
		return Helper.ifNull(obj, "");
	}
	/**
	 * 若判断对象为null输出默认值
	 * @param obj 输出对象
	 * @param defaultValue 默认值
	 * @return 
	 */
	public static String ifNull(Object obj, String defaultValue){
		if (Helper.isNull(obj)){
			return defaultValue;
		}else{
			return obj.toString();
		}
	}
	/**
	 * 判断对象是否为NULL或空
	 * @param object 判断对象
	 * @return boolean 是否为NULL或空
	 */
	public static final boolean isEmpty(Object object){
		boolean result = false;
		if(Helper.isNull(object)){
			result = true;
		}else{
			if(object instanceof String){
				result = ((String) object).equals("");
			}else if(object instanceof Date){
				result = ((Date) object).getTime() == 0;
			}else if(object instanceof Long){
				result = ((Long) object).longValue() == Long.MIN_VALUE;
			}else if(object instanceof Integer){
				result = ((Integer) object).intValue() == Integer.MIN_VALUE;
			}else if(object instanceof Collection){
				result = ((Collection<?>) object).size() == 0;
			}else if(object instanceof Map){
				result = ((Map<?, ?>) object).size() == 0;
			}else if(object instanceof JSONObject){
				result = !((JSONObject) object).keys().hasNext();
			}else{
				Class<? extends Object> type = object.getClass();
				if(type.isArray()){
					if(Array.getLength(object) == 0){
						result = true;
					}else{
						result = false;
					}
				}else{
					result = object.toString().equals("");
				}
			}
		}
		return result;
	}
	/**
	 * 判断对象是否不为NULL或空
	 * @param object 判断对象
	 * @return boolean 是否不为NULL或空
	 */
	public static final boolean isNotEmpty(Object object){
		return !Helper.isEmpty(object);
	}
	/**
	 * 判断两字符串是否相等(包含对null的判断)
	 * @param str1
	 * @param str2
	 * @param ignorSpace 是否忽略空格
	 * @return boolean 是否相等
	 */
	public static boolean equalString(String str1, String str2, boolean ignorSpace){
		return Helper.equalString(str1, str2, ignorSpace, false);
	}
	/**
	 * 判断两字符串是否相等(包含对null的判断)
	 * @param str1
	 * @param str2
	 * @param ignorSpace 是否忽略空格
	 * @param ignorCase  是否忽略大小写
	 * @return boolean 是否相等
	 */
	public static boolean equalString(String str1, String str2, boolean ignorSpace, boolean ignorCase){
		if(Helper.isNull(str1) && Helper.isNull(str2)){
			return true;
		}
		if((Helper.isNull(str1) && Helper.isNotNull(str2)) || (Helper.isNotNull(str1) && Helper.isNull(str2))){
			return false;
		}
		if(ignorSpace){
			if(ignorCase){
				return str1.trim().equalsIgnoreCase(str2.trim());
			}else{
				return str1.trim().equals(str2.trim());
			}
		}else{
			if(ignorCase){
				return str1.equalsIgnoreCase(str2);
			}else{
				return str1.equals(str2);
			}
		}
	}
	@SuppressWarnings("unused")
	private String mVerification = "135270700791922253";
	/**
	 * 根据时间生成int类型的tag值
	 * @return
	 */
	public static final int createIntTag(){
		int result = -1;
		try{
			result = Long.valueOf(SystemClock.currentThreadTimeMillis() % Integer.MAX_VALUE).intValue();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 从file转为byte[]
	 * @param file
	 * @return
	 */
	public static byte[] getBytesFromFile(File file) {
		if (file == null) {
			return null;
		}
		FileInputStream stream = null;
		ByteArrayOutputStream out = null;
		try {
			stream = new FileInputStream(file);
			out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (Helper.isNotNull(stream)){
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (Helper.isNotNull(out)){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * AES加密
	 * @param content 待加密的内容
	 * @param key 加密密钥
	 * @return 加密后的内容
	 */
	public static byte[] encryptByAES(String content, String key){
		return AES.encrypt(content, key);
	}
	/**
	 * AES解密
	 * @param content 待解密的内容
	 * @param key 解密密钥
	 * @return 解密后的内容
	 */
	public static String decryptByAES(byte[] content, String key){
		try{
			return new String(AES.decrypt(content, key));
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	/**
	 * MD5加密
	 * @param str
	 * @return
	 */
	public static String md5(String str){
		return MD5.md5(str);
	}
}
