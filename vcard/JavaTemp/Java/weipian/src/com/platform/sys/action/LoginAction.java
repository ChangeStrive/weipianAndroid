package com.platform.sys.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.base.SystemConstants;
import com.platform.sys.model.SysMenu;
import com.platform.sys.model.SysUser;
import com.platform.sys.service.SysMenuService;
import com.platform.sys.service.SysUserService;
import com.platform.util.DateUtils;
import com.platform.util.StringUtil;

@Controller
@RequestMapping("/LoginAction")
public class LoginAction  extends BaseAction{

	@Autowired
	private SysUserService userSrv = null;
	
	@Autowired
	private SysMenuService menuSvc;
	
	@RequestMapping("/toLogin")
	public String toLogin(HttpServletRequest request){
		String backUrl=request.getParameter("backUrl");
		request.setAttribute("backUrl", backUrl);
		return "Admin/jsp/login.html";
	}
	
	@RequestMapping("/toIndex")
	public String toIndex(HttpServletRequest request) throws UnsupportedEncodingException{
		SysUser user=getCurrentUser(request);
		if(user!=null){
			SysMenu pidMenu=menuSvc.getFirstMenuByUserOfFirst(user.getFdId());
			if(pidMenu!=null){
				if(StringUtil.isNotNull(pidMenu.getFdUrl())){
					return "forward:/"+pidMenu.getFdUrl();
				}else{
					if(pidMenu.getFdLevel()==1){
						SysMenu menu=menuSvc.getSecondMenuByUserOfFirst(user.getFdId(),pidMenu.getFdId());
						if(menu!=null&&StringUtil.isNotNull(menu.getFdUrl())){
							return "forward:/"+menu.getFdUrl();
						}
					}
				}
			}
		}else{
			return "redirect:/LoginAction/toLogin?backUrl="+StringUtil.getFormatUrl(request,"LoginAction/toIndex");
		}
		return "Admin/jsp/error404.html";
	}
	
	@RequestMapping("/error404")
	public String toError404(){
		return "/Admin/jsp/error404.html";
	}
	
	@RequestMapping("/login")
	public void login(SysUser model,HttpServletRequest request,HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException{
		HttpSession session = request.getSession();
		
		JSONObject result = new JSONObject();
		
		response.setCharacterEncoding("UTF-8");
	
		if(model.getFdLoginName().equals("") || model.getFdLoginName() == null) {
			result.put("success", false);
			result.put("msg", "请输入用户账号！");
			writeJSON(response,result.toString());
			return ;
		}
		if(model.getFdPwd().equals("") || model.getFdPwd() == null) {
			result.put("success", false);
			result.put("msg", "请输入登录密码！");
			writeJSON(response,result.toString());
			return ;
		}
		String fdCode=request.getParameter("fdCode");
		if(!StringUtil.isNotNull(fdCode)){
			result.put("success", false);
			result.put("msg", "请输入验证码！");
			writeJSON(response,result.toString());
			return ;
		}
		String fdCode2=(String) request.getSession().getAttribute("rCode");
		if(!fdCode.equals(fdCode2)){
			result.put("success", false);
			result.put("msg", "验证码不正确！");
			writeJSON(response,result.toString());
			return ;
		}
		
		SysUser user = userSrv.getSysUserByFdLoginName(model.getFdLoginName());
		if(user == null) {
			result.put("success", false);
			result.put("msg", "用户账号不存在！");
		} else if(!model.getFdPwd().equals(user.getFdPwd())) {
			result.put("success", false);
			result.put("msg", "登录密码错误！");
		} else if(!user.getFdStatus().equals("1")) {
			result.put("success", false);
			result.put("msg", "账户已被禁用！");
		} else {
			SysUser suser=getCurrentUser(request);
			if(null != suser) {
				session.invalidate();
				session=request.getSession(true);
			}
			
			SysUser uUser=new SysUser();
			uUser.setFdId(user.getFdId());
			uUser.setFdLoginIp(request.getRemoteAddr());
			uUser.setFdLastTime(DateUtils.getNow());
			userSrv.update(uUser);
			
			session.setAttribute(SystemConstants.SESSION_USER_ATTR, user);
			result.put("success", true);
			result.put("msg", "登录成功！");
		}
		writeJSON(response,result.toString());
	}

	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		request.setAttribute("hasLogin", null);
		return "Admin/jsp/login.html";
	}
}
