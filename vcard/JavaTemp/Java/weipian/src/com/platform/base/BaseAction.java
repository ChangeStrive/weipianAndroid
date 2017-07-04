package com.platform.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.platform.sys.model.SysUser;
import com.platform.wp.model.AppUser;

public abstract class BaseAction{
	
	public Integer pageSize=20;
	
	protected SysUser getCurrentUser(HttpServletRequest request){
		return (SysUser) request.getSession().getAttribute(SystemConstants.SESSION_USER_ATTR);
	}
	
	protected AppUser getCurrentWebUser(HttpServletRequest request){
		return (AppUser) request.getSession().getAttribute(SystemConstants.SESSION_WEB_USER_ATTR);
	}
	
	protected void saveCurrentWebUser(HttpServletRequest request,AppUser user){
		 request.getSession().setAttribute(SystemConstants.SESSION_WEB_USER_ATTR,user);
	}
	
	
	
	protected void saveOpenId(HttpServletRequest request,String fdOpenId){
		request.getSession().setAttribute(SystemConstants.SESSION_WEIXIN_ID, fdOpenId);
	}
	
	protected String getOpenId(HttpServletRequest request){
		return (String) request.getSession().getAttribute(SystemConstants.SESSION_WEIXIN_ID);
	}
	
	protected String getWxUserName(HttpServletRequest request){
		return (String) request.getSession().getAttribute(SystemConstants.SESSION_WX_USERNAME);
	}
	
	protected void saveWxUserName(HttpServletRequest request,String userName){
		 request.getSession().setAttribute(SystemConstants.SESSION_WX_USERNAME,userName);
	}
	
	protected String getWxUserHeader(HttpServletRequest request){
		return (String) request.getSession().getAttribute(SystemConstants.SESSION_WX_USERHEADER);
	}
	
	protected void saveWxUserHeader(HttpServletRequest request,String fdUserHeader){
		 request.getSession().setAttribute(SystemConstants.SESSION_WX_USERHEADER,fdUserHeader);
	}
	
	protected String getWxUserSex(HttpServletRequest request){
		return (String) request.getSession().getAttribute(SystemConstants.SESSION_WX_USERSEX);
	}
	
	protected void saveWxUserSex(HttpServletRequest request,String fdSex){
		request.getSession().setAttribute(SystemConstants.SESSION_WX_USERSEX,fdSex);
	}
	
	public static void writeJSON(HttpServletResponse response,CharSequence... jsons) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if(jsons==null) return;
		PrintWriter out=response.getWriter();
		for(CharSequence json:jsons){
			out.append(json);
		}
	}
}
