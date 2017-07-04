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
import com.platform.util.FileUtil;
import com.platform.util.StringUtil;
import com.platform.web.model.WebInfo;
import com.platform.web.service.WebInfoService;

@Controller
@RequestMapping("/WebInfoAction")
public class WebInfoAction extends BaseAction{
	
	
	@Autowired
	private WebInfoService service = null;

	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		String fdTypeId=request.getParameter("fdTypeId");
		request.setAttribute("fdTypeId",fdTypeId);
		return "Admin/jsp/web/WebInfo/infoList.html";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd(HttpServletRequest request,HttpServletResponse response){
		String fdTypeId=request.getParameter("fdTypeId");
		request.setAttribute("fdTypeId",fdTypeId);
		return "Admin/jsp/web/WebInfo/infoAdd.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(String fdId,HttpServletRequest request,HttpServletResponse response) {
		
		String toUrl = "Admin/jsp/web/WebInfo/infoAdd.html";
		String fdTypeId=request.getParameter("fdTypeId");
		request.setAttribute("fdTypeId", fdTypeId);
		if(StringUtil.isNotNull(fdId)) {
			WebInfo item = service.get(fdId);
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new WebInfo());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(WebInfo model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		List<WebInfo> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WebInfo item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdTitle", item.getFdTitle());
				o.put("fdDesc", item.getFdDesc());
				o.put("fdPicUrl", item.getFdPicUrl());
				o.put("fdTypeId", item.getFdTypeId());
				o.put("fdTypeName", item.getFdTypeName());
				o.put("fdCreateTime", item.getFdCreateTime());
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	@RequestMapping("/save")
	public void save( WebInfo model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			WebInfo item=null;
			if(StringUtil.isNotNull(model.getFdId())){
				item=service.update(model);
			}else{
				item=service.save(model);
			}
			
			if(model.getFdTypeId().equals("2")){
				//TemplateUtils.saveNewsDetail(request,item);
			}else if(model.getFdTypeId().equals("1")){
				//TemplateUtils.saveAboutUsDetail(request);
			}else if(model.getFdTypeId().equals("0")){
				//TemplateUtils.saveContantUsDetail(request);
			}else if(model.getFdTypeId().equals("3")){
				//TemplateUtils.saveWhyUsDetail(request);
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
				String[] ids=fdId.split(",");
				for(int i=0;i<ids.length;i++){
					String path="web/it/html/news/"+ids[i]+".ftl";
					FileUtil.deleteTomcatFile(request, path);
				}
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
