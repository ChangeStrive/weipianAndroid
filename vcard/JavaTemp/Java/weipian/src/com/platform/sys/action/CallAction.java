package com.platform.sys.action;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.misc.BASE64Decoder;

import com.platform.base.BaseAction;
import com.platform.util.IpUtils;
import com.platform.util.PropertiesUtil;

@Controller
@RequestMapping("/CallAction")
public class CallAction extends BaseAction {

//	private String msg;// 请求的参数
//	private String reStr;// 返回的信息
//	private String param;
//	private String path;// 要下载的文件地址
	
	@RequestMapping("/post")
	public void post(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String msg=(String)request.getParameter("msg");// 请求的参数
		String reStr=(String)request.getParameter("reStr");// 返回的信息
		String param=(String)request.getParameter("param");
		String path=(String)request.getParameter("path");// 要下载的文件地址
		String msgInfo = doMsg(msg);
		JSONObject json = JSONObject.fromObject(msgInfo);
		String method = json.getString("method");
		Object[] obj = null;
		if (null != param) {
			String s8 = new String(param.getBytes("ISO-8859-1"), "UTF-8");
			obj = JSONArray.fromObject(s8).toArray();
		}
		String str="";
		if(obj!=null){
			for(int i=0;i<obj.length;i++){
				str+=obj[i];
				if(i<obj.length-1){
					str+=",";
				}
			}
		}
		
		String ip=IpUtils.getIpAddr(request);
		if(method.equals("reg")||method.equals("login")){
			Object[] b=new Object[obj.length+1];
			for(int i=0;i<obj.length;i++){
				b[i]=obj[i];
			}
			b[obj.length]=ip;
			obj=b;
		}
		String ret = doCall(method, obj);
//		System.out.println(ret);
		writeJSON(response,ret);
	}
	@RequestMapping("/call")
	public void call(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String msg=(String)request.getParameter("msg");// 请求的参数
		String msgInfo = doMsg(msg);
		JSONObject json = JSONObject.fromObject(msgInfo);
		String method = json.getString("method");

		Object jsobj = null;
		Object[] param = null;
		try {
			jsobj = json.get("param");
			if (null != jsobj) {
				param = json.getJSONArray("param").toArray();
			}
		} catch (Exception e) {
		}
		
		String str="";
		if(param!=null){
			for(int i=0;i<param.length;i++){
				str+=param[i];
				if(i<param.length-1){
					str+=",";
				}
			}
		}
		
		String ip=IpUtils.getIpAddr(request);
		if(method.equals("reg")||method.equals("login")){
			Object[] b=new Object[param.length+1];
			for(int i=0;i<param.length;i++){
				b[i]=param[i];
			}
			b[param.length]=ip;
			param=b;
		}
		String ret = doCall(method, param);
//		System.out.println(ret);
		writeJSON(response,ret);
	}

	/**
	 * 解析出请求参数
	 * 
	 * @return
	 * @throws IOException
	 */
	public String doMsg(String msg) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(msg);
		return new String(b, "UTF-8");
	}

	/**
	 * 调用请求
	 * 
	 * @param methodName
	 * @param params
	 * @return
	 */
	private String doCall(String methodName, Object[] params) {
		PropertiesUtil mPropertiesUnit = new PropertiesUtil();
		Properties mProperties = mPropertiesUnit
				.getProperties("ActionConfig.properties");
		String className = mProperties.getProperty(methodName);

		Object cls = getInstance(className);

		String ret = null;
		try {
			Method method = null;
			if (null == params || params.length == 0) {
				method = Class.forName(className).getMethod(methodName);
				ret = (String) method.invoke(cls);
			} else {
				Class<String>[] parameterTypes = new Class[params.length];
				for (int i = 0; i < params.length; i++) {
					parameterTypes[i] = String.class;
				}
				method = Class.forName(className).getMethod(methodName,
						parameterTypes);
				ret = (String) method.invoke(cls, params);
			}
			//System.out.println("Class Name : "+className+",Method Name: "+methodName);
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		return ret;
	}

	private Object getInstance(String className) {
		Object obj;
		try {
			obj = Class.forName(className).newInstance();
			return obj;
		} catch (Exception e) {
			return null;
		}
	}

	public Object getModel() {
		return null;
	}
}
