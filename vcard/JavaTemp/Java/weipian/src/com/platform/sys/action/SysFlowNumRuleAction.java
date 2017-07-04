package com.platform.sys.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.base.BaseAction;
import com.platform.sys.model.SysFlowNumRule;
import com.platform.sys.service.SysFlowNumRuleService;
import com.platform.util.FlowNumRuleUtils;
import com.platform.util.StringUtil;

@Controller
@RequestMapping("/SysFlowNumRuleAction")
public class SysFlowNumRuleAction extends BaseAction  {
	
	@Autowired
	private SysFlowNumRuleService flowNumRuleSrv = null;

	
	@RequestMapping("/list")
	public String list(){
		return "Admin/jsp/sys/flow/flowList.html";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd(){
		return "Admin/jsp/sys/flow/flowAdd.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(SysFlowNumRule model,HttpServletRequest request) {
		String toUrl = "Admin/jsp/sys/flow/flowAdd.html";
		if(StringUtil.isNotNull(model.getFdId())) {
			SysFlowNumRule item = flowNumRuleSrv.get(model.getFdId());
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new SysFlowNumRule());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(SysFlowNumRule model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String strStart=request.getParameter("start");
		String strLimit=request.getParameter("limit");
		int start=0;
		int limit=pageSize;
		if(StringUtil.isNotNull(strStart)){
			start=Integer.parseInt(strStart);
		}
		
		if(StringUtil.isNotNull(strLimit)){
			limit=Integer.parseInt(strLimit);
		}
		Map<String,String> map=StringUtil.getParams(request, "");
		List<SysFlowNumRule> list=flowNumRuleSrv.list(map,model,start, limit);
		int count=flowNumRuleSrv.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				SysFlowNumRule item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdClassName", item.getFdClassName());
				o.put("fdRemark", item.getFdRemark());
				o.put("fdRule", item.getFdRule());
				o.put("fdStartValue", item.getFdStartValue());
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	@RequestMapping("/save")
	public void save(SysFlowNumRule model,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(model.getFdId())){
				flowNumRuleSrv.update(model);
			}else{
				flowNumRuleSrv.save(model);
			}
			result.put("success", true);
			result.put("msg", "保存成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "保存失败!");
		}
		writeJSON(response,result.toString());
	}
	
	@RequestMapping("/delete")
	public void delete(String fdId,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)){
				flowNumRuleSrv.delete(fdId);
			}
			result.put("success", true);
			result.put("msg", "删除成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "删除失败!");
		}
		writeJSON(response,result.toString());
	}
	
	@RequestMapping("/getFlowNum")
	public void getFlowNum(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String className=request.getParameter("className");
		JSONObject result=new JSONObject();
		if(StringUtil.isNotNull(className)){
			String flowNum=FlowNumRuleUtils.getFlowNum(className);
			result.put("flowNum", flowNum);
			result.put("success", true);
			result.put("msg", "保存成功!");
		}else{
			result.put("success", false);
			result.put("msg", "保存失败!");
		}
		writeJSON(response,result.toString());
	}
	
}
