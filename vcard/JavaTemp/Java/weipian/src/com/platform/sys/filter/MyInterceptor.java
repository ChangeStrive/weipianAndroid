package com.platform.sys.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.platform.base.SystemConstants;
import com.platform.sys.model.SysUser;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppUser;

public class MyInterceptor extends HandlerInterceptorAdapter{
	 	public void afterCompletion(HttpServletRequest request,  
	            HttpServletResponse response, Object arg2, Exception arg3)  
	            throws Exception {  
	        // TODO Auto-generated method stub  
	       // System.out.println("回调执行");  
	 		
	    }  
	  
	    public void postHandle(HttpServletRequest request, HttpServletResponse response,  
	            Object arg2, ModelAndView arg3) throws Exception {  
	        // TODO Auto-generated method stub  
	        //System.out.println("调用controller后进入");  
	    	try{
		    	HttpSession httpSession=request.getSession();
		    	String hxltUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		    	request.setAttribute("hxltUrl", hxltUrl);
		    	
		    	String downAction = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/file/";
		    	request.setAttribute("downAction", downAction);
		    	
		    	SysUser user=null;
		    	if(httpSession!=null){
		    		user=(SysUser)httpSession.getAttribute(SystemConstants.SESSION_USER_ATTR);
		    		if(user!=null){
		    			request.setAttribute("hasLogin", true);
		    			request.setAttribute("hxltUserName", user.getFdName());
		    		}
		    		
		    		String wid=(String)httpSession.getAttribute(SystemConstants.SESSION_WEIXIN_ID);
		    		if(StringUtil.isNotNull(wid)){
		    			request.setAttribute("hasWxId",1);
		    		}else{
		    			request.setAttribute("hasWxId",0);
		    		}
		    		
		    		AppUser mUser=(AppUser)httpSession.getAttribute(SystemConstants.SESSION_WEB_USER_ATTR);
		    		if(mUser==null){
		    			request.setAttribute("hasWxLogin",0);
		    		}else{
		    			request.setAttribute("hasWxLogin",1);
		    		}
		    		request.setAttribute("sysTime",System.currentTimeMillis());
		    		
		    		request.setAttribute("sysTime",System.currentTimeMillis());
		    	}
	    	}catch (Exception e) {
				// TODO: handle exception
	    		//e.printStackTrace();
	    		//System.out.println("有错页面："+request.getRequestURI());
	    		//e.printStackTrace();
			}
	    }  
	      
	    public boolean preHandle(HttpServletRequest 
	    		request, HttpServletResponse response,  
	            Object arg2) throws Exception {  
	        return true;  
	    }  
	    
	    
}
