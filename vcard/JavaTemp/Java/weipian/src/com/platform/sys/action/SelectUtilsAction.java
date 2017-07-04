package com.platform.sys.action;


import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.sys.service.SelectUtilService;
import com.platform.util.StringUtil;

@Controller
@RequestMapping("/SelectUtilsAction")
public class SelectUtilsAction extends BaseAction{
	
	@Autowired
	private SelectUtilService selectSvc;
	
	/**
	 * 选择当前分公司下商家用户
	 * @throws IOException 
	 */
	@RequestMapping("/selectSeller")
	public void selectSeller(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,String> map=StringUtil.getParams(request, "limit,valueId,fdCode,fdName,fdCompanyId");
		JSONObject object=selectSvc.selectSeller(map);
		writeJSON(response,object.toString());
	}
	
	/**
	 * 选择当前分公司下众筹股东
	 * @throws IOException 
	 */
	@RequestMapping("/selectSalesman")
	public void selectSalesman(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,String> map=StringUtil.getParams(request, "limit,valueId,fdReferralCode,fdName,fdCompanyId,fdTel");
		JSONObject object=selectSvc.selectSalesman(map);
		writeJSON(response,object.toString());
	}
	
	/**
	 * 选择当前分公司下的未使用的会员卡
	 * @throws IOException 
	 */
	@RequestMapping("/selectNoZvCard")
	public void selectNoZvCard(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,String> map=StringUtil.getParams(request, "limit,valueId,fdReferralCode,fdName,fdCompanyId,fdTel,fdNo,fdSalesmanId");
		JSONObject object=selectSvc.selectNoZvCard(map);
		writeJSON(response,object.toString());
	}
	
	
	/**
	 * 通用下拉框选择
	 * @throws IOException 
	 * @throws IOException 
	 */
	@RequestMapping("/commonCommob")
	public void commonCommob(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String key=request.getParameter("key");
		JSONObject object=selectSvc.commonCommob(key);
		writeJSON(response,object.toString());
	}
	
	/**
	 * 选择分公司
	 * @throws IOException 
	 */
	@RequestMapping("/selectCompany")
	public void selectCompany(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String,String> map=StringUtil.getParams(request, "limit,valueId,fdName,fdAreaName");
		JSONObject object=selectSvc.selectCompany(map);
		writeJSON(response,object.toString());
	}
}
