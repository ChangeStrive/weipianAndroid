package com.platform.wp.action;

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
import com.platform.sys.model.SysUser;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppConfig;
import com.platform.wp.service.AppConfigService;

@Controller
@RequestMapping("/AppConfigAction")
public class AppConfigAction extends BaseAction{
	
	
	@Autowired
	private AppConfigService service = null;

	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		return "Admin/jsp/app/AppConfig/AppConfigList.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(AppConfig model,HttpServletRequest request,HttpServletResponse response) {
		String toUrl = "Admin/jsp/app/AppConfig/AppConfigAdd.html";
		if(StringUtil.isNotNull(model.getFdId())) {
			AppConfig item = service.get(model.getFdId());
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new AppConfig());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(AppConfig model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		SysUser user=getCurrentUser(request);
		List<AppConfig> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppConfig item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdKey", item.getFdKey());//唯一标识
				o.put("fdValue", item.getFdValue());//值
				o.put("fdRemark", item.getFdRemark());//备注
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	@RequestMapping("/save")
	public void save(AppConfig model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			
			SysUser user=getCurrentUser(request);
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
