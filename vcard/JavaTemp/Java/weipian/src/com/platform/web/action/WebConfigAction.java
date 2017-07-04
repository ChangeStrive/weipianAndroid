package com.platform.web.action;

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
import com.platform.util.StringUtil;
import com.platform.web.model.WebConfig;
import com.platform.web.service.WebConfigService;

@Controller
@RequestMapping("/WebConfigAction")
public class WebConfigAction extends BaseAction{
	
	@Autowired
	private WebConfigService service = null;

	
	
	@RequestMapping("/toTest")
	public String toTest(HttpServletRequest request,HttpServletResponse response){
		String code=request.getParameter("code");
		request.setAttribute("code", code);
		return "web/jsp/test.html";
	}

	@RequestMapping("/toIndex")
	public String toIndex(){
		return "web/jsp/index.html";
	}
	
	@RequestMapping("/toZhuce")
	public String toZhuce(){
		return "web/jsp/zhuce.html";
	}
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		
		WebConfig item = service.get();
		if(item != null) {
			request.setAttribute("item", item);
		}
		return "Admin/jsp/web/WebConfig/config.html";
	}
	
	@RequestMapping("/getList")
	public void getList(WebConfig model,HttpServletRequest request,HttpServletResponse response) throws IOException{

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
		List<WebConfig> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WebConfig item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdWebName", item.getFdWebName());
				o.put("fdLogoPicUrl", item.getFdLogoPicUrl());
				o.put("fdCopyright", item.getFdCopyright());
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	@RequestMapping("/save")
	public void save(WebConfig model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(model.getFdId())){
				service.update(model);
			}else{
				service.save(model);
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
	public void delete(String fdId,HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)){
				service.delete(fdId);
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
	
}
