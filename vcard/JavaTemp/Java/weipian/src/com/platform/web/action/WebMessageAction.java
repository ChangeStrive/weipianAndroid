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
import com.platform.util.DateUtils;
import com.platform.util.StringUtil;
import com.platform.web.model.WebMessage;
import com.platform.web.service.WebMessageService;

@Controller
@RequestMapping("/WebMessageAction")
public class WebMessageAction extends BaseAction {

	@Autowired
	private WebMessageService service = null;

	@RequestMapping("/list")
	public String list(){
		return "Admin/jsp/web/WebMessage/WebMessageList.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(String fdId,HttpServletRequest request,HttpServletResponse response) {
		
		String toUrl = "Admin/jsp/web/WebMessage/WebMessageAdd.html";
		if(StringUtil.isNotNull(fdId)) {
			WebMessage item = service.get(fdId);
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new WebMessage());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(WebMessage model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		List<WebMessage> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WebMessage item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdUserName", item.getFdUserName());
				o.put("fdCreateTime", item.getFdCreateTime());
				o.put("fdEmail", item.getFdEmail());
				o.put("fdTel", item.getFdTel());
				
				o.put("fdContent", item.getFdContent());
				o.put("fdReplay", item.getFdReplay());
				o.put("fdStatus", item.getFdStatus());
				
				array.add(o);
			}
		}
		object.put("totalSize", count);
		object.put("success", true);
		object.put("list", array);
		writeJSON(response,object.toString());
	}

	
	@RequestMapping("/getMsgBoardList")
	public void getMsgBoardList(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		List<WebMessage> list=service.getMsgBoardList(start, limit);
		int count=service.getMsgBoardListCount();
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				WebMessage item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdUserName", item.getFdUserName());
				o.put("fdCreateTime", item.getFdCreateTime());
				o.put("fdEmail", item.getFdEmail());
				o.put("fdTel", item.getFdTel());
				
				o.put("fdContent", item.getFdContent());
				o.put("fdReplay", item.getFdReplay());
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
	public void save(WebMessage model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			if(StringUtil.isNotNull(model.getFdId())){
				service.update(model);
			}else{
				model.setFdCreateTime(DateUtils.getNow());
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
