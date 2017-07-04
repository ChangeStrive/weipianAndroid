package com.platform.weixin.action;

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
import com.platform.weixin.model.WeiXinWelcomes;
import com.platform.weixin.service.WeiXinWelcomesService;

@Controller
@RequestMapping("/WeiXinWelcomesAction")
public class WeiXinWelcomesAction extends BaseAction{
	
	
	@Autowired
	private WeiXinWelcomesService service = null;

	
	public String preview(){
		return "Admin/jsp/weixin/WeiXinWelcomes/welcomesList.html"; 
	}
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		
		String fdPid=request.getParameter("fdPid");
		if(!StringUtil.isNotNull(fdPid)){
			fdPid="#";
		}
		request.setAttribute("fdPid", fdPid);
		return "Admin/jsp/weixin/WeiXinWelcomes/welcomesTabList.html";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd(){
		return "Admin/jsp/web/WeiXinWelcomes/infoAdd.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(String fdId,HttpServletRequest request,HttpServletResponse response) {
		
		String toUrl = "Admin/jsp/weixin/WeiXinWelcomes/welcomesAdd.html";
		if(StringUtil.isNotNull(fdId)){
			WeiXinWelcomes item=service.get(fdId);
			request.setAttribute("item", item);
			request.setAttribute("fdPid", item.getFdPid());
		}else{
			String fdPid=request.getParameter("fdPid");
			if(!StringUtil.isNotNull(fdPid)){
				fdPid="#";
			}
			request.setAttribute("fdPid", fdPid);
			request.setAttribute("item", new WeiXinWelcomes());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(WeiXinWelcomes model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		Map<String,String> map=StringUtil.getParams(request, "");
		List<WeiXinWelcomes> list=service.list(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WeiXinWelcomes item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdTitle", item.getFdTitle());
				o.put("fdPicUrl", item.getFdPicUrl());
				o.put("fdContent", item.getFdContent());
				o.put("fdCreateTime", item.getFdCreateTime());
				o.put("fdStatus", item.getFdStatus());
				if(item.getFdType().equals("1")) {
					List<WeiXinWelcomes> child = service.getListById(item.getFdId());
					JSONArray childArray = new JSONArray();
					if(list != null && list.size() > 0) {
						for (int j = 0; j < child.size(); j++) {
							JSONObject cObj = new JSONObject();
							WeiXinWelcomes cWelcomes = child.get(j);
							cObj.put("fdId", cWelcomes.getFdId());
							cObj.put("fdTitle", cWelcomes.getFdTitle());
							cObj.put("fdPicUrl", cWelcomes.getFdPicUrl());
							cObj.put("fdContent", cWelcomes.getFdContent());
							cObj.put("fdCreateTime", cWelcomes.getFdCreateTime());
							childArray.add(cObj);
						}
						
						o.put("child", childArray);
					}
				}
				array.add(o);
			}
		}
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}
	
	@RequestMapping("/getTabList")
	public void getTabList(WeiXinWelcomes model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		List<WeiXinWelcomes> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WeiXinWelcomes item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdTitle", item.getFdTitle());
				o.put("fdUrl", item.getFdUrl());
				o.put("fdPid", item.getFdPid());
				o.put("fdPidName", item.getFdPidName());
				o.put("fdPicUrl", item.getFdPicUrl());
				o.put("fdContent", item.getFdContent());
				o.put("fdType", item.getFdType());
				o.put("fdCreateTime", item.getFdCreateTime());
				o.put("fdStatus", item.getFdStatus());
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	@RequestMapping("/save")
	public void save(WeiXinWelcomes model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(model.getFdId())){
				service.update(model);
			}else{
				if(StringUtil.isNotNull(model.getFdPid())&&!model.getFdPid().equals("#")) {
					WeiXinWelcomes menu = service.get(model.getFdPid());
					model.setFdPidName(menu.getFdTitle());
				} else {
					model.setFdPid("#");
				}
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
	
	@RequestMapping("/setDefault")
	public void setDefault(String fdId,HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try {
			if(StringUtil.isNotNull(fdId)){
				service.setDefault(fdId);
			}
			result.put("success", true);
			result.put("msg", "设置成功!");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "删除失败!");
		}
		writeJSON(response,result.toString());
	}
	
	@RequestMapping("/modifStatus")
	public void modifStatus(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String fdId=request.getParameter("fdId");
		String fdStatus=request.getParameter("fdStatus");
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(fdId)){
				service.modifStatus(fdId,fdStatus);
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

}
