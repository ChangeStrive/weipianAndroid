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
import com.platform.util.DateUtils;
import com.platform.util.StringUtil;
import com.platform.wp.model.AppUser;
import com.platform.wp.service.AppUserService;

@Controller
@RequestMapping("/AppUserAction")
public class AppUserAction extends BaseAction{
	
	@Autowired
	private AppUserService service = null;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		return "Admin/jsp/app/AppUser/AppUserList.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(AppUser model,HttpServletRequest request,HttpServletResponse response) {
		String toUrl = "Admin/jsp/app/AppUser/AppUserAdd.html";
		if(StringUtil.isNotNull(model.getFdId())) {
			AppUser item = service.get(model.getFdId());
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new AppUser());
		}
		return toUrl;
	}
	@RequestMapping("/editCard")
	public String editCard(AppUser model,HttpServletRequest request,HttpServletResponse response) {
		String toUrl = "Admin/jsp/app/AppUser/editCard.html";
		if(StringUtil.isNotNull(model.getFdId())) {
			AppUser item = service.get(model.getFdId());
			request.setAttribute("item", item);
		}else{
			return "redirect:AppUserAction/list";
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(AppUser model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		List<AppUser> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppUser item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdCode", item.getFdCode());
				o.put("fdName", item.getFdName());
				o.put("fdAmount", item.getFdAmount());
				o.put("fdSex", item.getFdSex());
				o.put("fdPicUrl", item.getFdPicUrl());
				o.put("fdCreateTime", item.getFdCreateTime());
				o.put("fdFirstCount", item.getFdFirstCount());
				o.put("fdSecondCount", item.getFdSecondCount());
				o.put("fdThreeCount", item.getFdThreeCount());
				o.put("fdSex", item.getFdSex());
				o.put("fdAmount", item.getFdAmount());
				o.put("fdShopType", item.getFdShopType());
				o.put("fdBirthday", item.getFdBirthday());
				o.put("fdProvince", item.getFdProvince());
				o.put("fdCity", item.getFdCity());
				o.put("fdArea", item.getFdArea());
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
	public void save(AppUser model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
		JSONObject result=new JSONObject();
		try{
			
			if(StringUtil.isNotNull(model.getFdId())){
				result=service.update(model);
			}else{
				model.setFdCreateTime(DateUtils.getNow());
				result=service.save(model);
			}
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
	
	@RequestMapping("/saveUserMessage")
	public void saveUserMessage(HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			String fdUserId=request.getParameter("fdUserId");
			String fdUserMessage=request.getParameter("fdUserMessage");
			if(StringUtil.isNotNull(fdUserId)){
				service.saveUserMessage(fdUserId,fdUserMessage);
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
	
	@RequestMapping("/toUpdatePwd")
	public String toUpdatePwd(String fdId,HttpServletRequest request,HttpServletResponse response) {
		if(StringUtil.isNotNull(fdId)) {
			AppUser item = service.get(fdId);
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new AppUser());
		}
		return "Admin/jsp/app/AppUser/updatePwd.html";
	}
	
	@RequestMapping("/changePassword")
	public void changePassword(String fdId,HttpServletRequest request,HttpServletResponse response) throws IOException{
		JSONObject result=new JSONObject();
		try{
			String fdPwd=request.getParameter("fdPwd");
			if(StringUtil.isNotNull(fdId)&&StringUtil.isNotNull(fdPwd)){
				service.changePassword(fdId,fdPwd);
			}
			result.put("success", true);
			result.put("msg", "操作成功!");
		}catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", "操作失败!");
		}
		writeJSON(response,result.toString());
	}
	
}
