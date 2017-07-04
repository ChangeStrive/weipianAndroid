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
import com.platform.wp.model.AppUserShop;
import com.platform.wp.service.AppUserShopService;

@Controller
@RequestMapping("/AppUserShopAction")
public class AppUserShopAction extends BaseAction{
	
	@Autowired
	private AppUserShopService service = null;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response){
		return "Admin/jsp/app/AppUserShop/AppUserShopList.html";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(AppUserShop model,HttpServletRequest request,HttpServletResponse response) {
		String toUrl = "Admin/jsp/app/AppUserShop/AppUserShopAdd.html";
		if(StringUtil.isNotNull(model.getFdId())) {
			AppUserShop item = service.get(model.getFdId());
			request.setAttribute("item", item);
		}else{
			request.setAttribute("item", new AppUserShop());
		}
		return toUrl;
	}
	
	@RequestMapping("/getList")
	public void getList(AppUserShop model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
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
		Map<String,String> map=StringUtil.getParams(request, "fdUserCode,fdUserName,fdStoreName,fdStoreAddress,fdStoreBrand,fdStoreType,fdStatus");
		List<AppUserShop> list=service.list(map,model,start, limit);
		int count=service.getCount(map,model);
		JSONObject object=new JSONObject();
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				AppUserShop item=list.get(i);
				JSONObject o=new JSONObject();
				o.put("fdId", item.getFdId());
				o.put("fdUserId", item.getFdUserId());
				o.put("fdUserCode", item.getFdUserCode());
				o.put("fdUserName", item.getFdUserName());
				o.put("fdStoreName", item.getFdStoreName());
				o.put("fdStoreType", item.getFdStoreType());
				if(StringUtil.isNotNullStr(item.getFdPicUrl())){
					String[] fdPicUrlArr = item.getFdPicUrl().split(",");
					o.put("fdPicUrl1", fdPicUrlArr[0]);
					o.put("fdPicUrl2", fdPicUrlArr[1]);
				}else{
					o.put("fdPicUrl1", "");
					o.put("fdPicUrl2", "");
				}
				o.put("fdStoreAddress", item.getFdStoreAddress());
				o.put("fdStoreBrand", item.getFdStoreBrand());
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
	public void save(AppUserShop model,HttpServletRequest request,HttpServletResponse response) throws  IOException{
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
	
	
}
