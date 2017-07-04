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
import com.platform.web.model.WebExtendConfig;
import com.platform.web.service.WebExtendConfigService;

@Controller
@RequestMapping("/WebExtendConfigAction")
public class WebExtendConfigAction extends BaseAction{
	
	
	@Autowired
	private WebExtendConfigService service = null;

	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		return "Admin/jsp/web/WebExtendConfig/WebExtendConfigList.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(WebExtendConfig model,HttpServletRequest request,HttpServletResponse response) {
		String toUrl = "Admin/jsp/web/WebExtendConfig/WebExtendConfigAdd.html";
		if(StringUtil.isNotNull(model.getFdId())) {
			WebExtendConfig item = service.get(model.getFdId());
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new WebExtendConfig());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(WebExtendConfig model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		List<WebExtendConfig> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WebExtendConfig item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdKey", item.getFdKey());
				o.put("fdName", item.getFdName());
				o.put("fdSeqNo", item.getFdSeqNo());
				o.put("fdValue", item.getFdValue());
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	@RequestMapping("/save")
	public void save(WebExtendConfig model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(model.getFdId())){
				service.update(model);
			}else{
				service.save(model);
			}
			//TemplateUtils.saveLeftHtml(request);
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
