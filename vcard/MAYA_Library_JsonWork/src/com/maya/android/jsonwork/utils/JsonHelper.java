package com.maya.android.jsonwork.utils;

import java.lang.reflect.Type;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maya.android.utils.Helper;
/**
 * Gson 对象序列化反序列化帮助类</br>
 * 在Entity里面的变量前加上"@SerializedName ("XXXXX")"</br>
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-8
 *
 */
public class JsonHelper {
	private static Gson sGson = null;
	private static Gson sExposeGson = null;
	/**
	 * 取得gson对象
	 * @return Gson
	 */
	public static Gson getGson(){
		if(Helper.isNull(sGson)){
			sGson = new Gson();
		}
		return sGson;
	}
	/**
	 * 取得具有Expose属性的gson对象
	 * @return Gson
	 */
	public static Gson getExposeGson(){
		if(Helper.isNull(sExposeGson)){
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.excludeFieldsWithoutExposeAnnotation();
			sExposeGson = gsonBuilder.create();
		}
		return sExposeGson;
	}
	/**
	 * 将对象序列化为JSON字符串
	 * @param src
	 * @return String
	 */
	public static String toJson(Object src){
		return getGson().toJson(src);
	}
	/**
	 * 根据指定类型将对象序列化为JSON字符串
	 * @param src
	 * @param typeOfSrc
	 * @return String
	 */
	public static String toJson(Object src, Type typeOfSrc){
		return getGson().toJson(src, typeOfSrc);
	}
	/**
	 * 将对象序列化为JSON字符串，过滤Expose
	 * @param src
	 * @return String
	 */
	public static String toExposeJson(Object src){
		return getExposeGson().toJson(src);
	}
	/**
	 * 根据指定类型将对象序列化为JSON字符串，过滤Expose
	 * @param src
	 * @param typeOfSrc
	 * @return String
	 */
	public static String toExposeJson(Object src, Type typeOfSrc){
		return getExposeGson().toJson(src, typeOfSrc);
	}
	/**
	 * 从JSON字符串反序列化为指定类型的对象
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T>T fromJson(String json, Class<T> classOfT){
		return getGson().fromJson(json, classOfT);
	}
	/**
	 * 从JSON对象反序列化为指定类型的对象
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T>T fromJson(JSONObject json, Class<T> classOfT){
		if(Helper.isNotNull(json)){
			return fromJson(json.toString(), classOfT);
		}
		return null;
	}
	/**
	 * 从JSON字符串反序列化为指定类型的对象
	 * @param json
	 * @param typeOfT
	 * @return
	 */
	public static <T>T fromJson(String json, Type typeOfT){
		return getGson().fromJson(json, typeOfT);
	}
	/**
	 * 从JSON对象反序列化为指定类型的对象
	 * @param json
	 * @param typeOfT
	 * @return
	 */
	public static <T>T fromJson(JSONObject json, Type typeOfT){
		if(Helper.isNotNull(json)){
			return fromJson(json.toString(), typeOfT);
		}
		return null;
	}
	/**
	 * 从JSON字符串反序列化为指定类型的对象，过滤Expose
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T>T fromExposeJson(String json, Type typeOfT){
		return getExposeGson().fromJson(json, typeOfT);
	}
	/**
	 * 从JSON对象反序列化为指定类型的对象，过滤Expose
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T>T fromExposeJson(JSONObject json, Type typeOfT){
		if(Helper.isNotNull(json)){
			return fromExposeJson(json.toString(), typeOfT);
		}
		return null;
	}
	/**
	 * 从JSON字符串反序列化为指定类型的对象，过滤Expose
	 * @param json
	 * @param typeOfT
	 * @return
	 */
	public static <T>T fromExposeJson(String json, Class<T> classOfT){
		return getExposeGson().fromJson(json, classOfT);
	}
	/**
	 * 从JSON对象反序列化为指定类型的对象，过滤Expose
	 * @param json
	 * @param typeOfT
	 * @return
	 */
	public static <T>T fromExposeJson(JSONObject json, Class<T> classOfT){
		if(Helper.isNotNull(json)){
			return fromExposeJson(json.toString(), classOfT);
		}
		return null;
	}
	
}
