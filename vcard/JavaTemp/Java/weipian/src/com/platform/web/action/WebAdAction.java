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
import com.platform.web.model.WebAd;
import com.platform.web.service.WebAdService;

@Controller
@RequestMapping("/WebAdAction")
public class WebAdAction extends BaseAction{
	
	
	@Autowired
	private WebAdService service = null;

	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		String fdTypeId=request.getParameter("fdTypeId");
		request.setAttribute("fdTypeId",fdTypeId);
		return "Admin/jsp/web/WebAd/WebAdList.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(WebAd model,HttpServletRequest request,HttpServletResponse response) {
		String fdTypeId=request.getParameter("fdTypeId");
		String toUrl = "Admin/jsp/web/WebAd/WebAdAdd.html";
		if(StringUtil.isNotNull(model.getFdId())) {
			WebAd item = service.get(model.getFdId());
			request.setAttribute("item", item);
			fdTypeId=item.getFdTypeId();
		}else{
			request.setAttribute("item", new WebAd());
		}
		request.setAttribute("fdTypeId",fdTypeId);
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(WebAd model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		List<WebAd> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WebAd item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdPicUrl", item.getFdPicUrl());
				o.put("fdTitle", item.getFdTitle());
				o.put("fdSeqNo", item.getFdSeqNo());
				o.put("fdUrl", item.getFdUrl());
				o.put("fdTypeId", item.getFdTypeId());
				o.put("fdTypeName", item.getFdTypeName());
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	@RequestMapping("/save")
	public void save(WebAd model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
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
				service.delete(request,fdId);
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
